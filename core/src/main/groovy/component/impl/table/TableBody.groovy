package component.impl.table

import component.Component
import org.openqa.selenium.WebElement

/**
 * 表体
 */
class TableBody extends Component {
    /**
     * 表体表即行的集合
     */
    private List<Row> rows = new ArrayList<>()

    private WebElement element

    private String selector
    private String rowSelector
    private String cellSelector


    TableBody(String selector, String rowSelector, String cellSelector) {
        this.selector = selector
        this.rowSelector = "${this.selector} ${rowSelector}"
        element=findElementByJQuery(this.selector)
        this.cellSelector = cellSelector
        List<WebElement> rowElements = findElementsOfElementByJQuery(element,this.rowSelector)
        for (int i in 1..rowElements.size()) {
            rows.add(new Row("${this.rowSelector}", this.cellSelector, i))
        }
    }



    Row getRow(int rowIndex) {
        return getRows().get(rowIndex - 1)
    }

    List<Row> getRows(){
        return rows
    }
}
