package base.model

import base.browser.Browser
import base.browser.BrowserFactory
import base.emuns.EventType
import base.exceptions.SwitchFrameErrorException
import base.utils.ProjectUtils
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebElement

import java.util.regex.Pattern

import static base.asserts.Assert.*
import static base.emuns.BrowserType.IE

/**
 * 基础页面的单元测试类
 */
class BasicPageTest {
    static BasicPage basicPage = new BasicPage()
    static Browser browser
    final static String DEMO_FILE_NAME = "demo.html"
    final static String DEMO_WINDOW_TITLE = "MQTestHtmlDemo"
    static String defaultWindowHandle

    @BeforeClass()
    static void setUp() {
        browser = BrowserFactory.localCreate(IE)
        browser.go("file://" + ProjectUtils.getProjectTestResourcesDir() + DEMO_FILE_NAME)
        defaultWindowHandle = basicPage.getWindowHandle()
    }

    @AfterClass
    static void tearDown() {
        if (browser)
            browser.exit()
    }


    void doSomeActionAndReset(Closure closure) {
        try {
            closure.call()
        } catch (Exception e) {
            e.printStackTrace()
        } finally {
            List<String> handles = basicPage.getWindowHandles()
            if (handles.size() > 1)
                (handles - defaultWindowHandle).each {
                    basicPage.switchWindow(it)
                    basicPage.close()
                }
            basicPage.switchWindow(defaultWindowHandle)
        }
    }

    boolean doSomeActionAndReturnException(Class<? extends Exception> exception, Closure closure) {
        boolean hasException
        try {
            closure.call()
        } catch (Exception e) {
            if (e.class.equals(exception))
                hasException = true
        }
        return hasException

    }

    @Test
    void testGetPageSource() {
        assertStringContains(basicPage.getPageSource(), DEMO_WINDOW_TITLE)
    }

    @Test
    void testGetCurrentUrl() {
        assertStringContains(basicPage.getCurrentUrl(), DEMO_FILE_NAME)
    }

    @Test
    void testGetTitleWithWindowClosed() {
        doSomeActionAndReset() {
            basicPage.click("#button17")
            basicPage.switchWindow("window1")
            basicPage.close()
            assertNull(basicPage.getTitle())
            basicPage.switchWindow(DEMO_WINDOW_TITLE)
        }
    }

    @Test
    void testGetWindowHandleWithWindowClosed() {
        doSomeActionAndReset() {
            basicPage.click("#button17")
            basicPage.switchWindow("window1")
            basicPage.close()
            assertNull(basicPage.getWindowHandle())
            basicPage.switchWindow(DEMO_WINDOW_TITLE)
        }
    }

    @Test
    void testFindElementByJQuery() {
        assertNotNull(basicPage.findElementByJQuery("li#li1"))
    }


    @Test
    void testFindElementByJQueryNotExist() {
        assertNull(basicPage.findElementByJQuery("li#notExist", 1))
    }


    @Test
    void testFindElementsByJQuery() {
        assertListNotEmpty(basicPage.findElementsByJQuery("li"))
    }

    @Test
    void testFindElementsByJQueryNotExist() {
        assertListIsEmpty(basicPage.findElementsByJQuery("li#notExist", 1))
    }

    @Test
    void testFindElementOfElementByJQuery() {
        assertNotNull(basicPage.findElementOfElementByJQuery("div", "li"))
    }

    @Test
    void testFindElementOfElementByJQueryNotExist() {
        assertNull(basicPage.findElementOfElementByJQuery("div", "li#notExist"))
    }

    @Test
    void testFindElementsOfElementByJQuery() {
        assertListNotEmpty(basicPage.findElementsOfElementByJQuery("div", "li"))
    }

    @Test
    void testFindElementsOfElementByJQueryNotExist() {
        assertListIsEmpty(basicPage.findElementsOfElementByJQuery("div", "li#notExist"))
    }

