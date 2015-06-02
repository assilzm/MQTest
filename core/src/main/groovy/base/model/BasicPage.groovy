package base.model

import base.emuns.EventType
import base.exceptions.JavaScriptExecuteException
import base.exceptions.SwitchFrameErrorException
import base.utils.LogUtils
import base.utils.StringUtils
import base.utils.TimerUtils
import org.apache.log4j.Logger
import org.openqa.selenium.*
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

import java.util.NoSuchElementException
import java.util.regex.Pattern

import static EventType.MOUSEOUT
import static EventType.MOUSEOVER
import static StringUtils.getList
import static StringUtils.stringHasAllListString
import static TestContentManager.currentJavascriptExecutor
import static base.asserts.Assert.*
import static base.model.JQModel.JQUERY_PIX
import static base.model.JQModel.createAttrSelector
import static base.model.JQModel.createJQueryString
import static base.model.JQModel.injectJQueryIfNeeded
import static base.utils.Timeout.waitFor

/**
 * 抽象的web页面,包含页面的基本操作
 */
class BasicPage {

    /**
     * logger实例
     */
    private final static Logger logger = LogUtils.getLogger(BasicPage)

    /**
     * webdriver实例
     */
    public static WebDriver driver

    /*****************************************默认参数***********************************************************/

    /**
     * 当前frame路径表
     */
    static List<String> framePath = new ArrayList<>()

    /**
     * 让元素闪烁的样式
     */
    protected final static String flashStyle = "background-color:red; outline:1px solid rgb(136,255,136);"

    /**
     * 默认的切换窗口的超时时间
     */
    static int SWITCH_WINDOW_TIMEOUT_IN_SECONDS = 30

    /**
     * 默认的查找元素的超时时间
     */
    static int FIND_ELEMENT_TIMEOUT_IN_SECONDS = 0

    /**
     * 默认元素闪烁次数
     */
    static int FLASH_ELEMENT_TIMEOUT_IN_SECONDS = 1

    /**
     * 默认的切换frame的超时时间
     */
    static int SWITCH_FRAME_TIMEOUT_IN_SECONDS = 10

    /**
     * 默认的切换到Alert的超时时间
     */
    static int SWITCH_ALERT_TIMEOUT_IN_SECONDS = 5

    /**
     * 用于分隔多个frame的分隔符
     */
    static String FRAME_SEPARATOR = "->"

    /**
     * 是否强制切换frame
     */
    static boolean IS_FORCE_SWITCH_FRAME = false


    final static String WEB_ELEMENT_ARGUMENT = "arguments[0]"

    private final static String UUID_MATCHER = /^[0-9a-z]{8}(-[0-9a-z]{4}){3}-[0-9a-z]{12}$/

    /** ******************************************************************************************************/

    /***************************************对webdriverAPI的简单封装*******************************************/

    /**
     * 设置driver实例
     * @param driver
     */
    static void setDriver(WebDriver driver) {
        this.driver = driver
    }

    /**
     * 取得driver实例
     * @return driver实例
     */
    static WebDriver getDriver() {
        return driver
    }

    /**
     * 取得当前窗口标题
     * @return 当前窗口标题
     */
    String getTitle() {
        try {
            return driver.getTitle()
        } catch (NoSuchWindowException) {
            return null
        }
    }

    /**
     * 取得当前dom的源代码
     * @return 当前dom的源代码
     */
    String getPageSource() {
        return driver.getPageSource()
    }

    /**
     * 取得当前dom的url地址
     * @return 当前dom的url地址
     */
    String getCurrentUrl() {
        return driver.getCurrentUrl()
    }

    /**
     * 取得当前driver的窗口句柄
     * @return 句柄，如当前窗口已关闭，则返回null
     */
    String getWindowHandle() {
        try {
            return driver.getWindowHandle()
        } catch (NoSuchWindowException) {
            return null
        }
    }

    /**
     * 取得当前driver的所有窗口句柄
     * @return 所有窗口句柄的列表
     */
    List<String> getWindowHandles() {
        return driver.getWindowHandles().toList()
    }

    /**
     * 关闭当前窗口
     */
    void close() {
        driver.close()
    }

    /** ******************************************************************************************************/

    /*****************************************元素的基本操作***************************************************/

    /**
     * 使用jquery选择器查找元素，仅会返回第一个匹配的元素
     * @param selector 元素的选择器
     * @param timeoutInSeconds 超时时间，如为0，则不使用超时控制
     * @return 匹配的第一个元素，如元素不存在，返回null
     */
    WebElement findElementByJQuery(String selector, int timeoutInSeconds = FIND_ELEMENT_TIMEOUT_IN_SECONDS) {
        List<WebElement> elements = findElementsByJQuery(selector, timeoutInSeconds)
        return elements?.size() ? elements.get(0) : null

    }

    /**
     * 使用jquery选择器查找多个元素
     * @param selector 元素的选择器
     * @param timeoutInSeconds 超时时间，如为0，则不使用超时控制
     * @return 匹配的所有元素列表
     */
    List<WebElement> findElementsByJQuery(String selector, int timeoutInSeconds = FIND_ELEMENT_TIMEOUT_IN_SECONDS) {
        List<WebElement> els
        assertGreater("超时时间必须大于等于0", timeoutInSeconds, -1)
        String jQselector = "return ${JQUERY_PIX}(\"$selector\")"
        if (timeoutInSeconds == 0)
            els = (List<WebElement>) executeScript(jQselector)
        else
            waitFor(timeoutInSeconds, 100, false) {
                els = (List<WebElement>) executeScript(jQselector)
                return true
            }
        return els == null ? new ArrayList<WebElement>() : els
    }

