package top.leekm.minihttp.core;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by lkm on 2017/3/20.
 */

public class Log {

    private final static CopyOnWriteArrayList<Logger> sLoggers = new CopyOnWriteArrayList<>();

    public static void log(String tag, String message) {
        tag = timeStamp() + "-" + tag;
        for (Logger logger : sLoggers) {
            logger.log(tag, message);
        }
    }

    public static void warn(String tag, String message) {
        tag = timeStamp() + "-" + tag;
        for (Logger logger : sLoggers) {
            logger.warn(tag, message);
        }
    }

    public static void error(String tag, String message) {
        tag = timeStamp() + "-" + tag;
        for (Logger logger : sLoggers) {
            logger.error(tag, message);
        }
    }

    public static void exception(String tag, Throwable throwable) {
        tag = timeStamp() + "-" + tag;
        for (Logger logger : sLoggers) {
            logger.exception(tag, throwable);
        }
    }

    private static String timeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss:sss");
        return format.format(new Date());
    }

    public static void registLogger(Logger logger) {
        sLoggers.add(logger);
    }

    public static void unRegistLogger(Logger logger) {
        sLoggers.remove(logger);
    }

    public interface Logger {
        void log(String tag, String message);

        void warn(String tag, String message);

        void error(String tag, String message);

        void exception(String tag, Throwable throwable);
    }

}
