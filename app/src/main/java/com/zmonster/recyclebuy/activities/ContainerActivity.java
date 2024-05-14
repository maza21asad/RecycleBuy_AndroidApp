package com.zmonster.recyclebuy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.base.DefaultActivity;
import com.zmonster.recyclebuy.fragment.InfoDetailFragment;
import com.zmonster.recyclebuy.fragment.SellerDetailFragment;
import com.zmonster.recyclebuy.fragment.ShopDetailFragment;


/**
 * @author Monster_4y
 */
public class ContainerActivity extends DefaultActivity {

    public final static int FRAGMENT_SELLER_DETAIL = 1;
    public final static int FRAGMENT_SHOP_DETAIL = 2;
    public final static int FRAGMENT_INFO_DETAIL = 3;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        Intent intent = getIntent();
        int flag = intent.getIntExtra("flag", FRAGMENT_SELLER_DETAIL);
        switch (flag) {
            case FRAGMENT_SELLER_DETAIL:
                setActionBarTitle(getString(R.string.detail_tip));
                String eventID = intent.getStringExtra("itemID");
                fragment = SellerDetailFragment.newInstance(eventID);
                break;
            case FRAGMENT_SHOP_DETAIL:
                setActionBarTitle(getString(R.string.shop_detail_tip));
                String itemID = intent.getStringExtra("itemID");
                fragment = ShopDetailFragment.newInstance(itemID);
                break;
            case FRAGMENT_INFO_DETAIL:
                setActionBarTitle(getString(R.string.info_detail_tip));
                String infoID = intent.getStringExtra("itemID");
                fragment = InfoDetailFragment.newInstance(infoID);
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .show(fragment)
                    .commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    public static void go(Context context, String itemID, int fragmentFlag) {
        Intent intent = new Intent(context, ContainerActivity.class);
        intent.putExtra("flag", fragmentFlag);
        intent.putExtra("itemID", itemID);
        context.startActivity(intent);
    }


}
