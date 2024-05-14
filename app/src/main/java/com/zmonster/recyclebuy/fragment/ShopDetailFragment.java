package com.zmonster.recyclebuy.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupPosition;
import com.makeramen.roundedimageview.RoundedImageView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.squareup.picasso.Picasso;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.BuyActivity;
import com.zmonster.recyclebuy.bean.Cart;
import com.zmonster.recyclebuy.bean.Favorite;
import com.zmonster.recyclebuy.bean.Shop;
import com.zmonster.recyclebuy.bean.UrlBean;
import com.zmonster.recyclebuy.bean.User;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.CartProxy;
import com.zmonster.recyclebuy.proxy.FavoriteProxy;
import com.zmonster.recyclebuy.proxy.ShopProxy;
import com.zmonster.recyclebuy.proxy.UserProxy;
import com.zmonster.recyclebuy.utils.SystemUtil;
import com.zmonster.recyclebuy.view.CustomPopup;
import com.zmonster.recyclebuy.view.FontTextView;
import com.zmonster.recyclebuy.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class ShopDetailFragment extends Fragment {

    private Activity mActivity;
    private String itemID;
    private FontTextView shopTitle;
    private FontTextView shopDescription;
    private FontTextView shopSales;
    private FontTextView shopCurrPrice;
    private FontTextView addCart;
    private FontTextView nowBuy;
    private FontTextView outPrice;
    private ViewPager shopCoverVpager;
    private Shop mData;
    private Indicator indicatorLayout;
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;
    private AppCompatImageView addLike;

    public ShopDetailFragment() {
    }

    public static ShopDetailFragment newInstance(String itemID) {
        ShopDetailFragment fragment = new ShopDetailFragment();
        fragment.itemID = itemID;
        Log.d("ShopDetailFragment:",itemID);
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
        View contentView = inflater.inflate(R.layout.fragment_shop_detail, container, false);
        EventBus.getDefault().register(this);
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

        SharedPreferences sp = mActivity.getSharedPreferences("userids", Context.MODE_PRIVATE);
        Long userID = sp.getLong("id", 0);
        List<Favorite> favorite = FavoriteProxy.getInstance().findFavorite(userID + "", itemID);

        loading.dismiss();
        if (favorite != null && favorite.size() > 0) {
            addLike.setColorFilter(Color.parseColor("#FFCE44"));
            addLike.setSelected(true);
        } else {
            addLike.setColorFilter(Color.parseColor("#E9E9E9"));
            addLike.setSelected(false);
        }

    }

    List<UrlBean> urls = new ArrayList<>();

    private void loadData(Shop data) {
        this.mData = data;
        if (mData == null || mData.getTotal() < 1) {
            nowBuy.setText(R.string.kucun_empty);
        }
        shopTitle.setText(data.getTitle());
        shopDescription.setText(data.getMessage());
        shopSales.setText("Sales:" + mData.getSales());
        if (mData.getDiscount_price() == 0) {
            shopCurrPrice.setText("" + mData.getPrice());
            outPrice.setVisibility(View.GONE);
        } else {
            shopCurrPrice.setText("Discount price：" + mData.getDiscount_price());
            outPrice.setText("Original price：" + mData.getPrice());
            outPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            outPrice.setVisibility(View.VISIBLE);
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

        }
        imagePageAdapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        shopTitle = (FontTextView) view.findViewById(R.id.shop_title);
        shopDescription = (FontTextView) view.findViewById(R.id.shop_description);
        shopSales = (FontTextView) view.findViewById(R.id.shop_sales);
        addCart = (FontTextView) view.findViewById(R.id.add_cart);
        nowBuy = (FontTextView) view.findViewById(R.id.now_buy);
        outPrice = (FontTextView) view.findViewById(R.id.out_price);
        shopCurrPrice = (FontTextView) view.findViewById(R.id.current_price);
        shopCoverVpager = (ViewPager) view.findViewById(R.id.shop_cover_vpager);
        addLike = (AppCompatImageView) view.findViewById(R.id.add_like);
        indicatorLayout = (Indicator) view.findViewById(R.id.indicator_layout);
        indicatorViewPager = new IndicatorViewPager(indicatorLayout, shopCoverVpager);
        inflate = LayoutInflater.from(mActivity);
        indicatorViewPager.setAdapter(imagePageAdapter);
        SharedPreferences sp = mActivity.getSharedPreferences("userids", Context.MODE_PRIVATE);
        Long userID = sp.getLong("id", 0);

        User hostUser = UserProxy.getInstance().findUser(userID);
        //Collection method
        addLike.setOnClickListener(v -> {
            if (!addLike.isSelected()) {
                Favorite favorite = new Favorite();
                favorite.setImageUrl(mData.getImageUrl());
                favorite.setMessage(mData.getMessage());
                favorite.setPrice(mData.getDiscount_price() == 0 ? "" + mData.getPrice() : "" + mData.getPrice());
                favorite.setShopId(mData.getId()+"");
                favorite.setTitle(mData.getTitle());
                favorite.setUserName(hostUser.getUserName());
                favorite.setUserId(userID + "");
                favorite.setVideoUrl(mData.getVideoUrl());
                FavoriteProxy.getInstance().insertData(favorite);
                mActivity.runOnUiThread(() -> {
                    addLike.setColorFilter(Color.parseColor("#FFCE44"));
                    addLike.setSelected(true);

                });

            } else {
                Favorite favorite = new Favorite();
                favorite.setUserId(userID + "");
                favorite.setShopId(itemID);
                //delete
                FavoriteProxy.getInstance().deleteData(userID,itemID);
                mActivity.runOnUiThread(() -> {
                    addLike.setColorFilter(Color.parseColor("#E9E9E9"));
                    addLike.setSelected(false);
                });
            }
        });


        addCart.setOnClickListener(v -> {
            if (mData == null || mData.getTotal() < 1) {
                Toast.makeText(mActivity, getString(R.string.kucun_no_more), Toast.LENGTH_SHORT).show();
                return;
            }
            CustomPopup customPopup = new CustomPopup(mActivity);
            customPopup.setData(mData);
            customPopup.setType(0);
            customPopup.setOnBottomClickListener((type, count) -> {
                if (type == 0) {
                    LoadingDialog loading = new LoadingDialog(
                            mActivity,
                            R.style.LoadingDialogStyle
                    );
                    loading.setMessage(getString(R.string.add_cart));
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();

                    User hostUser1 = UserProxy.getInstance().findUser(userID);
                    List<Cart> list = CartProxy.getInstance().findCart(userID + "", itemID);

                    if (list == null || list.size() == 0) {
                        Cart cart = new Cart();
                        cart.setCount("" + count);
                        cart.setImageUrl(mData.getImageUrl());
                        cart.setVideoUrl(mData.getVideoUrl());
                        cart.setTitle(mData.getTitle());
                        cart.setMessage(mData.getMessage());
                        cart.setPrice(mData.getDiscount_price() == 0 ? "" + mData.getPrice() : "" + mData.getDiscount_price());
                        cart.setShopId(mData.getId()+"");
                        cart.setUserId(userID + "");
                        cart.setUserName(hostUser1.getUserName());
                        CartProxy.getInstance().insertData(cart);
                        mActivity.runOnUiThread(() -> {
                            loading.dismiss();
                            customPopup.dismiss();
                            Toast.makeText(mActivity, getString(R.string.add_cart_success), Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new RefreshTimeLineEvent());

                        });
                    } else {
                        Cart cart = list.get(0);
                        int oldCount = Integer.parseInt(cart.getCount());
                        int newCount = oldCount + count;

                        cart.setCount(newCount+"");
                        CartProxy.getInstance().updateData(cart);
                        mActivity.runOnUiThread(() -> {
                            loading.dismiss();
                            customPopup.dismiss();
                            Toast.makeText(mActivity, getString(R.string.add_cart_success), Toast.LENGTH_SHORT).show();
                            EventBus.getDefault().post(new RefreshTimeLineEvent());

                        });
                    }

                }
            });
            new XPopup.Builder(mActivity)
                    .popupPosition(PopupPosition.Bottom)//right
                    .hasStatusBarShadow(true)
                    .asCustom(customPopup)
                    .show();
        });
        nowBuy.setOnClickListener(v -> {
            if (mData == null || mData.getTotal() < 1) {
                Toast.makeText(mActivity, getString(R.string.kucun_no_more), Toast.LENGTH_SHORT).show();
                return;
            }
            CustomPopup customPopup = new CustomPopup(mActivity);
            customPopup.setData(mData);
            customPopup.setType(1);
            customPopup.setOnBottomClickListener((type, count) -> {
                if (type == 1) {
                    customPopup.dismiss();
                    Intent intent = new Intent(mActivity, BuyActivity.class);
                    intent.putExtra("itemID", itemID);
                    intent.putExtra("count", count);
                    mActivity.startActivity(intent);
                }
            });
            new XPopup.Builder(mActivity)
                    .popupPosition(PopupPosition.Bottom)//right
                    .hasStatusBarShadow(true)
                    .asCustom(customPopup)
                    .show();
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RefreshTimeLineEvent(RefreshTimeLineEvent event) {
        refresh();
    }
}
