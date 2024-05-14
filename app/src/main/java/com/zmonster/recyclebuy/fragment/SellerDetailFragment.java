package com.zmonster.recyclebuy.fragment;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.squareup.picasso.Picasso;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.bean.Shop;
import com.zmonster.recyclebuy.bean.UrlBean;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.ShopProxy;
import com.zmonster.recyclebuy.utils.SystemUtil;
import com.zmonster.recyclebuy.view.FontEditText;
import com.zmonster.recyclebuy.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class SellerDetailFragment extends Fragment {

    private Activity mActivity;
    private String itemID;
    private FontEditText shopTitle;
    private FontEditText shopDescription;
    private TextView shopSales;
    private MaterialButton updateShopInfo;
    private FontEditText shopKucun;
    private FontEditText shopOrigPrice;
    private FontEditText shopCurrPrice;
    private MaterialButton deleteShop;
    private ViewPager shopCoverVpager;
    private Shop mData;
    private Indicator indicatorLayout;
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;

    public SellerDetailFragment() {
    }

    public static SellerDetailFragment newInstance(String itemID) {
        SellerDetailFragment fragment = new SellerDetailFragment();
        fragment.itemID = itemID;
        return fragment;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_seller_detail, container, false);
        initView(contentView);
        refresh();
        return contentView;
    }

    LoadingDialog loading = null;

    private void refresh() {
        loading = new LoadingDialog(
                mActivity,
                R.style.LoadingDialogStyle
        );
        loading.setMessage(getString(R.string.loading));
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        List<Shop> shop = ShopProxy.getInstance().findShopByObjectId(itemID);
        mActivity.runOnUiThread(() -> {
            loading.dismiss();
            if (shop != null && shop.size() > 0) {
                loadData(shop.get(0));
            } else {
                Toast.makeText(mActivity, R.string.no_data, Toast.LENGTH_SHORT).show();
            }
        });

    }

    List<UrlBean> urls = new ArrayList<>();

    private void loadData(Shop data) {
        this.mData = data;
        shopTitle.setText(data.getTitle());
        shopDescription.setText(data.getMessage());
        shopSales.setText("" + mData.getSales());
        shopKucun.setText("" + mData.getTotal());
        shopOrigPrice.setText("" + mData.getPrice());
        if (mData.getDiscount_price() == 0) {
            shopCurrPrice.setText("" + mData.getPrice());
        } else {
            shopCurrPrice.setText("" + mData.getDiscount_price());
        }
        urls.clear();
        if (mData.getVideoUrl() != null) {
            UrlBean urlBean = new UrlBean();
            urlBean.setPath(mData.getVideoUrl());
            urlBean.setVideo(true);
            urls.add(urlBean);
        }
        if (mData.getImageUrl() != null) {
            if (mData.getImageUrl().contains(",")) {
                String[] split = mData.getImageUrl().split(",");
                for (String url : split) {
                    UrlBean urlBean = new UrlBean();
                    urlBean.setPath(url);
                    urlBean.setVideo(false);
                    urls.add(urlBean);
                }
            } else {
                UrlBean urlBean = new UrlBean();
                urlBean.setPath(mData.getImageUrl());
                urlBean.setVideo(false);
                urls.add(urlBean);
            }

        } else {
            UrlBean urlBean = new UrlBean();
            urlBean.setPath(mData.getImageUrl());
            urlBean.setVideo(false);
            urls.add(urlBean);
        }
        imagePageAdapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        shopTitle = (FontEditText) view.findViewById(R.id.shop_title);
        shopDescription = (FontEditText) view.findViewById(R.id.shop_description);
        shopSales = (TextView) view.findViewById(R.id.shop_sales);
        updateShopInfo = (MaterialButton) view.findViewById(R.id.update_shop_info);
        shopKucun = (FontEditText) view.findViewById(R.id.shop_kucun);
        shopOrigPrice = (FontEditText) view.findViewById(R.id.shop_orig_price);
        shopCurrPrice = (FontEditText) view.findViewById(R.id.shop_curr_price);
        deleteShop = (MaterialButton) view.findViewById(R.id.delete_shop);
        shopCoverVpager = (ViewPager) view.findViewById(R.id.shop_cover_vpager);
        indicatorLayout = (Indicator) view.findViewById(R.id.indicator_layout);
        indicatorViewPager = new IndicatorViewPager(indicatorLayout, shopCoverVpager);
        inflate = LayoutInflater.from(mActivity);
        indicatorViewPager.setAdapter(imagePageAdapter);
        deleteShop.setOnClickListener(v -> {
            loading = new LoadingDialog(
                    mActivity,
                    R.style.LoadingDialogStyle
            );
            loading.setMessage(getString(R.string.delete_shop));
            loading.setCanceledOnTouchOutside(false);
            loading.show();
            Shop shop = new Shop();
            shop.setId(Long.valueOf(itemID));
            ShopProxy.getInstance().deleteData(shop);
            mActivity.runOnUiThread(() -> {
                if (loading != null) {
                    loading.dismiss();
                }

                Toast.makeText(mActivity, getString(R.string.delete_shop_success), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new RefreshTimeLineEvent());
                mActivity.finish();

            });

        });
        updateShopInfo.setOnClickListener(v -> {
            if (shopTitle.getText() == null || shopTitle.getText().toString().trim().isEmpty()) {
                Toast.makeText(mActivity, getString(R.string.title_cannot_empty), Toast.LENGTH_SHORT).show();
            } else if (shopDescription.getText() == null || shopDescription.getText().toString().trim().isEmpty()) {
                Toast.makeText(mActivity, getString(R.string.message_cannot_empty), Toast.LENGTH_SHORT).show();
            } else if (shopOrigPrice.getText() == null || shopOrigPrice.getText().toString().trim().isEmpty()) {
                Toast.makeText(mActivity, getString(R.string.price_cannot_empty), Toast.LENGTH_SHORT).show();
            } else if (shopKucun.getText() == null || shopKucun.getText().toString().trim().isEmpty() || shopKucun.getText().toString().equals("0")) {
                Toast.makeText(mActivity, getString(R.string.kucun_cannot_empty), Toast.LENGTH_SHORT).show();
            } else {
                loading = new LoadingDialog(
                        mActivity,
                        R.style.LoadingDialogStyle
                );
                loading.setMessage(getString(R.string.update));
                loading.setCanceledOnTouchOutside(false);
                loading.show();
                Shop shop = new Shop();
                shop.setPrice(Integer.parseInt(shopOrigPrice.getText().toString()));
                if (shopCurrPrice.getText() != null) {
                    shop.setDiscount_price(Integer.parseInt(shopCurrPrice.getText().toString()));
                }
                shop.setTitle(shopTitle.getText().toString());
                shop.setMessage(shopDescription.getText().toString());
                shop.setTotal(Integer.parseInt(shopKucun.getText().toString()));
                shop.setId(Long.valueOf(itemID));
                ShopProxy.getInstance().updateData(shop);
                mActivity.runOnUiThread(() -> {
                    if (loading != null) {
                        loading.dismiss();
                    }
                    refresh();
                    Toast.makeText(mActivity, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new RefreshTimeLineEvent());

                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private IndicatorViewPager.IndicatorPagerAdapter imagePageAdapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {



        @Override
        public View getViewForTab(int position, View convertView,
                                  ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.indicator_view, container,
                        false);
            }
            return convertView;
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        /**
         * Get every interface
         */
        @Override
        public View getViewForPage(int position, View convertView,
                                   ViewGroup container) {
            if (convertView == null) {
                convertView = inflate.inflate(R.layout.cover_content_view, container,
                        false);
            }
            RoundedImageView coverView = convertView.findViewById(R.id.cover_view);
            JzvdStd videoView = convertView.findViewById(R.id.video_view);
            UrlBean urlBean = urls.get(position);
            if (urlBean.isVideo()) {
                videoView.setVisibility(View.VISIBLE);
                coverView.setVisibility(View.GONE);
                videoView.setUp(urlBean.getPath()
                        , getString(R.string.shop_video_title));
                Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(urlBean.getPath(), MediaStore.Images.Thumbnails.MICRO_KIND);
                videoView.posterImageView.setImageBitmap(bitmap);
                videoView.startVideoAfterPreloading();
                videoView.startVideo();
            } else {
                videoView.setVisibility(View.GONE);
                coverView.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(urlBean.getPath())
                        .resize(SystemUtil.getScreenWidth(mActivity), coverView.getLayoutParams().height)
                        .centerInside()
                        .into(coverView);
            }

            return convertView;
        }

        /**
         * Get the number of interfaces
         */
        @Override
        public int getCount() {
            return urls.size();
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

}
