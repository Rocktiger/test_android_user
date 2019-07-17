package user.test.com.test_android_user.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import user.test.com.test_android_user.BaseRecycleViewAdapter;
import user.test.com.test_android_user.R;
import user.test.com.test_android_user.bean.AccountFlowBean;

public class AccountFlowListAdapter extends BaseRecycleViewAdapter<AccountFlowBean> {
    public AccountFlowListAdapter(List<AccountFlowBean> mDatas) {
        super(mDatas);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_flow, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        //todo 动态加载图片
//        viewHolder.mIv_bank.setImageDrawable();
        viewHolder.mTv_bank_name_number.setText(mDatas.get(position).getBankNameNumber());
        viewHolder.mTv_bank_num_money.setText(mDatas.get(position).getBankNumMoney());
        viewHolder.tv_bank_data.setText(mDatas.get(position).getBankDate());
    }

    class ViewHolder extends BaseRecycleViewAdapter.BaseViewHolder {
        private ImageView mIv_bank;
        private TextView mTv_bank_name_number, mTv_bank_num_money, tv_bank_data;

        public ViewHolder(View itemView) {
            super(itemView);
            mIv_bank = itemView.findViewById(R.id.iv_bank);
            mTv_bank_name_number = itemView.findViewById(R.id.tv_bank_name_number);
            mTv_bank_num_money = itemView.findViewById(R.id.tv_bank_num_money);
            tv_bank_data = itemView.findViewById(R.id.tv_bank_data);
        }
    }
}
