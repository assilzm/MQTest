package base.utils

import base.browser.Chrome
import base.browser.Firefox
import base.browser.InternetExplorer
import base.browser.Phantomjs
import base.emuns.BrowserType
import base.browser.BrowserFactory
import org.apache.log4j.Logger
import org.openqa.selenium.io.TemporaryFilesystem
import org.openqa.selenium.os.WindowsUtils


/**
 * 执行过程清理类，目前仅对windows有效
 */
class TempCleanUtils {
    protected final static Logger logger = LogUtils.getLogger(TempCleanUtils)

    /**
     * 执行清理，关闭多余的进程及清理临时文件夹
     * @param type 浏览器类型
     */
    static void cleanTemp(BrowserType type = BrowserType.IE) {
        if (WindowsUtils.thisIsWindows()) {
            //关闭意外情况下未关闭的进程
            switch (type) {
                case BrowserType.IE:
                    InternetExplorer.getProcesses().each {
                        killProcess(it)
                    }
                    break
                case BrowserType.FIREFOX:
                    Firefox.getProcesses().each {
                        killProcess(it)
                    }
                    break
                case BrowserType.CHROME:
                    Chrome.getProcesses().each {
                        killProcess(it)
                    }
                    break
                case BrowserType.PHANTOMJS:
                    Phantomjs.getProcesses().each {
                        killProcess(it)
                    }
                    break
                default:
                    logger.error("found not support browser:${BrowserFactory.type.toString()}")
            }
            //删除临时文件
            logger.debug("begin clean temp files.")
            TemporaryFilesystem.getDefaultTmpFS().deleteTemporaryFiles()
            cleanTempFiles()
            logger.debug("clean temp files finished.")

        }
    }

    /**
     *
     * @param process
     */
    static private void killProcess(String process) {
        if (WindowsUtils.thisIsWindows()) {
            try {
                logger.debug("kill process:$process")
                WindowsUtils.executeCommand(System.getenv("windir") + "\\system32\\taskkill.exe", "/f", "/t", "/im", process)
            } catch (WindowsRegistryException) {
                logger.warn("$process process do not exist!")
            }
        }
    }

    /**
     * 清理临时文件夹中的垃圾文件
     */
    static void cleanTempFiles() {
        List<String> profileDirs = ["\\.tmp\$",
                                    "scoped_dir",
                                    "native-platform",
                                    "webdriver-profile",
                                    "userprofile",
                                    "seleniumSslSupport"]
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        for (file in tmpDir.listFiles()) {
            if (file.exists())
                profileDirs.each { fileReg ->
                    if (file.getName() =~ fileReg) {
//                        logger.debug("Cleaning up tmp profile directory: " + file.getName())
                        try {
                            if (file.isDirectory())
                                file.deleteDir()
                            else
                                file.delete()
                        } catch (Exception e) {
                            logger.error("Failed to delete tmp profile ${file.getAbsolutePath()}.", e);
                        }
                    }

                }
        }
    }
}
