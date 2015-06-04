package component.impl.table

import component.Component
/**
 * 表体
 */
class TableBody extends Component{
    /**
     * 表体表即行的集合
     */
    List<Row> rows=new ArrayList<>()


    Row getRow(int rowIndex){
        return rows.get(rowIndex-1)
    }
}
