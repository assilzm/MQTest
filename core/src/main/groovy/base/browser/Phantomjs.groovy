package base.browser

import base.utils.LogUtils
import base.utils.ProjectUtils
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.phantomjs.PhantomJSDriverService
import org.openqa.selenium.remote.DesiredCapabilities

import static base.model.TestContentManager.currentWebDriver
/**
 * Phantomjs控制类
 */
class Phantomjs extends AbstractBrowser {

    /**
     * 需要模拟的UA
     */
    static String USER_AGENT = "User-Agent:Mozilla/5.0 (compatible; MSIE 11.0; Windows NT 8.1)"

    Phantomjs(){
        logger = LogUtils.getLogger(Phantomjs)
    }


    DesiredCapabilities configure() {
        DesiredCapabilities capabilities = DesiredCapabilities.phantomjs()
        capabilities.merge(capabilitiesFromEnvironment)
        return capabilities
    }


    void localStart() {
        System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, ProjectUtils.projectResourcesDir + "phantomjs.exe")
        def driver = new PhantomJSDriver(configure())
        setCurrentWebDriver(driver)
    }

    static List<String> getProcesses() {
        ["phantomjs.exe"]
    }

}
