package net.tieupgames.tugsmud.parser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Parser {

    private final ParseEventScanner _scanner;
    private final ParseEventHandler _handler;

    Parser(ParseEventScanner scanner, ParseEventHandler handler) {
        _scanner = scanner;
        _handler = handler;
    }

    void parse(JSONObject jsonDataRoot) throws ParseException, ReflectiveOperationException {
        for (String eventTypeName : jsonDataRoot.keySet()) {
            Class<?> eventType = _scanner.getRegisteredEvent(eventTypeName);
            ParseEvent eventData = eventType.getAnnotation(ParseEvent.class);

            Object rawEventInvocations = jsonDataRoot.get(eventTypeName);
            if (!(rawEventInvocations instanceof JSONObject)) {
                throw ParseException.incorrectJsonValue("a dictionary", rawEventInvocations);
            }
            JSONObject eventInvocations = ((JSONObject) rawEventInvocations);
            List<String> sortedInvocationNames = new ArrayList<>(eventInvocations.keySet());
            Collections.sort(sortedInvocationNames);

            for (String eventInvocationName : sortedInvocationNames) {
                Object eventInvocation = eventInvocations.get(eventInvocationName);
                if (!(eventInvocation instanceof JSONObject)) {
                    throw ParseException.incorrectJsonValue("a dictionary", eventInvocationName);
                }
                _handler.handleEvent(eventInvocationName, eventData.produces(), eventType, (JSONObject) eventInvocation);
            }
        }

    }
}
