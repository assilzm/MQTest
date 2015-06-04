package component.impl.table

import component.Component
import component.interfaces.Table

/**
 * TODO:
 * 抽象的列表类
 */
abstract class AbstractTable extends Component implements Table{

    /**
     * 列头
     */
    TableHead tableHead=new TableHead()

    /**
     * 表体
     */
    TableBody tableBody=new TableBody()


    Cell getHead(int index){
       return tableHead.getHead(index)
    }

    Cell getCell(int rowIndex,int columnIndex){
        return getRow(rowIndex).getCell(columnIndex)
    }

    Row getRow(int index){
        return getTableBody().getRow(index)
    }


}
