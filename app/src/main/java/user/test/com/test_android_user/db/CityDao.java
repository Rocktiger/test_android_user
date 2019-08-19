package user.test.com.test_android_user.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import user.test.com.test_android_user.bean.City;


/**
 * {@link DataBaseHelper}
 */
public class CityDao {

    private static final String TABLE = "t_city";
    private final String CITY_ID = "city_id";
    private final String CITY_NAME = "city_name";
    private final String PINYIN = "pinyin";
    private final String SERVICE_OPRN = "service_open";

    private DataBaseHelper mDataBaseHelper;
    private SQLiteDatabase db;

    public CityDao(Context context) {
        mDataBaseHelper = new DataBaseHelper(context);
    }

    public boolean isEmpty() {
        db = mDataBaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) as c from " + TABLE, null);
        if (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            if (count > 0) {
                return false;
            }
        }
        return true;
    }

    public long insert(City city) {
        db = mDataBaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CITY_ID, city.getCity_id());
        values.put(CITY_NAME, city.getName());
        values.put(PINYIN, city.getPinyin());
        values.put(SERVICE_OPRN, city.getService_open());
        long result = db.insert(TABLE, null, values);
        closeQuietly(db);
        return result;
    }

    public boolean queryCityExist(SQLiteDatabase db, String city_id){
        if (db == null){
            db = mDataBaseHelper.getReadableDatabase();
        }
        boolean isExist = false;
        Cursor cursor = null;
        try{
            cursor = db.query(TABLE, new String[]{CITY_ID}, CITY_ID + "=?", new String[]{city_id}, null, null, null, null);
            if (cursor !=null && cursor.getCount()>0) {
                isExist = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closeQuietly(cursor);
        }
        return isExist;
    }

    public long insertOrUpdate(List<City> citys) {
        long result = -1;
        db = mDataBaseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (City city : citys) {
                if (city != null && city.checkCity()) {
                    ContentValues values = new ContentValues();
                    values.put(CITY_ID, city.getCity_id());
                    values.put(CITY_NAME, city.getName());
                    values.put(PINYIN, city.getPinyin());
                    values.put(SERVICE_OPRN, city.getService_open());
                    if (!queryCityExist(db, city.getCity_id()+"")){
                        result = db.insert(TABLE, null, values);
                    }else {
                        result = db.update(TABLE, values, CITY_ID + "=?", new String[]{
                                city.getCity_id()+""
                        });
                    }
                }
            }
            result = 0;
            db.setTransactionSuccessful();
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        } finally {
            db.endTransaction();
            closeQuietly(db);
        }
        return result;
    }

    public List<City> queryAll() {
        db = mDataBaseHelper.getReadableDatabase();
        List<City> cities = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setCity_id(cursor.getInt(cursor.getColumnIndex(CITY_ID)));
                city.setCity_name(cursor.getString(cursor.getColumnIndex(CITY_NAME)));
                city.setPinyin(cursor.getString(cursor.getColumnIndex(PINYIN)));
                city.setService_open(cursor.getInt(cursor.getColumnIndex(SERVICE_OPRN)));
                cities.add(city);
            } while (cursor.moveToNext());
        }
        closeQuietly(db);
        Collections.sort(cities, new CityComparator());
        return cities;
    }

    public List<City> searchCity(final String keyword) {
        return searchCity(keyword, true);
    }

    public List<City> searchCity(final String keyword, boolean isIncludePinYin) {
        db = mDataBaseHelper.getReadableDatabase();
        String sqlStr = "select * from " + TABLE + " where city_name like \"%" + keyword + "%\"" + (isIncludePinYin ? " or pinyin like \"%" + keyword + "%\"" : "");
        Cursor cursor = db.rawQuery(sqlStr, null);
        List<City> result = new ArrayList<>();
        City city;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CITY_NAME));
            String pinyin = cursor.getString(cursor.getColumnIndex(PINYIN));
            city = new City(name, pinyin);
            city.setService_open(cursor.getInt(cursor.getColumnIndex(SERVICE_OPRN)));
            city.setCity_id(cursor.getInt(cursor.getColumnIndex(CITY_ID)));
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }

    private void closeQuietly(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * sort by a-z
     */
    private class CityComparator implements Comparator<City> {
        @Override
        public int compare(City lhs, City rhs) {
            if (lhs == null || rhs == null || TextUtils.isEmpty(lhs.getPinyin()) || TextUtils.isEmpty(rhs.getPinyin())) {
                return 0;
            }
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            return a.compareTo(b);
        }
    }
}
