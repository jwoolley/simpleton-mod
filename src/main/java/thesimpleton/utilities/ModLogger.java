package thesimpleton.utilities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import java.security.InvalidParameterException;
import java.util.HashMap;

public class ModLogger {
    public static ModLogger create(String logPrefix, Level maxLogLevel) {
        if (!_loggerMap.containsKey(logPrefix)) {
            if (maxLogLevel.compareTo(MIN_LOG_LEVEL) >= 0 && maxLogLevel.compareTo(MAX_LOG_LEVEL) <= 0) {
                ModLogger logger = new ModLogger(logPrefix, maxLogLevel);
                _loggerMap.put(logPrefix, logger);
            } else {
                throw new InvalidParameterException("Unsupported log Level: " + maxLogLevel.name());
            }
        } else {
            _staticLogger.warn("Logger instance already exists with prefix " + logPrefix + " and log level " + _loggerMap.get(logPrefix)._maxLogLevel);
        }
        return _loggerMap.get(logPrefix);
    }

    public void enable() {
        _isEnabled = true;
    }

    public void disable() { _isEnabled = false; }

    public boolean isEnabled() {
        return _isEnabled;
    }

    public Level getLogLevel() {
        return _maxLogLevel;
    }

    public void setLogLevel(Level level) {
        _maxLogLevel = level;
    }


    public static ModLogger create(String logPrefix) {
        return create(logPrefix, MAX_LOG_LEVEL);
    }

    public static ModLogger create(Class clazz, Level maxLogLevel) {
        return create(clazz.getName());
    }

    public static ModLogger create(Class clazz) {
        return create(clazz, MAX_LOG_LEVEL);
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
        _staticLogger.info("Creating ModLogger with log Level " + Level.TRACE);
        ModLogger modLogger = ModLogger.create(TheSimpletonMod.class, Level.TRACE);
        _staticLogger.info("Set modLogger Level to: " + Level.TRACE);
        modLogger.error("Show me the error logs!");
        modLogger.warn("Show me the warn logs!");
        modLogger.info("Show me the info logs!");
        modLogger.debug("Show me the debug logs!");
        modLogger.trace("Show me the trace logs!");

        modLogger.setLogLevel(Level.DEBUG);
        _staticLogger.info("Set modLogger Level to: " + Level.DEBUG);
        modLogger.error("Show me the error logs!");
        modLogger.warn("Show me the warn logs!");
        modLogger.info("Show me the info logs!");
        modLogger.debug("Show me the debug logs!");
        modLogger.trace("Show me the trace logs!");

        modLogger.setLogLevel(Level.INFO);
        _staticLogger.info("Set modLogger Level to: " + Level.INFO);
        modLogger.error("Show me the error logs!");
        modLogger.warn("Show me the warn logs!");
        modLogger.info("Show me the info logs!");
        modLogger.debug("Show me the debug logs!");
        modLogger.trace("Show me the trace logs!");

        modLogger.setLogLevel(Level.ERROR);
        _staticLogger.info("Set modLogger Level to: " + Level.ERROR);
        modLogger.error("Show me the error logs!");
        modLogger.warn("Show me the warn logs!");
        modLogger.info("Show me the info logs!");
        modLogger.debug("Show me the debug logs!");
        modLogger.trace("Show me the trace logs!");

        _staticLogger.info("Attempting to create duplicate ModLogger");

        ModLogger duplicateLogger = ModLogger.create(TheSimpletonMod.class, Level.TRACE);

        if (!modLogger.equals(duplicateLogger)) {
            throw new AssertionError("Did not expect duplicate ModLogger creation to succeed!");
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
