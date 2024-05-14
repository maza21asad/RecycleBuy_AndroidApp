package com.zmonster.recyclebuy.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.base.DefaultActivity;
import com.zmonster.recyclebuy.adapter.PointsAdapter;
import com.zmonster.recyclebuy.bean.Points;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.PointsProxy;
import com.zmonster.recyclebuy.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class PointsDetailActivity extends DefaultActivity {
    private RecyclerView listView;
    private PointsAdapter listAdapter;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout emptyLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_detail);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        initView();
        initData();
        refresh();
    }

    private void initData() {
        listAdapter = new PointsAdapter(this);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(mLayoutManager);
        listView.setAdapter(listAdapter);
    }

    private void refresh() {
        LoadingDialog progress = new LoadingDialog(
                this,
                R.style.LoadingDialogStyle
        );
        progress.setMessage(getString(R.string.loading));
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        SharedPreferences sp = getSharedPreferences("userids", Context.MODE_PRIVATE);
        Long userID = sp.getLong("id", 0);
        List<Points> datas = PointsProxy.getInstance().findPoints(userID+"");
        if (userID != 0 && datas != null && datas.size() >0){
            progress.dismiss();
            refreshLayout.setRefreshing(false);

            listAdapter.setDatas(datas);
            emptyLayoutView.setVisibility(datas.size() == 0 ? View.VISIBLE : View.GONE);
        } else {
            progress.dismiss();
            emptyLayoutView.setVisibility(View.VISIBLE);
        }

    }

    private void initView() {
        emptyLayoutView = findViewById(R.id.empty_layout_view);
        AppCompatImageView goBack = findViewById(R.id.go_back);
        listView = findViewById(R.id.list_view);
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            refresh();
        });
        goBack.setOnClickListener(v -> finish());
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshTimeLineEvent(RefreshTimeLineEvent event) {
        refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}