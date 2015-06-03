package component.impl.table

import component.Component
import component.interfaces.Table

/**
 * TODO:
 * 列表的抽象类
 */
abstract class AbstractTable extends Component implements Table{

    /**
     * 列表由行组成
     */
    List<Row> rows=new ArrayList<>()
}