    /**
     * 使用jquery选择器在一个元素中查一个子元素
     * @param selector 元素的选择器
     * @param subElementsSelector 子元素的选择器
     * @return 匹配的第一个元素，如元素不存在，返回null
     */
    WebElement findElementOfElementByJQuery(String selector, String subElementsSelector) {
        return findElementOfElementByJQuery(findElementByJQuery(selector), subElementsSelector)
    }

    /**
     * 使用jquery选择器在一个元素中查一个子元素
     * @param el 元素
     * @param subElementsSelector 子元素的选择器
     * @return 匹配的第一个元素，如元素不存在，返回null
     */
    WebElement findElementOfElementByJQuery(WebElement el, String subElementsSelector) {
        List<WebElement> els = findElementsOfElementByJQuery(el, subElementsSelector)
        return els.size() == 0 ? null : els.get(0)
    }

    /**
     * 使用jquery选择器在一个元素中查一组子元素
     * @param selector 元素的选择器
     * @param subElementsSelector 子元素的选择器
     * @return 匹配的所有元素列表，如元素不存在，返回空
     */
    List<WebElement> findElementsOfElementByJQuery(String selector, String subElementsSelector) {
        return findElementsOfElementByJQuery(findElementByJQuery(selector), subElementsSelector)
    }

    /**
     * 使用jquery选择器在一个元素中查一组子元素
     * @param el 元素
     * @param subElementsSelector 子元素的选择器
     * @return 匹配的所有元素列表，如元素不存在，返回空
     */
    List<WebElement> findElementsOfElementByJQuery(WebElement el, String subElementsSelector) {
        List<WebElement> els = (List<WebElement>) executeScriptIfCanUseLocalJQuery(el,
                ".find(\"$subElementsSelector\").get();")
        return els == null ? new ArrayList<WebElement>() : els
    }

    /**
     * 鼠标移动到元素，仅对第一个找到的元素有效
     * @param selector 元素的选择器
     */
    void moveToElement(String selector) {
        moveToElement(findElementByJQuery(selector))
    }

    /**
     * 鼠标移动到元素
     * @param el 元素
     */
    void moveToElement(WebElement el) {
        trigger(el, MOUSEOVER)
    }

    /**
     * 鼠标移出元素，仅对第一个找到的元素有效
     * @param selector 元素的选择器
     */
    void moveOutElement(String selector) {
        moveOutElement(findElementByJQuery(selector))
    }

    /**
     * 鼠标移出元素
     * @param el 元素
     */
    void moveOutElement(WebElement el) {
        trigger(el, MOUSEOUT)
    }

    /**
     * 在input、textarea中输入内容，仅会在找到的第一个元素中输入
     * @param selector 元素的选择器
     * @param content 内容
     */
    void type(String selector, String content) {
        type(findElementByJQuery(selector), content)
    }

    /**
     * 在input,textarea中输入内容
     * @param el 元素
     * @param content 内容
     */
    void type(WebElement el, String content) {
        expectElementTagName("type方法仅对input,textarea元素生效，您提供的元素为[${el.getTagName()}]", el, "input", "textarea")
        focus(el)
        flash(el)
        executeScriptIfCanUseLocalJQuery(el, ".val(\"${StringUtils.formatString(content)}\");")
    }

    /**
     * 使用JS方法点击元素，仅会点击找到的第一个元素
     * @param selector 元素的选择器
     */
    void clickByJs(String selector) {
        clickByJs(findElementByJQuery(selector))
    }

    /**
     * 使用JS方法点击元素
     * @param el 元素的选择器
     */
    void clickByJs(WebElement el) {
        focus(el)
        flash(el)
        executeScriptIfCanUseLocalJQuery(el, ".click();")
    }

    /**
     * 点击元素，仅会点击找到的第一个元素
     * @param selector 元素的选择器
     */
    void click(String selector) {
        click(findElementByJQuery(selector))
    }

    /**
     * 点击元素
     * @param el 元素
     */
    void click(WebElement el) {
        focus(el)
        flash(el)
        el.click()
    }

    /**
     * 双击元素，仅会点击找到的第一个元素
     * @param selector 元素的选择器
     */
    void doubleClick(String selector) {
        doubleClick(findElementByJQuery(selector))
    }

    /**
     * 双击元素
     * @param el 元素
     */
    void doubleClick(WebElement el) {
        focus(el)
        flash(el)
        new Actions(driver).doubleClick(el).perform()
    }

    /**
     * 双击元素，仅会点击找到的第一个元素
     * @param selector 元素的选择器
     */
    void doubleClickByJs(String selector) {
        doubleClickByJs(findElementByJQuery(selector))
    }

    /**
     * 双击元素
     * @param el 元素
     */
    void doubleClickByJs(WebElement el) {
        focus(el)
        flash(el)
        //为增加兼容性，优先使用页面自带的jQuery双击
        executeScriptIfCanUseLocalJQuery(el, ".dblclick()")
    }

    /**
     * 将焦点定位至某元素，该方法会将该元素置为页面可视区域，仅会点击找到的第一个元素
     * @param selector 元素的选择器
     */
    void focus(String selector) {
        focus(findElementByJQuery(selector))
    }

    /**
     * 将焦点定位至某元素，该方法会将该元素置为页面可视区域
     * @param el 元素
     */
    void focus(WebElement el) {
        try {
            executeScriptIfCanUseLocalJQuery(el, ".focus();")
        } catch (JavaScriptExecuteException) {
            logger.error("将元素置为焦点错误，跳过执行。")
        }
    }

    /**
     * 将焦点移出某元素
     * @param selector 元素的选择器
     */
    void blur(String selector) {
        blur(findElementByJQuery(selector))
    }

    /**
     * 将焦点移出某元素
     * @param el 元素
     */
    void blur(WebElement el) {
        try {
            executeScriptIfCanUseLocalJQuery(el, ".blur();")
        } catch (JavaScriptExecuteException) {
            logger.error("将焦点移出某元素错误，跳过执行。")
        }
    }

