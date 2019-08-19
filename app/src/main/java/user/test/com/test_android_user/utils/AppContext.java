package user.test.com.test_android_user.utils;

import android.content.Context;

public class AppContext {
    
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static Context getAppContext() {
        return mContext;
    }
}
