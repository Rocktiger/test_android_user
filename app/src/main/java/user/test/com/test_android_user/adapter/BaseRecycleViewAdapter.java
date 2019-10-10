package user.test.com.test_android_user.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter {

    protected List<T> mDatas = new ArrayList<T>();

    public BaseRecycleViewAdapter() {

    }

    public BaseRecycleViewAdapter(List<T> datas) {
        mDatas = datas;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addDatas(ArrayList<T> datas, boolean notify) {
        if (null == datas || datas.size() < 0) return;
        mDatas.addAll(datas);
        if (notify)
            notifyDataSetChanged();
    }


    public void setDatas(ArrayList<T> datas, boolean notify) {
        if (null == datas || datas.size() < 0) return;
        mDatas.clear();
        mDatas.addAll(datas);
        if (notify)
            notifyDataSetChanged();
    }

    public void clearData(boolean notify) {
        mDatas.clear();
        if (notify)
            notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public static interface RecycleViewItemListener {

        public void onItemClick(View view, int position);

        public void OnItemLongClickListener(View view, int position);
    }

    public static class SimpleRecycleViewItemListener implements RecycleViewItemListener {
        @Override
        public void onItemClick(View view, int position) {

        }

        @Override
        public void OnItemLongClickListener(View view, int position) {

        }
    }

    private RecycleViewItemListener mItemListener;

    public void setItemListener(RecycleViewItemListener listener) {
        mItemListener = listener;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
            if (null != mItemListener) {
                itemView.setOnClickListener(v -> {
                    mItemListener.onItemClick(itemView, getAdapterPosition());
                });
                itemView.setOnLongClickListener(v -> {
                    mItemListener.OnItemLongClickListener(itemView, getAdapterPosition());
                    return false;
                });
            }
        }
    }
}