    /**
     * 移除元素，对所有匹配元素有效
     * @param selector 元素的选择器
     */
    void remove(String selector) {
        executeScriptIfCanUseLocalJQuery(selector, ".remove();")
    }

    /**
     * 移除某元素
     * @param el 元素
     */
    void remove(WebElement el) {
        executeScriptIfCanUseLocalJQuery(el, ".remove();")
    }

    /**
     * 移除元素的属性，对所有匹配元素有效
     * @param selector 元素的选择器
     * @param attributeName 属性名
     */
    void removeAttribute(String selector, String attributeName) {
        executeScriptIfCanUseLocalJQuery(selector, ".removeAttr(\"$attributeName\");")
    }

    /**
     * 移除元素的属性，对所有匹配元素有效
     * @param el 元素
     * @param attributeName 属性名
     */
    void removeAttribute(WebElement el, String attributeName) {
        executeScriptIfCanUseLocalJQuery(el, ".removeAttr(\"$attributeName\");")
    }

    /**
     * 取得当前焦点所在的元素
     * @return 当前焦点所在的元素
     */
    WebElement getActiveElement() {
        return (WebElement) executeScript(
                "return document.activeElement;")
    }

    /**
     * 在select元素中选择某个选项，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @param optionText 需要选择的项目文本
     */
    void select(String selector, String optionText) {
        select(findElementByJQuery(selector), [optionText])
    }

    /**
     * 在select元素中选择某个选项，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @param optionText 需要选择的多个项目文本
     */
    void select(String selector, List<String> optionText) {
        select(findElementByJQuery(selector), optionText)
    }

    /**
     * 在select元素中选择某个选项
     * @param el 元素
     * @param optionText 需要选择的多个项目文本
     */
    void select(WebElement el, List<String> optionText) {
        List<Pattern> options = new ArrayList<>()
        optionText.each {
            assertNotNull("要选中的文本必须存在", it)
            Pattern pattern = Pattern.compile("^$it\$")
            options.add(pattern)
        }
        selectByRegex(el, options)
    }

    /**
     * 在select元素中选择某个选项，正则匹配，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @param optionText 需要选择的项目文本
     */
    void selectByRegex(String selector, Pattern optionPattern) {
        selectByRegex(selector, [optionPattern])
    }

    /**
     * 在select元素中选择某个选项，正则匹配，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @param optionText 需要选择的多个项目文本
     */
    void selectByRegex(String selector, List<Pattern> optionPatterns) {
        selectByRegex(findElementByJQuery(selector), optionPatterns)
    }

    /**
     * 在select元素中选择某个选项，正则匹配
     * @param el 元素
     * @param optionText 需要选择的多个项目文本
     */
    void selectByRegex(WebElement el, List<Pattern> optionPatterns) {
        expectElementTagName("type方法仅对input,textarea元素生效，您提供的元素为[${el.getProperties()}]", el, "select")
        List<WebElement> options = getOptions(el)
        List<String> selectedOptions = new ArrayList<>()
        if (!(getAttribute(el, "multiple") == "multiple")) {
            click(el)
        }
        for (pattern in optionPatterns) {
            assertNotNull("要选中的内容必须存在", pattern)
            for (int i in 0..<options.size()) {
                String optionText = getText(options.get(i))
                if (optionText =~ pattern) {
                    selectedOptions.add(optionText)
                    if (!isElementSelected(options.get(i)))
                        click(options.get(i))
                    break
                }
            }
        }
        if (selectedOptions.size() != optionPatterns.size())
            throw new NoSuchElementException("元素中包含如下选项：${options.toString()},需要选中的选项[${optionPatterns.join(",")}],只选中了${selectedOptions.toString()}。")
    }

    /**
     * 复选框是否被选中，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @return 选择的状态
     */
    boolean isCheckBoxSelected(String selector) {
        return isCheckBoxSelected(findElementByJQuery(selector))
    }

    /**
     * 复选框是否被选中
     * @param el 元素
     * @return 选择的状态
     */
    boolean isCheckBoxSelected(WebElement el) {
        expectElementValue("复选框是否选中仅对raido有效，您提供的元素为[${el.getProperties()}]", el, "type", "checkbox")
        return isElementSelected(el)

    }

    /**
     * 单选框是否被选中，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @return 选择的状态
     */
    boolean isRadioSelected(String selector) {
        isRadioSelected(findElementByJQuery(selector))

    }

    /**
     * 单选框是否被选中
     * @param el 元素
     * @return 选择的状态
     */
    boolean isRadioSelected(WebElement el) {
        expectElementValue("单选框是否选中仅对raido有效，您提供的元素为[${el.getProperties()}]", el, "type", "radio")
        return isElementSelected(el)
    }

    /**
     * 元素是否被选中公用方法，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @return 选择的状态
     */
    boolean isElementSelected(String selector) {
        return isElementSelected(findElementByJQuery(selector))
    }

    /**
     * 元素是否被选中公用方法
     * @param el 元素
     * @return 选择的状态
     */
    boolean isElementSelected(WebElement el) {
        assertElemenNotNull(el)
        return el.isSelected()
    }

    /**
     * 选中单选框，仅对第一个匹配的元素有效
     * @param selector 选择器
     */
    void selectRadio(String selector) {
        selectRadio(findElementByJQuery(selector))
    }

    /**
     * 选中单选框
     * @param el 元素
     */
    void selectRadio(WebElement el) {
        assertTrue("需要操作的单选框必须是可操作的。", el.isEnabled())
        if (!isRadioSelected(el))
            click(el)
    }

    /**
     * 控制复选框公用方法
     * @param el 元素
     * @param isChoice 是否选中
     */
    void controlCheckbox(WebElement el, boolean isChoice) {
        assertTrue("需要操作的复选框必须是可操作的。", el.isEnabled())
        if (isChoice != isCheckBoxSelected(el))
            click(el)
    }

