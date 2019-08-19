package user.test.com.test_android_user.bean;

import android.support.annotation.Keep;
import android.text.TextUtils;

@Keep
public class City {
    private int city_id;
    private String city_name;
    private String pinyin;
    private int service_open;
    private int type;

    public City() {
    }

    public City(String name, String pinyin) {
        this.city_name = name;
        this.pinyin = pinyin;
    }

    public City(String name, int type) {
        this.city_name = name;
        this.type = type;
    }

    /**
     * 检查城市合法性
     * @return
     */
    public boolean checkCity(){
        if (city_id == 0 || TextUtils.isEmpty(city_name)){
            return false;
        }
        return true;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getName() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getService_open() {
        return service_open;
    }

    public void setService_open(int service_open) {
        this.service_open = service_open;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
