package base.model
import base.emuns.EventType
import base.exceptions.JavaScriptExecuteException
import base.utils.LogUtils
import base.utils.StringUtils
import org.apache.log4j.Logger
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions

import java.util.regex.Pattern

import static base.asserts.Assert.*
import static base.emuns.EventType.MOUSEOUT
import static base.emuns.EventType.MOUSEOVER
import static base.model.JQModel.*
import static base.model.TestContentManager.getCurrentJavascriptExecutor
import static base.utils.Timeout.waitFor
/**
 * 基础的web页面操作封装
 */
abstract class BasicActions {

    /**
     * webdriver实例
     */
    protected static WebDriver driver

    /**
     * 默认的查找元素的超时时间
     */
    static int FIND_ELEMENT_TIMEOUT_IN_SECONDS = 0

    /**
     * 默认元素闪烁次数
     */
    static int FLASH_ELEMENT_TIMEOUT_IN_SECONDS = 1

    /**
     * js中使用webelement的时所用的参数名
     */
    final static String WEB_ELEMENT_ARGUMENT = "arguments[0]"

    /**
     * 让元素闪烁的样式
     */
    protected final static String flashStyle = "background-color:red; outline:1px solid rgb(136,255,136);"


    /**
     * logger实例
     */
    private final static Logger logger = LogUtils.getLogger(BasicActions)

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
        assertElementNotNull(el)
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
        assertElementNotNull(el)
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
        assertElementNotNull(el)
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
        assertElementNotNull(el)
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
        assertElementNotNull(el)
        return findElementsOfElementByJQuery(el, "option:selected")
    }

    /**
     * 取得元素的文本，仅对第一个匹配的元素有效
     * @param selector 元素的选择器
     * @return 元素的显示文本
     */
    String getText(String selector) {
        return getText(findElementByJQuery(selector))
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
        assertElementNotNull(el)
        return getAttribute(el, "text")
    }

    /**
     * 取得多个元素的文本
     * @param els 元素
     * @return 元素的显示文本列表
     */
    List<String> getTexts(List<WebElement> els) {
        assertElementNotNull(els)
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
    String getAttribute(WebElement el, String attrName) {
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
    List<String> getAttributes(String selector, String attrName) {
        return getAttributes(findElementsByJQuery(selector), attrName)
    }

    /**
     * 取得某元素多个子元素的属性值
     * @param selector 被查找的元素的选择器
     * @param subElementSelector 子元素选择器
     * @param attrName 属性名，如为text,则设置显示文本
     */
    List<String> getAttributes(String selector, String subElementSelector, String attrName) {
        return getAttributes(findElementByJQuery(selector),subElementSelector,attrName)
    }

    /**
     * 取得某元素多个子元素的属性值
     * @param el 被查找的元素
     * @param subElementSelector 子元素选择器
     * @param attrName 属性名，如为text,则设置显示文本
     */
    List<String> getAttributes(WebElement el, String subElementSelector, String attrName = "text") {
        return getAttributes(findElementsOfElementByJQuery(el,subElementSelector),attrName)
    }

    /**
     * 取得多个元素的属性值
     * @param els 元素的列表
     * @param attrName 属性名，如为text,则设置显示文本
     */
    List<String> getAttributes(List<WebElement> els, String attrName) {
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
        assertElementNotNull(els)
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
            assertElementNotNull(el)
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
        assertElementNotNull(el)
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
        assertElementNotNull(el)
        String jString = createJQueryString(WEB_ELEMENT_ARGUMENT)
        return executeScript("return $jString$function;", el)
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
        assertElementNotNull(el)
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
        assertElementNotNull(el)
        String value = getAttribute(el, attribute)
        assertObjectInList(message, value, tagNames.toList())
    }

    /**
     * 断言要操作的元素必须存在
     */
    void assertElementNotNull(WebElement el) {
        assertNotNull("要进行操作的元素必须存在", el)
    }

    /**
     * 断言要操作的元素必须存在
     */
    void assertElementNotNull(List<WebElement> els) {
        assertNotNull("要进行操作的元素必须存在", els)
    }
    /** ******************************************************************************************************/

}
