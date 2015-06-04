package component.impl.table

import component.Component
/**
 * TODO:
 * 单元格
 */
class Cell extends Component {

    /**
     * 行号
     */
    int rowNumber = -1
    /**
     * 列号
     */
    int columnNumber = -1
    /**
     * 列头
     */
    String headName = null
    /**
     * 行头，如有的话
     */
    String cellText = null
}
