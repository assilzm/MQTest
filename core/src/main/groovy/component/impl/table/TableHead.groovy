package component.impl.table

import component.Component
import org.openqa.selenium.WebElement

/**
 * 列头
 */
class TableHead extends Component{


    private String selector

    private String headSelector


    private List<Cell> cells=new ArrayList<>()


    TableHead(String selector,String headSelector){
        this.selector=selector
        headSelector="${this.selector} ${headSelector}"
        List<WebElement> cellElements=findElementsByJQuery(this.headSelector)
        for(int i in 0..<cellElements.size()){
            cells.add(new Cell(headSelector, i))
        }
    }

    String getSelector() {
        return selector
    }

    String getHeadSelector() {
        return headSelector
    }


    List<Cell> getCells() {
        return cells
    }

    Cell getHead(int headIndex){
        return cells.get(headIndex-1)
    }

    Cell getHead(String headName){
        for(Cell cell in cells){
            if(cell.text==headName){
                return cell
            }
        }
    }
}
