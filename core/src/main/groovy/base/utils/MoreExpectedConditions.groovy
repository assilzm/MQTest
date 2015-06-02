package base.utils

import base.model.TestContentManager
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoSuchWindowException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition

import static base.model.JQModel.injectJQuery

/**
 * 扩展ExpectedCondition<br>
 * 然而目前并没有用，暂时保留
 */
class MoreExpectedConditions {
	private MoreExpectedConditions() {}

    /**
     * An expectation for checking the value of js variable on current page.
     *
     * @param jsVariable variable name
     * @param title the expected value, which must be an exact match
     * @return WebDriver instance when the value matches, null otherwise
     */
    static ExpectedCondition<List<WebElement>> waitForElementsArePresented(final String jsVariable) {
        return new ExpectedCondition<List<WebElement>>() {
            private String js = "try{" +
                    "return ${jsVariable};" +
                    "}catch(e){" +
                    "return null;" +
                    "}";


            public List<WebElement> apply(WebDriver driver) {
                def jse = (JavascriptExecutor) driver
                def el = (List<WebElement>) jse.executeScript(this.js)
                if (el.size() > 0)
                    return el
            }


            public String toString() {
                return "$jsVariable is not presented"
            }
        };
    }

    /**
	 * An expectation for checking the value of js variable on current page.
	 *
	 * @param jsVariable variable name
	 * @param title the expected value, which must be an exact match
	 * @return WebDriver instance when the value matches, null otherwise
	 */
	static ExpectedCondition<WebDriver> javascriptVariableInCurrentWindowIs(final String jsVariable, final String value) {
		return new ExpectedCondition<WebDriver>() {
			private String currentValue = "";
			private String js = "try{" +
					"return ${jsVariable};" +
					"}catch(e){" +
					"return '';" +
					"}";


			public WebDriver apply(WebDriver driver) {
				def jse = (JavascriptExecutor) driver
				this.currentValue = jse.executeScript(this.js)
				if (this.currentValue.equals(value))
					return driver
			}


			public String toString() {
				return "except value to be ${value}. " +
						"Current value of ${jsVariable} is ${this.currentValue}"
			}
		}
	}

	/**
	 * An expectation for checking the value of js variable on current page.
	 *
	 * @param jsVariable variable name
	 * @param title the expected value, which must be an exact match
	 * @return WebDriver instance when the value matches, null otherwise
	 */
	static ExpectedCondition<WebDriver> javascriptVariableIsExtraWindow(final String jsVariable, final String value) {
		return new ExpectedCondition<WebDriver>() {
			private String currentValue = "";
			private String js = "try{" +
					"return ${jsVariable};" +
					"}catch(e){" +
					"return '';" +
					"}";


			public WebDriver apply(WebDriver driver) {
				for (String handle : driver.getWindowHandles()) {
					def hasSwitched=false
					Timeout.waitFor(10, 100, false) {
						driver.switchTo().window(handle)
						hasSwitched=true
					}
					if(!hasSwitched)
						throw new Exception("切换错误,重新开始切换.")
					def jse = (JavascriptExecutor) driver
					this.currentValue = jse.executeScript(this.js)
					if (this.currentValue.equals(value))
						return driver
				}
			}


			public String toString() {
				return "except value to be ${value}. " +
						"Current value of ${jsVariable} is ${this.currentValue}"
			}
		};
	}

	/**
	 * An expectation for checking the value of js variable on current page.
	 *
	 * @param jsVariable variable name
	 * @param title the expected value, which must be an exact match
	 * @return WebDriver instance when the value matches, null otherwise
	 */
	static ExpectedCondition<WebDriver> windowHasJavascriptVariable(final String javascriptVariable) {
		return new ExpectedCondition<WebDriver>() {
			private Boolean currentValue = false
			private String javascript = "try{" +
					"return ${javascriptVariable}.length>0 && ${javascriptVariable}!=undefined;" +
					"}catch(e){" +
					"return false;" +
					"}";


			public WebDriver apply(WebDriver driver) {
				for (String handle : driver.getWindowHandles()) {
					driver.switchTo().window(handle)
					TestContentManager.setCurrentWebDriver(driver)
					injectJQuery()
					def jse = (JavascriptExecutor) driver
					this.currentValue = (Boolean) jse.executeScript(this.javascript)
					if (this.currentValue) {
						return driver
					}
				}
			}


			public String toString() {
				return "except target window has javascript variable:${javascriptVariable}. " +
						"Current value is undefined or ${javascriptVariable}.length<0."
			}
		};
	}

	/**
	 * An expectation for checking title contains same subtitles.
	 *
	 * @param subTitles subtitles
	 * @return true when title matches all subtitles, false otherwise
	 */
	static ExpectedCondition<WebDriver> titleHas(final String... subTitles) {
		return new ExpectedCondition<WebDriver>() {
			private String title = ""


			public WebDriver apply(WebDriver driver) {

					for (String handle : driver.getWindowHandles()) {
					def hasTitle = true
					driver.switchTo().window(handle)
					TestContentManager.setCurrentWebDriver(driver)
					this.title = driver.getTitle()
					hasTitle = hasTitle && (subTitles.length > 0)
					for (String subTitle : subTitles) {
						hasTitle = hasTitle && this.title.contains(subTitle)
					}
					if (hasTitle)
						return driver
				}
				throw new NoSuchWindowException("There's no such window with title[$subTitles]")
			}


			public String toString() {
				return "except title contains [${subTitles.toString()}]. " +
						"Current title is ${this.title}"
			}
		};
	}

    /**
     * An expectation for checking title matches same regexes.
     *
     * @param subTitles regex
     * @return true when title matches all subtitles, false otherwise
     */
    static ExpectedCondition<WebDriver> titleRegex(final String... regexes) {
        return new ExpectedCondition<WebDriver>() {
            private String title = ""


            public WebDriver apply(WebDriver driver) {
                for (String handle : driver.getWindowHandles()) {
                    def hasTitle = true
                    driver.switchTo().window(handle)
                    TestContentManager.setCurrentWebDriver(driver)
                    this.title = driver.getTitle()
                    hasTitle = hasTitle && (regexes.length > 0)
                    for (String subTitle : regexes) {
                        hasTitle = hasTitle && this.title =~ subTitle
                    }
                    if (hasTitle)
                        return driver
                }
                throw new NoSuchWindowException("There's no such window title matches[$regexes]")
            }


            public String toString() {
                return "except title matches [${regexes.toString()}]. " +
                        "Current title is ${this.title}"
            }
        }
    }
}
