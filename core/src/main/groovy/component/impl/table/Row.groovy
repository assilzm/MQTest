package component.impl.table

import component.Component

/**
 * TODO:
 * 行
 */
class Row extends Component{

    /**
     * 行即单元格的集合
     */
    List<Cell> cells=new ArrayList<>()


    Cell getCell(int columnIndex){
        return cells.get(columnIndex-1)
    }
}
