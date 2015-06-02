package base.utils

import org.apache.log4j.Logger
import org.apache.log4j.PropertyConfigurator
/**
 * Log类封装
 */
class LogUtils {

    /**
     * 生成给定类的Log4j对象
     * @param className 类名
     * @return Log4j对象
     */
	static Logger getLogger(Class className) {
		config()
		def logger = Logger.getLogger(className)
		return logger
	}

    /**
     * 默认的Log4j配置
     */
    private static void config() {
        Properties pro = new Properties();
        pro.put("log4j.rootLogger", "info,ConsoleLog")
        pro.put("log4j.appender.ConsoleLog", "org.apache.log4j.ConsoleAppender")
        pro.put("log4j.appender.ConsoleLog.Threshold", "debug")
        pro.put("log4j.appender.ConsoleLog.layout", "org.apache.log4j.PatternLayout")
        pro.put("log4j.appender.ConsoleLog.layout.ConversionPattern", "[%5p] %d{yyyy-MM-dd HH:mm:ss} %c - %m%n")
        PropertyConfigurator.configure(pro)
    }

}