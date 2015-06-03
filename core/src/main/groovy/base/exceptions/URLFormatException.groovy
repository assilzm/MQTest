package base.exceptions

/**
 * URL��ʽ���쳣
 */
class URLFormatException extends Exception {
    URLFormatException(String msg) {
        super(msg)
    }

    URLFormatException(String msg, Throwable cause) {
        super(msg, cause)
    }

    URLFormatException(Throwable cause) {
        super(cause)
    }
}
