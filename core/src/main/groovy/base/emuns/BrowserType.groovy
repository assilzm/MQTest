package base.emuns

/**
 * 浏览器枚举
 */

enum BrowserType {
    IE('Internet_Explorer'),
    FIREFOX('Firefox'),
    CHROME('Chrome'),
    PHANTOMJS('Phantomjs');

    private String value

    BrowserType(String _value) {
        value = _value
    }
}
