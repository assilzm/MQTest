package component.impl.table

import component.Component
/**
 * 列头
 */
class TableHead extends Component{

    List<Cell> cells=new ArrayList<>()


    Cell getHead(int headIndex){
        return cells.get(headIndex-1)
    }
}
