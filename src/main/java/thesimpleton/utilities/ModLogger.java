package thesimpleton.utilities;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Utility class for quick instantiation of loggers for development and debugging.
 *
 * NOTE: the fudging of log levels (any log calls below log level INFO will actually use INFO, with a secondary
 *      tag for the specified level) exists because it doesn't appear to be possible to get the apache logger to
 *      print logs below INFO in this project. If a fix can be made for this, this logic should be removed.
 */

public class ModLogger {
    public static ModLogger create(String logPrefix) {
        return create(logPrefix, MAX_LOG_LEVEL);
    }

    public static ModLogger create(String logPrefix, Level maxLogLevel) {
        if (!_loggerMap.containsKey(logPrefix)) {
            if (maxLogLevel.compareTo(MIN_LOG_LEVEL) >= 0 && maxLogLevel.compareTo(MAX_LOG_LEVEL) <= 0) {
                _staticLogger.info("Creating " + ModLogger.class.getSimpleName() + " with prefix " + logPrefix
                    + " and max log level" + maxLogLevel);
                ModLogger logger = new ModLogger(logPrefix, maxLogLevel);
                _loggerMap.put(logPrefix, logger);
            } else {
                throw new InvalidParameterException("Unsupported log Level: " + maxLogLevel.name());
            }
        } else {
            throw new InvalidParameterException("Logger instance already exists with prefix " + logPrefix);
        }
        return _loggerMap.get(logPrefix);
    }

    public static ModLogger create(Class clazz, String  additionalLogPrefix) {
        return create(clazz, additionalLogPrefix, MAX_LOG_LEVEL);
    }

    public static ModLogger create(Class clazz, String  additionalLogPrefix, Level maxLogLevel) {
        _staticLogger.info("Called new " + ModLogger.class.getSimpleName() + ".create() with class + " + clazz.getSimpleName()
            + ", additionalLogPrefix " + additionalLogPrefix + " and maxLogLevel " + maxLogLevel);
        return create(clazz.getSimpleName() + "." + additionalLogPrefix, maxLogLevel);
    }

    public static ModLogger create(Class clazz) {
        return create(clazz, MAX_LOG_LEVEL);
    }

    public static ModLogger create(Class clazz, Level maxLogLevel) {
        return create(clazz.getSimpleName(), maxLogLevel);
    }


    public void enable() {
        _isEnabled = true;
    }

    public void disable() { _isEnabled = false; }

    public boolean isEnabled() {
        return _isEnabled;
    }

    public Level getMaxLogLevel() {
        return _maxLogLevel;
    }

    public void setMaxLogLevel(Level level) {
        _maxLogLevel = level;
    }

    public void log(String message) {
        _log(_maxLogLevel, message);
    }

    public void error(String message) {
        _log(Level.ERROR, message);
    }

    public void warn(String message) {
        _log(Level.WARN, message);
    }

    public void info(String message) {
        _log(Level.INFO, message);
    }

    public void debug(String message) {
        _log(Level.DEBUG, message);
    }

    public void trace(String message) {
        _log(Level.TRACE, message);
    }

    private ModLogger(String logPrefix, Level maxLogLevel) {
        _internalLogger = LogManager.getLogger(logPrefix);
        _maxLogLevel = maxLogLevel;
        _isEnabled = true;
    }

    private void _log(Level logLevel, String message) {
        if (_isEnabled && _maxLogLevel.compareTo(logLevel) >= 0)  {
            if (logLevel.compareTo(MAX_MANAGER_SUPPORTED_LOG_LEVEL) <= 0) {
                _logInternal(logLevel, logLevel, message);
            } else {
                _logInternal(logLevel, MAX_MANAGER_SUPPORTED_LOG_LEVEL, message);
            }
        }
    }

    private void _logInternal(Level logLevel, Level internalLogLevel, String message) {
        if (internalLogLevel.compareTo(MAX_MANAGER_SUPPORTED_LOG_LEVEL) <= 0) {
            String prefixedMessage = "[" + logLevel.name() + "] " + message;
            _internalLogger.log(internalLogLevel, prefixedMessage);
        } else {
            _staticLogger.warn("Log level " + internalLogLevel.name() + " not supported!");
        }
    }

    public static void main(String[] args) {
        testModLoggerSimple();
    }

