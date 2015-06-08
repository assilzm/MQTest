package base.browser

import base.emuns.OperationType
import base.utils.LogUtils
import base.utils.OperationUtils
import base.utils.ProjectUtils
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.DesiredCapabilities

import static base.asserts.Assert.assertNotNull
import static base.model.TestContentManager.currentWebDriver
/**
 * chrome控制类
 */
class Chrome extends AbstractBrowser {


    Chrome() {
        logger = LogUtils.getLogger(Chrome)
    }


    DesiredCapabilities configure() {
        String chromeDriver
        OperationType operationName = OperationUtils.operation
        def resourceDir=ProjectUtils.projectResourcesDir
        if (operationName==OperationType.WINDOWS)
            chromeDriver = resourceDir + "chromedriver.exe"
        if (operationName==OperationType.MAC)
            chromeDriver = resourceDir + "chromedriver"
        assertNotNull(chromeDriver)
        System.setProperty("webdriver.reap_profile", "true")
        System.setProperty("webdriver.chrome.driver", chromeDriver)
        DesiredCapabilities capabilities = DesiredCapabilities.chrome()
        ChromeOptions chromeOptions=new ChromeOptions()
        chromeOptions.addArguments("test-type")
        chromeOptions.addArguments("disable-sync-synced-notifications")
        chromeOptions.addArguments("disable-desktop-notifications")
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions)
        capabilities.setCapability("enable-sync-synced-notifications", false)
        capabilities.merge(capabilitiesFromEnvironment)
        return capabilities
    }


    void localStart() {
        def driver = new ChromeDriver(configure())
        maximize()
        setCurrentWebDriver(driver)
    }

    static List<String> getProcesses() {
        ["chrome.exe", "chromedriver.exe"]
    }


}
