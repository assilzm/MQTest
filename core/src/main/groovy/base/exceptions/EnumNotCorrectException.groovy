package base.exceptions

/**
 * √∂æŸ¥ÌŒÛ“Ï≥£
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
