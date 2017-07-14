package net.tieupgames.tugsmud.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Registry {

    private final Map<String, Object> _namedEntries;
    private final Map<Class<?>, EntryIDTracker> _idTrackers;

    public Registry() {
        this(new HashMap<>(), new HashMap<>());
    }

    /**
     * A constructor that allows manual injection of the underlying maps. This
     * creates, in effect, a view of the injected maps, which is useful for
     * testing.
     * @param entries a map to hold entries, by name
     * @param idTrackers a map to hold lists of entries, by class
     */
    Registry(Map<String, Object> entries, Map<Class<?>, EntryIDTracker> idTrackers) {
        _namedEntries = entries;
        _idTrackers = idTrackers;
    }

    /**
     * Gets some value from this registry. A value of the expected class must already
     * be registered to the indicated name; otherwise, {@code null} is returned. If a
     * value exists of a different class, a {@link ClassCastException} is thrown,
     * unless the types are compatible.
     * @param name the name to look for an object under
     * @param expectedClass sets T
     * @param <T> the class of object to find
     * @return the object at that name/class pair, or {@code null}
     */
    public <T> T get(String name, Class<T> expectedClass) {
        Objects.requireNonNull(name, "name can't be null");
        Object value = _namedEntries.get(name);
        return expectedClass.cast(value);
    }

    public <T> T get(int id, Class<T> expectedClass) {
        getIDTracker(expectedClass);
        Object value = _idTrackers.get(expectedClass).get(id);
        Objects.requireNonNull(value, "couldn't find any " + expectedClass.getSimpleName() + " with ID " + id);
        return expectedClass.cast(value);
    }

    public <T> int getIdFor(T object, Class<? super T> expectedClass) {
        return getIDTracker(expectedClass).getId(object);
    }

    /**
     * Registers a value for some key. Once some {@code value} has been registered,
     * {@code register(key, registryClass} will find the same {@code value}.
     * <p>Registered values must be associated with some class. This class must be
     * a superclass of the value's actual type. Registry IDs (of type {@code int})
     * are tracked per registry class.</p>
     * <p>It is an error to register more than one value to the same key.</p>
     * @param key the key to register the object under
     * @param value the object to register
     * @param registryClass some superclass of {@code value}
     * @throws ParseException if there already is a value at that key/class pair
     */
    public void add(String key, Object value, Class<?> registryClass) throws ParseException {
        Objects.requireNonNull(key, "key can't be null");
        Objects.requireNonNull(value, "value can't be null");
        if (_namedEntries.containsKey(key)) {
            throw ParseException.duplicateEntry(key);
        }
        getIDTracker(registryClass).add(registryClass.cast(value));
        _namedEntries.put(key, value);
    }

    private EntryIDTracker getIDTracker(Class<?> clazz) {
        Objects.requireNonNull(clazz, "class must not be null");
        return _idTrackers.computeIfAbsent(clazz, anything -> new EntryIDTracker());
    }

    @Override
    public String toString() {
        return _namedEntries.toString();
    }
}
