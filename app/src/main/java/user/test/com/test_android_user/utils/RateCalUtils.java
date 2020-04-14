package user.test.com.test_android_user.utils;

import java.text.NumberFormat;

public class RateCalUtils {

    //日收益率
    public static double dayRate(double benjin, double cost, int time) {
        return benjin / (cost * time);
    }

    //月收益率
    public static double monthRate(double benjin, double cost, int time) {
        return dayRate(benjin, cost, time) * 30;
    }

    //年收益率
    public static double yearRate(double benjin, double cost, int time) {
        return monthRate(benjin, cost, time) * 12;
    }

    public static String formatPercent(double rate) {
        return getPercentFormat(rate, 9999, 3);
    }

    private static String getPercentFormat(double d, int IntegerDigits, int FractionDigits) {
        NumberFormat nf = java.text.NumberFormat.getPercentInstance();
        nf.setMaximumIntegerDigits(IntegerDigits);//小数点前保留几位
        nf.setMinimumFractionDigits(FractionDigits);// 小数点后保留几位
        String str = nf.format(d);
        return str;
    }
}
