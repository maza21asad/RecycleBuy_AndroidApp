package com.zmonster.recyclebuy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.bean.LocalImage;
import com.zmonster.recyclebuy.holder.BaseViewHolder;
import com.zmonster.recyclebuy.holder.LocalImageItemViewHolder;

import java.util.ArrayList;
import java.util.List;


public class CoverSelectAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private final static int ITEMVIEWTYPE_TYPE_NORMAL = 1;

    private List<LocalImage> mData = new ArrayList<>();
    private Activity mActivity;

    public CoverSelectAdapter(Activity activity) {
        mActivity = activity;
    }

    public void setDatas(List<LocalImage> datas) {
        if (datas != null && datas.size() > 0) {
            mData.clear();
            mData.addAll(datas);
            if (mData.size() <= 4) {
                LocalImage localImage = new LocalImage();
                localImage.setAdd(true);
                mData.add(localImage);
            }
        } else {
            mData.clear();
            LocalImage localImage = new LocalImage();
            localImage.setAdd(true);
            mData.add(localImage);
        }
        notifyDataSetChanged();
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = new LocalImageItemViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.local_image_item_layout, parent, false));
        if (mActivity != null) {
            holder.setActivity(mActivity);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        final LocalImage item = mData.get(position);
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


    public List<String> getDatas() {
        List<String> data = new ArrayList<>();
        if (mData.size() > 0) {
            for (LocalImage localImage : mData) {
                if (!localImage.isAdd() && localImage.getPath() != null) {
                    data.add(localImage.getPath());
                }
            }
        }
        return data;
    }
}