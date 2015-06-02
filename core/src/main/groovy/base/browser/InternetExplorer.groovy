package base.browser
import base.emuns.BrowserType
import base.utils.LogUtils
import base.utils.ProjectUtils
import base.utils.TempCleanUtils
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.remote.DesiredCapabilities

import static base.model.TestContentManager.currentWebDriver
import static base.utils.Timeout.waitFor

/**
 * IE控制类
 */
class InternetExplorer extends AbstractBrowser implements Browser {



    InternetExplorer() {
        logger = LogUtils.getLogger(InternetExplorer)
    }

    void exit(){
        def driver = getCurrentWebDriver()
        if (driver) {
            waitFor(15, 10, false) {
                driver.quit()
                return true
            }
        }
        setCurrentWebDriver(null)
        if (BrowserFactory.isLocal()) {
            TempCleanUtils.cleanTemp(BrowserType.IE)
        }
    }


    DesiredCapabilities configure() {
        DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer()
        capabilities.merge(capabilitiesFromEnvironment)
        capabilities.setCapability(
                InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                true)
        capabilities.setCapability("ignoreZoomSetting", true);
        capabilities.setCapability("useActiveAccessibility", true)
        return capabilities
    }


    void localStart() {
        String projectPath =  ProjectUtils.projectDir
        System.setProperty("webdriver.ie.driver", projectPath + "/src/main/resources/IEDriverServer.exe")
        def driver = new InternetExplorerDriver(configure())
        maximize()
        setCurrentWebDriver(driver)
    }


    static List<String> getProcesses() {
        ["iexplore.exe", "IEDriverServer.exe"]
    }
}
