package user.test.com.test_android_user.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期操作工具类.
 */
public class DateUtil {

    /**
     * 英文简写如：2016
     */
    public static String FORMAT_Y = "yyyy";
    /**
     * 英文简写如：2016
     */
    public static String FORMAT_M = "MM";

    /**
     * 英文简写  如：2016-11
     */
    public static String FORMAT_YM = "yyyy-MM";

    /**
     * 英文简写如：12:01
     */
    public static String FORMAT_HM = "HH:mm";

    /**
     * 英文简写如：9:01
     */
    public static String FORMAT_HMM = "H:mm";

    /**
     * 英文简写  如：04-23
     */
    public static String FORMAT_MD = "MM-dd";

    /**
     * 英文简写如：1-12 12:01
     */
    public static String FORMAT_MDHM = "MM-dd HH:mm";

    /**
     * 英文简写（默认）如：2016-12-01
     */
    public static String FORMAT_YMD = "yyyy-MM-dd";

    /**
     * 英文全称  如：2016-12-01 23:15
     */
    public static String FORMAT_YMDHM = "yyyy-MM-dd HH:mm";

    /**
     * 英文全称  如：2016-12-01 23:15:06
     */
    public static String FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";

    /**
     * 精确到毫秒的完整时间    如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL_SN = "yyyyMMddHHmmssS";

    /**
     * 中文简写  如：2016年12月
     */
    public static String FORMAT_YM_CN = "yyyy年MM月";

    /**
     * 中文简写  如：2016年12月01日
     */
    public static String FORMAT_YMD_CN = "yyyy年MM月dd日";

    /**
     * 中文简写  如：2016年12月01日  12时
     */
    public static String FORMAT_YMDH_CN = "yyyy年MM月dd日 HH时";

    /**
     * 中文简写  如：2016年12月01日  12时12分
     */
    public static String FORMAT_YMDHM_CN = "yyyy年MM月dd日 HH时mm分";

    /**
     * 中文全称  如：2016年12月01日  23时15分06秒
     */
    public static String FORMAT_YMDHMS_CN = "yyyy年MM月dd日  HH时mm分ss秒";

    /**
     * 精确到毫秒的完整中文时间
     */
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";
    /**
     * 中英混写  如：12月01日  12:12
     */
    public static String FORMAT_MDHM_CN_EL = "MM月dd日 HH:mm";

