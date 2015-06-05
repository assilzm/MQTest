package component.interfaces

import component.impl.table.Cell
import component.impl.table.Row
import org.openqa.selenium.WebElement

/**
 * TODO:
 * 抽象的列表<br>
 * 标准的列表应该具有相同的方法
 */

interface Table {

    /**
     * 是否存在某行，精确查找
     * @param map 行列文本对应的map
     * @return
     */
    boolean hasRow(Map<String, String> map)

    /**
     * 是否存在某行
     * @param map 行列文本对应的map
     * @param isUnique 是否精确搜索
     * @return
     */
    boolean hasRow(Map<String, String> map, boolean isUnique)

    /**
     * 是否存在某行,精确搜索
     * @param header 表头文本
     * @param displayText 显示内容
     * @return
     */
    boolean hasRow(String header, String displayText)

    /**
     * 是否存在某行
     * @param header 表头文本
     * @param displayText 显示内容
     * @param isUnique 是否精确搜索
     * @return
     */
    boolean hasRow(String header, String displayText, boolean isUnique)


    /**
     * 根据主列取得某行行元素,精确匹配
     * @param displayText 显示内容
     * @return
     */
    WebElement getRow( String displayText)



    /**
     * 取得某行行元素,精确匹配
     * @param header 表头文本
     * @param displayText 显示内容
     * @return
     */
    WebElement getRow(String header, String displayText)

    /**
     * 取得某行
     * @param row 行号
     * @return
     */
    Row getRow(int row)

    /**
     * 取得某行行元素
     * @param header 表头文本
     * @param displayText 显示内容
     * @param isUnique 是否精确搜索
     * @return
     */
    WebElement getRow(String header, String displayText, boolean isUnique)

    /**
     * 取得某行行元素,精确查找
     * @param map 行列文本对应的map
     * @return
     */
    WebElement getRow(Map<String, String> map)

    /**
     * 取得某行行元素
     * @param map 行列文本对应的map
     * @param isUnique 是否精确搜索
     * @return
     */
    WebElement getRow(Map<String, String> map, boolean isUnique)

    /**
     * 取得某行行元素
     * @param map 行列文本对应的map
     * @param isUnique 是否精确搜索
     * @return
     */
    List<WebElement> getRows(Map<String, String> map, boolean isUnique)

    /**
     * 取得某列所有单元格元素,默认精确搜索
     * @param headerName 表头文本
     * @return
     */
    List<WebElement> getColumnCells(String headerName)

    /**
     * 取得某列所有单元格元素
     * @param headerName 表头文本
     * @param isUnique 是否精确搜索
     * @return
     */
    List<WebElement> getColumnCells(String headerName, boolean isUnique)


    /**
     * 取得某列所有单元格文本,精确搜索
     * @param headerName 表头文本
     * @return
     */
    List<String> getColumnCellTexts(int index)

    /**
     * 取得某列所有单元格文本,精确搜索
     * @param headerName 表头文本
     * @return
     */
    List<String> getColumnCellTexts(String headerName)

    /**
     * 取得某列所有单元格文本
     * @param headerName 表头文本
     * @param isUnique 是否精确搜索
     * @return
     */
    List<String> getColumnCellTexts(String headerName, boolean isUnique)


    /**
     * 取得满足条件行元素
     * @param map 行列对应关系
     * @return
     */
    List<WebElement> getRows(Map<String,String> map)



    /**
     * 取得某行行元素,精确搜索
     * @param header 表头文本
     * @param displayText 显示内容
     * @return
     */
    List<WebElement> getRows(String header, String displayText)


    /**
     * 根据主列取得某行行元素，精确搜索
     * @param isUnique 是否精确搜索
     * @return
     */
    List<WebElement> getRows(String displayText)



    /**
     * 取得某行行元素
     * @param header 表头文本
     * @param displayText 显示内容
     * @param isUnique 是否精确搜索
     * @return
     */
    List<WebElement> getRows(String header, String displayText, boolean isUnique)

