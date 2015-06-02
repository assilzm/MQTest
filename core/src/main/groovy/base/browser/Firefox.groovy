package base.browser
import base.emuns.BrowserType
import base.model.TestContentManager
import base.utils.LogUtils
import base.utils.TempCleanUtils
import base.utils.Timeout
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


    void exit(){
        def driver = TestContentManager.getCurrentWebDriver()
        if (driver) {
            Timeout.waitFor(15, 10, false) {
                driver.quit()
                return true
            }
        }
        TestContentManager.setCurrentWebDriver(null)
        if (BrowserFactory.isLocal()) {
            TempCleanUtils.cleanTemp(BrowserType.FIREFOX)
        }
    }
}
