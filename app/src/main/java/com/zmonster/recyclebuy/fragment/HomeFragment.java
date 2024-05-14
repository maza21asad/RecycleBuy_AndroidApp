package com.zmonster.recyclebuy.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.adapter.HomeAdapter;
import com.zmonster.recyclebuy.bean.Shop;
import com.zmonster.recyclebuy.bean.User;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.ShopProxy;
import com.zmonster.recyclebuy.proxy.UserProxy;
import com.zmonster.recyclebuy.view.FontTextView;
import com.zmonster.recyclebuy.view.LoadingDialog;
import com.zmonster.recyclebuy.view.SpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class HomeFragment extends Fragment {

    private Activity mActivity;
    private RecyclerView listView;
    private String state = "0";
    private User hostUser;
    private HomeAdapter listAdapter;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout emptyLayoutView;
    private boolean init = true;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mActivity = getActivity();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
        }
    }

    private View contentView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        contentView = inflater.inflate(R.layout.fragment_home, container, false);
        EventBus.getDefault().register(this);
        init();
        return contentView;
    }

    private void init() {
        initView();
        initData();
        refresh();
    }

    private void initData() {
        SharedPreferences sp = getContext().getSharedPreferences("userids", Context.MODE_PRIVATE);
        Long userID = sp.getLong("id", 0);
        hostUser = UserProxy.getInstance().findUser(userID);
        listAdapter = new HomeAdapter(mActivity);
        StaggeredGridLayoutManager mLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        listView.setLayoutManager(mLayoutManager);
        listView.addItemDecoration(new SpacesItemDecoration(8));
        listView.setAdapter(listAdapter);
    }

    private void initView() {
        emptyLayoutView = contentView.findViewById(R.id.empty_layout_view);
        FontTextView actionBarTitle = contentView.findViewById(R.id.actionbar_title);
        ImageView actionbarBackBtn = contentView.findViewById(R.id.actionbar_back_btn);
        actionBarTitle.setText(getString(R.string.shop));
        actionbarBackBtn.setVisibility(View.GONE);
        listView = contentView.findViewById(R.id.list_view);
        refreshLayout = contentView.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(true);
            refresh();
        });
    }

    private LoadingDialog progress = null;


    @Override
    public void onViewCreated(View view, Bundle bundle) {

    }

    private void refresh() {
        if (init) {
            progress = new LoadingDialog(
                    mActivity,
                    R.style.LoadingDialogStyle
            );
            progress.setMessage(getString(R.string.loading));
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }

        mActivity.runOnUiThread(() -> {
            if (init && progress != null) {
                init = false;
                progress.dismiss();
            }
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
            List<Shop> datas = ShopProxy.getInstance().getALL();
            if (datas != null){
                listAdapter.setDatas(datas);
                emptyLayoutView.setVisibility(datas.size() == 0 ? View.VISIBLE : View.GONE);
            } else {
                emptyLayoutView.setVisibility(View.VISIBLE);
            }


        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshTimeLineEvent(RefreshTimeLineEvent event) {
        refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