    /**
     * 选中复选框，仅对第一个匹配的元素有效
     * @param selector 选择器
     */
    void selectCheckBox(String selector) {
        selectCheckBox(findElementByJQuery(selector))
    }

    /**
     * 选中复选框
     * @param el 元素
     */
    void selectCheckBox(WebElement el) {
        assertElemenNotNull(el)
        controlCheckbox(el, true)
    }

    /**
     * 取消选中复选框，仅对第一个匹配的元素有效
     * @param selector 选择器
     */
    void unSelectCheckBox(String selector) {
        unSelectCheckBox(findElementByJQuery(selector))
    }

    /**
     * 取消选中复选框
     * @param el 元素
     */
    void unSelectCheckBox(WebElement el) {
        assertElemenNotNull(el)
        controlCheckbox(el, false)
    }

    /**
     * 取得Select元素的所有选项的文本，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @return select元素的所有选项的文本
     */
    List<String> getOptionTexts(String selector) {
        return getOptionTexts(findElementByJQuery(selector))
    }

    /**
     * 取得Select元素的所有选项的文本
     * @param el 元素
     * @return select元素的所有选项的文本
     */
    List<String> getOptionTexts(WebElement el) {
        return getTexts(getOptions(el))
    }

    /**
     * 取得Select元素的所有选项
     * @param selector 元素的选择器
     * @return select元素的所有选项
     */
    List<WebElement> getOptions(String selector) {
        return getOptions(findElementByJQuery(selector))
    }

    /**
     * 取得Select元素的所有选项
     * @param el 元素
     * @return select元素的所有选项
     */
    List<WebElement> getOptions(WebElement el) {
        assertElemenNotNull(el)
        return findElementsOfElementByJQuery(el, "option")
    }

    /**
     * 取得Select元素的已选中选项，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @return select元素的已选项
     */
    String getSelectedOptionText(String selector) {
        return getSelectedOptionText(findElementByJQuery(selector))
    }

    /**
     * 取得Select元素的第一个已选中选项
     * @param el 元素
     * @return select元素的第一个已选项
     */
    String getSelectedOptionText(WebElement el) {
        List<String> list = getSelectedOptionTexts(el)
        return list?.size() ? list.get(0) : null
    }

    /**
     * 取得Select元素的第一个已选中选项
     * @param selector 元素的选择器
     * @return select元素的第一个已选项
     */
    WebElement getSelectedOption(String selector) {
        return getSelectedOption(findElementByJQuery(selector))
    }

    /**
     * 取得Select元素的第一个已选中选项
     * @param el 元素
     * @return select元素的第一个已选项
     */
    WebElement getSelectedOption(WebElement el) {
        List<WebElement> list = getSelectedOptions(el)
        return list?.size() ? list.get(0) : null
    }

    /**
     * 取得Select元素的所有已选中选项，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @return select元素的已选项
     */
    List<String> getSelectedOptionTexts(String selector) {
        return getSelectedOptionTexts(findElementByJQuery(selector))
    }

    /**
     * 取得Select元素的所有已选中选项的文本，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @return select元素的已选项的文本
     */
    List<String> getSelectedOptionTexts(WebElement el) {
        return getTexts(getSelectedOptions(el))
    }

    /**
     * 取得Select元素的所有已选中选项
     * @param selector 元素的选择器
     * @return select元素的已选项
     */
    List<WebElement> getSelectedOptions(String selector) {
        return getSelectedOptions(findElementByJQuery(selector))
    }

    /**
     * 取得Select元素的所有已选中选项
     * @param selector 元素的选择器
     * @return select元素的已选项
     */
    List<WebElement> getSelectedOptions(WebElement el) {
        assertElemenNotNull(el)
        return findElementsOfElementByJQuery(el, "option:selected")
    }

    /**
     * 取得元素的文本，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @return 元素的显示文本
     */
    String getText(String selector) {
        return getAttribute(findElementByJQuery(selector))
    }

    /**
     * 取得多个元素的文本
     * @param selector 元素的选择器
     * @return 元素的显示文本列表
     */
    List<String> getTexts(String selector) {
        return getAttributes(selector, "text")
    }

    /**
     * 取得元素的文本
     * @param el 元素
     * @return 元素的显示文本
     */
    String getText(WebElement el) {
        assertElemenNotNull(el)
        return getAttribute(el, "text")
    }

    /**
     * 取得多个元素的文本
     * @param els 元素
     * @return 元素的显示文本列表
     */
    List<String> getTexts(List<WebElement> els) {
        assertElemenNotNull(els)
        List<String> texts = new ArrayList<>()
        els.each {
            texts.add(getText(it))
        }
        return texts
    }

    /**
     * 取得元素的某个属性值
     * @param selector 元素的选择器
     * @param attrName 属性名，如为text,则取得显示文本
     * @return 属性值，如不存在返回null
     */
    String getAttribute(String selector, String attrName) {
        return getAttribute(findElementByJQuery(selector), attrName)
    }

    /**
     * 取得元素的某个属性值
     * @param el 元素
     * @param attrName 属性名，如为text,则取得显示文本
     * @return 属性值，如不存在返回null
     */
    String getAttribute(WebElement el, String attrName = "text") {
        List<String> values = getAttributes([el], attrName)
        return values?.size() ? values.get(0) : null
    }

    /**
     * 设置元素的某个属性值
     * @param el 元素
     * @param attrName 属性名，如为text,则设置显示文本
     * @param attrValue 需要设置的属性值
     */
    void setAttribute(WebElement el, String attrName, Object attrValue) {
        setAttributes([el], attrName, attrValue)
    }

