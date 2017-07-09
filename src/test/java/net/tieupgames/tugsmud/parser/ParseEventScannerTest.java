package net.tieupgames.tugsmud.parser;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.ClassAnnotationMatchProcessor;
import org.junit.Test;

import java.util.Arrays;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParseEventScannerTest {

    private static final class SomeProducedClass {
        SomeProducedClass(SomeParseEvent e) {}
    }

    @ParseEvent(name="SomeParseEvent", produces=SomeProducedClass.class)
    private static final class SomeParseEvent {}

    @ParseEvent(name="MisconfiguredParseEvent", produces=SomeProducedClass.class)
    private static final class MisconfiguredParseEvent {}

    private static ParseEventScanner getTestInstance(Class<?>... events) {
        ClassAnnotationMatchProcessor processor[] = new ClassAnnotationMatchProcessor[1];

        FastClasspathScanner mockClasspathScanner = mock(FastClasspathScanner.class);
        when(mockClasspathScanner.matchClassesWithAnnotation(
                eq(ParseEvent.class),
                any(ClassAnnotationMatchProcessor.class)))
                .thenAnswer(invocation -> {
                    processor[0] = (ClassAnnotationMatchProcessor) invocation.getArguments()[1];
                    return mockClasspathScanner;
                });
        when(mockClasspathScanner.scan()).thenAnswer(invocation -> {
            Arrays.asList(events).forEach(processor[0]::processMatch);
            return null; // if the ParseEventScanner ever starts using this return value, this won't work any more
        });
        @SuppressWarnings("unchecked")
        Supplier<FastClasspathScanner> mockScannerFactory = mock(Supplier.class);
        when(mockScannerFactory.get()).thenReturn(mockClasspathScanner);
        return new ParseEventScanner(mockScannerFactory);
    }

    @Test
    public void getRegisteredEvent() throws Exception {
        ParseEventScanner instance = getTestInstance(SomeParseEvent.class);
        Class<?> registeredEvent = instance.getRegisteredEvent(SomeParseEvent.class.getAnnotation(ParseEvent.class).name());
        assertEquals(SomeParseEvent.class, registeredEvent);

        try {
            instance.getRegisteredEvent("some unknown event");
            fail("should have thrown a ParseException on missing event");
        } catch (ParseException e) {
            assertEquals(ParseException.Reason.UNKNOWN_EVENT, e.getReason());
        }
    }

    @Test
    public void constructorRejectsUnannotatedClasses() throws Exception {
        try {
            getTestInstance(Object.class);
            fail("assert should have fired for class missing @ParseEvent");
        } catch (AssertionError e) {}
    }

    @Test
    public void constructorRejectsDuplicateClasses() throws Exception {
        try {
            getTestInstance(SomeParseEvent.class, SomeParseEvent.class);
            fail("assert should have fired for multiple event classes with same name");
        } catch (AssertionError e) {}
    }

    @Test
    public void constructorRejectsClassProducingWrongClass() throws Exception {
        try {
            getTestInstance(MisconfiguredParseEvent.class);
            fail("assert should have fired for class producing class without appropriate constructor");
        } catch (AssertionError e) {}
    }
}