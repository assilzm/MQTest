package component.impl.table

import base.model.BasicPage

/**
 * TOD:
 * 单元格类
 */
class Cell extends BasicPage{

    /**
     * 行号，如果需要
     */
    int rowNumber=-1
    /**
     * 列号，如果需要
     */
    int columnNumber=-1
    /**
     * 列头名称，如果需要
     */
    String headName=null
    /**
     * 行头名称，如果需要，虽然不大常见。暂时扔这，考虑下是不是需要这个属性
     */
    String rowName=null
}
