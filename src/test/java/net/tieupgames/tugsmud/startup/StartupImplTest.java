package net.tieupgames.tugsmud.startup;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.matchprocessor.ImplementingClassMatchProcessor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StartupImplTest {

    private static final Startup mockStartup = mock(Startup.class);
    private static final List<StartupTask> taskHistory = new ArrayList<>();

    private static class SomeStartupTask implements StartupTask {
        @Override public void run(Startup startup) {
            assertEquals(mockStartup, startup);
            taskHistory.add(this);
        }
    }

    @StartupTaskDependency(SomeStartupTask.class)
    private static final class DependentStartupTaskA extends SomeStartupTask {}

    @StartupTaskDependency(SomeStartupTask.class)
    @StartupTaskDependency(DependentStartupTaskA.class)
    private static final class DependentStartupTaskB extends SomeStartupTask {}

    @StartupTaskDependency(DependentStartupTaskB.class)
    private static final class DependentStartupTaskC extends SomeStartupTask {}

    @Test
    @SuppressWarnings("unchecked") // mocking generics
    public void run() throws Exception {
        ImplementingClassMatchProcessor[] pointer = new ImplementingClassMatchProcessor[1];
        FastClasspathScanner mockClasspathScanner = mock(FastClasspathScanner.class);
        when(mockClasspathScanner.matchClassesImplementing(any(),any())).thenAnswer(invocation -> {
            pointer[0] = (ImplementingClassMatchProcessor)invocation.getArguments()[1];
            return mockClasspathScanner;
        });
        when(mockClasspathScanner.scan()).thenAnswer(invocation -> {
            pointer[0].processMatch(DependentStartupTaskA.class);
            pointer[0].processMatch(DependentStartupTaskC.class);
            pointer[0].processMatch(SomeStartupTask.class);
            pointer[0].processMatch(DependentStartupTaskB.class);
            return null;
        });

        new StartupImpl(mockStartup, mockClasspathScanner).run();

        assertTrue(taskHistory.get(0) instanceof SomeStartupTask);
        assertTrue(taskHistory.get(1) instanceof DependentStartupTaskA);
        assertTrue(taskHistory.get(2) instanceof DependentStartupTaskB);
        assertTrue(taskHistory.get(3) instanceof DependentStartupTaskC);
    }
}