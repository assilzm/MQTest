package component.impl.table

import component.Component
import component.interfaces.Table

/**
 * TODO:
 * �б�ĳ�����
 */
abstract class AbstractTable extends Component implements Table{

    /**
     * �б��������
     */
    List<Row> rows=new ArrayList<>()
}
