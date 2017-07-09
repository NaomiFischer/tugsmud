package net.tieupgames.tugsmud.parser;

public class ParseException extends Exception {

    private final String _detail;
    private final Reason _reason;

    ParseException(String detail, Reason reason) {
        _detail = detail;
        _reason = reason;
    }

    public enum Reason {
        UNKNOWN_EVENT,
        MISSING_JSON_VALUE,
        INCORRECT_JSON_VALUE,
        DUPLICATE_ENTRY,
        ;
    }

    public Reason getReason() {
        return _reason;
    }

    public String getDetail() {
        return _detail;
    }

    @Override
    public String getMessage() {
        return _detail + ": " + _reason;
    }

    public ParseException clarify(String detail) {
        return new ParseException(detail + ", " + _detail, _reason);
    }

    static ParseException unknownEvent(String eventName) {
        return new ParseException(eventName, Reason.UNKNOWN_EVENT);
    }

    static ParseException missingJsonValue(String fieldName) {
        return new ParseException(fieldName, Reason.MISSING_JSON_VALUE);
    }

    static ParseException incorrectJsonValue(Class<?> expectedType, Object wrongValue) {
        return incorrectJsonValue(expectedType.getSimpleName(), wrongValue);
    }
    static ParseException incorrectJsonValue(String expectedType, Object wrongValue) {
        return new ParseException("expected " + expectedType + ", have " + wrongValue, Reason.INCORRECT_JSON_VALUE);
    }

    static ParseException duplicateEntry(String entryName) {
        return new ParseException(entryName, Reason.DUPLICATE_ENTRY);
    }
}
