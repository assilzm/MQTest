package base.exceptions

/**
 * 字符串格式化异常
 */
class StringFormatException extends Exception {
    StringFormatException(String msg) {
        super(msg)
    }

    StringFormatException(String msg, Throwable cause) {
        super(msg, cause)
    }

    StringFormatException(Throwable cause) {
        super(cause)
    }
}
