package base.exceptions

/**
 * ö�ٴ����쳣
 */
class EnumNotCorrectException extends Exception {
    EnumNotCorrectException(String msg) {
        super(msg)
    }

    EnumNotCorrectException(String msg, Throwable cause) {
        super(msg, cause)
    }

    EnumNotCorrectException(Throwable cause) {
        super(cause)
    }
}
