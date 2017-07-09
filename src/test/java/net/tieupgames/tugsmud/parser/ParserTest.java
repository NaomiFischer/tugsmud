package net.tieupgames.tugsmud.parser;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ParserTest {

    private static final class SomeProducedClass {
        SomeProducedClass(SomeParseEvent event) {}
    }

    @ParseEvent(name="SomeParsEvent", produces=SomeProducedClass.class)
    private static final class SomeParseEvent {
        SomeParseEvent(String name) {}
    }

    private final String someKey = "it probably unlocks handcuffs";

    @Test
    public void parse() throws Exception {
        ParseEventScanner mockScanner = mock(ParseEventScanner.class);
        when(mockScanner.getRegisteredEvent(anyString())).thenReturn(SomeParseEvent.class);
        ParseEventHandler mockHandler = mock(ParseEventHandler.class);
        Parser instance = new Parser(mockScanner, mockHandler);

        JSONObject mockEventInvocation = new JSONObject();
        JSONObject mockEventInvocations = new JSONObject();
        JSONObject mockJSONDataRoot = new JSONObject();
        mockEventInvocations.put(someKey, mockEventInvocation);
        mockJSONDataRoot.put("SomeParseEvent", mockEventInvocations);

        instance.parse(mockJSONDataRoot);
        verify(mockHandler).handleEvent(someKey, SomeProducedClass.class, SomeParseEvent.class, mockEventInvocation);

        // okay, that worked - now let's make sure we get our expected exceptions on malformed input
        mockEventInvocations.put(someKey, 42);
        try {
            instance.parse(mockJSONDataRoot);
        } catch (ParseException e) {
            assertEquals(ParseException.Reason.INCORRECT_JSON_VALUE, e.getReason());
        }

        mockJSONDataRoot.put("SomeParseEvent", 42);
        try {
            instance.parse(mockJSONDataRoot);
        } catch (ParseException e) {
            assertEquals(ParseException.Reason.INCORRECT_JSON_VALUE, e.getReason());
        }
    }
}