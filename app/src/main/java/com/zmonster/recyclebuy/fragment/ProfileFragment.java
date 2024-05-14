package com.zmonster.recyclebuy.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.EditPasswordActivity;
import com.zmonster.recyclebuy.activities.EditUserInfoActivity;
import com.zmonster.recyclebuy.activities.FavoriteActivity;
import com.zmonster.recyclebuy.activities.LoginActivity;
import com.zmonster.recyclebuy.activities.OrderActivity;
import com.zmonster.recyclebuy.activities.PointsDetailActivity;
import com.zmonster.recyclebuy.activities.SellerCenterActivity;
import com.zmonster.recyclebuy.activities.releaseNewsActivity;
import com.zmonster.recyclebuy.bean.User;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.UserProxy;
import com.zmonster.recyclebuy.view.FontTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * @author Monster_4y
 */
public class ProfileFragment extends Fragment {

    private Activity mActivity;
    private FontTextView userName;
    private FontTextView descrition;
    private AlertDialog show = null;
    private SwipeRefreshLayout refreshLayout;
    private RoundedImageView userIcon;
    private RoundedImageView userSexState;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
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
        contentView = inflater.inflate(R.layout.fragment_profile, container, false);
        EventBus.getDefault().register(this);
        initView(contentView);
        initData();
        return contentView;
    }

    private void initData() {
        SharedPreferences sp = getContext().getSharedPreferences("userids", Context.MODE_PRIVATE);
        Long userID = sp.getLong("id", 0);
        User hostUser = UserProxy.getInstance().findUser(userID);
        userName.setText(hostUser.getUserName());
        descrition.setText(hostUser.getDescription());
        userSexState.setImageResource(hostUser.getSex().equals(getString(R.string.man)) ? R.mipmap.male : R.mipmap.female);
        if (!hostUser.getCover().isEmpty()) {
            Picasso.get()
                    .load(hostUser.getCover())
                    .placeholder(R.mipmap.default_user_icon)
                    .error(R.mipmap.default_user_icon)
                    .resize(userIcon.getLayoutParams().width, userIcon.getLayoutParams().height)
                    .centerCrop()
                    .into(userIcon);
        }
    }

    private void initView(View contentView) {
        userName = contentView.findViewById(R.id.user_name);
        userIcon = contentView.findViewById(R.id.user_icon);
        FontTextView myFavorite = contentView.findViewById(R.id.my_favorite);
        userSexState = contentView.findViewById(R.id.user_sex_state);
        descrition = contentView.findViewById(R.id.user_description);
        FontTextView profileInfo = contentView.findViewById(R.id.profile_info);
        FontTextView userInctegral = contentView.findViewById(R.id.user_inctegral);
        FontTextView myOrder = contentView.findViewById(R.id.my_order);
        FontTextView sellerCenter = contentView.findViewById(R.id.seller_center);
        FontTextView releaseNews = contentView.findViewById(R.id.release_news);
        FontTextView exit = contentView.findViewById(R.id.exit);
        FontTextView updatePassword = contentView.findViewById(R.id.update_password);
        refreshLayout = contentView.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this::refresh);

        SharedPreferences sharedPreferences = mActivity.getSharedPreferences("userids", Context.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "");

            sellerCenter.setVisibility(View.VISIBLE);
        profileInfo.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, EditUserInfoActivity.class);
            mActivity.startActivity(intent);
        });
        updatePassword.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, EditPasswordActivity.class);
            mActivity.startActivity(intent);
        });
        userInctegral.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, PointsDetailActivity.class);
            mActivity.startActivity(intent);
        });
        myOrder.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, OrderActivity.class);
            mActivity.startActivity(intent);
        });
        myFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, FavoriteActivity.class);
            mActivity.startActivity(intent);
        });
        releaseNews.setOnClickListener(v->{
            Intent intent = new Intent(mActivity, releaseNewsActivity.class);
            mActivity.startActivity(intent);
        });
        sellerCenter.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, SellerCenterActivity.class);
            mActivity.startActivity(intent);
        });
        exit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.tip);
            builder.setMessage(getString(R.string.tip_exit_current_current));
            builder.setCancelable(false);
            builder.setPositiveButton(getString(R.string.exit), (dialog, which) -> {
                if (show != null) {
                    show.dismiss();
                }
                //UserProxy.getInstance().clear();
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                mActivity.finish();

            });
            builder.setNegativeButton(getString(R.string.no_thx), (dialog, which) -> {
                if (show != null) {
                    show.dismiss();
                }
            });
            show = builder.show();

        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshTimeLineEvent(RefreshTimeLineEvent event) {
        refresh();
    }

    private void refresh() {
        SharedPreferences sp = mActivity.getSharedPreferences("userids", Context.MODE_PRIVATE);
        Long userID = sp.getLong("id", 0);

        if (mActivity != null) {
            mActivity.runOnUiThread(() -> {
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
                    User user = UserProxy.getInstance().findUser(userID);
                    SharedPreferences sharedPreferences = mActivity.getSharedPreferences("userids", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putLong("id", user.getId());
                    editor.apply();
                    user.setId(userID);

                    userName.setText(user.getUserName());
                    descrition.setText(user.getDescription());
                    userSexState.setImageResource(user.getSex().equals("男") ? R.mipmap.male : R.mipmap.female);
                    if (!user.getCover().isEmpty()) {
                        Picasso.get()
                                .load(user.getCover())
                                .placeholder(R.mipmap.default_user_icon)
                                .error(R.mipmap.default_user_icon)
                                .resize(userIcon.getLayoutParams().width, userIcon.getLayoutParams().height)
                                .centerCrop()
                                .into(userIcon);
                    }



            });
        }

    }


    @Override
    public void onViewCreated(View view, Bundle bundle) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
