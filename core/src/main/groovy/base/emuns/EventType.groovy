package base.emuns

/**
 * JQuery trigger事件枚举
 * 封装常用的鼠标和键盘操作事件
 */
enum EventType {
    CLICK('click'),
    DBCLICK('dblclick'),
    MOUSEENTER('mouseenter'),
    MOUSELEAVE('mouseleave'),
    MOUSEOVER('mouseover'),
    MOUSEOUT('mouseout'),
    MOUSEDOWN('mousdown'),
    MOUSEUP('mouseup'),
    FOCUSIN('focusin'),
    FOCUSOUT('focusout'),
    KEYPRESS('keypress'),
    KEYUP('keyup'),
    CHANGE('change'),
    BLUR('blur')


    String value

    EventType(String _value) {
        value = _value
    }
}
