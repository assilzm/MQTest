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
    Head head=new Head()

    /**
     * 表体
     */
    Body body=new Body()


    Cell getCell(){

    }

    Row getRow(int index){

    }


}
