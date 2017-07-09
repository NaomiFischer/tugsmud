package net.tieupgames.tugsmud.startup;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import net.tieupgames.tugsmud.parser.Registry;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Main {

    private static CommandLine COMMAND_LINE;

    public static void main(String[] args) throws Exception {
        COMMAND_LINE = new DefaultParser().parse(getOptions(), args);
        new Startup(new FastClasspathScanner()).run();
        System.out.println(Registry.INSTANCE);
    }

    public static CommandLine getCommandLine() {
        return COMMAND_LINE;
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