    /**
     * 设置元素的属性值
     * @param selector 元素的选择器
     * @param attrName 属性名，如为text,则设置显示文本
     * @param attrValue 需要设置的属性值
     */
    void setAttribute(String selector, String attrName, Object attrValue) {
        setAttribute(findElementByJQuery(selector), attrName, attrValue)
    }

    /**
     * 设置元素的属性值，对多个元素有效
     * @param selector 元素的选择器
     * @param attrName 属性名，如为text,则设置显示文本
     * @param attrValue 需要设置的属性值
     */
    List<String> getAttributes(String selector, String attrName = "text") {
        return getAttributes(findElementsByJQuery(selector), attrName)
    }

    /**
     * 设置多个元素的属性值
     * @param els 元素的列表
     * @param attrName 属性名，如为text,则设置显示文本
     * @param attrValue 需要设置的属性值
     */
    List<String> getAttributes(List<WebElement> els, String attrName = "text") {
        List<String> attrs = new ArrayList<String>()
        List<String> retValues = (List<String>) executeScript(createAttrSelector(WEB_ELEMENT_ARGUMENT, attrName), els)
        if (retValues != null)
            retValues.each {
                attrs.add(it.trim())
            }
        return attrs
    }

    /**
     * 设置元素的属性值，对所有匹配元素有效
     * @param selector 元素的选择器
     * @param attrName 属性名，如为text,则设置显示文本
     * @param attrValue 需要设置的属性值
     */
    void setAttributes(String selector, String attrName, Object attrValue) {
        executeScript(createAttrSelector("\"$selector\"", attrName, attrValue))
    }

    /**
     * 设置元素的属性值，对所有匹配元素有效
     * @param els 元素列表
     * @param attrName 属性名，如为text,则设置显示文本
     * @param attrValue 需要设置的属性值
     */
    void setAttributes(List<WebElement> els, String attrName, Object attrValue) {
        assertElemenNotNull(els)
        executeScript(createAttrSelector(WEB_ELEMENT_ARGUMENT, attrName, attrValue), els)
    }

    /**
     * 让元素闪烁，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @param times 闪烁次数，默认为{@link #FLASH_ELEMENT_TIMEOUT_IN_SECONDS}
     */
    void flash(String selector, int times = FLASH_ELEMENT_TIMEOUT_IN_SECONDS) {
        flash(findElementByJQuery(selector), times)
    }

    /**
     * 让元素闪烁
     * @param el 元素
     * @param times 闪烁次数，默认为{@link #FLASH_ELEMENT_TIMEOUT_IN_SECONDS}
     */
    void flash(WebElement el, int times = FLASH_ELEMENT_TIMEOUT_IN_SECONDS) {
        if (times > 0) {
            assertElemenNotNull(el)
            String attributeName = "style"
            String oldStyle = getAttribute(el, attributeName)
            try {
                times.times {
                    setAttribute(el, attributeName, flashStyle)
                    if (oldStyle == null || oldStyle.length() == 0) {
                        removeAttribute(el, attributeName)
                    } else {
                        setAttribute(el, attributeName, oldStyle)
                    }
                }
            } catch (JavaScriptExecuteException e) {
                logger.error("闪烁元素失败，跳过闪烁.错误信息：" + e.getStackTrace())
            }
        } else if (times < 0) {
            throw new IllegalArgumentException("闪烁次数必须大于等于0")
        }
    }

    /**
     * 执行一段javascript
     * @param script javascript代码
     * @param args 参数列表
     * @return 设置的返回值，如没设置返回值则返回null
     */
    Object executeScript(String script, Object... args) {
        injectJQueryIfNeeded()
        try {
            getCurrentJavascriptExecutor().executeScript(script, args)
        } catch (WebDriverException e) {
            throw new JavaScriptExecuteException(script, e.getCause())
        }
    }

    /**
     * 异步执行一段js
     * @param delayInMills 延迟执行的时间，单位为毫秒
     * @param script javascript代码
     * @param args 参数列表
     */
    void executeAsyncScript(long delayInMills = 100, String script, Object... args) {
        executeScript(
                "window.setTimeout( function(){" +
                        script +
                        "},${delayInMills})", args)
    }

    /**
     * 执行一个触发器，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @param evt 事件名称
     */
    void trigger(String selector, EventType evt) {
        trigger(findElementByJQuery(selector), evt)
    }

    /**
     * 执行一个触发器
     * @param el 元素
     * @param evt 事件名称
     */
    void trigger(WebElement el, EventType evt) {
        assertElemenNotNull(el)
        String function = ".trigger('${evt.value}')"
        executeScriptIfCanUseLocalJQuery(el, function)
    }

    /**
     * 执行一个js函数，可以对全局有效
     * @param selector 元素的选择器
     * @param function 需要执行的方法
     */
    private Object executeScriptIfCanUseLocalJQuery(String selector, String function) {
        String jString = createJQueryString("\"$selector\"")
        return executeScript("return $jString$function")
    }

    /**
     * 执行某些事件时，为了增加兼容性，如果页面有JQuery，优先使用页面自带的
     * @param el 元素
     * @param function 需要执行的方法
     */
    private Object executeScriptIfCanUseLocalJQuery(WebElement el, String function) {
        assertElemenNotNull(el)
        String jString = createJQueryString(WEB_ELEMENT_ARGUMENT)
        return executeScript("return $jString$function;", el)
    }

    /** ******************************************************************************************************/

    /*****************************************frame操作*******************************************************/

    /**
     * 换到上一级frame
     * @return webdriver的示例
     */
    protected WebDriver switchToParentFrame() {
        if (framePath?.size() > 0) {
            framePath.pop()
            driver.switchTo().parentFrame()
        } else {
            logger.debug("上一次frame路径不存在，跳过。")
        }
        return driver
    }

