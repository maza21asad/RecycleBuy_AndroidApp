package com.zmonster.recyclebuy.holder;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseViewHolder<BmobObject> extends RecyclerView.ViewHolder {


    protected Activity mActivity;
    protected RecyclerView.Adapter adapter;


    public void setActivity(Activity act) {
        mActivity = act;
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindData(BmobObject data);



    public static BaseViewHolder creator(Context context, ViewGroup parent) {
        throw new IllegalStateException("No implement");
    }

    public void bindDates(List<BmobObject> entries) {
    }

    public void onRecycled() {
    }

    public void onDestroy() {

    }
}
