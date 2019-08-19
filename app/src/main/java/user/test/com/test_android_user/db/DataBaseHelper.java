package user.test.com.test_android_user.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "city.db"; // 数据库名称
    public static final int VERSION = 1;             // 版本号

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + "\"t_city\" (" +
                "\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," +
                "\"city_id\" TEXT NOT NULL UNIQUE," +
                "\"city_name\" TEXT NOT NULL ," +
                "\"service_open\" INTEGER ," +
                "\"pinyin\" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("drop table if exists t_city");
            onCreate(db);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
