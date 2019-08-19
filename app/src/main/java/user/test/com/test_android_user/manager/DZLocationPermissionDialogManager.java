package user.test.com.test_android_user.manager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.FragmentActivity;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.concurrent.locks.ReentrantLock;

import user.test.com.test_android_user.R;
import user.test.com.test_android_user.utils.SuperPermissionUtils;

public class DZLocationPermissionDialogManager {

    private static DZLocationPermissionDialogManager mInstance = null;


    private static ReentrantLock LOCK = new ReentrantLock();


    public static DZLocationPermissionDialogManager getInstance() {
        try {
            LOCK.lock();
            if (null == mInstance)
                mInstance = new DZLocationPermissionDialogManager();
        } finally {
            LOCK.unlock();
        }
        return mInstance;
    }

    private DZLocationPermissionDialogManager() {

    }

    private Dialog mPermissionDialog = null;

    public synchronized void checkLocationPermission(Activity activity, LocationPermissionCallback callback) {

        if (null == callback) throw new NullPointerException("回调函数不能为空！");

        if (null != mPermissionDialog && mPermissionDialog.isShowing()) {
            callback.onGranted(false);
            return;
        }
        new RxPermissions((FragmentActivity) activity).request(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        callback.onGranted(true);
                    } else {
                        if (null != mPermissionDialog && mPermissionDialog.isShowing()) {
                            callback.onGranted(false);
                            return;
                        }
                        mPermissionDialog = SuperPermissionUtils.gotoPermissionSetting(activity, activity.getString(R.string.permissions_location));
                        callback.onGranted(false);
                    }
                });
    }

    public void dismissPermissionDialog() {

        if (null != mPermissionDialog && mPermissionDialog.isShowing())
            mPermissionDialog.dismiss();
    }

    public interface LocationPermissionCallback {

        void onGranted(boolean granted);
    }
}
