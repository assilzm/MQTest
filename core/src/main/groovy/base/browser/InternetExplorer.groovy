package base.browser

import base.utils.LogUtils
import base.utils.ProjectUtils
import org.openqa.selenium.ie.InternetExplorerDriver
import org.openqa.selenium.remote.DesiredCapabilities

import static base.model.TestContentManager.currentWebDriver
/**
 * IE控制类
 */
class InternetExplorer extends AbstractBrowser implements Browser {



    InternetExplorer() {
        logger = LogUtils.getLogger(InternetExplorer)
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
