package user.test.com.test_android_user.manager;

import android.app.Application;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import user.test.com.test_android_user.bean.DZLatLon;
import user.test.com.test_android_user.bean.DZLocation;
import user.test.com.test_android_user.utils.AppContext;

/**
 * Location管理器，统一管理
 * Created by huzeyin on 2018/2/2.
 */

public class DZLocationManager {

    private static final String TAG = "DZLocationManager";

    private ArrayList<DZLocationListener> mListeners;

    private static DZLocationManager sInstance = null;

    private static final ReentrantLock LOCK = new ReentrantLock();

    private AMapLocationClient mapLocationClient;
    private AMapLocationClientOption mLocationOption;


    public static DZLocationManager getInstance() {
        try {
            LOCK.lock();
            if (null == sInstance)
                sInstance = new DZLocationManager();
        } finally {
            LOCK.unlock();
        }
        return sInstance;
    }

    private DZLocationManager() {
        mListeners = new ArrayList<>();
    }

    private volatile AMapLocation mAMapLocation;

    private AMapLocationListener mInnerAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

            if (null == aMapLocation) return;

            mAMapLocation = aMapLocation;

            //检查是否有listeners
            synchronized (mListeners) {
                if (mListeners.size() > 0) {
                    int errorCode = aMapLocation.getErrorCode();
                    if (errorCode == 12 || errorCode == 13) {
                        if (checkLastPermissionErrorTime()) {
                            notifyOnPermissionError();
                        }
                        return;
                    }
                    notifyOnLocationChanged(mAMapLocation);
                }
            }
        }
    };

    /**
     * 获取当前的定位城市
     *
     * @return
     */
    public DZLocation getDZLocation() {

        if (null == mAMapLocation) return null;
        DZLocation location = new DZLocation();
        location.latLon = new DZLatLon(mAMapLocation.getLatitude(), mAMapLocation.getLongitude());
        location.city = mAMapLocation.getCity();
        return location;
    }

    /**
     * 开始定位 对应activity的onResume
     */
    public void startLocation() {

        //如果已经启动了定位，就不需要在重新设置参数了，防止多次调用
        if (null != mapLocationClient && mapLocationClient.isStarted()) return;

        restartLocation();
    }

    /**
     * 重新定位
     */
    public void restartLocation() {
        resetOption();
        if (mapLocationClient == null) {
            mapLocationClient = new AMapLocationClient(AppContext.getAppContext());
        }
        mapLocationClient.setLocationListener(mInnerAMapLocationListener);
        mapLocationClient.setLocationOption(mLocationOption);
        mapLocationClient.startLocation();
    }

    /**
     * 停止定位，但是资源不会释放 对应activity的onPause
     */
    public void stopLocation() {
        //do nothing 因为是单例模式，所以如果调用了这个方法，会导致不再定位
//        if (mapLocationClient != null && mapLocationClient.isStarted()) {
//            mapLocationClient.stopLocation();
//            mapLocationClient.unRegisterLocationListener(mInnerAMapLocationListener);
//        }
    }

    /**
     * 停止定位，释放所有资源 ,这个方法在app退出后，需要调用 对应activity的onDestroy
     */
    public void destroyLocation() {
        if (null != mapLocationClient) {
            mapLocationClient.unRegisterLocationListener(mInnerAMapLocationListener);
            mapLocationClient.onDestroy();
            mapLocationClient = null;
            mLocationOption = null;
        }
    }

    public DZLatLon getLastLatLng() {

        DZLatLon lastLatLng = null;

        if (mapLocationClient != null) {
            AMapLocation location = mapLocationClient.getLastKnownLocation();
            if (location != null) {
                lastLatLng = new DZLatLon(location.getLatitude(), location.getLongitude());
            }
        }
        if (null == lastLatLng && null != mAMapLocation) {
            lastLatLng = new DZLatLon(mAMapLocation.getLatitude(), mAMapLocation.getLongitude());
        }
        if (null != lastLatLng) {
//            DZLogUtils.LOGD(TAG, "getLastLatLng latitude:" + lastLatLng.latitude + "---longitude:" + lastLatLng.longitude);
        } else {
//            DZLogUtils.LOGE(TAG, "getLastLatLng is null");
        }
        return lastLatLng;
    }

    public void addMapLocationListener(DZLocationListener listener) {
        synchronized (mListeners) {
            if (!mListeners.contains(listener))
                mListeners.add(listener);
        }
    }

    public void removeMapLocationListener(Object value) {
        synchronized (mListeners) {
            if (mListeners.contains(value)) {
                mListeners.remove(value);
            }
        }
    }

    public static interface DZLocationListener {

        void onLocationChanged(DZLocation location);

        /**
         * 权限错误
         */
        void onPermissionError();
    }

    private void notifyOnLocationChanged(AMapLocation aMapLocation) {
        synchronized (mListeners) {
            DZLocation location = new DZLocation();
            location.latLon = new DZLatLon(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            location.city = aMapLocation.getCity();
            for (DZLocationListener listener : mListeners) {
                listener.onLocationChanged(location);
            }
        }
    }

    private void notifyOnPermissionError() {
        synchronized (mListeners) {
            for (DZLocationListener listener : mListeners) {
                listener.onPermissionError();
            }
        }
    }

    private void resetOption() {
        if (mLocationOption == null) {
            mLocationOption = new AMapLocationClientOption();
        }
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(2000);
        mLocationOption.setSensorEnable(true);
        mLocationOption.setGpsFirst(false);
    }


    private static Long lastTime;

    private boolean checkLastPermissionErrorTime() {
        boolean result = false;
        long current = System.currentTimeMillis();
        if (lastTime == null) {
            lastTime = new Long(0);
        }
        int second = (int) ((current - lastTime) / 1000);
        if (second > 60) {
            result = true;
            lastTime = current;
        }
        return result;
    }
}
