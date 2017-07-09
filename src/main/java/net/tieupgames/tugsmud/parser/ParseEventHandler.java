package net.tieupgames.tugsmud.parser;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.BiFunction;
import java.util.function.Supplier;

class ParseEventHandler {

    @FunctionalInterface
    interface EventFactory {
        Object get(String name, JSONObject json, Class<?> eventType, Registry registry) throws ReflectiveOperationException, ParseException;
    }

    private final EventFactory _eventFactory;
    private final Registry _registry;

    ParseEventHandler(EventFactory creation, Registry registry) {
        _eventFactory = creation;
        _registry = registry;
    }

    static boolean isJSONInitializationCandidate(Field f) {
        return (f.getModifiers() & (Modifier.TRANSIENT | Modifier.FINAL | Modifier.STATIC)) == 0;
    }

    <R> R handleEvent(String name, Class<R> produces, Class<?> eventType, JSONObject json) throws ParseException, ReflectiveOperationException {
            Object event = _eventFactory.get(name, json, eventType, _registry);
            R produced;
            if (Supplier.class.isAssignableFrom(eventType)) {
                produced = produces.cast(((Supplier<?>)event).get());
            } else {
                produced = produces.getDeclaredConstructor(eventType).newInstance(event);
            }
            _registry.add(name, produced);
            return produced;
    }

    static <T> T eventFromJSONObject(String name, JSONObject json, Class<T> eventType, Registry registry) throws ReflectiveOperationException, ParseException {
        Constructor<T> constructor = eventType.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        T event = constructor.newInstance(name);

        // initialize each field from the JSON object
        for (Field field : eventType.getDeclaredFields()) {
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();

            // ignore fields we don't initialize (eg. static, final, ...)
            if (!isJSONInitializationCandidate(field)) {
                continue;
            }

            // if the field is missing from the JSON, we have two options -
            // skip it if the field is optional, and fail otherwise
            if (!json.keySet().contains(fieldName)) {
                // if we can't find a JSON value, but the field is optional, just leave it as-is
                if (field.isAnnotationPresent(OptionalParseField.class)) {
                    continue;
                }
                // otherwise, indicate that the field is missing
                throw ParseException.missingJsonValue(fieldName);
            }

            // make sure we can access the field
            field.setAccessible(true);

            // we've got our field ready - read in the JSON value and convert it

            // and now we're good to set our field, woohoo!
            try {
                Object convertedValue = getConversionStrategy(event, field, registry).apply(json, fieldName);
                field.set(event, convertedValue);
            } catch (IllegalArgumentException e) {
                // this occurs if the type wasn't converted properly -
                // which should only happen if the JSON file has the wrong type for its value
                throw ParseException.incorrectJsonValue(fieldType, json.get(fieldName)).clarify(fieldName);
            }
        }

        // event is fully initialized!
        return event;
    }

    private static BiFunction<JSONObject, String, Object> getConversionStrategy(Object event, Field field, Registry registry) {
        final Class<?> clazz = field.getType();

        // do we have a custom conversion function?
        if (field.isAnnotationPresent(ParseFieldWithConversionFunction.class)) {
            return (json, key) -> {
              try {
                  ParseFieldWithConversionFunction data = field.getAnnotation(ParseFieldWithConversionFunction.class);
                  Method conversionFunction = event.getClass().getDeclaredMethod(data.value(), JSONObject.class, String.class);
                  Object thiz = Modifier.isStatic(conversionFunction.getModifiers()) ? null : event;
                  conversionFunction.setAccessible(true);
                  return conversionFunction.invoke(thiz, json, key);
              } catch (ReflectiveOperationException e) {
                  throw new RuntimeException(e);
              }
            };
        }

        // no conversion function. what if we're an enum?
        if (clazz.isEnum()) {
            Class<? extends Enum> enumClazz = clazz.asSubclass(Enum.class);
            return (json, key) -> {
                @SuppressWarnings("unchecked") Object o = json.getEnum(enumClazz, key);
                return clazz.cast(o);
            };
        }

        // class?
        if (clazz == Class.class) {
            return (json, key) -> {
                String className = json.get(key).toString();
                try {
                    return Class.forName(className);
                } catch (ClassNotFoundException e) {
                    throw new JSONException("couldn't find class " + className);
                }
            };
        }

        //TODO: arrays, collections
        //TODO: inline definitions

        // right, just the normal behavior, then...
        return (json, key) -> {
            // start by getting whatever value is in the JSON object
            Object object = json.get(key);
            assert object != null : "JSONObject#get should never return null";

            // check the registry...
            Object checkRegistry = registry.get(object.toString(), clazz);
            if (checkRegistry != null) {
                return checkRegistry;
            }

            // okay, nothing in the registry - return the object exactly as retrieved
            return object;
        };
    }
}
