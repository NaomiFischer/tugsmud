package net.tieupgames.tugsmud.parser;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ParseEventHandlerTest {

    // these "unused" fields are, in fact, used in a test case, since we can't mock Field instances
    @SuppressWarnings("unused")
    private Object someField;
    @SuppressWarnings("unused")
    private final Object someFinalField = null;
    @SuppressWarnings("unused")
    private static Object someStaticField;
    @SuppressWarnings("unused")
    private transient Object someTransientField;

    private final String someString = "more like a bit of twine, really";
    private final int someInt = 0xDEADBEEF;
    private final double someDouble = 13.37;
    private final boolean someBoolean = true;
    private final Object someObject = new Object();
    private final Class<?> someClass = Object.class;

    private final Registry mockRegistry = mock(Registry.class);
    private final ParseEventHandler.EventFactory mockEventFactory = mock(ParseEventHandler.EventFactory.class);
    private final JSONObject mockJSONObject = mock(JSONObject.class);

    private enum SomeEnum {
        EXPECTED
    }

    @ParseEvent(name="SomeParseEvent", produces=SomeProducedClass.class)
    @SuppressWarnings("unused") // "unused" fields are used while testing the parser
    private static class SomeParseEvent {
        private int someInt;
        private double someDouble;
        private boolean someBoolean;
        private String someString;
        private SomeEnum someEnum;
        private Object someRegistryObject;
        private Class<?> someClass;
        @OptionalParseField
        private Void someOptionalField;
        @ParseFieldWithConversionFunction("someConversionFunction")
        private Object someFieldWithConversionFunction;
        private final String _name;
        SomeParseEvent(String name) {
            _name = name;
        }
        private Object someConversionFunction(JSONObject jsonObject, String key) {
            return new Object();
        }
    }

    @ParseEvent(name="DeadlyParseEvent", produces=SomeProducedClass.class)
    @SuppressWarnings("unused") // "unused" fields are used while testing the parser
    private static class DeadlyParseEvent {
        @ParseFieldWithConversionFunction("deadlyConversionFunction")
        private String someString;
        DeadlyParseEvent(String name) {}
        private Object deadlyConversionFunction(JSONObject jsonObject, String key) throws ReflectiveOperationException {
            throw new ReflectiveOperationException("dummy ROE");
        }
    }

    @ParseEvent(name="SomeSupplierParseEvent", produces=SomeProducedClass.class)
    private static class SomeSupplierParseEvent extends SomeParseEvent implements Supplier<SomeProducedClass> {
        SomeSupplierParseEvent(String name) {
            super(name);
        }
        @Override public SomeProducedClass get() {
            return new SomeProducedClass(this, this);
        }
    }

    @SuppressWarnings("unused") // "unused" fields are used while testing the parser
    private static final class SomeProducedClass {
        private final SomeParseEvent _parseEvent;
        private final Supplier<?> _calledFromSupplier;
        SomeProducedClass(SomeParseEvent parseEvent) {
            this(parseEvent, null);
        }
        SomeProducedClass(SomeParseEvent parseEvent, Supplier<?> calledFromSupplier) {
            _parseEvent = parseEvent;
            _calledFromSupplier = calledFromSupplier;
        }
    }


    private void configureMockEventFactory(Function<String, ?> subfactory) throws Exception {
        when(mockEventFactory.get(anyString(), any(JSONObject.class), any(Class.class), any(Registry.class))).thenAnswer(
                invocation -> subfactory.apply((String)invocation.getArguments()[0]));
    }

    @Before
    public void setup() throws Exception {
        configureMockEventFactory(SomeParseEvent::new);
    }

    @Test
    public void isJSONInitializationCandidate() throws Exception {
        assertTrue(ParseEventHandler.isJSONInitializationCandidate(getClass().getDeclaredField("someField")));
        assertFalse("can't initialize final fields", ParseEventHandler.isJSONInitializationCandidate(
                getClass().getDeclaredField("someFinalField")));
        assertFalse("can't initialize static fields", ParseEventHandler.isJSONInitializationCandidate(
                getClass().getDeclaredField("someStaticField")));
        assertFalse("can't initialize transient fields", ParseEventHandler.isJSONInitializationCandidate(
                getClass().getDeclaredField("someTransientField")));
    }

    @Test
    public void handleEvent() throws Exception {
        ParseEventHandler instance = new ParseEventHandler(mockEventFactory, mockRegistry);
        SomeProducedClass produced = instance.handleEvent(someString, SomeProducedClass.class, SomeParseEvent.class, mockJSONObject);
        assertNotNull(produced);
        verify(mockRegistry).add(someString, produced);
        verify(mockEventFactory).get(someString, mockJSONObject, SomeParseEvent.class, mockRegistry);

        // now make sure we use the event's get() method, if it's a supplier
        configureMockEventFactory(SomeSupplierParseEvent::new);
        produced = instance.handleEvent(someString, SomeProducedClass.class, SomeSupplierParseEvent.class, mockJSONObject);
        assertNotNull(produced);
        verify(mockRegistry).add(someString, produced);
        verify(mockEventFactory).get(someString, mockJSONObject, SomeParseEvent.class, mockRegistry);
        assertTrue(produced._calledFromSupplier instanceof SomeSupplierParseEvent);
    }

    @Test
    public void eventFromJSONObject() throws Exception {
        when(mockJSONObject.get("someInt")).thenReturn(someInt);
        when(mockJSONObject.get("someDouble")).thenReturn(someDouble);
        when(mockJSONObject.get("someBoolean")).thenReturn(someBoolean);
        when(mockJSONObject.get("someString")).thenReturn(someString);
        when(mockJSONObject.get("someEnum")).thenReturn(SomeEnum.EXPECTED);
        when(mockJSONObject.getEnum(SomeEnum.class, "someEnum")).thenReturn(SomeEnum.EXPECTED);
        when(mockJSONObject.get("someRegistryObject")).thenReturn(someString);
        when(mockJSONObject.get("someClass")).thenReturn(someClass.getCanonicalName());

        Set<String> keys = new HashSet<>(Arrays.asList(
                "someInt", "someDouble", "someBoolean", "someString", "someEnum", "someClass", "someRegistryObject", "someFieldWithConversionFunction"
        ));
        when(mockJSONObject.keySet()).thenReturn(keys);

        when(mockRegistry.get(someString, Object.class)).thenReturn(someObject);

        SomeParseEvent parseEvent = ParseEventHandler.eventFromJSONObject(someString, mockJSONObject, SomeParseEvent.class, mockRegistry);

        assertNotNull(parseEvent);
        assertEquals(someString, parseEvent._name);
        assertEquals(someInt, parseEvent.someInt);
        assertEquals(someDouble, parseEvent.someDouble, 0);
        assertEquals(someBoolean, parseEvent.someBoolean);
        assertEquals(someString, parseEvent.someString);
        assertEquals(SomeEnum.EXPECTED, parseEvent.someEnum);
        assertEquals(someObject, parseEvent.someRegistryObject);
        assertEquals(someClass, parseEvent.someClass);

        // make sure we fail properly on a bad conversion function
        configureMockEventFactory(DeadlyParseEvent::new);
        try {
            ParseEventHandler.eventFromJSONObject(someString, mockJSONObject, DeadlyParseEvent.class, mockRegistry);
            fail("should have failed with an RuntimeException on ReflectiveOperationException in DeadlyParseEvent#deadlyConversionFunction");
        } catch (RuntimeException e) {}
        configureMockEventFactory(SomeParseEvent::new);

        // make sure we fail properly on missing Class fields
        when(mockJSONObject.get("someClass")).thenReturn("not a real class");
        try {
            ParseEventHandler.eventFromJSONObject(someString, mockJSONObject, SomeParseEvent.class, mockRegistry);
            fail("should have failed with a JSONException on missing class");
        } catch (JSONException e) {}
        when(mockJSONObject.get("someClass")).thenReturn(someClass.getCanonicalName());

        // ensure we get the appropriate exceptions on wrong/missing values
        when(mockJSONObject.get("someDouble")).thenReturn(someString); // eg. "someDouble": "a string"
        try {
            ParseEventHandler.eventFromJSONObject(someString, mockJSONObject, SomeParseEvent.class, mockRegistry);
        } catch (ParseException e) {
            assertEquals(ParseException.Reason.INCORRECT_JSON_VALUE, e.getReason());
        }

        when(mockJSONObject.get("someDouble")).thenThrow(new JSONException("")); //eg. "someDouble" is omitted entirely
        keys.remove("someDouble");
        try {
            ParseEventHandler.eventFromJSONObject(someString, mockJSONObject, SomeParseEvent.class, mockRegistry);
        } catch (ParseException e) {
            assertEquals(ParseException.Reason.MISSING_JSON_VALUE, e.getReason());
        }

    }
}