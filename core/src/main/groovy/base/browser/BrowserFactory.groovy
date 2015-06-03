package base.browser

import base.emuns.BrowserType

/**
 * 浏览器工厂类
 */
class BrowserFactory {
    /**
     * 当前浏览器的类型
     */
    static BrowserType type

    /**
     * 当前的浏览器
     */
    static Browser currentBrowser = null

    /**
     * remote服务器的连接信息
     */
    static String remoteWebDriverServer=null

    /**
     * 浏览器是否为本地启动
     */
    private static boolean local = false


    final static Map<BrowserType, String> map = new HashMap<BrowserType, String>()  {
        {
            put(BrowserType.IE, InternetExplorer.class.name)
            put(BrowserType.FIREFOX, Firefox.class.name)
            put(BrowserType.CHROME, Chrome.class.name)
            put(BrowserType.PHANTOMJS, Phantomjs.class.name)

        }
    }

    /**
     * 使用selenium RC远程启动浏览器
     * @param name 浏览器类型
     * @param remoteAddress remote服务器的连接信息
     * @return 当前浏览器对象
     */
    static Browser remoteCreate(BrowserType name, String remoteAddress) {
        type = name
	    remoteAddress= "http://"+remoteAddress.replaceAll(/^http:\/\//,"")
	    currentBrowser = (Browser) Class.forName(map.get(name)).newInstance()
        currentBrowser.remoteStart(remoteAddress)
	    remoteWebDriverServer=remoteAddress
        local = false
        return currentBrowser
    }

    /**
     * 使用本地的WebDriver启动浏览器
     * @param name 浏览器类型
     * @return 当前浏览器对象
     */
    static Browser localCreate(BrowserType name) {
        type = name
        currentBrowser = (Browser) Class.forName(map.get(name)).newInstance()
        currentBrowser.localStart()
        local = true
        return currentBrowser
    }

    /**
     * 返回是否为本地启动的浏览器
     * @return
     */
    static boolean isLocal(){
        return local;
    }

}
