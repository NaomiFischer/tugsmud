package net.tieupgames.tugsmud.startup;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import net.tieupgames.tugsmud.parser.Registry;
import org.apache.commons.cli.*;

public class Startup {

    private final CommandLine _commandLine;
    private final Registry _registry;

    public static void main(String[] args) throws Exception {
        Startup startup = new Startup(new DefaultParser().parse(getOptions(), args), new Registry());
        new StartupImpl(startup, new FastClasspathScanner()).run();
        System.out.println(startup._registry);
    }

    Startup(CommandLine commandLine, Registry registry) {
        _commandLine = commandLine;
        _registry = registry;
    }

    public CommandLine getCommandLine() {
        return _commandLine;
    }

    public Registry getRegistry() {
        return _registry;
    }

    //TODO: replace this with some higher-level mechanism
    private static Options getOptions() {
       return new Options()
               .addOption(Option.builder("c")
                       .longOpt("configuration-file")
                       .desc("a .json file containing game data")
                       .hasArg()
                       .build())
               ;
    }

}
