package base.model
import base.exceptions.SwitchFrameErrorException
import base.utils.LogUtils
import base.utils.StringUtils
import base.utils.TimerUtils
import org.apache.log4j.Logger
import org.openqa.selenium.*

import static StringUtils.stringHasAllListString
import static base.asserts.Assert.*
import static base.utils.StringUtils.getList
import static base.utils.Timeout.waitFor
/**
 * 抽象的web页面
 */
class BasicPage extends BasicActions {

    /**
     * logger实例
     */
    private final static Logger logger = LogUtils.getLogger(BasicPage)

    /*****************************************默认参数***********************************************************/

    /**
     * 当前frame路径表
     */
    static List<String> framePath = new ArrayList<>()

    /**
     * 默认的切换窗口的超时时间
     */
    static int SWITCH_WINDOW_TIMEOUT_IN_SECONDS = 30

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


    private final static String UUID_MATCHER = /^[0-9a-z]{8}(-[0-9a-z]{4}){3}-[0-9a-z]{12}$/

    /** ******************************************************************************************************/

    /***************************************对webdriverAPI的简单封装*******************************************/

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
     * TODO:更智能的frame切换，不需要再指定是否强制切换frame
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
        switchWindow([displayTitleOrHandle], timeoutInSeconds)
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
    protected WebDriver switchWindowByHandle(String windowHandle, int timeoutInSeconds = SWITCH_WINDOW_TIMEOUT_IN_SECONDS) {
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
    protected boolean isTargetWindowIsCurrentWindow(List<String> targetWindowTitles, boolean byRegex = false) {
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
     * 切换到窗口公用方法
     * @param displayTitleOrHandle 用于匹配窗口标题的正则表达式或者部分文本的列表
     * @param byRegex 是否正则匹配
     * @param timeoutInSeconds 超时时间
     * @return webdriver的实例
     */
    protected WebDriver switchWindowMethod(List<String> displayTitleOrHandle, boolean byRegex, int timeoutInSeconds) {

        boolean hasWindow = false
        assertNotNull("必须指定要切换到的窗口信息", displayTitleOrHandle)
        waitFor(timeoutInSeconds, 500, false) {
            if (displayTitleOrHandle.size() == 1 && displayTitleOrHandle.get(0) ==~ UUID_MATCHER)
                switchWindowByHandle(displayTitleOrHandle.get(0))
            else {
                List<String> currentHandles = getWindowHandles()
                assertGreater(currentHandles.size(), 0)
                if (!(getWindowHandle() in currentHandles))
                    driver.switchTo().window(currentHandles.get(0))
                if (!isTargetWindowIsCurrentWindow(displayTitleOrHandle, byRegex))
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
    protected WebDriver switchToOtherWindow(List<String> displayTitles, boolean byRegex = false) {
        boolean hasSwitched = false
        Set<String> allHandles = new HashSet<>()
        String currentHandle = getWindowHandle()
        allHandles.clear()
        allHandles.addAll(getWindowHandles())
        if (allHandles.contains(currentHandle))
            allHandles.remove(currentHandle)
        for (String handle : allHandles) {
            driver.switchTo().window(handle)
            logger.debug("已切换到窗口：" + handle + "\t窗口标题：" + driver.getTitle() + "\t当前URL：" + driver.getCurrentUrl())
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
   static Alert switchToAlert(int timeoutInSeconds = SWITCH_ALERT_TIMEOUT_IN_SECONDS) {
        Alert alert=null
        waitFor(timeoutInSeconds,500){
            alert=driver.switchTo().alert()
            return true
        }
        return alert
    }

    /**
     * 点击一个alert窗口的确定按钮，并返回提示文本
     * @param timeoutInSeconds 超时时间，默认值为{@link #SWITCH_ALERT_TIMEOUT_IN_SECONDS}
     * @return alert的提示文本
     */
    static String acceptAlert(int timeoutInSeconds = SWITCH_ALERT_TIMEOUT_IN_SECONDS) {
        Alert alert = switchToAlert(timeoutInSeconds)
        String text = getAlertText(alert)
        alert.accept()
        return text
    }

    /**
     * 点击一个alert窗口的取消按钮，并返回提示文本
     * @param timeoutInSeconds 超时时间，默认值为{@link #SWITCH_ALERT_TIMEOUT_IN_SECONDS}
     * @return alert的提示文本
     */
    static String dismissAlert(int timeoutInSeconds = SWITCH_ALERT_TIMEOUT_IN_SECONDS) {
        Alert alert = switchToAlert(timeoutInSeconds)
        String text = getAlertText(alert)
        alert.dismiss()
        return text
    }

    static String getAlertText(Alert alert) {
        String text = alert.getText()
        logger.debug("alert提示文本：" + text)
        return text
    }
    /** ******************************************************************************************************/


}
