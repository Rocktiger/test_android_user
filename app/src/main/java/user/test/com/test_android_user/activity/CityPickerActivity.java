package user.test.com.test_android_user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import user.test.com.test_android_user.R;
import user.test.com.test_android_user.bean.City;
import user.test.com.test_android_user.bean.DZLocation;
import user.test.com.test_android_user.bean.LocateState;
import user.test.com.test_android_user.db.CityDao;
import user.test.com.test_android_user.manager.DZLocationManager;
import user.test.com.test_android_user.manager.DZLocationPermissionDialogManager;
import user.test.com.test_android_user.utils.AppContext;
import user.test.com.test_android_user.utils.PinYin;
import user.test.com.test_android_user.widget.CityPickerView;


public class CityPickerActivity extends AppCompatActivity implements CityPickerView.OnCityPickerViewListener, DZLocationManager.DZLocationListener {

    private CityPickerView cityPickerView;
    private CityDao cityDao;

    @Override
    public void onResume() {
        super.onResume();
        DZLocationManager.getInstance().addMapLocationListener(this);
        DZLocationManager.getInstance().startLocation();
    }

    @Override
    public void onPause() {
        super.onPause();
        DZLocationManager.getInstance().removeMapLocationListener(this);
        DZLocationManager.getInstance().stopLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citypicker);
        cityPickerView = (CityPickerView) findViewById(R.id.view_citypick);
        cityPickerView.setOnCityPickerViewListener(this);
        DZLocationManager.getInstance().addMapLocationListener(this);
        DZLocationPermissionDialogManager.getInstance().checkLocationPermission(this, granted -> {
            if (granted) {
                DZLocationManager.getInstance().startLocation();
            }
        });
        cityDao = new CityDao(AppContext.getAppContext());
        List<City> cities = cityDao.queryAll();
        boolean isGetNew = true;
        if (cities != null && !cities.isEmpty()) {
            loadData(cities);
            isGetNew = false;
        }
        getAllCitiTask(cities, isGetNew);
    }

    private void getAllCitiTask(List<City> oldCities, boolean isGetNew) {
//        new SysApi().getCitys(-1, new DZRxSubscriberHelper<DCity>(CityPickerActivity.this, isGetNew) {
//            @Override
//            public void _onNext(DCity cityBean) {
//                List<DCityItem> area = cityBean.getArea();
//
//                if (area == null || area.isEmpty()) {
//                    return;
//                }
//
//                /**
//                 * 节省性能这里只做数量的对比
//                 * 数量相等，不做比较，插入，等 直接返回
//                 */
//                if (oldCities != null && area.size() == oldCities.size()) {
//                    return;
//                }
//
//                List<City> citys = new ArrayList<>();
//                for (DCityItem cityBean2 : area) {
//                    City city = new City();
//                    city.setCity_id(cityBean2.getId());
//                    city.setCity_name(cityBean2.getArea());
//                    city.setService_open(cityBean2.getServiceOpen());
//                    city.setPinyin(PinYin.getPinYin(cityBean2.getArea()).substring(0, 1));
//                    citys.add(city);
//                }
//
//                BaseDataManager.getInstance().upCityDBData(cityBean);
//                loadData(citys);
//            }
//        });
        //todo  数据查询
        List<City> citys = new ArrayList<>();
        City city1 = new City();
        city1.setCity_id(1);
        city1.setCity_name("上海");
        city1.setPinyin(PinYin.getPinYin("上海".substring(0, 1)));
        citys.add(city1);
        City city2 = new City();
        city2.setCity_id(2);
        city2.setCity_name("北京");
        city2.setPinyin(PinYin.getPinYin("北京".substring(0, 1)));
        citys.add(city2);
        City city3 = new City();
        city3.setCity_id(3);
        city3.setCity_name("广州");
        city3.setPinyin(PinYin.getPinYin("广州".substring(0, 1)));
        citys.add(city3);
        City city4 = new City();
        city4.setCity_id(4);
        city4.setCity_name("深圳");
        city4.setPinyin(PinYin.getPinYin("深圳".substring(0, 1)));
        citys.add(city4);
        City city5 = new City();
        city5.setCity_id(5);
        city5.setCity_name("长沙");
        city5.setPinyin(PinYin.getPinYin("长沙".substring(0, 1)));
        citys.add(city5);
        CityDao cityDao = new CityDao(AppContext.getAppContext());
        cityDao.insertOrUpdate(citys);
        loadData(citys);

    }

    private void loadData(List<City> cities) {
        cityPickerView.updateData(cities);
    }

    @Override
    public void onSelect(String city) {
        Intent data = new Intent();
        data.putExtra("extra_city", city);
        setResult(RESULT_OK, data);
        Toast.makeText(this, city, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public List<City> onSearchCity(String keyword) {
        return cityDao.searchCity(keyword);
    }

    @Override
    public void onLocationChanged(DZLocation location) {
        String city = location.city;
        cityPickerView.updateLocation(LocateState.SUCCESS, city);
    }

    @Override
    public void onPermissionError() {
        cityPickerView.updateLocation(LocateState.FAILED, null);
    }

}