    @Test
    void testMoveToElement() {
        basicPage.moveToElement(".span3")
        assertEquals(basicPage.getText(".span3"), "mouseover")
    }

    @Test
    void testMoveOutElement() {
        basicPage.moveOutElement(".span3")
        assertEquals(basicPage.getText(".span3"), "mouseout")
    }

    @Test
    void testType() {
        basicPage.type("#input4", "done")
        assertEquals(basicPage.getText("#input4"), "done")
    }

    @Test
    void testClickByJs() {
        basicPage.clickByJs("#button5")
        assertEquals(basicPage.getText("#input5"), "clickbyjs")
    }

    @Test
    void testClick() {
        basicPage.clickByJs("#button6")
        assertEquals(basicPage.getText("#input6"), "click")
    }

    @Test
    void testDoubleClick() {
        basicPage.doubleClick("#span7")
        assertEquals(basicPage.getText("#input7"), "dbclick")
    }

    @Test
    void testDoubleClickByJs() {
        basicPage.doubleClickByJs("#span8")
        assertEquals(basicPage.getText("#input8"), "dbclickbyjs")
    }

    @Test
    void testFocusAndGetActiveElementAndBlur() {
        String selector="#input9"
        basicPage.focus(selector)
        assertEquals(basicPage.getActiveElement(), basicPage.findElementByJQuery(selector))
        assertEquals(basicPage.getText(selector), "focus")
        basicPage.blur(selector)
        assertNotEquals(basicPage.getActiveElement(), basicPage.findElementByJQuery(selector))
        assertEquals(basicPage.getText(selector), "blur")
    }


    @Test
    void testRemove() {
        String selector="#input10"
        basicPage.remove(selector)
        assertNull(basicPage.findElementByJQuery(selector))
    }

    @Test
    void testRemoveByElement() {
        String selector="#a10"
        WebElement el=basicPage.findElementByJQuery(selector)
        basicPage.remove(el)
        assertNull(basicPage.findElementByJQuery(selector))
    }

    @Test
    void testRemoveAttribute() {
        String selector="#input11"
        String testAttribute = "testValue"
        basicPage.removeAttribute(selector, testAttribute)
        assertNull(basicPage.getAttribute(selector, testAttribute))
    }

    @Test
    void testGetOptions() {
        assertEquals(basicPage.getOptions("#select12").size(),3)
    }



    @Test
    void testGetOptionTexts() {
        assertListEquals(basicPage.getOptionTexts("#select12"), ["1238", "357835", "24576"])
    }



    @Test
    void testGetSelectedOptionTexts() {
        List<String> selectOptions = ["a", "bcd", "efgh"]
        String selector = "#list12"
        basicPage.select(selector, selectOptions)
        assertEquals(basicPage.getSelectedOptions("#list12").size(),3)
        assertListEquals(basicPage.getSelectedOptionTexts(selector), selectOptions)
    }



    @Test
    void testSelectByText1() {
        String testOption = "24576"
        basicPage.select("#select12", testOption)
        assertEquals(basicPage.getSelectedOption("#select12").getText(), testOption)
    }

    @Test
    void testSelectByText2() {
        String testOption = "1238"
        basicPage.select("#select12", testOption)
        assertEquals(basicPage.getSelectedOptionText("#select12"), testOption)
    }

    @Test
    void testTrigger(){
        String selector="#button17_2"
        basicPage.trigger(selector,EventType.CLICK)
        assertEquals(basicPage.getText(selector), "trigger")
    }


    @Test
    void testSelectByTextWithNull() {
        assertNull(basicPage.getSelectedOptionText("#select12_2"))
    }


    @Test(expected = NoSuchElementException)
    void testSelectByTextWithException() {
        basicPage.select("#list12", ["a", "1238"])
    }


    @Test
    void testSelectByRegex() {
        Pattern regex = Pattern.compile("\\d{6,}")
        basicPage.selectByRegex("#select12", regex)
        assertEquals(basicPage.getSelectedOptionText("#select12"), "357835")
    }

