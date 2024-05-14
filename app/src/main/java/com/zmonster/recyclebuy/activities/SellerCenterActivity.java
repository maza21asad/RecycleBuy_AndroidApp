package com.zmonster.recyclebuy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.base.DefaultActivity;
import com.zmonster.recyclebuy.adapter.SellerCenterAdapter;
import com.zmonster.recyclebuy.bean.Shop;
import com.zmonster.recyclebuy.bean.User;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.ShopProxy;
import com.zmonster.recyclebuy.proxy.UserProxy;
import com.zmonster.recyclebuy.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class SellerCenterActivity extends DefaultActivity {
    private RecyclerView listView;
    private User hostUser;
    private SellerCenterAdapter listAdapter;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout emptyLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_center);
        actionBar = getSupportActionBar();
        setActionBarTitle(getString(R.string.seller_center));
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        initView();
        initData();
        refresh();
    }

    private void initData() {
        SharedPreferences sp = getSharedPreferences("userids", Context.MODE_PRIVATE);
        Long userID = sp.getLong("id", 0);
        hostUser = UserProxy.getInstance().findUser(userID);
        listAdapter = new SellerCenterAdapter(this);
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
        if (userID != 0) {
            progress.dismiss();
            refreshLayout.setRefreshing(false);
            List<Shop> datas = ShopProxy.getInstance().findShop(userID + "");
            if (datas != null) {
                listAdapter.setDatas(datas);
                emptyLayoutView.setVisibility(datas.size() == 0 ? View.VISIBLE : View.GONE);
            } else {
                emptyLayoutView.setVisibility(View.VISIBLE);
            }
        } else {
            progress.dismiss();
            refreshLayout.setRefreshing(false);
            emptyLayoutView.setVisibility(View.VISIBLE);
        }

    }

    private void initView() {
        emptyLayoutView = findViewById(R.id.empty_layout_view);
        FloatingActionButton addEvent = findViewById(R.id.add_event);
        listView = findViewById(R.id.list_view);
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            refresh();
        });
        addEvent.setOnClickListener(v -> {
            Intent in = new Intent(SellerCenterActivity.this, ShopCreateActivity.class);
            startActivity(in);
        });
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