package user.test.com.test_android_user.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class HistoryUtils {

    public static String getHistory(Context context) {
        SharedPreferences sp = context.getSharedPreferences("venus", Context.MODE_PRIVATE);
        String history = sp.getString("venus_city", "");
        return history;
    }

    public static String[] getHistoryArray(Context context) {
        String history = getHistory(context);
        if (TextUtils.isEmpty(history)) {
            return null;
        }
        if (history.indexOf(",") < 0) {
            return new String[]{history};
        }
        return history.split(",");
    }

    public static void putHistory(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String[] historyArray = getHistoryArray(context);
        SharedPreferences sp = context.getSharedPreferences("venus", Context.MODE_PRIVATE);
        if (historyArray == null || historyArray.length == 0) {
            sp.edit().putString("venus_city", str).commit();
            return;
        }
        // 过滤重复
        List<String> temp =Arrays.asList(historyArray);
        List<String> tempArray= new ArrayList<>(temp);
        Iterator<String> it = tempArray.iterator();
        while (it.hasNext()) {
            String st = it.next();
            if (str.equals(st)) {
                it.remove();
            }
        }
        historyArray = tempArray.toArray(new String[1]);
        String history = str;
        for (int i = 0; i < Math.min(historyArray.length, 8); i++) {
            history += ",";
            history += historyArray[i];
        }
        sp.edit().putString("venus_city", history).commit();
    }
}