    @Test
    void testIsCheckBoxSelected() {
        assertEquals(basicPage.isCheckBoxSelected("#checkbox13_1"), true)
        assertEquals(basicPage.isCheckBoxSelected("#checkbox13_2"), false)

    }

    @Test
    void testIsRadioSelected() {
        assertEquals(basicPage.isRadioSelected("#radio14_1"), true)
        assertEquals(basicPage.isRadioSelected("#radio14_2"), false)
    }

    @Test
    void testSelectCheckBox() {
        basicPage.selectCheckBox("#checkbox13_1")
        basicPage.selectCheckBox("#checkbox13_2")
        assertTrue(basicPage.isCheckBoxSelected("#checkbox13_1"))
        assertTrue(basicPage.isCheckBoxSelected("#checkbox13_2"))
    }

    @Test
    void testSelectRadio() {
        basicPage.selectRadio("#radio14_1")
        basicPage.selectRadio("#radio14_2")
        assertTrue(basicPage.isRadioSelected("#radio14_1"))
        assertTrue(basicPage.isRadioSelected("#radio14_2"))
    }


    @Test
    void testUnSelectCheckBox() {
        basicPage.unSelectCheckBox("#checkbox13_1")
        basicPage.unSelectCheckBox("#checkbox13_2")
        assertFalse(basicPage.isCheckBoxSelected("#checkbox13_1"))
        assertFalse(basicPage.isCheckBoxSelected("#checkbox13_2"))
    }

    @Test
    void testIsElementSelected() {
        assertTrue(basicPage.isElementSelected("#radio14_1"))
    }

    @Test
    void testGetText() {
        assertEquals(basicPage.getText("#input15"), "some texts1")
    }

    @Test
    void testGetTexts() {
        assertListEquals(basicPage.getTexts("#div15>*"), ["some texts1", "some texts2"])
    }

    @Test
    void testGetAttribute() {
        assertEquals(basicPage.getAttribute("#input16", "value"), "value1")

    }

    @Test
    void testSetAttribute() {
        String testAttribute = "setAttr"
        String testValue = "value set"
        basicPage.setAttribute("#input16", testAttribute, testValue)
        assertEquals(basicPage.getAttribute("#input16", testAttribute), testValue)

    }

    @Test
    void testGetAttributes() {
        assertListEquals(basicPage.getAttributes("#div16>*", "value"), ["value1", "value2"])
    }

    @Test
    void testSetAttributes() {
        String testAttribute = "setAttrs"
        String testValue = "value set"
        String selector = "#div16>*"
        basicPage.setAttributes(selector, testAttribute, testValue)
        assertListEquals(basicPage.getAttributes(selector, testAttribute), [testValue, testValue])
    }

    @Test
    void testFlash() {
        String selector = "#input4"
        basicPage.flash(selector, 0)
        basicPage.flash(selector, 1)
        basicPage.flash(selector, 3)
    }

    @Test(expected = IllegalArgumentException)
    void testFlashWithException() {
        basicPage.flash("#input4", -1)
    }

    @Test
    void testExecuteAsyncScript() {
        String selector = "#a1"
        String value = "value"
        basicPage.executeAsyncScript("\$(\"$selector\").text(\"$value\");")
        sleep(500)
        assertEquals(basicPage.getText(selector), value)
    }

    @Test
    void testSwitchFrame() {
        doSomeActionAndReset() {
            basicPage.switchFrame("#frame1->#frame2")
            assertNotNull(basicPage.findElementByJQuery("#reset"))
            basicPage.switchFrame()
            assertNotNull(basicPage.findElementByJQuery("#input4"))
        }
    }

    @Test
    void testSwitchToParentFrame() {
        doSomeActionAndReset() {
            basicPage.switchFrame("#frame1->#frame2")
            basicPage.switchToParentFrame()
            assertNotNull(basicPage.findElementByJQuery("#frame2"))
        }
    }

