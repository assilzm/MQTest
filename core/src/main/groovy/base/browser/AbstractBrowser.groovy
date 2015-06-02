package base.browser
import base.exceptions.URLFormatException
import base.model.TestContentManager
import org.apache.log4j.Logger
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.Augmenter
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.Proxy

import static TestContentManager.getCurrentWebDriver
import static TestContentManager.setCurrentWebDriver
import static org.openqa.selenium.Proxy.ProxyType.DIRECT

/**
 * 浏览器基类
 */
abstract class AbstractBrowser implements Browser {

    Logger logger


    final static String PROXY_NAME = "proxy"

    final static String DEFAULT_REMOTE_PORT = "4444"


    private Proxy proxy = new Proxy()

    private Proxy directProxy = new Proxy().setProxyType(DIRECT)

    /**
     * 从环境变量中读取参数设置代理，环境变量参数名默认为proxy,格式为http://127.0.0.1或者http://127.0.0.1:8087
     * @see #PROXY_NAME
     */
    void setProxyFromEnvironment() {
        def proxy = System.getenv(PROXY_NAME)
        if (!proxy) {
            proxy = System.getProperty(PROXY_NAME)
        }
        if (proxy)
            setProxy(proxy)
    }

    /**
     * 使用url字符串配置代理
     * @param proxyString 代理的url字符串
     */
    void setProxy(String proxyString) {
        proxy.setHttpProxy(proxyString)
    }

    /**
     * 从环境变量中取得代理信息
     * @return 代理的实例
     */
    Proxy getProxyFromEnvironment() {
        setProxyFromEnvironment()
        if (proxy.httpProxy) {
            return proxy
        } else {
            return directProxy
        }
    }

    /**
     * 从环境变量中配置启动浏览器所需的参数，具体参数细节请参考selenium的配置API
     * @return 配置的实例
     */
    DesiredCapabilities getCapabilitiesFromEnvironment() {
        DesiredCapabilities capabilities = new DesiredCapabilities()

        capabilities.setJavascriptEnabled(true)
        capabilities.setCapability(CapabilityType.ForSeleniumServer.AVOIDING_PROXY, true)
        capabilities.setCapability(CapabilityType.ForSeleniumServer.ONLY_PROXYING_SELENIUM_TRAFFIC, true)
        System.setProperty("http.nonProxyHosts", "localhost")
        capabilities.setCapability(
                CapabilityType.PROXY, getProxyFromEnvironment())
        if (proxy.httpProxy)
            logger.debug("set http proxy:${proxy.httpProxy}")
        capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true)
        return capabilities
    }

    /**
     * 打开某个URL
     * @param url url字符串，HTTP地址可以不带前缀
     */
    void go(String url) {
        def driver = getCurrentWebDriver()
        if (!(url=~/^(http?|file):\/\//))
            url = "http://" + url
        if (driver) {
            driver.get(url)
            setCurrentWebDriver(driver)
        }
    }

    /**
     * 取得当前浏览器的webdriver实例
     * @return webdriver的实例
     */
    WebDriver getDriver() {
        getCurrentWebDriver()
    }

    /**
     * 浏览器最大化
     */
    void maximize() {
        def driver = getCurrentWebDriver()
        if (driver)
            driver.manage().window().maximize()
    }

    /**
     * 使用selenium remote远程启动浏览器
     * @param remoteAddress remote服务器的连接信息，如不带端口，会默认使用4444端口
     */
    void remoteStart(String remoteAddress) {
        String url = ""
        String port = DEFAULT_REMOTE_PORT
        def remoteAddressMacth = remoteAddress =~ /(http:\/\/)?([^\/:]+)(?::(\d+))?/
        if (remoteAddressMacth.find()) {
            url = remoteAddressMacth.group(2)
            if (remoteAddressMacth.group(3))
                port = remoteAddressMacth.group(3)
        } else {
            throw new URLFormatException("the given url[$remoteAddress],format is not correct ")
        }
        def driver = new Augmenter().augment(new RemoteWebDriver(new URL("http://${url}:${port}"), configure()))
        maximize()
        setCurrentWebDriver(driver)
    }
}
