package base.exceptions

/**
 * 执行javascript异常
 */
class JavaScriptExecuteException extends Exception {
    JavaScriptExecuteException(String msg) {
        super(msg)
    }

    JavaScriptExecuteException(String msg, Throwable cause) {
        super(msg, cause)
    }

    JavaScriptExecuteException(Throwable cause) {
        super(cause)
    }
}