    @Test
    void testSwitchToParentFrameInDefaultFrame() {
        basicPage.switchToParentFrame()
        assertNotNull(basicPage.findElementByJQuery("#frame1"))
    }

        @Test
    void testSwitchFrameWithException() {
        boolean hasException = false
        try {
            basicPage.switchFrame("#frame1->#frame2->#frame3", 1)
        } catch (SwitchFrameErrorException) {
            hasException = true
        } finally {
            basicPage.switchFrame()
        }
        assertTrue(hasException)

    }

    @Test
    void testSwitchFrameWithForce() {
        doSomeActionAndReset() {
            String selector = "#reset"
            basicPage.switchFrame("#frame1->#frame2")
            WebElement el = basicPage.findElementByJQuery(selector)
            basicPage.click(selector)
            basicPage.switchFrame("#frame1->#frame2", true)
            assertNotEquals(basicPage.findElementByJQuery(selector).toString(), el.toString())
        }
    }

    @Test
    void testSwitchFrameWithForceAndTimeout() {
        doSomeActionAndReset() {
            String selector = "#reset"
            basicPage.switchFrame("#frame1->#frame2",true,5)
            WebElement el = basicPage.findElementByJQuery(selector)
            basicPage.click(selector)
            basicPage.switchFrame("#frame1->#frame2", true)
            assertNotEquals(basicPage.findElementByJQuery(selector).toString(), el.toString())
        }
    }

    @Test
    void testSwitchFrameWithEmptyList() {
        doSomeActionAndReset() {
            basicPage.switchFrame([])
            assertNotNull(basicPage.findElementByJQuery("#frame1"))
        }
    }

    @Test
    void testSwitchFrameWithEmptyStringAndTimeout() {
        doSomeActionAndReset() {
            basicPage.switchFrame("",false,3)
            assertNotNull(basicPage.findElementByJQuery("#frame1"))
        }
    }

    @Test
    void testSwitchFrameWithError() {
        doSomeActionAndReset() {
            doSomeActionAndReturnException(SwitchFrameErrorException) {
                basicPage.switchFrame("#frame1->#frame2->#frame3->#frame4",3)
            }
        }
    }

    @Test
    void testSwitchFrameWithNull() {
        doSomeActionAndReset() {
                basicPage.switchToFrame(new String())
            }
    }

    @Test
    void testSwitchFrameWithoutForce() {
        doSomeActionAndReset() {
            String selector = "#reset"
            basicPage.switchFrame("#frame1->#frame2")
            WebElement el = basicPage.findElementByJQuery(selector)
            basicPage.click(selector)
            basicPage.switchFrame("#frame1->#frame2")
            assertTrue(doSomeActionAndReturnException(StaleElementReferenceException) {
                el.click()
            })
        }
    }


    @Test
    void testSwitchWindowByRegex() {
        doSomeActionAndReset() {
            basicPage.click("#button17")
            basicPage.switchWindowByRegex("window\\d+")
        }
    }



    @Test
    void testSwitchToCurrentWindow() {
         basicPage.switchWindow(DEMO_WINDOW_TITLE)
    }

    @Test
    void testAcceptAlert() {
        String selector = "#button18"
        basicPage.click(selector)
        assertEquals(basicPage.acceptAlert(), "alert show")
        assertEquals(basicPage.getText(selector), "true")
    }

    @Test
    void testDismissAlert() {
        String selector = "#button18"
        basicPage.click(selector)
        assertEquals(basicPage.dismissAlert(), "alert show")
        assertEquals(basicPage.getText(selector), "false")
    }

    @Test
    void testExpectElementTagName() {
        String selector = "#button18"
        basicPage.expectElementTagName("验证给定的元素是input",selector,"input")
    }

    @Test
    void testExpectElementValue() {
        String selector = "#button18"
        basicPage.expectElementValue("验证给定的元素type是button",selector,"type","button")
    }
}
