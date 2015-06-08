package base.browser

import base.model.TestContentManager
import base.utils.LogUtils
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxProfile
import org.openqa.selenium.remote.DesiredCapabilities
/**
 * Firefox控制类
 */
class Firefox extends AbstractBrowser implements Browser {


    Firefox(){
        logger = LogUtils.getLogger(Firefox)
    }


    DesiredCapabilities configure() {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox()
        capabilities.merge(capabilitiesFromEnvironment)
        return capabilities
    }


    void localStart() {
        def temp=System.getProperty("java.io.tmpdir")
        def firefoxProfileDir=new File(temp+"firefoxWebdriver/")
        if (!firefoxProfileDir.exists()||!firefoxProfileDir.isDirectory())
            firefoxProfileDir.mkdirs()
        FirefoxProfile profile = new FirefoxProfile(firefoxProfileDir);
        def driver = new FirefoxDriver(null,profile,configure())
        maximize()
        TestContentManager.setCurrentWebDriver(driver)
    }

    static List<String> getProcesses() {
        ["firefox.exe"]
    }

}
