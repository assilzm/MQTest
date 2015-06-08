package component.impl.table

import component.Component
import component.interfaces.Table

import static base.asserts.Assert.assertGreater
import static base.asserts.Assert.assertNotNull

/**
 * TODO:
 * 抽象的列表类
 */
abstract class AbstractTable extends Component implements Table {

    String tableSelecotr = "table"

    String tableBodySelector = "tbody"

    String tableHeadSelector = "thead"

    String rowSelector = "tr"

    String headSelector = "th"

    String cellSelector = "td"

    /**
     * 列头
     */
    TableHead tableHead

    /**
     * 表体
     */
    TableBody tableBody

    /**
     * 初始化列表的方法
     */
    void init() {
        tableHeadSelector = "${tableSelecotr} ${tableHeadSelector}"
        tableBodySelector = "${tableSelecotr} ${tableBodySelector}"
        if (tableHeadSelector)
            tableHead = new TableHead(tableHeadSelector, headSelector)
        tableBody = new TableBody(tableBodySelector, rowSelector, cellSelector)
    }


    Cell getHead(int index) {
        return tableHead.getHead(index)
    }

    Cell getHead(String headName) {
        return tableHead.getHead(headName)
    }

    Cell getCell(int rowIndex, int columnIndex) {
        return getRow(rowIndex).getCell(columnIndex)
    }

    Row getRow(int index) {
        return getTableBody().getRow(index)
    }

    List<Row> getRows() {
        return getTableBody().getRows()
    }

    Cell getCell(String headName, String text) {
        List<Cell> cells = getCells(headName, text)
        return cells.size() ? cells.get(0) : null
    }

    List<Cell> getCells(String headName, String text) {
        int headIndex = getHead(headName).getIndex()
        assertGreater("验证提供的列头存在", -1)
        List<Cell> cells = new ArrayList<>()
        for (Row row in getRows()) {
            Cell cell = row.getCell(headIndex)
            if (cell.getText() == text) {
                cells.add(cell)
            }
        }
        return cells
    }

    Row getRow(String headName, String text) {
        Cell cell = getCell(headName, text)
        return cell == null ? null : getRow(cell)
    }

    List<Row> getRows(String headName, String text) {
        List<Cell> cells = getCells(headName, text)
        return getRows(cells)
    }

    Row getRow(Cell cell) {
        assertNotNull("提供的单元格必须存在。", cell)
        List<Row> rows=getRows([cell])
        return rows.size() ? rows.get(0) : null
    }

    List<Row> getRows(List<Cell> cells){
        List<Row> rows=new ArrayList<>()
        List<Row> allRows=getRows()
        cells.each {cell->
            Row row=allRows.find({row-> row.getCells().contains(cell) })
            if(!rows.contains(row)) rows.add(row)
        }
        return rows
    }


}
