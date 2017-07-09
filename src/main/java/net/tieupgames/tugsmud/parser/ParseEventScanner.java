package net.tieupgames.tugsmud.parser;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

class ParseEventScanner {

    private final Map<String, Class<?>> _events;

    ParseEventScanner(Supplier<FastClasspathScanner> classpathScannerFactory) {
        _events = new HashMap<>();
        classpathScannerFactory.get()
                .matchClassesWithAnnotation(ParseEvent.class, event -> {
                    ParseEvent data = event.getAnnotation(ParseEvent.class);
                    assert data != null;
                    String eventName = data.name();
                    assert !_events.containsKey(eventName) : "have multiple parse events with name " + eventName;
                    assert hasConstructor(event, data.produces()) : event + " doesn't have an appropriate constructor";
                    _events.put(eventName, event);
                })
                .scan();
    }

    Class getRegisteredEvent(String eventName) throws ParseException {
        Class<?> event = _events.get(eventName);
        if (event == null) {
            throw ParseException.unknownEvent(eventName);
        }
        return event;
    }

    private boolean hasConstructor(Class<?> parseEvent, Class<?> produces) {
        // this use of exceptions is generally frowned upon, and for good reason
        // however, as there is no other way to check for the presence of a constructor...
        // (we could just... do this up above, but I want it as a boolean so I can stick it
        // in an assert)
        try {
            produces.getDeclaredConstructor(parseEvent);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
