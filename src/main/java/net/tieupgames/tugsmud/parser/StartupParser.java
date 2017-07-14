package net.tieupgames.tugsmud.parser;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import net.tieupgames.tugsmud.startup.Startup;
import net.tieupgames.tugsmud.startup.StartupTask;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;

@SuppressWarnings("unused") // used during startup
public class StartupParser implements StartupTask {

    @Override
    public void run(Startup startup) throws Exception {
        String jsonFilePath = startup.getCommandLine().getOptionValue("c");
        JSONTokener tokener = new JSONTokener(new FileReader(new File(jsonFilePath)));
        JSONObject json = new JSONObject(tokener);
        new Parser(new ParseEventScanner(FastClasspathScanner::new),
                   new ParseEventHandler(ParseEventHandler::eventFromJSONObject, startup.getRegistry()))
                .parse(json);
    }
}