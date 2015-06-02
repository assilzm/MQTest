package base.browser

import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.DesiredCapabilities

/**
 * 浏览器接口
 */
interface Browser {
    /**
     * 配置浏览器
     * @return 配置
     */
    DesiredCapabilities configure()
    /**
     * 本地启动浏览器
     */
    void localStart()
    /**
     * 远程启动浏览器
     * @param server端url，可以带端口,eg:http://192.168.1.2 or http://192.168.1.2:4444
     */
    void remoteStart(String remoteAddress)


    /**
     * 退出浏览器，并关闭浏览器进程
     */
    void exit()

    /**
     *  打开一个url
     * @param url
     */
    void go(String url)


    /**
     * 取得webdriver实例
     * @return
     */
    WebDriver getDriver()

    /**
     * 窗口最大化
     */
    void maximize()


}
