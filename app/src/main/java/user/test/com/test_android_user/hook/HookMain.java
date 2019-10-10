package user.test.com.test_android_user.hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class HookMain implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        findAndHookMethod(
                "android.telephony.TelephonyManager",  //要hook的包名+类名
                loadPackageParam.classLoader,                   //classLoader固定
                "getDeviceId",                         //要hook的方法名
                //方法参数 没有就不填
                new XC_MethodHook() {
                    @Override
                    //方法执行前执行
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    }
                    //方法执行后执行,改方法的返回值一定要在方法执行完毕后更改
                    @Override
                    protected void afterHookedMethod(MethodHookParam param)
                            throws Throwable {
                        param.setResult("1231321512313");

                    }
                }
        );
    }
}
