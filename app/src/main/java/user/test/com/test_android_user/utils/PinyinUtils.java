package user.test.com.test_android_user.utils;

import java.util.regex.Pattern;

import user.test.com.test_android_user.bean.City;


/**
 * author zaaach on 2016/1/28.
 */
public class PinyinUtils {
    /**
     * 获取拼音的首字母（大写）
     * @return
     */
    public static String getFirstLetter(City city){
        if(city == null) {
            return "";
        }
        String pinyin = city.getPinyin();
        if(pinyin == null) {
            return "";
        }
        String c = pinyin.substring(0, 1);
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c).matches()){
            return c.toUpperCase();
        }
        return city.getName();
    }
}
