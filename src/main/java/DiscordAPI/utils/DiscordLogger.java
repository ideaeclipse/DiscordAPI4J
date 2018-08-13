package DiscordAPI.utils;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import java.io.PrintStream;
import java.time.LocalTime;

public class DiscordLogger extends MarkerIgnoringBase {

    private final String name;
    private volatile int level = Level.INFO.ordinal();
    private volatile PrintStream standard, error;

    public DiscordLogger(String name) {
        this.name = name;
        standard = System.out;
        error = System.err;
    }

    public void setLevel(Level level) {
        this.level = level.ordinal();
    }

    public void setStandardStream(PrintStream stream) {
        this.standard = stream;
    }

    public void setErrorStream(PrintStream stream) {
        this.error = stream;
    }

    private void log(Level level, String message, Throwable error) {
        if (level.ordinal() >= this.level) {
            PrintStream stream = level.ordinal() >= Level.WARN.ordinal() ? this.error : standard;

            stream.format("%s: [%s][%s][%s] - %s\n", LocalTime.now(), level, Thread.currentThread().getName(), name, message);

            if (error != null)
                error.printStackTrace(stream);
        }
    }

    @Override
    public boolean isTraceEnabled() {
        return level == Level.TRACE.ordinal();
    }

    @Override
    public void trace(String msg) {
        log(Level.TRACE, msg, null);
    }

    @Override
    public void trace(String format, Object arg) {
        FormattingTuple tuple = MessageFormatter.format(format, arg);
        log(Level.TRACE, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        FormattingTuple tuple = MessageFormatter.format(format, arg1, arg2);
        log(Level.TRACE, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void trace(String format, Object... arguments) {
        FormattingTuple tuple = MessageFormatter.arrayFormat(format, arguments);
        log(Level.TRACE, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void trace(String msg, Throwable t) {
        log(Level.TRACE, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return level <= Level.DEBUG.ordinal();
    }

    @Override
    public void debug(String msg) {
        log(Level.DEBUG, msg, null);
    }

    @Override
    public void debug(String format, Object arg) {
        FormattingTuple tuple = MessageFormatter.format(format, arg);
        log(Level.DEBUG, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        FormattingTuple tuple = MessageFormatter.format(format, arg1, arg2);
        log(Level.DEBUG, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void debug(String format, Object... arguments) {
        FormattingTuple tuple = MessageFormatter.arrayFormat(format, arguments);
        log(Level.DEBUG, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void debug(String msg, Throwable t) {
        log(Level.DEBUG, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return level <= Level.INFO.ordinal();
    }

    @Override
    public void info(String msg) {
        log(Level.INFO, msg, null);
    }

    @Override
    public void info(String format, Object arg) {
        FormattingTuple tuple = MessageFormatter.format(format, arg);
        log(Level.INFO, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        FormattingTuple tuple = MessageFormatter.format(format, arg1, arg2);
        log(Level.INFO, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void info(String format, Object... arguments) {
        FormattingTuple tuple = MessageFormatter.arrayFormat(format, arguments);
        log(Level.INFO, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void info(String msg, Throwable t) {
        log(Level.INFO, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return level <= Level.WARN.ordinal();
    }

    @Override
    public void warn(String msg) {
        log(Level.WARN, msg, null);
    }

    @Override
    public void warn(String format, Object arg) {
        FormattingTuple tuple = MessageFormatter.format(format, arg);
        log(Level.WARN, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        FormattingTuple tuple = MessageFormatter.format(format, arg1, arg2);
        log(Level.WARN, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void warn(String format, Object... arguments) {
        FormattingTuple tuple = MessageFormatter.arrayFormat(format, arguments);
        log(Level.WARN, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void warn(String msg, Throwable t) {
        log(Level.WARN, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return level <= Level.ERROR.ordinal();
    }

    @Override
    public void error(String msg) {
        log(Level.ERROR, msg, null);
    }

    @Override
    public void error(String format, Object arg) {
        FormattingTuple tuple = MessageFormatter.format(format, arg);
        log(Level.ERROR, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        FormattingTuple tuple = MessageFormatter.format(format, arg1, arg2);
        log(Level.ERROR, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void error(String format, Object... arguments) {
        FormattingTuple tuple = MessageFormatter.arrayFormat(format, arguments);
        log(Level.ERROR, tuple.getMessage(), tuple.getThrowable());
    }

    @Override
    public void error(String msg, Throwable t) {
        log(Level.ERROR, msg, t);
    }

    /**
     * Log levels.
     */
    public enum Level {
        TRACE, DEBUG, INFO, WARN, ERROR, NONE
    }
}
