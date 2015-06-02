package base.utils

import base.exceptions.StringFormatException

import static base.asserts.Assert.assertNotNull


/**
 * 字符串处理类
 */
class StringUtils {


    final static String DIGITAL_FORMAT = /\d+(\.\d+)?/
    final static String RATE_FORMAT = /\d+(\.\d+)?%/


    /**
     * 默认的参数分割符
     */
    final static String ESCAPE_CHARS = /(?:[、,，]|->)/

    /**
     * 输入参数的字符串转为列表
     * @param str 输入参数对应的字符串
     * @param regex 分割字符串所使用正则表达式，默认为{@link #ESCAPE_CHARS}
     * @return 分割字符串后生成的列表
     */
    static List<String> getList(String str, String regex = ESCAPE_CHARS) {
        assertNotNull(str)
        List<String> list = new ArrayList<String>()
        //统一转换为新的特殊字符
        list.addAll(str.split(regex))
        return list
    }

    /**
     * unicode转String
     * @param text
     * @return
     */
    static String native2String(String text) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        char c;
        while (i < text.length()) {
            c = text.charAt(i);
            if (c == '\\' && (i + 1) != text.length() && text.charAt(i + 1) == 'u') {
                sb.append((char) Integer.parseInt(text.substring(i + 2, i + 6), 16));
                i += 6;
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }


    /**
     * 规整字符串,特殊符号前加\\
     * @param text 要处理的字符串
     * @return 处理后的字符串
     */
    static String formatString(String text) {
        text.replaceAll(/([\^*()\-+?\[\]\}\{\.'"\\])/, "\\\\\$1")
    }

    /**
     * 检查字符串中是否含有某列表中所有字符串
     * @param string 字符串
     * @param listString 待验证字符串列表
     * @param byRegex 是否为正则
     * @return
     */
    static boolean stringHasAllListString(String string, List<String> listString, boolean byRegex = false) {
        boolean hasString = true
        for (String _string : listString) {
            if (byRegex) {
                boolean m = string =~ _string
                hasString = hasString && m
            } else
                hasString = hasString && string.contains(_string)
        }
        hasString
    }

    /**
     * 格式化字符串为n位小数形式，默认2位
     * @param str 字符串
     * @param decimal 小数位数
     * @return 格式化后的字符串
     */
    static String formatStringToDecimal(String str,int decimal=2){
        assertDigitalFormat(str)
        formatDoubleToDecimal(convertStringToDouble(str),decimal)
    }

    /**
     * 格式化浮点数为n位小数形式，默认2位
     * @param dou 浮点数
     * @param decimal 小数位数
     * @return 格式化后的字符串
     */
    static String formatDoubleToDecimal(Double dou,int decimal=2){
        String.format("%.${decimal}f",dou)
    }

    /**
     * 转换字符串到整数
     * @param str
     * @return
     */
    static Integer convertStringToInteger(String str){
        convertStringToDouble(str).toInteger()
    }

    /**
     * 转换字符串到浮点数
     * @param str
     * @return
     */
    static Double convertStringToDouble(String str){
        assertDigitalFormat(str)
        Double.parseDouble(str)
    }

    /**
     * 转换字符串到百分比
     * @param str
     * @return
     */
    static String convertStringToRate(String str,int decimal=0 ){
        assertDigitalFormat(str)
        formatDecimalToRate(convertStringToDouble(str),decimal)
    }

    /**
     * 将浮点数转换为百分比
     * @param str
     * @return
     */
    static String formatDecimalToRate(Double dou,int decimal=0 ){
        String.format("%.${decimal}f%%",dou*100)
    }

    /**
     * 断言字符串是否是数字
     * @param str
     */
    static void assertDigitalFormat(String str){
        if(!isDigitalFormat(str))
            throw new StringFormatException("字符串${str}不是数字格式。")
    }

    /**
     * 检查字符串是否是数字
     * @param str
     */
    static boolean isDigitalFormat(String str){
        return str==~DIGITAL_FORMAT
    }

    /**
     * 检查字符串是否是百分比
     * @param str
     */
    static boolean isRateFormat(String str){
        return str==~RATE_FORMAT
    }

    /**
     * 将指定字符串重复N次
     * @param str 指定的字符串
     * @param times 次数
     */
    static String createRepeatedString(String str,int times=1){
        String retStr=""
        times.times {
            retStr+=str
        }
        return retStr
    }
}
