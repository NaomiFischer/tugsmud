package net.tieupgames.tugsmud.parser;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseExceptionTest {

    private final ParseException.Reason someReason = ParseException.Reason.UNKNOWN_EVENT;
    private final String someDetail = "wheeeeeeeeee";

    @Test
    public void getReason() throws Exception {
        assertEquals(someReason, new ParseException(someDetail, someReason).getReason());
    }

    @Test
    public void getDetail() throws Exception {
        assertEquals(someDetail, new ParseException(someDetail, someReason).getDetail());
    }

    @Test
    public void getMessage() throws Exception {
        assertEquals(someDetail + ": " + someReason, new ParseException(someDetail, someReason).getMessage());
    }

    @Test
    public void clarify() throws Exception {
        String someClarification = "fwoosh";
        ParseException instance = new ParseException(someDetail, someReason).clarify(someClarification);
        assertEquals(someClarification + ", " + someDetail, instance.getDetail());
        assertEquals(someReason, instance.getReason());
    }

    @Test
    public void unknownEvent() throws Exception {
        ParseException instance = ParseException.unknownEvent(someDetail);
        assertEquals(ParseException.Reason.UNKNOWN_EVENT, instance.getReason());
        assertEquals(someDetail, instance.getDetail());
    }

    @Test
    public void missingJsonValue() throws Exception {
        ParseException instance = ParseException.missingJsonValue(someDetail);
        assertEquals(ParseException.Reason.MISSING_JSON_VALUE, instance.getReason());
        assertEquals(someDetail, instance.getDetail());
    }

    @Test
    public void incorrectJsonValue() throws Exception {
        ParseException instance = ParseException.incorrectJsonValue(Object.class, someDetail);
        assertEquals(ParseException.Reason.INCORRECT_JSON_VALUE, instance.getReason());
        assertEquals("expected " + Object.class.getSimpleName() + ", have " + someDetail, instance.getDetail());
    }

    @Test
    public void incorrectJsonValue1() throws Exception {
        ParseException instance = ParseException.incorrectJsonValue(someDetail, someDetail);
        assertEquals(ParseException.Reason.INCORRECT_JSON_VALUE, instance.getReason());
        assertEquals("expected " + someDetail + ", have " + someDetail, instance.getDetail());
    }

    @Test
    public void duplicateEntry() throws Exception {
        ParseException instance = ParseException.duplicateEntry(someDetail);
        assertEquals(ParseException.Reason.DUPLICATE_ENTRY, instance.getReason());
        assertEquals(someDetail, instance.getDetail());
    }
}