    /**
     * 切换到frame，如当前frame为目标frame，则不切换
     * @param frameSelectors frame/iframe的选择器，多层frame使用->隔开,如：#frame1->.frame2->#frame3
     * @param timeoutInSecondsPerFrame 单个frame的超时时间
     * @return webdriver的示例
     */
    WebDriver switchFrame(String frameSelectors, int timeoutInSecondsPerFrame = SWITCH_FRAME_TIMEOUT_IN_SECONDS) {
        switchFrame(getList(frameSelectors, FRAME_SEPARATOR), IS_FORCE_SWITCH_FRAME)
    }

    /**
     * 切换到frame，如当前frame为目标frame，则不切换
     * @param frameSelectorList frame列表,如["#frame1",".frame2”,"#frame3"]
     * @param timeoutInSecondsPerFrame 单个frame的超时时间
     * @return webdriver的示例
     */

    WebDriver switchFrame(List<String> frameSelectorList, int timeoutInSecondsPerFrame = SWITCH_FRAME_TIMEOUT_IN_SECONDS) {
        switchFrame(frameSelectorList, IS_FORCE_SWITCH_FRAME, timeoutInSecondsPerFrame)
    }

    /**
     * 切换到frame
     * @param frameSelectors frame/iframe的选择器，多层frame使用->隔开,如：#frame1->.frame2->#frame3
     * @param forceSwitch 如为false，当前frame为目标frame，则不切换。如为true，当前frame为目标frame，也会强制切换。
     * @return webdriver的示例
     */
    WebDriver switchFrame(String frameSelectors, boolean forceSwitch) {
        switchFrame(getList(frameSelectors, FRAME_SEPARATOR), forceSwitch)
    }

    /**
     * 切换到frame
     * @param frameSelectorList frame列表,如["#frame1",".frame2”,"#frame3"]
     * @param forceSwitch 如为false，当前frame为目标frame，则不切换。如为true，当前frame为目标frame，也会强制切换。
     * @return webdriver的示例
     */
    WebDriver switchFrame(List<String> frameSelectorList, boolean forceSwitch) {
        switchFrame(frameSelectorList, forceSwitch, SWITCH_FRAME_TIMEOUT_IN_SECONDS)
    }

    /**
     * 切换到frame
     * @param frameSelectors frame/iframe的选择器，多层frame使用->隔开,如：#frame1->.frame2->#frame3，如为null或者空，则切换到最外层
     * @param forceSwitch 如为false，当前frame为目标frame，则不切换。如为true，当前frame为目标frame，也会强制切换。
     * @param timeoutInSecondsPerFrame 单个frame的超时时间
     * @return webdriver的示例
     */
    WebDriver switchFrame(String frameSelector, boolean forceSwitch, int timeoutInSecondsPerFrame) {
        if (frameSelector)
            switchFrame(getList(frameSelector, FRAME_SEPARATOR), forceSwitch, timeoutInSecondsPerFrame)
        else
            switchFrame(timeoutInSecondsPerFrame)
    }

    /**
     * TODO:更加只能的frame切换，不需要再指定是否强制切换frame
     * 切换到frame
     * @param frameSelectorList frame列表,如["#frame1",".frame2”,"#frame3"]，如为null或者空，则切换到最外层
     * @param forceSwitch 如为false，当前frame为目标frame，则不切换。如为true，当前frame为目标frame，也会强制切换。
     * @param timeoutInSecondsPerFrame 单个frame的超时时间
     * @return webdriver的示例
     */
    WebDriver switchFrame(List<String> frameSelectorList, boolean forceSwitch, int timeoutInSecondsPerFrame) {
        assertGreater("超时时间必须大于0", timeoutInSecondsPerFrame)
        boolean doSwitch = true
        List<String> hasNotSwitchedFrame = new ArrayList<>()
        if (frameSelectorList == null || frameSelectorList.empty) {
            switchFrame(timeoutInSecondsPerFrame)
            return driver
        }
        if (framePath == frameSelectorList && !forceSwitch)
            doSwitch = false
        if (doSwitch) {
            logger.debug("准备切换到frame${frameSelectorList}")
            TimerUtils tr = new TimerUtils()
            tr.start()
            switchFrame(timeoutInSecondsPerFrame)
            hasNotSwitchedFrame.clear()
            hasNotSwitchedFrame.addAll(frameSelectorList)
            for (frameSelector in frameSelectorList) {
                switchToFrame(frameSelector, timeoutInSecondsPerFrame)
                if (hasNotSwitchedFrame.get(0) == frameSelector)
                    hasNotSwitchedFrame.remove(0)
            }
            if (hasNotSwitchedFrame.size())
                throw new SwitchFrameErrorException("切换frame失败，需要切换的frame${frameSelectorList},尚未切换的frame[${hasNotSwitchedFrame.join("->")}]")
            else if (frameSelectorList.size())
                logger.debug("已切换到frame${frameSelectorList},耗时${tr.cost()}")
        } else {
            logger.debug("当前frame${frameSelectorList}不需要切换")
        }
        return driver
    }

    /**
     * 切换到最外层frame
     * @param timeoutInSeconds 超时值
     * @return webdriver的实例
     */
    WebDriver switchFrame(int timeoutInSeconds = SWITCH_FRAME_TIMEOUT_IN_SECONDS) {
        waitFor(timeoutInSeconds, 10) {
            driver.switchTo().defaultContent()
            waitForPageLoad()
            return true
        }
        framePath.clear()
        logger.debug("已切换到最外层frame.")
        return driver
    }

    /**
     * 切换到当前frame的下层frame
     * @param frame frame元素
     * @return webdriver的实例
     */
    protected WebDriver switchToFrame(WebElement frame) {
        return driver.switchTo().frame(frame)
    }

