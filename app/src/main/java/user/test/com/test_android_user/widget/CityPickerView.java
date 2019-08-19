package user.test.com.test_android_user.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import user.test.com.test_android_user.R;
import user.test.com.test_android_user.adapter.CityListAdapter;
import user.test.com.test_android_user.adapter.ResultListAdapter;
import user.test.com.test_android_user.bean.City;
import user.test.com.test_android_user.bean.LocateState;
import user.test.com.test_android_user.utils.DBManager;
import user.test.com.test_android_user.utils.HistoryUtils;
import user.test.com.test_android_user.widget.stickylist.StickyListHeadersListView;


public class CityPickerView extends FrameLayout implements View.OnClickListener {

    private StickyListHeadersListView mListView;
    private ListView mResultListView;
    private SideBar mLetterBar;
    private ImageView clearBtn;
    private ViewGroup emptyView;
    private EditText searchBox;

    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private final List<City> mAllCities = new ArrayList<>();

    public CityPickerView(Context context) {
        this(context, null);
    }

    public CityPickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CityPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutInflater.inflate(R.layout.cp_view_city_list, this, true);

        initData();
        initView();
    }

    DBManager dbManager;

    public DBManager getDb() {
        if (dbManager == null) {
            dbManager = new DBManager(getContext());
        }
        return dbManager;
    }

    public List<City> getDefault() {
        getDb().copyDBFile();
        return getDb().getAllCities();
    }

    public List<City> searchCity(String keyword) {
        if (onCityPickerViewListener != null) {
            return onCityPickerViewListener.onSearchCity(keyword);
        }
        return getDb().searchCity(keyword);
    }

    public void updateData(List<City> cities) {
        mAllCities.clear();
        mAllCities.addAll(cities);
        mCityAdapter.updateSections();
        mCityAdapter.notifyDataSetChanged();
    }

    private void initData() {

        mCityAdapter = new CityListAdapter(getContext(), mAllCities);
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                if (onCityPickerViewListener != null) {
                    onCityPickerViewListener.onSelect(name);
                    HistoryUtils.putHistory(CityPickerView.this.getContext(), name);
                }
            }

            @Override
            public void onLocateClick() {
                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
            }
        });

        mResultAdapter = new ResultListAdapter(getContext(), null);
    }

    private void initView() {
        mListView = (StickyListHeadersListView) findViewById(R.id.listview_all_city);
        mListView.setAdapter(mCityAdapter);

        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideBar) findViewById(R.id.side_letter_bar);
        mLetterBar.setTextView(overlay);
        mLetterBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });

        searchBox = (EditText) findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                    mResultListView.setVisibility(View.VISIBLE);
                    List<City> result = searchCity(keyword);
                    if (result == null || result.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }
                }
            }
        });

        emptyView = (ViewGroup) findViewById(R.id.empty_view);
        mResultListView = (ListView) findViewById(R.id.listview_search_result);
        mResultListView.setAdapter(mResultAdapter);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onCityPickerViewListener != null) {
                    onCityPickerViewListener.onSelect(mResultAdapter.getItem(position).getName());
                    HistoryUtils.putHistory(CityPickerView.this.getContext(), mResultAdapter.getItem(position).getName());
                }
            }
        });

        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
        clearBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_search_clear) {
            searchBox.setText("");
            clearBtn.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            mResultListView.setVisibility(View.GONE);
        }
    }

    public void updateLocation(int state, String city) {
        mCityAdapter.updateLocateState(LocateState.SUCCESS, city);
    }

    OnCityPickerViewListener onCityPickerViewListener;

    public void setOnCityPickerViewListener(OnCityPickerViewListener onCityPickerViewListener) {
        this.onCityPickerViewListener = onCityPickerViewListener;
    }

    public interface OnCityPickerViewListener {
        void onSelect(String name);

        List<City> onSearchCity(String keyword);
    }
}
