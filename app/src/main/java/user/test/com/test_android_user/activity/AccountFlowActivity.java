package user.test.com.test_android_user.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import user.test.com.test_android_user.R;
import user.test.com.test_android_user.widget.SpacesItemDecoration;
import user.test.com.test_android_user.adapter.AccountFlowListAdapter;
import user.test.com.test_android_user.bean.AccountFlowBean;
import user.test.com.test_android_user.utils.DateUtil;
import user.test.com.test_android_user.widget.MDatePicker;

public class AccountFlowActivity extends AppCompatActivity {

    private TextView mTv_select_month;
    private TextView mTv_month;
    private com.scwang.smartrefresh.layout.SmartRefreshLayout mSrl_refresh;
    private android.support.v7.widget.RecyclerView mRv_list;

    private AccountFlowListAdapter mAdapter;
    private List<AccountFlowBean> mDatas = new ArrayList<>();
    private Date selectDate = new Date();
    private int currentPage = 1;//默认第一页
    private String startDate = "2009-01";
    //    private String endDate = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YM);
    private String endDate = "2029-01";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_flow);
        bindViews();
        initEvent();
        initAdapter();
        //todo 请求接口获取数据
        getListAccountFlow(DateUtil.date2Str(selectDate, DateUtil.FORMAT_YM_CN), currentPage, true);
    }


    private void bindViews() {
        mTv_select_month = (TextView) findViewById(R.id.tv_select_month);
        mTv_month = (TextView) findViewById(R.id.tv_month);
        mSrl_refresh = (com.scwang.smartrefresh.layout.SmartRefreshLayout) findViewById(R.id.srl_refresh);
        mRv_list = (android.support.v7.widget.RecyclerView) findViewById(R.id.rv_list);
    }

    private void initEvent() {
        mTv_select_month.setOnClickListener(v -> {
            new MDatePicker(this, time -> {
                if (TextUtils.isEmpty(time)) {
                    Toast.makeText(v.getContext(), "时间选择有误!", Toast.LENGTH_SHORT).show();
                    return;
                }
                selectDate = DateUtil.str2Date(time, DateUtil.FORMAT_YM);
                currentPage = 1;
                getListAccountFlow(DateUtil.date2Str(selectDate, DateUtil.FORMAT_YM_CN), currentPage, true);

            }, startDate, endDate).show(DateUtil.date2Str(selectDate, DateUtil.FORMAT_YM));

        });
        mSrl_refresh.setOnRefreshListener(refreshLayout -> {
            currentPage = 1;
            getListAccountFlow(DateUtil.date2Str(selectDate, DateUtil.FORMAT_YM_CN), currentPage, true);
        });
        mSrl_refresh.setOnLoadMoreListener(refreshLayout -> {
            getListAccountFlow(DateUtil.date2Str(selectDate, DateUtil.FORMAT_YM_CN), currentPage, false);
        });
    }


    private void getListAccountFlow(String data, int pageNum, boolean isRefresh) {
        //网咯请求成功
        currentPage++; //页码+1
        mTv_select_month.setText(data);
        mTv_month.setText(getMonth(data) + "月账单");
        if (isRefresh) {
            mDatas.clear();
        }
        //todo 这里添加数据
//        mDatas.addAll();
        AccountFlowBean accountFlowBean = new AccountFlowBean();
        accountFlowBean.setBankNameNumber("中国银行|1213");
        accountFlowBean.setBankNumMoney("20笔   200000.0元");
        accountFlowBean.setBankDate("2011.5.2-2012.5.5");
        AccountFlowBean accountFlowBean1 = new AccountFlowBean();
        accountFlowBean1.setBankNameNumber("中国银行|1213");
        accountFlowBean1.setBankNumMoney("20笔   200000.0元");
        accountFlowBean1.setBankDate("2011.5.2-2012.5.5");
        AccountFlowBean accountFlowBean2 = new AccountFlowBean();
        accountFlowBean2.setBankNameNumber("中国银行|1213");
        accountFlowBean2.setBankNumMoney("20笔   200000.0元");
        accountFlowBean2.setBankDate("2011.5.2-2012.5.5");
        AccountFlowBean accountFlowBean3 = new AccountFlowBean();
        accountFlowBean3.setBankNameNumber("中国银行|1213");
        accountFlowBean3.setBankNumMoney("20笔   200000.0元");
        accountFlowBean3.setBankDate("2011.5.2-2012.5.5");
        AccountFlowBean accountFlowBean4 = new AccountFlowBean();
        accountFlowBean4.setBankNameNumber("中国银行|1213");
        accountFlowBean4.setBankNumMoney("20笔   200000.0元");
        accountFlowBean4.setBankDate("2011.5.2-2012.5.5");
        mDatas.add(accountFlowBean);
        mDatas.add(accountFlowBean1);
        mDatas.add(accountFlowBean2);
        mDatas.add(accountFlowBean3);
        mDatas.add(accountFlowBean4);
        mAdapter.notifyDataSetChanged();
        if (mSrl_refresh.isLoading()) {
            mSrl_refresh.finishLoadMore();
        }
        if (mSrl_refresh.isRefreshing()) {
            mSrl_refresh.finishRefresh();
        }
        Toast.makeText(this, "currentPage==" + currentPage-- + "---当前月份" + DateUtil.date2Str(selectDate, DateUtil.FORMAT_YM_CN), Toast.LENGTH_LONG).show();
        //todo  网咯请求失败
//
//        if (mSrl_refresh.isLoading()) {
//            mSrl_refresh.finishLoadMore();
//        }
//        if (mSrl_refresh.isRefreshing()) {
//            mSrl_refresh.finishRefresh();
//        }
    }

    private void initAdapter() {
        //这个可以替换 去github官网看
        mSrl_refresh.setRefreshHeader(new ClassicsHeader(this));
        mSrl_refresh.setRefreshFooter(new ClassicsFooter(this).setSpinnerStyle(SpinnerStyle.Translate));
        mAdapter = new AccountFlowListAdapter(mDatas);
        mRv_list.setLayoutManager(new LinearLayoutManager(this));
        mRv_list.addItemDecoration(new SpacesItemDecoration(40));
        mRv_list.setAdapter(mAdapter);
    }

    private String getMonth(String data) {
        Date date = DateUtil.str2Date(data, DateUtil.FORMAT_YM_CN);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return (calendar.get(Calendar.MONTH) + 1) + "";
    }

}