    /**
     * 切换到单个frame
     * @param frameSelector 如果frame没有id,可以使用selector:开始,后面跟该frame的选择器,如:frame1->selector:frame[class='frameClass']
     * @param timeoutInSeconds 超时值
     * @return webdriver的实例
     */
    protected WebDriver switchToFrame(String frameSelector, int timeoutInSeconds = SWITCH_FRAME_TIMEOUT_IN_SECONDS) {
        boolean hasSwitched = false
        waitFor(timeoutInSeconds, 100, false) {
            if (frameSelector) {
                WebElement frame = findElementByJQuery(frameSelector)
                String elTagName = frame.getTagName().toLowerCase()
                assertListContainsObject("要切换frame只能是iframe和frame元素，您提供的元素为[$elTagName]", ["iframe", "frame"], elTagName)

                switchToFrame(frame)
            } else {
                driver.switchTo().defaultContent()
            }
            framePath.add(frameSelector)
            waitForPageLoad()
            hasSwitched = true
            return true
        }
        if (!hasSwitched && frameSelector)
            throw new NoSuchFrameException("切换到[$frameSelector]出错")
        if (!hasSwitched && !frameSelector)
            throw new NoSuchFrameException("切换到最外层Frame出错")
        logger.debug("已切换到frame[${frameSelector}]")
        return driver
    }

    /**
     * 等待当前dom加载完成，不能用于判断ajax的状态
     * @param timeoutInSeconds 超时时间
     */
    void waitForPageLoad(int timeoutInSeconds = SWITCH_FRAME_TIMEOUT_IN_SECONDS) {
        String jsCode = "return document.readyState"
        String target = "complete"
        String status
        waitFor(timeoutInSeconds, 10, true) {
            status = executeScript(jsCode)
            return status == target
        }
    }

    /** ******************************************************************************************************/

    /*********************************************窗口操作****************************************************/

    /**
     * 切换到窗口
     * @param displayTitleOrHandle 窗口标题的部分显示文本或窗口的句柄
     * @param timeoutInSeconds 超时时间
     * @return webdriver的实例
     */
    WebDriver switchWindow(String displayTitleOrHandle, int timeoutInSeconds = SWITCH_WINDOW_TIMEOUT_IN_SECONDS) {
        switchWindow([displayTitleOrHandle],  timeoutInSeconds)
    }

    /**
     * 切换到窗口
     * @param displayTitleOrHandle 窗口标题包含的文本列表
     * @param timeoutInSeconds 超时时间
     * @return webdriver的实例
     */
    WebDriver switchWindow(List<String> displayUniqueTitles, int timeoutInSeconds = SWITCH_WINDOW_TIMEOUT_IN_SECONDS) {
        switchWindowMethod(displayUniqueTitles, false, timeoutInSeconds)
    }

    /**
     * 切换到窗口
     * @param regex 用于匹配窗口标题的正则表达式
     * @param timeoutInSeconds 超时时间
     * @return webdriver的实例
     */
    WebDriver switchWindowByRegex(String regex, int timeoutInSeconds = SWITCH_WINDOW_TIMEOUT_IN_SECONDS) {
        switchWindowByRegex([regex], timeoutInSeconds)
    }

    /**
     * 切换到窗口
     * @param regex 用于匹配窗口标题的正则表达式列表
     * @param timeoutInSeconds 超时时间
     * @return webdriver的实例
     */
    WebDriver switchWindowByRegex(List<String> regex, int timeoutInSeconds = SWITCH_WINDOW_TIMEOUT_IN_SECONDS) {
        switchWindowMethod(regex, true, timeoutInSeconds)
    }

    /**
     * 切换到窗口
     * @param windowHandle 窗口的句柄
     * @param timeoutInSeconds 超时时间
     * @return webdriver的实例
     */
    private WebDriver switchWindowByHandle(String windowHandle, int timeoutInSeconds = SWITCH_WINDOW_TIMEOUT_IN_SECONDS) {
        waitFor(timeoutInSeconds, 100) {
            driver.switchTo().window(windowHandle)
            logger.debug("切换到窗口：" + getTitle())
            return true
        }
        return driver
    }

    /**
     * 验证当前窗口是不是需要切换到的目标窗口
     * @param targetWindowTitles 窗口标题包含的文本或者正则表达式列表
     * @param byRegex 是否正则匹配
     * @return webdriver的实例
     */
    private boolean isTargetWindowIsCurrentWindow(List<String> targetWindowTitles, boolean byRegex = false) {
        try {
            String currentTitle = getTitle()
            if (stringHasAllListString(
                    currentTitle,
                    targetWindowTitles, byRegex)) {
                switchFrame()
                logger.debug("当前窗口[${currentTitle}]满足切换条件。")
                return true
            }
        } catch (Exception e) {
            logger.debug("检查当前窗口出错，出错信息：" + e.getStackTrace())
        }
        return false
    }

    /**
     * 如果当前窗口为目标窗口，停留在当前窗口。如不是，则抛出异常
     * @param targetWindowTitles 窗口标题包含的文本或者正则表达式列表
     * @param byRegex 是否正则匹配
     * @return webdriver的实例
     */
    private WebDriver stayInWindow(List<String> targetWindowTitles, boolean byRegex = false) {
        println "2"
        if (isTargetWindowIsCurrentWindow(targetWindowTitles, byRegex))
            return driver
        else
            throw new WebDriverException("当前窗口[${getTitle()}]不是目标窗口[${targetWindowTitles.join(",")}].")
    }

