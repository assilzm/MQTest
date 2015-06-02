package base.utils

import org.apache.log4j.Logger
import org.openqa.selenium.NoAlertPresentException
import org.openqa.selenium.NoSuchFrameException
import org.openqa.selenium.NoSuchWindowException
import org.openqa.selenium.WebDriverException

import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * 超时控制类
 */
class Timeout {
    private final static Logger logger = LogUtils.getLogger(Timeout)
    final
    static List<Class> baseErrors = [WebDriverException.class, NoSuchFrameException.class, NoSuchWindowException.class, NoAlertPresentException.class, TimeoutException.class]

    /**
     * 超时处理
     * @param timeOutInSeconds 总共的等待时间，单位秒，默认5s
     * @param sleepInMills 每次执行的间隔时间，单位毫秒，默认10ms
     * @param isThrow 是否在执行失败时抛出异常，默认为true
     * @param ignoredExceptions 需要忽略的异常
     * @param bodyAndReturn 带返回boolean结果的闭包
     */
    static void waitFor(int timeOutInSeconds = 5, long sleepInMills = 10,
                        boolean isThrow = true, List<Class> ignoredExceptions = baseErrors,
                        Closure bodyAndReturn) throws Exception {
        Exception ex = null
        Boolean after = false
        long timeout = TimeUnit.SECONDS.toMillis(timeOutInSeconds)
        long start = System.currentTimeMillis()
        sleep(10)
        while (System.currentTimeMillis() - start < timeout) {
            def pool = Executors.newSingleThreadScheduledExecutor()
            def rs
            try {
                rs = pool.submit {
                    after = (boolean) bodyAndReturn.call()
                }
                rs.get(timeOutInSeconds, TimeUnit.SECONDS)
            } catch (Exception e) {
                if (!(e.class in [InterruptedException, ExecutionException, TimeoutException]) && isThrow) {
                    logger.error("${timeOutInSeconds}s执行完毕,返回结果仍然为false.", e)
                    throw e
                }
                ex = e
            } finally {
                if (rs)
                    rs.cancel(true)
            }
            pool.shutdown()
            //若已返回true,则跳出循环
            if (after.equals(true))
                break
            sleep(sleepInMills)
        }
        //是否超时
        if (isThrow && !after) {
            if (ex) {
                logger.error("最后一次执行的错误信息:")
                throw ex
            }
            throw new TimeoutException("Time out in ${timeOutInSeconds}s and return $after")
        }
    }
}
