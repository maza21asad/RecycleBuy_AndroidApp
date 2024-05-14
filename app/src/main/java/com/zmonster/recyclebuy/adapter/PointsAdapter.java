package com.zmonster.recyclebuy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.bean.Points;
import com.zmonster.recyclebuy.holder.BaseViewHolder;
import com.zmonster.recyclebuy.holder.PointsItemViewHolder;

import java.util.ArrayList;
import java.util.List;


public class PointsAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final static int ITEMVIEWTYPE_TYPE_NORMAL = 1;

    private List<Points> mData = new ArrayList<>();
    private Activity mActivity;

    public PointsAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setDatas(List<Points> datas) {
        if (datas != null && datas.size() > 0) {
            mData.clear();
            mData.addAll(datas);
            notifyDataSetChanged();
        } else {
            mData.clear();
            notifyDataSetChanged();
        }
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = new PointsItemViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.points_item_view, parent, false));
        if (mActivity != null) {
            holder.setActivity(mActivity);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        final Points item = mData.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ITEMVIEWTYPE_TYPE_NORMAL;
    }


}