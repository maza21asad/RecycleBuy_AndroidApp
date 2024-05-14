package com.zmonster.recyclebuy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.bean.GreenPoints;
import com.zmonster.recyclebuy.holder.BaseViewHolder;
import com.zmonster.recyclebuy.holder.GreenItemViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Monster_4y
 */
public class GreenAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final static int ITEMVIEWTYPE_TYPE_NORMAL = 1;
    private List<GreenPoints> mData = new ArrayList<>();
    private Activity mActivity;

    public GreenAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setDatas(List<GreenPoints> datas) {
        if (datas != null && datas.size() > 0) {
            mData.clear();
            mData.addAll(datas);
            notifyDataSetChanged();
        } else {
            mData.clear();
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder holder = new GreenItemViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.points_item_view, parent, false));
        if (mActivity != null) {
            holder.setActivity(mActivity);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        final GreenPoints item = mData.get(position);
        holder.bindData(item);
        if(onRecyclerViewListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getAdapterPosition();
                    onRecyclerViewListener.onItemClick(holder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    @Override
    public int getItemViewType(int position) {
        return ITEMVIEWTYPE_TYPE_NORMAL;
    }


    private OnRecyclerViewListener onRecyclerViewListener;

    public interface OnRecyclerViewListener{
        void onItemClick(View view, int position);
    }

    public void setOnRecyclerViewListener(OnRecyclerViewListener mOnItemClickListener){
        this.onRecyclerViewListener = mOnItemClickListener;
    }
}