    /**
     * 切换到窗口公用方法
     * @param displayTitleOrHandle 用于匹配窗口标题的正则表达式或者部分文本的列表
     * @param byRegex 是否正则匹配
     * @param timeoutInSeconds 超时时间
     * @return webdriver的实例
     */
    private WebDriver switchWindowMethod(List<String> displayTitleOrHandle, boolean byRegex, int timeoutInSeconds) {

        boolean hasWindow = false
        assertNotNull("必须指定要切换到的窗口信息", displayTitleOrHandle)
        waitFor(timeoutInSeconds, 500, false) {
            if (displayTitleOrHandle.size() == 1 && displayTitleOrHandle.get(0) ==~ UUID_MATCHER)
                switchWindowByHandle(displayTitleOrHandle.get(0))
            else {
                List<String> currentHandles = getWindowHandles()
                assertGreater(currentHandles.size(), 0)
                logger.debug(getWindowHandles())
                if (!(getWindowHandle() in currentHandles))
                    driver.switchTo().window(currentHandles.get(0))
                currentHandles.size() == 1 ?
                        stayInWindow(displayTitleOrHandle, byRegex) :
                        switchToOtherWindow(displayTitleOrHandle, byRegex)
            }
            hasWindow = true
            return true
        }
        if (!hasWindow)
            throw new NoSuchWindowException(
                    "切换到窗口[${displayTitleOrHandle}]出错")
        framePath.clear()
        return driver
    }

    /**
     * 切换到除本窗口外的其他窗口 
     * @param displayTitles 用于匹配窗口标题的正则表达式或者部分文本的列表
     * @param byRegex 是否正则匹配
     * @return webdriver的实例
     */
    private WebDriver switchToOtherWindow(List<String> displayTitles, boolean byRegex = false) {
        boolean hasSwitched = false
        Set<String> allHandles = new HashSet<>()
        String currentHandle = getWindowHandle()
        allHandles.clear()
        allHandles.addAll(getWindowHandles())
        if (allHandles.contains(currentHandle))
            allHandles.remove(currentHandle)
        for (String handle : allHandles) {
            driver.switchTo().window(handle)
            logger.debug("已换到窗口：" + handle + "\t窗口标题：" + driver.getTitle() + "\t当前URL：" + driver.getCurrentUrl())
            if (stringHasAllListString(
                    driver.getTitle(),
                    displayTitles, byRegex)) {
                waitForPageLoad()
                hasSwitched = true
            }
        }
        if (hasSwitched) return driver
        throw new NoSuchWindowException("在当前所有窗口中未找到目标窗口$displayTitles")
    }

    /** ******************************************************************************************************/

    /*******************************************Alert操作****************************************************/

    /**
     * 等待alert出现并切换到alert
     * @return alert的实例
     */
    Alert waitForAlert(int timeoutInSeconds = SWITCH_ALERT_TIMEOUT_IN_SECONDS) {
        return new WebDriverWait(driver, timeoutInSeconds, 100).until(ExpectedConditions.alertIsPresent())
    }

    /**
     * 点击一个alert窗口的确定按钮，并返回提示文本
     * @param timeoutInSeconds 超时时间，默认值为{@link #SWITCH_ALERT_TIMEOUT_IN_SECONDS}
     * @return alert的提示文本
     */
    String acceptAlert(int timeoutInSeconds = SWITCH_ALERT_TIMEOUT_IN_SECONDS) {
        Alert alert = waitForAlert(timeoutInSeconds)
        String text = getAlertText(alert)
        alert.accept()
        return text
    }

    /**
     * 点击一个alert窗口的取消按钮，并返回提示文本
     * @param timeoutInSeconds 超时时间，默认值为{@link #SWITCH_ALERT_TIMEOUT_IN_SECONDS}
     * @return alert的提示文本
     */
    String dismissAlert(int timeoutInSeconds = SWITCH_ALERT_TIMEOUT_IN_SECONDS) {
        Alert alert = waitForAlert(timeoutInSeconds)
        String text = getAlertText(alert)
        alert.dismiss()
        return text
    }

    String getAlertText(Alert alert) {
        String text = alert.getText()
        logger.debug("alert提示文本：" + text)
        return text
    }
    /** ******************************************************************************************************/

    /***************************************其他*************************************************************/

    /**
     * 验证元素为期望的元素，仅对第一个匹配的元素有效
     * @param message 需要抛出的异常信息
     * @param selector 选择器
     * @param tagNames 期望的标签名列表
     */
    void expectElementTagName(String message, String selector, String... tagNames) {
        expectElementTagName(message, findElementByJQuery(selector), tagNames)
    }

    /**
     * 验证元素为期望的元素
     * @param message 需要抛出的异常信息
     * @param el 元素
     * @param tagNames 期望的标签名列表
     */
    void expectElementTagName(String message , WebElement el, String... tagNames) {
        assertElemenNotNull(el)
        String elTagName = el.getTagName().toLowerCase()
        assertObjectInList(message, elTagName, tagNames.toList())
    }

    /**
     * 验证元素的属性为期望的值，仅对第一个匹配的元素有效
     * @param message 需要抛出的异常信息
     * @param selector 选择器
     * @param attribute 属性名称
     * @param tagNames 期望的属性值列表
     */
    void expectElementValue(String message, String selector, String attribute, String... tagNames) {
        expectElementValue(message, findElementByJQuery(selector), attribute, tagNames)
    }

    /**
     * 验证元素的属性为期望的值
     * @param message 需要抛出的异常信息
     * @param el 元素
     * @param attribute 属性名称
     * @param tagNames 期望的属性值列表
     */
    void expectElementValue(String message, WebElement el, String attribute, String... tagNames) {
        assertElemenNotNull(el)
        String value = getAttribute(el, attribute)
        assertObjectInList(message, value, tagNames.toList())
    }

    /**
     * 断言要操作的元素必须存在
     */
    void assertElemenNotNull(WebElement el) {
        assertNotNull("要进行操作的元素必须存在", el)
    }

    /**
     * 断言要操作的元素必须存在
     */
    void assertElemenNotNull(List<WebElement> els) {
        assertNotNull("要进行操作的元素必须存在", els)
    }
    /** ******************************************************************************************************/

}
