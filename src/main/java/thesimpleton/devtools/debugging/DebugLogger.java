package thesimpleton.devtools.debugging;

import org.apache.logging.log4j.Level;
import thesimpleton.utilities.ModLogger;

import java.util.HashMap;

public class DebugLogger {
    public DebugLogger(String prefix) {
        _logger = ModLogger.create(prefix, Level.DEBUG);
    }

    public void log(String message) {
        log(EMPTY_STRING, message);
    }

    public void log(Class sourceClass, String message) {
        log(sourceClass.getSimpleName(), message);
    }

    public void log(String prefix, String message) {
        String formattedPrefix = prefix.length() > 0 ? "[" + prefix + "] " : EMPTY_STRING;
        _logger.debug(formattedPrefix + message);
    }

    public static void main(String[] args) {
        _testDebugLogger();
    }

    private static void _testDebugLogger() {
        DebugLogger logger = new DebugLogger("TestDebugLogger");
        logger.log("Testing with no prefix");
        logger.log(HashMap.class, "Testing with class prefix");
        logger.log("SomeArbitraryPrefix", "Testing with string prefix");
    }

    private static final String EMPTY_STRING = "";
    private ModLogger _logger;
}
