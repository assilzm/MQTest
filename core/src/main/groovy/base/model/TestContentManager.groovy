package base.model

import base.utils.LogUtils
import org.apache.log4j.Logger
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver

/**
 * 执行过程参数仓库类
 */
class TestContentManager {
    private final static Logger logger = LogUtils.getLogger(TestContentManager)

    /**
     * 全局属性的HashMap
     */
    private static Map<String, Object> ScenarioProperty = new HashMap()

    /**
     * 清空缓存数据
     */
    static void clearScenarioProperty() {
        ScenarioProperty.clear()
    }

    /**
     * 从map中添加全局属性键值对
     * @param name 键值对
     */
    static void setScenarioPropertyByMap(Map<String, Object> map) {
        map.each { String key, Object value ->
            setScenarioProperty(key, value)
        }
    }

    /**
     * 添加全局属性键值对,key不区分大小写
     * @param name 全局属性名称
     * @param obj 全局属性值
     */
    static void setScenarioProperty(String key, Object value) {
        ScenarioProperty.put(key.toUpperCase(), value)
    }

    /**
     * 获取指定名称的全局属性的值,key不区分大小写<br>
     *     appAddress:应用程序的访问地址<br>
     *     remoteAddress:远程浏览器的地址
     * @param name 全局属性名称
     * @return the value of the key
     */
    static Object getScenarioProperty(String key) {
        return ScenarioProperty.get(key.toUpperCase())
    }

    /**
     * 获取指定名称的全局属性的字符串值,key不区分大小写<br>
     *     appAddress:应用程序的访问地址<br>
     *     remoteAddress:远程浏览器的地址
     * @param name 全局属性名称
     * @return the value of the key
     */
    static String getScenarioPropertyToString(String key) {
        return (String) getScenarioProperty(key)
    }

    /**
     * 获取当前的<code>WebDriver</code>
     * @return webdriver的实例
     */
    static WebDriver getCurrentWebDriver() {
        return BasicPage.getDriver()
    }
    /**
     * 更新当前的<code>WebDriver</code>
     * @param webdriver的实例
     */
    static void setCurrentWebDriver(WebDriver driver) {
        BasicPage.setDriver(driver)
    }

    /**
     * 获取当前<code>WebDriver</code>对应的<code>js执行器</code>
     * @return js执行器的实例
     */
    static JavascriptExecutor getCurrentJavascriptExecutor() {
        return (JavascriptExecutor) BasicPage.getDriver()
    }

}
