package base.model

import base.exceptions.JavaScriptExecuteException
import base.utils.LogUtils
import base.utils.ProjectUtils
import org.apache.log4j.Logger
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait

import static TestContentManager.getCurrentJavascriptExecutor
import static TestContentManager.getCurrentWebDriver
import static base.utils.StringUtils.formatString

/**
 * JQuery工具类
 */
class JQModel {

    private final static Logger logger = LogUtils.getLogger(JQModel)

    private final static String JQUERY_VAR = "AT"

    private final static String JQUERY_NAME = "jQuery"

    final static String JQUERY_PIX = "$JQUERY_VAR.$JQUERY_NAME"

    private final static String JQUERY_FILE_NAME = "jquery1.9.1.js"

    private static String jquerySource = null

    private final
    static String jQueryIdentify = "try{return typeof $JQUERY_PIX =='function';}catch(e){return false;}"

    /**
     * 验证页面上是否有自带的JQuery
     * @return 是否含有JQUERY
     */
    static boolean hasJQuery() {
        boolean hasJQuery = false
        try {
            hasJQuery = (boolean) getCurrentJavascriptExecutor().executeScript("return typeof jQuery == 'function';")
        } catch (JavaScriptExecuteException e) {
            logger.error("执行验证jQuery的方法出错，跳过执行。错误信息:" + e.getStackTrace())
        }
        return hasJQuery
    }

    /**
     * 生成页面使用的JQuery字符串，优先使用页面自带的JQuery
     * @param selector dom对象或者选择器
     * @return 生成后的JQuery字符串
     */
    static String createJQueryString(String selector){
        boolean hasJQuery = hasJQuery()
        String jQString = "$JQUERY_PIX($selector)"
        //页面有jQuery优先使用页面自带trigger的事件
        if (hasJQuery)
            jQString = "jQuery($jQString)"
        return jQString
    }

    /**
     * 在jquery没有注入时注入jquery
     */
    static void injectJQueryIfNeeded() {
        if (!isJQueryLoaded())
            injectJQuery()
    }

    /**
     * 判断jquery对象是否已注入
     * @return true or false
     */
    static boolean isJQueryLoaded() {
        boolean loaded
        try {
            loaded = (boolean) getCurrentJavascriptExecutor().executeScript(jQueryIdentify)
        } catch (WebDriverException) {
            loaded = false
        }
        return loaded
    }

    /**
     * 注入jquery
     * @param timeoutInSeconds 注入jquery操作的超时值，默认10s
     */
    static void injectJQuery(int timeoutInSeconds = 10) {
        def rs = new WebDriverWait(getCurrentWebDriver(), timeoutInSeconds, 100)
                .until(new ExpectedCondition<Boolean>() {

            Boolean apply(WebDriver d) {
                def jse = (JavascriptExecutor) d
                jse.executeScript(javascriptContent())
                return (Boolean) jse.executeScript(jQueryIdentify)
            }
        })
        if (!rs) {
            def injectResult = (Boolean) getCurrentJavascriptExecutor().executeScript(jQueryIdentify)
            logger.error(jQueryIdentify + ":" + injectResult)
            throw new JavaScriptExecuteException("inject jquery error after $timeoutInSeconds seconds.")
        }
    }

    /**
     * 读取js文件内容
     * @return
     */
    static String javascriptContent() {
        if (jquerySource == null) {
            //工程中读取jq文件
            File file = new File(ProjectUtils.getProjectResourcesDir() + JQUERY_FILE_NAME)
            jquerySource = file.getText("UTF-8")

            //jar包中读取jq文件
            //jquerySource = Thread.currentThread().getContextClassLoader().getResourceAsStream(JQUERY_FILE_NAME).getText("utf-8")
        }

        //避免冲突，注入jQuery对象为JQUERY_VAR.JQUERY_NAME
        return "window.$JQUERY_VAR = window.$JQUERY_VAR || {};\n" +
                jquerySource +
                "///////////////////////////////////////////////////////////////////////////////\n" +
                "///////////////////////////////////////////////////////////////////////////////\n" +
                "///////////////////////////////////////////////////////////////////////////////\n" +
                "// make jQuery play nice\n" +
                "function JQueryWrapper() {\n" +
                "// public\n" +
                "    /**\n" +
                "     * The wrapper is an object, so it can't act like a function. We supply\n" +
                "     * an explicit init() method to be used where jQuery() previously applied.\n" +
                "     */\n" +
                "    this.init = function (selector, context) {\n" +
                "        return new this.jQuery.fn.init(selector, context);\n" +
                "    };\n" +
                "    this.clean = function (elems, context, fragment) {\n" +
                "        return this.jQuery.clean(elems, context, fragment);\n" +
                "    };\n" +
                "}\n" +
                "JQueryWrapper.prototype.jQuery = jQuery;\n" +
                "jQuery.noConflict(true);\n" +
                "$JQUERY_PIX = new JQueryWrapper().jQuery;"
    }


    /**
     * 生成属性选择器
     * @param attrName 属性名称
     * @param attrValue 属性值
     * @return
     */
    static String createAttrSelector(String selector,String attrName = "text", Object attrValue = null) {
        String value = attrValue instanceof String ?
                "\"${formatString(attrValue)}\"" : attrValue
        return "var attrName = \"$attrName\";\n" +
                "var attrValue = $value;\n" +
                "var arr = [];\n" +
                "${JQUERY_PIX}($selector).each(\n" +
                "    function () {\n" +
                "        var value;\n" +
                "        var el = ${JQUERY_PIX}(this);\n" +
                "        if (el.is(\"input\") && (attrName == \"value\" || attrName == \"text\")) {\n" +
                "            if (attrValue == null)\n" +
                "                value = el.val();\n" +
                "            else\n" +
                "                el.val(attrValue);\n" +
                "        } else if (attrName == \"text\") {\n" +
                "            if (attrValue == null)\n" +
                "                value = el.text();\n" +
                "            else\n" +
                "                el.text(attrValue);\n" +
                "        } else {\n" +
                "            if (attrValue == null)\n" +
                "                value = el.attr(attrName);\n" +
                "            else\n" +
                "                el.attr(attrName, attrValue);\n" +
                "        }\n" +
                "        if (value != null)\n" +
                "            arr.push(value);\n" +
                "    }\n" +
                ");\n" +
                "return arr;"
    }

    /**
     * 为按逗号分隔的选择器增加子选择器
     * @param parentSelector 复选择器，多个选择器用逗号分隔
     * @param subSelector 子选择器，多个选择器用逗号分隔
     * @return 生成后的选择器
     */
    static String appendSelector(String parentSelector, String subSelector) {
        String selector = new String()
        parentSelector.split(",").each { first ->
            subSelector.split(",").each { second ->
                selector += first.concat(" " + second) + ","
            }
        }
        return selector.endsWith(',') ? selector.substring(0, selector.length() - 1) : selector
    }

}
