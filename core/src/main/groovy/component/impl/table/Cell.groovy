package component.impl.table

import component.Component
import org.openqa.selenium.WebElement

/**
 * TODO:
 * 单元格
 */
class Cell extends Component {

    /**
     * 当前单元格的页面元素对象
     */
    private WebElement element

    /**
     * 单元格显示的文本
     */
    private String text

    /**
     * 列号
     */
    private int index = -1

    /**
     * 当前元素的选择器
     */
    private String selector


    Cell(String selector, int index) {
        this.index = index
        this.selector = "${selector}:eq(${this.index - 1})"
        element = findElementByJQuery(selector)
        text=getText(element)
    }


    WebElement getElement() {
        return element
    }

    String getText() {
        return text
    }

    int getIndex() {
        return index
    }

    String getSelector() {
        return selector
    }

    void click(){
        click(element)
    }
}
