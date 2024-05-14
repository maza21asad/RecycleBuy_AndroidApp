package com.zmonster.recyclebuy.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.base.DefaultActivity;
import com.zmonster.recyclebuy.adapter.FavoriteAdapter;
import com.zmonster.recyclebuy.bean.Favorite;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.FavoriteProxy;
import com.zmonster.recyclebuy.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;


public class FavoriteActivity extends DefaultActivity {
    private RecyclerView listView;
    private FavoriteAdapter listAdapter;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout emptyLayoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        actionBar = getSupportActionBar();
        setActionBarTitle(getString(R.string.favorite));
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        initView();
        initData();
        refresh();
    }

    private void initData() {
        listAdapter = new FavoriteAdapter(this);
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


        runOnUiThread(() -> {
            progress.dismiss();
            refreshLayout.setRefreshing(false);
            List<Favorite> favorite = FavoriteProxy.getInstance().findFavorite(userID + "");
            if (favorite != null ){
                listAdapter.setDatas(favorite);
                emptyLayoutView.setVisibility(favorite.size() == 0 ? View.VISIBLE : View.GONE);
            } else {
                emptyLayoutView.setVisibility(View.VISIBLE);
            }

        });

    }

    private void initView() {
        emptyLayoutView = findViewById(R.id.empty_layout_view);
        listView = findViewById(R.id.list_view);
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            refresh();
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