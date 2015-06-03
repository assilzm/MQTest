package base.exceptions

/**
 * �ַ�����ʽ���쳣
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
