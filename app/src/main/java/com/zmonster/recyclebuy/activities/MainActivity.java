package com.zmonster.recyclebuy.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.base.DefaultActivity;
import com.zmonster.recyclebuy.bean.Shop;
import com.zmonster.recyclebuy.fragment.CartFragment;
import com.zmonster.recyclebuy.fragment.HomeFragment;
import com.zmonster.recyclebuy.fragment.InformationFragment;
import com.zmonster.recyclebuy.fragment.ProfileFragment;
import com.zmonster.recyclebuy.proxy.ShopProxy;

import java.util.ArrayList;

public class MainActivity extends DefaultActivity implements OnTabSelectListener {
    private final int SDK_PERMISSION_REQUEST = 127;
    private HomeFragment mHomeFragment;
    private CartFragment mCartFragment;
    private ProfileFragment mProfileFragment;
    private InformationFragment mInformationFragment;
    private long exitTime = 0;
    private Fragment mCurrentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        initView();
        //权限申请
        getPersimmions();

    }

    private void initView() {
        BottomBar bottomBar = findViewById(R.id.bottom_bar);
        initFragments();
        bottomBar.setOnTabSelectListener(this);
    }


    private void initFragments() {
        FragmentManager manager = getSupportFragmentManager();
        mHomeFragment = (HomeFragment) manager.findFragmentByTag(HomeFragment.class.getName());
        mCartFragment = (CartFragment) manager.findFragmentByTag(CartFragment.class.getName());
        mProfileFragment = (ProfileFragment) manager.findFragmentByTag(ProfileFragment.class.getName());
        mInformationFragment = (InformationFragment) manager.findFragmentByTag(InformationFragment.class.getName());
    }



    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();

            if (checkSelfPermission(Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    permissions.add(Manifest.permission.ACCESS_MEDIA_LOCATION);
                }
            }
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[0]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @Override
    public void onTabSelected(int tabId) {
        switch (tabId) {
            case R.id.tab_home:
                if (mHomeFragment == null) mHomeFragment = HomeFragment.newInstance();
                switchFragments(mHomeFragment);
                break;
            case R.id.tab_paper:
                if (mInformationFragment == null) mInformationFragment = InformationFragment.newInstance();
                switchFragments(mInformationFragment);
                break;
            case R.id.tab_event:
                if (mCartFragment == null) mCartFragment = CartFragment.newInstance();
                switchFragments(mCartFragment);
                break;
            case R.id.tab_my:
                if (mProfileFragment == null) mProfileFragment = ProfileFragment.newInstance();
                switchFragments(mProfileFragment);
                break;
            case R.id.tab_green:
                Intent in = new Intent(MainActivity.this, ShopCreateActivity.class);
                startActivity(in);
                break;
            default:
                throw new UnsupportedOperationException("Illegal branch!");
        }

    }

    private void switchFragments(Fragment target) {
        if (mCurrentFragment == target) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        if (target.isAdded()) {
            transaction.show(target);
        } else {
            transaction.add(R.id.main_container, target, target.getClass().getName());
        }
        transaction.commit();
        mCurrentFragment = target;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp();
        }
        return false;
    }

    public void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 1000) {
            Toast.makeText(this, getString(R.string.tip_click_more_exit), Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

}