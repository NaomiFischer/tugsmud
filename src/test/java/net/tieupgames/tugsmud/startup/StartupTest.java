package net.tieupgames.tugsmud.startup;

import net.tieupgames.tugsmud.parser.Registry;
import org.apache.commons.cli.CommandLine;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class StartupTest {

    private final CommandLine commandLine = mock(CommandLine.class);
    private final Registry registry = mock(Registry.class);

    private final Startup instance = new Startup(commandLine, registry);

    @Test
    public void getCommandLine() throws Exception {
        assertEquals(commandLine, instance.getCommandLine());
    }

    @Test
    public void getRegistry() throws Exception {
        assertEquals(registry, instance.getRegistry());
    }
}