package com.akvelon.client.util;

/**
 * Logger singleton store System.Logger instance,
 * that log messages that will be routed to the underlying logging framework.
 */
public class Logger {
    private static Logger INSTANCE;

    private Logger() {
    }

    public synchronized static Logger getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Logger();
        }

        return INSTANCE;
    }

    /**
     * System.Logger instances log messages that will be routed to the underlying.
     * logging framework the LoggerFinder uses.
     */
    private System.Logger logger;

    public void setLogger(System.Logger logger) {
        this.logger = logger;
    }

    /**
     * Logs a message.
     *
     * @param level the log message level.
     * @param msg   the string message (or a key in the message catalog, if this logger is a localized logger);
     *              can be null.
     */
    public void log(System.Logger.Level level, String msg) {
        if (logger != null) logger.log(level, msg);
    }

    /**
     * Logs a message.
     *
     * @param level     the log message level.
     * @param msg       the string message (or a key in the message catalog, if this logger is a localized logger);
     *                  can be null.
     * @param throwable a Throwable associated with the log message; can be null.
     */
    public void log(System.Logger.Level level, String msg, Throwable throwable) {
        if (logger != null) logger.log(level, msg, throwable);
    }
}