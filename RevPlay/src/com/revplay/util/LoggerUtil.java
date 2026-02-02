package com.revplay.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtil {

    private static final Logger LOGGER = Logger.getLogger("RevPlayLogger");
    private static FileHandler fileHandler;

    static {
        try {
            // Remove default console handlers to keep CLI clean
            Logger rootLogger = Logger.getLogger("");
            /*
             * Handler[] handlers = rootLogger.getHandlers();
             * for (Handler handler : handlers) {
             * if (handler instanceof ConsoleHandler) {
             * rootLogger.removeHandler(handler);
             * }
             * }
             */
            // Ideally we don't want to silence everything, but for a CLI app, logs
            // shouldn't print to stdout.
            // We will just configure our named logger.

            LOGGER.setUseParentHandlers(false); // Don't use console handler of parent

            fileHandler = new FileHandler("application.log", true); // Append mode
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);

            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);

            LOGGER.info("Logger initialized.");

        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    public static void logInfo(String message) {
        LOGGER.info(message);
    }

    public static void logWarning(String message) {
        LOGGER.warning(message);
    }

    public static void logError(String message, Throwable t) {
        LOGGER.log(Level.SEVERE, message, t);
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