    public static void testModLoggerSimple() {
        _staticLogger.info("Creating class-based ModLogger with log Level " + Level.TRACE);
        ModLogger classLogger = ModLogger.create(TheSimpletonMod.class, Level.TRACE);

        _staticLogger.info("Setting modLogger Level to: " + Level.TRACE);
        classLogger.error("Show me the error logs!");
        classLogger.warn("Show me the warn logs!");
        classLogger.info("Show me the info logs!");
        classLogger.debug("Show me the debug logs!");
        classLogger.trace("Show me the trace logs!");

        _staticLogger.info("Setting modLogger Level to: " + Level.DEBUG);
        classLogger.setMaxLogLevel(Level.DEBUG);
        classLogger.error("Show me the error logs!");
        classLogger.warn("Show me the warn logs!");
        classLogger.info("Show me the info logs!");
        classLogger.debug("Show me the debug logs!");
        classLogger.trace("Show me the trace logs!");

        _staticLogger.info("Setting modLogger Level to: " + Level.INFO);
        classLogger.setMaxLogLevel(Level.INFO);
        classLogger.error("Show me the error logs!");
        classLogger.warn("Show me the warn logs!");
        classLogger.info("Show me the info logs!");
        classLogger.debug("Show me the debug logs!");
        classLogger.trace("Show me the trace logs!");

        _staticLogger.info("Setting modLogger Level to: " + Level.ERROR);
        classLogger.setMaxLogLevel(Level.ERROR);
        classLogger.error("Show me the error logs!");
        classLogger.warn("Show me the warn logs!");
        classLogger.info("Show me the info logs!");
        classLogger.debug("Show me the debug logs!");
        classLogger.trace("Show me the trace logs!");

        _staticLogger.info("Creating prefix-based ModLogger with log Level " + Level.TRACE);
        ModLogger customPrefixLogger = ModLogger.create("SomeCustomPrefix", Level.TRACE);
        customPrefixLogger.error("Show me the error logs!");
        customPrefixLogger.warn("Show me the warn logs!");
        customPrefixLogger.info("Show me the info logs!");
        customPrefixLogger.debug("Show me the debug logs!");
        customPrefixLogger.trace("Show me the trace logs!");

        _staticLogger.info("Creating ModLogger with no log level specified (should default to " + Level.TRACE + ")");
        ModLogger defaultLogLevelLogger = ModLogger.create("UsingDefaultLogLevel");
        defaultLogLevelLogger.error("Show me the error logs!");
        defaultLogLevelLogger.warn("Show me the warn logs!");
        defaultLogLevelLogger.info("Show me the info logs!");
        defaultLogLevelLogger.debug("Show me the debug logs!");
        defaultLogLevelLogger.trace("Show me the trace logs!");

        _staticLogger.info("Attempting to create duplicate ModLogger with class prefix");
        try {
            ModLogger.create(TheSimpletonMod.class);
            throw new AssertionError("Did not expect ModLogger creation with duplicate class prefix to succeed!");
        } catch (Exception e) {
            _staticLogger.info("Unsupported ModLogger creation failed as expected.");
        }

        _staticLogger.info("Attempting to create duplicate ModLogger with custom prefix");
        final String duplicateLoggerPrefix = "DoNotDuplicateMe";
        ModLogger.create(duplicateLoggerPrefix);
        try {
            ModLogger.create(duplicateLoggerPrefix);
            throw new AssertionError("Did not expect ModLogger creation with duplicate custom prefix to succeed!");
        } catch (Exception e) {
            _staticLogger.info("Unsupported ModLogger creation failed as expected.");
        }

        _staticLogger.info("Attempting to create ModLogger with unsupported log Level " + Level.FATAL);
        try {
            ModLogger.create("This should fail", Level.FATAL);
            throw new AssertionError("Did not expect ModLogger creation with unsupported log level to succeed!");
        } catch (Exception e) {
            _staticLogger.info("Unsupported ModLogger creation failed as expected.");
        }
    }

    private Logger _internalLogger;
    private Level _maxLogLevel;

    private boolean _isEnabled;

    private static Level MIN_LOG_LEVEL = Level.ERROR;
    private static Level MAX_LOG_LEVEL = Level.TRACE;
    private static Level MAX_MANAGER_SUPPORTED_LOG_LEVEL = Level.INFO;

    static private HashMap<String, ModLogger> _loggerMap = new HashMap<>();
    static private Logger _staticLogger = LogManager.getLogger(ModLogger.class);
}
