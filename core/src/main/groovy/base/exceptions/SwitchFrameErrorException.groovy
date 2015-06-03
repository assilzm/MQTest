package base.exceptions

/**
 * �л�frame�쳣
 */
class SwitchFrameErrorException extends Exception {
    SwitchFrameErrorException(String msg) {
        super(msg)
    }

    SwitchFrameErrorException(String msg, Throwable cause) {
        super(msg, cause)
    }

    SwitchFrameErrorException(Throwable cause) {
        super(cause)
    }
}
