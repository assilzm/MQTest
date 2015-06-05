package component.impl.table

import component.Component
import component.interfaces.Table

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

    List<Row> getRows(){
        return getTableBody().getRows()
    }

    Cell getCell(String headName, String text) {
        int headIndex = getHead(headName).index
        for (Row row in getRows()) {
            Cell cell = row.getCell(headIndex)
            if (cell.getText() == text) {
                return cell
            }
        }
        return null
    }

}
