package user.test.com.test_android_user.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import user.test.com.test_android_user.R;
import user.test.com.test_android_user.bean.City;
import user.test.com.test_android_user.bean.LocateState;
import user.test.com.test_android_user.utils.PinyinUtils;
import user.test.com.test_android_user.widget.WrapHeightGridView;
import user.test.com.test_android_user.widget.stickylist.StickyListHeadersAdapter;


/**
 * author zaaach on 2016/1/26.
 */
public class CityListAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private static int VIEW_TYPE_COUNT = 3;

    private static int VIEW_TYPE_LOCATION = 0;
    private static int VIEW_TYPE_HISTORY = 1;
    private static int VIEW_TYPE_HOT = 2;
    private static int VIEW_TYPE_NORMAL = 3;

    private Context mContext;
    private LayoutInflater inflater;
    private List<City> mCities;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private OnCityClickListener onCityClickListener;
    private int locateState = LocateState.LOCATING;
    private String locatedCity;

    List<String> historyCities;

    public CityListAdapter(Context mContext, List<City> mCities) {
        this.mContext = mContext;
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(mContext);
        if (mCities == null) {
            mCities = new ArrayList<>();
        }

        mCities.add(0, new City("定位", "0"));
//        String[] historyArray = HistoryUtils.getHistoryArray(mContext);

//        if (historyArray == null || historyArray.length == 0) {
            mCities.add(1, new City("热门城市", "1"));
            VIEW_TYPE_COUNT = 3;
//        } else {
//            historyCities = new ArrayList<>();
//            List<String> tempHistoryCities = Arrays.asList(historyArray);
//            for (String city : tempHistoryCities) {
//                if (TextUtils.isEmpty(city) || "null".equals(city)) continue;
//                historyCities.add(city);
//            }
//            mCities.add(1, new City("历史", "1"));
//            mCities.add(2, new City("热门城市", "2"));
//            VIEW_TYPE_COUNT = 4;
//        }

        int size = mCities.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++) {
            //当前城市拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(mCities.get(index));
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(mCities.get(index - 1)) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }

    public void updateSections() {
        int size = mCities.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++) {
            //当前城市拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(mCities.get(index));
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(mCities.get(index - 1)) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }

    /**
     * 更新定位状态
     *
     * @param state
     */
    public void updateLocateState(int state, String city) {
        this.locateState = state;
        this.locatedCity = city;
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return position < VIEW_TYPE_COUNT - 1 ? position : VIEW_TYPE_COUNT - 1;
    }

    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public City getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        CityViewHolder holder;
        int viewType = getItemViewType(position);
        // 隐藏历史
        if (VIEW_TYPE_COUNT == 3) {
            if (viewType > 0) {
                viewType++;
            }
        }
        switch (viewType) {
            case 0:     //定位
                view = inflater.inflate(R.layout.cp_view_locate_city, parent, false);
                TextView state = (TextView) view.findViewById(R.id.tv_located_city);
                switch (locateState) {
                    case LocateState.LOCATING:
                        state.setText(mContext.getString(R.string.cp_locating));
                        break;
                    case LocateState.FAILED:
                        state.setText(R.string.cp_located_failed);
                        break;
                    case LocateState.SUCCESS:
                        state.setText(locatedCity);
                        break;
                }
                state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (locateState == LocateState.FAILED) {
                            //重新定位
                            if (onCityClickListener != null) {
                                onCityClickListener.onLocateClick();
                            }
                        } else if (locateState == LocateState.SUCCESS) {
                            //返回定位城市
                            if (onCityClickListener != null) {
                                onCityClickListener.onCityClick(locatedCity);
                            }
                        }
                    }
                });
                break;
            case 1:     //历史
                view = inflater.inflate(R.layout.cp_view_hot_city, parent, false);
                if (historyCities != null) {
                    WrapHeightGridView gridViewHistory = (WrapHeightGridView) view.findViewById(R.id.gridview_hot_city);
                    final HistoryCityGridAdapter historyCityGridAdapter = new HistoryCityGridAdapter(mContext, historyCities);
                    gridViewHistory.setAdapter(historyCityGridAdapter);
                    gridViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (onCityClickListener != null) {
                                onCityClickListener.onCityClick(historyCityGridAdapter.getItem(position));
                            }
                        }
                    });
                }
                break;
            case 2:     //热门
                view = inflater.inflate(R.layout.cp_view_hot_city, parent, false);
                WrapHeightGridView gridView = (WrapHeightGridView) view.findViewById(R.id.gridview_hot_city);
                final HotCityGridAdapter hotCityGridAdapter = new HotCityGridAdapter(mContext);
                gridView.setAdapter(hotCityGridAdapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (onCityClickListener != null) {
                            onCityClickListener.onCityClick(hotCityGridAdapter.getItem(position));
                        }
                    }
                });
                break;
            case 3:     //所有
                if (view == null) {
                    view = inflater.inflate(R.layout.cp_item_city_listview, parent, false);
                    holder = new CityViewHolder();
                    holder.letter = (TextView) view.findViewById(R.id.tv_item_city_listview_letter);
                    holder.name = (TextView) view.findViewById(R.id.tv_item_city_listview_name);
                    view.setTag(holder);
                } else {
                    holder = (CityViewHolder) view.getTag();
                }
                if (position >= 1) {
                    final String city = mCities.get(position).getName();
                    holder.name.setText(city);
                    holder.name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onCityClickListener != null) {
                                onCityClickListener.onCityClick(city);
                            }
                        }
                    });
                }
                break;
        }
        return view;
    }

    @Override
    public View getHeaderView(int position, View view, ViewGroup parent) {
        HeadViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.cp_activity_city_head, parent, false);
            holder = new HeadViewHolder();
            holder.title = (TextView) view.findViewById(R.id.tv_item_city_listview_letter);
            view.setTag(holder);
        } else {
            holder = (HeadViewHolder) view.getTag();
        }
        int viewType = getItemViewType(position);
        // 隐藏历史
        if (VIEW_TYPE_COUNT == 3) {
            if (viewType > 0) {
                viewType++;
            }
        }
        switch (viewType) {
            case 0:
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText("定位");
                break;
            case 1:
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText("历史");
                break;
            case 2:
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText("热门城市");
                break;
            default:
                if (position >= 1) {
                    String currentLetter = PinyinUtils.getFirstLetter(mCities.get(position));
                    String previousLetter = position >= 1 ? PinyinUtils.getFirstLetter(mCities.get(position - 1)) : "";
                    if (!TextUtils.equals(currentLetter, previousLetter)) {
                        holder.title.setText(currentLetter);
                    } else {
                        holder.title.setText(previousLetter);
                    }
                }
                break;
        }
        return view;
    }

    @Override
    public long getHeaderId(int position) {
        int viewType = getItemViewType(position);
        // 隐藏历史
        if (VIEW_TYPE_COUNT == 3) {
            if (viewType < 2) {
                return viewType;
            }
        } else if (VIEW_TYPE_COUNT == 4) {
            if (viewType < 3) {
                return viewType;
            }
        }
        String currentLetter = PinyinUtils.getFirstLetter(mCities.get(position));
        return 1 + currentLetter.charAt(0);
    }

    public static class CityViewHolder {
        TextView letter;
        TextView name;
    }

    public static class HeadViewHolder {
        TextView title;
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    public interface OnCityClickListener {
        void onCityClick(String name);

        void onLocateClick();
    }
}