    /**
     * 取得某行单元格取得另一个单元格
     * @param header 已知单元格表头文本
     * @param displayText 已知单元格表显示内容
     * @param isUnique 是否精确搜索
     * @param targetHeader 目标单元格表头文本
     * @return
     */
    WebElement getRowCell(String header, String displayText, boolean isUnique, String targetHeader)

    /**
     * 取得某行所有单元格，精确搜索
     * @param header 已知单元格表头文本
     * @param displayText 已知单元格表显示内容
     * @return
     */
    Map<String,WebElement> getRowCells(String header, String displayText)


    /**
     * 取得某行所有单元格
     * @param header 已知单元格表头文本
     * @param displayText 已知单元格表显示内容
     * @param isUnique 是否精确搜索
     * @return
     */
    Map<String,WebElement> getRowCells(String header, String displayText, boolean isUnique )

    /**
     * 取得某行单元格的checkbox，精确查找
     * @param displayText 主列单元格表显示内容
     * @return
     */
    WebElement getRowCheckbox( String displayText)


    /**
     * 取得某行单元格的checkbox
     * @param header 已知单元格表头文本
     * @param displayText 已知单元格表显示内容
     * @param isUnique 是否精确搜索
     * @return
     */
    WebElement getRowCheckbox(String header, String displayText, boolean isUnique )

    /**
     * 取得某行单元格的checkbox
     * @param rowIndex 行号
     * @return
     */
    WebElement getRowCheckbox(int rowIndex )

    /**
     * 取得某行单元格的checkbox
     * @param row 行
     * @return
     */
    WebElement getRowCheckbox(WebElement row )

    /**
     * 取得某行主列元素,精确搜索
     * @param map 行列文本对应的map
     * @return
     */
    WebElement getRowMainColumn(Map<String, String> map)

    /**
     * 取得某行主列元素
     * @param map 行列文本对应的map
     * @param isUnique 是否精确搜索
     * @return
     */
    WebElement getRowMainColumn(Map<String, String> map, boolean isUnique)

    /**
     * 通过行列号获取单元格对象
     * @param row 行号
     * @param column 列号
     * @return
     */
    Cell getCell(int row, int column)

    /**
     * 根据列名与列值获取单元格对象
     * @param header 标题
     * @param displayText 显示文本
     * @param isUnique 是否精确查找,默认为true
     * @return
     */
    Cell getCell(String header, String displayText, boolean isUnique)

    /**
     * 根据列名与列值获取单元格对象，精确查找
     * @param header 标题
     * @param displayText 显示文本
     * @return
     */
    Cell getCell(String header, String displayText)



    /**
     * 根据主列值获取单元格对象
     * @param displayText 显示文本
     * @param isUnique 是否精确查找
     * @return
     */
    Cell getCell(String displayText, boolean isUnique)

    /**
     * 根据主列值获取单元格对象，精确查找
     * @param displayText 显示文本
     * @return
     */
    Cell getCell(String displayText)

    /**
     * 根据列名与列值获取得多个单元格对象
     * @param header 标题
     * @param displayText 显示文本
     * @param isUnique 是否精确查找,默认为true
     */
    List<Cell> getCells(String header, String displayText, boolean isUnique)

    /**
     * 点击某单元格
     * @param row
     * @param column
     */
    void clickCell(int row, int column)

    /**
     * 点击某单元格
     * @param header
     * @param displayUniqueText
     * @param isUnique
     */
    void clickCell(String header, String displayUniqueText, boolean isUnique)

    /**
     * 点击某单元格
     * @param header
     * @param displayUniqueText
     */
    void clickCell(String header, String displayUniqueText)

    /**
     * 点击主列某单元格
     * @param displayUniqueText
     */
    void clickCell(String displayUniqueText)

    /**
     * 双击主列某单元格
     * @param displayUniqueText
     */
    void dbClickCell(String displayUniqueText)

    /**
     * 双击某单元格
     * @param header
     * @param displayUniqueText
     */
    void dbClickCell(String header, String displayUniqueText)