    public static Calendar calendar = null;
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static String GetFullTime(long timeMillis) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timeMillis);
    }

    public static String formatTime(long timeMillis, String format) {

        return new SimpleDateFormat(format).format(timeMillis);
    }

    /**
     * @Title: getDay @Description: 精确到天 @param @param ss @param @return
     * 设定文件 @return String 返回类型 @throws
     */
    public static String getDay(String ss) {
        long ms = Long.parseLong(ss);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");// 初始化Formatter的转换格式。
        String hms = formatter.format(ms);
        return hms;
    }

    /***
     * String 转时间戳
     */
    public static long stringToLong(String ss) {
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ss);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @Title: getSecond @Description:精确到秒 @param @param ss @param @return
     * 设定文件 @return String 返回类型 @throws
     */
    public static String getSecond(String ss) {
        return getSecond(ss, getDatePattern());
    }

    public static String getSecond(String ss, String pattern) {
        try {
            if (TextUtils.isEmpty(pattern)) {
                pattern = getDatePattern();
            }
            if (ss != null && !ss.trim().equals("")) {
                long ms = Long.parseLong(ss);
                SimpleDateFormat formatter = new SimpleDateFormat(pattern);// 初始化Formatter的转换格式。
                String hms = formatter.format(ms);
                return hms;
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static String getSeconds(String ss) {
        if (ss != null && !ss.trim().equals("")) {
            long ms = Long.parseLong(ss);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 初始化Formatter的转换格式。
            String hms = formatter.format(ms);
            return hms;
        } else {
            return "";
        }
    }

    public static Date str2Date(String str) {
        return str2Date(str, null);
    }


    public static Date str2Date(String str, String format) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    public static Calendar str2Calendar(String str) {
        return str2Calendar(str, null);
    }


    public static Calendar str2Calendar(String str, String format) {
        Date date = str2Date(str, format);
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }


    public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
        return date2Str(c, null);
    }


    public static String date2Str(Calendar c, String format) {
        if (c == null) {
            return null;
        }
        return date2Str(c.getTime(), format);
    }

    public static String millisStr(long millis) {
        if (millis == 0) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return date2Str(c);
    }

    public static String millisStr(long millis, String format) {
        if (millis == 0) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        return date2Str(c, format);
    }


    public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
        return date2Str(d, null);
    }


    public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
        if (d == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return s;
    }


    public static String getCurDateStr() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" +
                c.get(Calendar.DAY_OF_MONTH) + "-" +
                c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) +
                ":" + c.get(Calendar.SECOND);
    }


    /**
     * 获得当前日期的字符串格式
     *
     * @param format 格式化的类型
     * @return 返回格式化之后的事件
     */
    public static String getCurDateStr(String format) {
        Calendar c = Calendar.getInstance();
        return date2Str(c, format);

    }


    /**
     * @param time 当前的时间
     * @return 格式到秒
     */

    public static String getMillon(long time) {
        return new SimpleDateFormat(getDatePattern()).format(time);

    }


    /**
     * @param time 当前的时间
     * @return 当前的天
     */
    public static String getDay(long time) {
        return new SimpleDateFormat("yyyy-MM-dd").format(time);

    }


    /**
     * @param time 时间
     * @return 返回一个毫秒
     */
    // 格式到毫秒
    public static String getSMillon(long time) {
        return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);

    }


    /**
     * 在日期上增加数个整月
     *
     * @param date 日期
     * @param n    要增加的月数
     * @return 增加数个整月
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();

    }


    /**
     * 在日期上增加天数
     *
     * @param date 日期
     * @param n    要增加的天数
     * @return 增加之后的天数
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();

    }


    /**
     * 获取距现在某一小时的时刻
     *
     * @param format 格式化时间的格式
     * @param h      距现在的小时 例如：h=-1为上一个小时，h=1为下一个小时
     * @return 获取距现在某一小时的时刻
     */
    public static String getNextHour(String format, int h) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date();
        date.setTime(date.getTime() + h * 60 * 60 * 1000);
        return sdf.format(date);

    }


    /**
     * 获取时间戳
     *
     * @return 获取时间戳
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());

    }


    /**
     * 功能描述：返回月
     *
     * @param date Date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }


    /**
     * 功能描述：返回日
     *
     * @param date Date 日期
     * @return 返回日份
     */
    public static int getDay(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 功能描述：返回小
     *
     * @param date 日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }


    /**
     * 功能描述：返回分
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }


    /**
     * 获得默认的 date pattern
     *
     * @return 默认的格式
     */
    public static String getDatePattern() {
        return FORMAT_YMDHMS;
    }


    /**
     * 返回秒钟
     *
     * @param date Date 日期
     * @return 返回秒钟
     */
    public static int getSecond(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }


    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return 提取字符串的日期
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());

    }


    /**
     * 功能描述：返回毫
     *
     * @param date 日期
     * @return 返回毫
     */
    public static long getMillis(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }


    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期字符串
     * @return 按默认格式的字符串距离今天的天数
     */
    public static int countDays(String date) {
        if (date == null) return -1;
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;

    }

    /**
     * 按默认格式的字符串距离今天的小时
     *
     * @param date 日期字符串
     * @return 按默认格式的字符串距离今天的天数
     */
    public static int countHour(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        long t1 = c.getTime().getTime();
        return (int) ((t / 1000 - t1 / 1000) / 3600);
    }


    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return 提取字符串日期
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return 按用户格式字符串距离今天的天数
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    public static String formatSeconds(int second) {

        if (second < 60) {
            return "" + second + "秒";
        }
        if (second < 3600) {
            int minute = second / 60;
            second = second - minute * 60;
            if (minute < 10) {
                if (second < 10) {
                    return minute + "分" + second + "秒";
                }
                return minute + "分" + second + "秒";
            }
            if (second < 10) {
                return minute + "分" + second + "秒";
            }
            return minute + "分" + second + "秒";
        }
        int hour = second / 3600;
        int minute = (second - hour * 3600) / 60;
        second = second - hour * 3600 - minute * 60;
        if (hour < 10) {
            if (minute < 10) {
                if (second < 10) {
                    return hour + "时" + minute + "分" + second + "秒";
                }
                return hour + "时" + minute + "分" + second + "秒";
            }
            if (second < 10) {
                return hour + "时" + minute + "分" + second + "秒";
            }
            return hour + "时" + minute + "分" + second + "秒";
        }
        if (minute < 10) {
            if (second < 10) {
                return hour + "时" + minute + "分" + second + "秒";
            }
            return hour + "时" + minute + "分" + second + "秒";
        }
        if (second < 10) {
            return hour + "时" + minute + "分" + second + "秒";
        }
        return hour + "时" + minute + "分" + second + "秒";
    }


    public static boolean isToday(long value) {
        boolean b = false;
        Date time = new Date(value);
        Date today = new Date();
        if (time != null) {
            String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(today);
            String timeDate = new SimpleDateFormat("yyyy-MM-dd").format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }


    /**
     * @param time "yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static boolean isMoreThenGivenDays(String time, int days) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(time);
            long temp = date.getTime();
            //当前时间- 传递的时间 >days天
            if ((System.currentTimeMillis() - temp) > days * 24 * 60 * 60 * 1000) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String formatYMDHMSToMDHM(String timeStr) {
        try {
            return new SimpleDateFormat("M月d日H:mm").format(Long.parseLong(timeStr));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatYMDHMSToMD(String timeStr) {
        try {
            return new SimpleDateFormat("M月d日").format(Long.parseLong(timeStr));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatYMDHMSToHM(String timeStr) {
        try {
            return new SimpleDateFormat("H:mm").format(Long.parseLong(timeStr));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param date         当前时间 yyyy-MM-dd HH:mm:ss
     * @param strDateBegin 开始时间 23:00:00
     * @param strDateEnd   结束时间 05:00:00
     * @return
     */
    public static boolean isInDate(Date date, String strDateBegin, String strDateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(date);
        // 截取当前时间时分秒
        int strDateH = Integer.parseInt(strDate.substring(11, 13));
        int strDateM = Integer.parseInt(strDate.substring(14, 16));
        int strDateS = Integer.parseInt(strDate.substring(17, 19));
        // 截取开始时间时分秒
        int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));
        int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));
        int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));
        // 截取结束时间时分秒
        int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));
        int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));
        int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));

        if (strDateBeginH > strDateEndH) { // 开始时间小时>结束时间小时
            if (strDateH > strDateBeginH || strDateH < strDateEndH) { // 当前时间小时大于等于开始时间小时，或当前时间小时小于等于结束时间小时
                return true;
            } else if (strDateH == strDateBeginH) {
                if (strDateM > strDateBeginM) {
                    return true;
                } else if (strDateM == strDateBeginM) {
                    if (strDateS > strDateBeginS) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (strDateH == strDateEndH) {
                if (strDateM < strDateEndM) {
                    return true;
                } else if (strDateM == strDateEndM) {
                    if (strDateS <= strDateEndS) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else if (strDateBeginH == strDateEndH) { // 开始时间小时=结束时间小时
            if (strDateM > strDateBeginM && strDateM < strDateEndM) {
                return true;
            } else if (strDateM == strDateBeginM) {
                if (strDateS > strDateBeginS) {
                    return true;
                } else {
                    return false;
                }
            } else if (strDateM == strDateEndM) {
                if (strDateS <= strDateEndS) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else { // 开始时间小时<结束时间小时
            if (strDateH > strDateBeginH && strDateH < strDateEndH) {
                return true;
            } else if (strDateH == strDateBeginH) {
                if (strDateM > strDateBeginM) {
                    return true;
                } else if (strDateM == strDateBeginM) {
                    if (strDateS > strDateBeginS) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else if (strDateH == strDateEndH) {
                if (strDateM < strDateEndM) {
                    return true;
                } else if (strDateM == strDateEndM) {
                    if (strDateS <= strDateEndS) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    /**
     * 根据毫秒数转化成时分秒，HH:mm:ss
     *
     * @param l
     * @return
     */
    public static String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;

        second = l.intValue() / 1000;

        if (second >= 60) {
            minute = second / 60;
            second = second % 60;
        }

        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }

        return (getTwoLength(hour) + ":" + getTwoLength(minute) + ":" + getTwoLength(second));
    }

    /**
     * 长度为2的时分秒，HH，mm，ss
     *
     * @param data
     * @return
     */
    private static String getTwoLength(final int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }

    /**
     * @param timeStr 2018-12-13 19:53:00
     * @return
     */
    public static String formatTimeToMDHM(String timeStr) {
        String timeFormatted = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(timeStr);
            sdf.applyPattern("M月d日H:mm");
            timeFormatted = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeFormatted;
    }

    /**
     * @param timeStr 2018-12-13 19:53:00
     * @return
     */
    public static String formatTimeToHM(String timeStr) {
        String timeFormatted = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(timeStr);
            sdf.applyPattern("H:mm");
            timeFormatted = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeFormatted;
    }

    /**
     * @param timeStr 2018-12-13 19:53:00
     * @return
     */
    public static String formatTimeToMD(String timeStr) {
        String timeFormatted = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(timeStr);
            sdf.applyPattern("M月d日");
            timeFormatted = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeFormatted;
    }

    /**
     * 时间格式是 2018-12-13 19:53:00这种样式的
     *
     * @param timeStr 2018-12-13 19:53:00
     * @return
     */
    private String formatTime(String timeStr) {
        String timeFormatted = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(timeStr);
            sdf.applyPattern("M月d日H:mm");
            timeFormatted = sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeFormatted;
    }
}
