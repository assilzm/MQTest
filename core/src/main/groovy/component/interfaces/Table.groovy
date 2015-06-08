package component.interfaces
import component.impl.table.Cell
import component.impl.table.Row
/**
 * TODO:
 * 抽象的列表<br>
 * 标准的列表应该具有相同的方法
 */

interface Table {

    /**
     * 取得某行行元素,精确匹配
     * @param header 表头文本
     * @param displayText 显示内容
     * @return
     */
    Row getRow(String header, String displayText)

    /**
     * 取得某行
     * @param row 行号
     * @return
     */
    Row getRow(int row)


    /**
     * 取得某行行元素,精确搜索
     * @param header 表头文本
     * @param displayText 显示内容
     * @return
     */
    List<Row> getRows(String header, String displayText)


    /**
     * 通过行列号获取单元格对象
     * @param row 行号
     * @param column 列号
     * @return
     */
    Cell getCell(int row, int column)

    /**
     * 根据列名与列值获取单元格对象，精确查找
     * @param header 标题
     * @param displayText 显示文本
     * @return
     */
    Cell getCell(String header, String displayText)


    /**
     * 根据列名与列值获取得多个单元格对象
     * @param header 标题
     * @param displayText 显示文本
     * @param isUnique 是否精确查找,默认为true
     */
    List<Cell> getCells(String header, String displayText)

}