    /**
     * 双击某单元格
     * @param header
     * @param displayUniqueText
     * @param isUnique
     */
    void dbClickCell(String header, String displayUniqueText, boolean isUnique)

    /**
     * 双击某单元格
     * @param row
     * @param column
     */
    void dbClickCell(int row, int column)

    /**
     * 根据列号点击表头中的列对象
     * @param column 列号
     */
    void clickHeader(int column)

    /**
     * 根据列名点击表头中的列对象
     * @param header 表头显示的文字
     */
    void clickHeader(String header)

    /**
     * 获取表头中的所有的列对象
     * @return
     */
    List<WebElement> getAllHeaders()

    /**
     * 获取表头中的所有的列对象的名称
     * @return
     */
    List<String> getAllHeaderNames()

    /**
     * 获取主要列的表头文本
     * @return
     */
    String getMainHeaderName()

    /**
     * 根据显示文本获取某行的所有属性值,精确查找
     * @param header 列头显示文本
     * @param header 单元格显示文本
     */
    Map<String, String> getRowProperties(String header, String displayText)

    /**
     * 根据显示文本获取某行的所有属性值
     * @param header 列头显示文本
     * @param header 单元格显示文本
     * @param isUnique 是否精确查找,默认为true
     */
    Map<String, String> getRowProperties(String header, String displayText, boolean isUnique)

    /**
     * 根据主列显示文本获取某行的所有属性值
     * @param map 行的属性
     */
    Map<String, String> getRowProperties(Map<String,String> map)

    /**
     * 根据主列显示文本获取某行的所有属性值
     * @param map 行的属性
     * @param isUnique 是否精确查找,默认为true
     */
    Map<String, String> getRowProperties(Map<String,String> map, boolean isUnique)

    /**
     * 根据主列显示文本获取某行的所有属性值
     * @param name 主列显示文本
     * @param isUnique 是否精确查找,默认为true
     */
    Map<String, String> getRowProperties(String name)

    /**
     * 根据主列显示文本获取某行的所有属性值
     * @param name 主列显示文本
     * @param isUnique 是否精确查找,默认为true
     */
    Map<String, String> getRowProperties(String name, boolean isUnique)

    /**
     * 根据单元格获取多个符合条件的行的所有属性值,精确查找
     * @param header 列头显示文本
     * @param header 单元格显示文本
     */
    List<Map<String, String>> getRowsProperties(String header, String displayText)

    /**
     * 根据单元格获取多个符合条件的行的所有属性值
     * @param header 列头显示文本
     * @param header 单元格显示文本
     * @param isUnique 是否精确查找,默认为true
     */
    List<Map<String, String>> getRowsProperties(String header, String displayText, boolean isUnique)

    /**
     * 根据单元格获取多个符合条件的行的所有属性值
     * @param name 主列显示文本
     * @param isUnique 是否精确查找,默认为true
     */
    List<Map<String, String>> getRowsProperties(String name)

    /**
     * 根据单元格获取多个符合条件的行的所有属性值
     * @param name 主列显示文本
     * @param isUnique 是否精确查找,默认为true
     */
    List<Map<String, String>> getRowsProperties(String name, boolean isUnique)
    /**
     * 根据行号获取某行的所有属性值
     * @param rowNumber 行号
     */
    Map<String, String> getRowProperties(int rowNumber)

    /**
     * 根据单元格获取某行的所有属性值
     * @param cell 单元格
     */
    Map<String, String> getRowProperties(WebElement cell)
    /**
     * 获取当前表格中的所有行
     * @return
     */
    List<WebElement> getAllCurrentRows()

    /**
     * 选择行,精确匹配
     * @param header 列头文本
     * @param displayText 显示文本
     */
    void selectRow(String header, String displayText)

    /**
     * 取消选择行,精确匹配
     * @param header 列头文本
     * @param displayText 显示文本
     */
    void unSelectRow(String header, String displayText)

    /**
     * 根据主列选择行,精确匹配
     * @param displayText 主列显示文本
     */
    void selectRow(String displayText)

