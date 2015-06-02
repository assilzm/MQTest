package base.exceptions

/**
 * 日期格式化错误异常
 */
class DateFormatException extends Exception {
    DateFormatException(String msg) {
        super(msg)
    }

    DateFormatException(String msg, Throwable cause) {
        super(msg, cause)
    }

    DateFormatException(Throwable cause) {
        super(cause)
    }
}
