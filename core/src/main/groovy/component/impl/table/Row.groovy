package component.impl.table

import component.Component
import org.openqa.selenium.WebElement

/**
 * TODO:
 * 行
 */
class Row extends Component{

    private int index=-1

    private WebElement element

    private String selector

    private String cellSelector

    /**
     * 行即单元格的集合
     */

    List<Cell> cells=new ArrayList<>()


    Cell getCell(int columnIndex){
        return cells.get(columnIndex-1)
    }

    Row(String selector,String cellSelector,int index){
        this.index=index
        this.selector="${selector}:eq(${this.index-1})"
        cellSelector="${this.selector} ${cellSelector}"
        element=findElementByJQuery(this.selector)
        List<WebElement> cellElements=findElementsOfElementByJQuery(element,this.cellSelector)
        for(int i in 0..<cellElements.size()){
            cells.add(new Cell(cellSelector, i))
        }
    }

    List<Cell> getCells() {
        return cells
    }

    int getIndex() {
        return index
    }

    WebElement getElement() {
        return element
    }

    String getSelector() {
        return selector
    }

    String getCellSelector() {
        return cellSelector
    }

}
