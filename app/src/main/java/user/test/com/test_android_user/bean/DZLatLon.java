package user.test.com.test_android_user.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;

/**
 * 跟地图相关的经纬度都要用这个
 * Created by huzeyin on 2018/1/25.
 */
@Keep
public class DZLatLon implements Parcelable {
    //纬度
    public double latitude;
    //经度
    public double longitude;

    public DZLatLon(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DZLatLon(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public String toString() {
        return "lat/lng: (" + this.latitude + "," + this.longitude + ")";
    }

    public static final Creator<DZLatLon> CREATOR = new Creator<DZLatLon>() {

        @Override
        public DZLatLon createFromParcel(Parcel source) {
            return new DZLatLon(source);
        }

        @Override
        public DZLatLon[] newArray(int size) {
            return new DZLatLon[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel var1, int var2) {
        var1.writeDouble(this.latitude);
        var1.writeDouble(this.longitude);
    }
}
