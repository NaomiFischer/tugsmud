package net.tieupgames.tugsmud.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Registry {

    public static final Registry INSTANCE = new Registry(new HashMap<>());

    private final Map<String, Object> _entries;

    /**
     * A constructor that allows manual injection of a map of entries. Note that this map
     * is <strong>not</strong> defensively copied! In effect, this registry acts as a view
     * of that map.
     * @param entries the entry map
     */
    Registry(Map<String, Object> entries) {
        _entries = entries;
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
        Objects.requireNonNull(expectedClass, "expectedClass can't be null");
        Object value = _entries.get(name);
        Objects.requireNonNull(value, "couldn't find any " + expectedClass.getSimpleName() + " named " + name);
        return expectedClass.cast(value);
    }

    @Override
    public String toString() {
        return _entries.toString();
    }

    /**
     * Registers a value for some key. Once some {@code value} has been registered,
     * {@code register(key, value.getClass()} will find the same {@code value}.
     * <p>It is an error to register more than one value to the same key.</p>
     * @param key the key to register the object under
     * @param value the object to register
     * @throws ParseException if there already is a value at that key/class pair
     */
    public void add(String key, Object value) throws ParseException {
        Objects.requireNonNull(key, "key can't be null");
        Objects.requireNonNull(value, "value can't be null");
        if (_entries.containsKey(key)) {
            throw ParseException.duplicateEntry(key);
        }
        _entries.put(key, value);
    }
}
