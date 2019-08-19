package user.test.com.test_android_user.utils;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;


/**
 * Created by sj on 19/04/2017.
 */

public class SuperPermissionUtils {


    /**
     * 开启权限
     *
     * @param context
     * @param content
     */
    public static Dialog gotoPermissionSetting(Context context, String content) {
//        return new Dialog(context).setMessage(content)
//                .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
//                .setPositiveButton("去设置", (dialog, which) -> {
//                    SuperPermissionUtils.gotoPermissionSetting(context);
//                    dialog.dismiss();
//                }).show();
        Dialog dialog = new Dialog(context);
        dialog.setTitle(content);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                SuperPermissionUtils.gotoPermissionSetting(context);
            }
        });
        dialog.show();
        return dialog;
    }


    enum MANUFACTURER {
        unkonwn(null),
        miui(new Miui()),
        minushenyin(new MiuiShenyin()),
        huawei(new HuaWei()),
        meizu(new Meizu()),
        sony(new Sony()),
        oppo(new Oppo()),
        lg(new LG()),
        leshi(new Leshi()),
        _360(new _360());

        RomPermission romPermission;

        MANUFACTURER(RomPermission romPermission) {
            this.romPermission = romPermission;
        }

        public Intent getIntent(Context context) {
            if (romPermission == null) {
                for (MANUFACTURER manufacturer : this.values()) {
                    if (manufacturer.romPermission == null) {
                        continue;
                    }
                    Intent intent = manufacturer.romPermission.getIntent(context);
                    if (intent != null) {
                        sMANUFACTURER = manufacturer;
                        return intent;
                    }
                }
                return null;
            }
            return romPermission.getIntent(context);
        }
    }

    static MANUFACTURER sMANUFACTURER = MANUFACTURER.unkonwn;

    /**
     * 跳转到当前App权限管理页面
     *
     * @param context
     */
    public static void gotoPermissionSetting(Context context) {
        Intent intent = sMANUFACTURER.getIntent(context);
        if (intent != null && isIntentAvailable(intent, context)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            intent = getAppDetailSettingIntent(context);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    static abstract class RomPermission {

        protected abstract Intent get(Context context);

        public final Intent getIntent(Context context) {
            Intent intent = get(context);
            if (SuperPermissionUtils.isIntentAvailable(intent, AppContext.getAppContext())) {
                return intent;
            }
            return null;
        }
    }

    /**
     * miui 权限管理页面
     */
    static class Miui extends RomPermission {
        @Override
        protected Intent get(Context context) {
            Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
            return intent;
        }
    }

    /**
     * 小米 神隐模式
     */
    static class MiuiShenyin extends RomPermission {
        @Override
        protected Intent get(Context context) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName("com.miui.powerkeeper", "com.miui.powerkeeper.ui.HiddenAppsContainerManagementActivity"));
            intent.putExtra("extra_pkgname", context.getPackageName());
            return intent;
        }
    }

    /**
     * 魅族 权限管理页面
     */
    static class Meizu extends RomPermission {
        @Override
        protected Intent get(Context context) {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", context.getPackageName());
            return intent;
        }
    }

    /**
     * 华为 权限管理页面
     */
    static class HuaWei extends RomPermission {
        @Override
        protected Intent get(Context context) {
            Intent intent = new Intent();
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            return intent;
        }
    }

    /**
     * 索尼 权限管理页面
     */
    static class Sony extends RomPermission {
        @Override
        protected Intent get(Context context) {
            Intent intent = new Intent();
            intent.putExtra("packageName", context.getPackageName());
            ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
            intent.setComponent(comp);
            return intent;
        }
    }

    /**
     * oppo 权限管理页面
     */
    static class Oppo extends RomPermission {
        @Override
        protected Intent get(Context context) {
            Intent intent = new Intent();
            intent.putExtra("packageName", context.getPackageName());
            ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
            intent.setComponent(comp);
            return intent;
        }
    }

    /**
     * LG 权限管理页面
     */
    static class LG extends RomPermission {
        @Override
        protected Intent get(Context context) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.putExtra("packageName", context.getPackageName());
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
            intent.setComponent(comp);
            return intent;
        }
    }

    /**
     * 乐视 权限管理页面
     */
    static class Leshi extends RomPermission {
        @Override
        protected Intent get(Context context) {
            Intent intent = new Intent();
            intent.putExtra("packageName", context.getPackageName());
            ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
            intent.setComponent(comp);
            return intent;
        }
    }

    /**
     * 360 权限管理页面
     */
    static class _360 extends RomPermission {

        @Override
        protected Intent get(Context context) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.putExtra("packageName", context.getPackageName());
            ComponentName comp = new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
            intent.setComponent(comp);
            return intent;
        }
    }

    /**
     * 应用设置
     *
     * @param context
     * @return
     */
    private static Intent getAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return intent;
    }

    private static boolean isIntentAvailable(Intent intent, Context context) {
        return intent != null && context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
}
