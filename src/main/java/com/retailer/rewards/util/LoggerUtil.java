package com.retailer.rewards.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LoggerUtil {

    private LoggerUtil() {
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void info(Class<?> clazz, String message, Object... args) {
        LoggerFactory.getLogger(clazz).info(message, args);
    }

    public static void error(Class<?> clazz, String message, Object... args) {
        LoggerFactory.getLogger(clazz).error(message, args);
    }
}
