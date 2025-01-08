package utils;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public final class LoggerUtil {
    private LoggerUtil() {} // constructor prevents instantiation

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }
}
