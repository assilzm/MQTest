package base.utils

import base.exceptions.DateFormatException

import java.text.SimpleDateFormat

/**
 * 日期工具类
 */
class DateUtils {

    final static String DATE_FORMAT_STRING = "yyyy-MM-dd"
    final static String TIME_FORMAT_STRING = "yyyy-MM-dd HH:mm"
    final static String TIME_WITH_SECOND_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss"

    //日期文本
    final static List<String> TODAY_LIST = ["当天", "今天"]
    final static List<String> YESTERDAY_LIST = ["昨天"]
    final static List<String> TOMORROW_LIST = ["明天"]
    final static List<String> THE_DAY_AFTER_TOMORROW_LIST = ["后天"]

    //日期时间文本
    final static List<String> NOW_LIST = ["当前时间", "现在"]


    final static String DAYS_AFTER = /(\d+)天后/
    final static String DAYS_BEFORE = /(\d+)天前/

    final static String DATE_FORMAT = /\d{2,4}-\d{1,2}-\d{1,2}/

    final static String TIME_FORMAT = DATE_FORMAT + / \d{1,2}:\d{1,2}(:\d{1,2})?/


    static String getDate(int daysDiff = 0, String dateFormat = DATE_FORMAT_STRING) {
        SimpleDateFormat myFormatter = new SimpleDateFormat(dateFormat)
        Date nowTime = new Date()
        Calendar now = Calendar.getInstance()
        now.setTime(nowTime)
        now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH) + daysDiff)
        return myFormatter.format(now.getTime())
    }

    static String getDateAndTime(int seconds = 0, String dateFormat = TIME_FORMAT_STRING) {
        SimpleDateFormat myFormatter = new SimpleDateFormat(dateFormat)
        Date nowTime = new Date()
        Calendar now = Calendar.getInstance()
        now.setTime(nowTime)
        now.set(Calendar.SECOND, now.get(Calendar.SECOND) + seconds)
        return myFormatter.format(now.getTime())
    }

    static String getDateAndTimeByString(String time, String dateFormat = TIME_FORMAT_STRING) {
        if (time ==~ TIME_FORMAT) {
            return time
        } else {
            int timeDiff = 0
            switch (time) {
                case NOW_LIST:
                    timeDiff = 0
                    break
                case { it =~ DAYS_AFTER }:
                    def m = time =~ DAYS_AFTER
                    if (m.find())
                        timeDiff = m.group(1).toInteger()*60*60*24
                    break
                case { it =~ DAYS_BEFORE }:
                    def m = time =~ DAYS_BEFORE
                    if (m.find())
                        timeDiff = 0 - m.group(1).toInteger()*60*60*24
                    break
                default:
                    throw new DateFormatException("不支持的日期时间格式。")
            }
            return getDateAndTime(timeDiff, dateFormat)
        }

    }


    static String getDateByString(String time, String dateFormat = DATE_FORMAT_STRING) {
        if (time ==~ DATE_FORMAT) {
            return time
        } else {
            int timeDiff = 0
            switch (time) {
                case YESTERDAY_LIST:
                    timeDiff = -1
                    break
                case TODAY_LIST:
                    timeDiff = 0
                    break
                case TOMORROW_LIST:
                    timeDiff = 1
                    break
                case THE_DAY_AFTER_TOMORROW_LIST:
                    timeDiff = 2
                    break
                case { it =~ DAYS_AFTER }:
                    def m = time =~ DAYS_AFTER
                    if (m.find())
                        timeDiff = m.group(1).toInteger()
                    break
                case { it =~ DAYS_BEFORE }:
                    def m = time =~ DAYS_BEFORE
                    if (m.find())
                        timeDiff = 0 - m.group(1).toInteger()
                    break
                default:
                    throw new DateFormatException("不支持的日期格式。")
            }
            return getDate(timeDiff, dateFormat)
        }
    }
}