    /**
     * 根据主列判断某行是否选中,精确匹配
     * @param displayText 显示文本
     */
    boolean isRowSelected(String displayText)

    /**
     * 判断某行是否选中,精确匹配
     * @param header 列头文本
     * @param displayText 显示文本
     */
    boolean isRowSelected(String header, String displayText)

    /**
     * 根据主列取消选择行,精确匹配
     * @param displayText 主列显示文本
     */
    void unSelectRow(String displayText)

    /**
     * 选择行
     * @param header 列头文本
     * @param displayText 显示文本
     * @param isUnique 是否精确查找,默认为true
     */
    void selectRow(String header, String displayText, boolean isUnique)

    /**
     * 取消选择行
     * @param header 列头文本
     * @param displayText 显示文本
     * @param isUnique 是否精确查找,默认为true
     */
    void unSelectRow(String header, String displayText, boolean isUnique)

    /**
     * 选择行
     * @param row 行号
     */
    void selectRow(int row)

    /**
     * 取消选择某行<br>
     * 其实质为点击某行的第一个单元格
     * @param row 行号
     */
    void unSelectRow(int row)

    /**
     * 选择行
     * @param row 行元素
     */
    void selectRow(WebElement row)

    /**
     * 取消选择行
     * @param row 行元素
     */
    void unSelectRow(WebElement row)

    /**
     * 选择行,精确查找
     * @param map 行列文本对应的map
     */
    void selectRow(Map<String,String> map)

    /**
     * 取消选择行,精确查找
     * @param map 行列文本对应的map
     */
    void unSelectRow(Map<String,String> map)

    /**
     * 选择行
     * @param map 行列文本对应的map
     * @param isUnique 是否精确查找
     */
    void selectRow(Map<String,String> map, boolean isUnique)

    /**
     * 取消选择行
     * @param map 行列文本对应的map
     * @param isUnique 是否精确查找
     */
    void unSelectRow(Map<String,String> map, boolean isUnique)

    /**
     * 选择多行
     * @param rows 行号列表
     */
    void selectRows(List<Integer> rows)

    /**
     * 取消选择某几行<
     * @param row 行号
     */
    void unSelectRows(List<Integer> rows)

    /**
     * 全选所有行<br>
     * 表头中第一个单元格中包含type为checkbox的input元素
     */
    void selectAllRows()

    /**
     * 取消全选所有行<br>
     * 表头中第一个单元格中包含type为checkbox的input元素
     */
    void unSelectAllRows()

    /**
     * 当前表格中的记录数
     */
    int getCurrentPageRows()

    /**
     * 记录总数
     */
    int getTotalRows()

    /**
     * 当前页页码
     */
    int getCurrentPageNumber()

    /**
     * 获取当前的总页数
     */
    int getTotalPages()

    /**
     * 跳转到第number页
     * @param number 页号
     */
    void gotoPage(int number)

    /**
     * 设置每页显示数量
     * @param number 每页显示数量
     */
    void setRowsPerPage(int number)

    /**
     * 获取每页显示数量
     * @param number 每页显示数量
     */
    int getRowsPerPage()

    /**
     * 点击首页
     */
    void firstPage()

    /**
     * 点击末页
     */
    void lastPage()

    /**
     * 点击上一页
     */
    void prevPage()

    /**
     * 点击下一页
     */
    void nextPage()

    /**
     * 双击行
     * @param row 行号
     */
    void dbClickRow(int row)

    /**
     * 双击行,精确匹配
     * @param displayText 显示内容
     */
    void dbClickRow(String displayText)

    /**
     * 双击行,精确匹配
     * @param header 表头文本
     * @param displayText 显示内容
     */
    void dbClickRow(String header, String displayText)

    /**
     * 双击行
     * @param header 表头文本
     * @param displayText 显示内容
     * @param isUnique 是否精确搜索
     */
    void dbClickRow(String header, String displayText, boolean isUnique)


    /**
     * 取得所有翻页按钮
     */
    List<String> getAllLinkNames()

    /**
     * 取得所有翻页按钮
     */
    Map<String,WebElement> getAllLinks()
}
