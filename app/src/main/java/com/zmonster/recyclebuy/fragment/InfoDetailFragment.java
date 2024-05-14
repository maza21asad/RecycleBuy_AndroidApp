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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.makeramen.roundedimageview.RoundedImageView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.squareup.picasso.Picasso;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.bean.Information;
import com.zmonster.recyclebuy.bean.UrlBean;
import com.zmonster.recyclebuy.proxy.InformationProxy;
import com.zmonster.recyclebuy.utils.SystemUtil;
import com.zmonster.recyclebuy.view.FontEditText;
import com.zmonster.recyclebuy.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class InfoDetailFragment extends Fragment {

    private Activity mActivity;
    private String itemID;
    private FontEditText infoTitle;

    private ViewPager infoCoverVpager;
    private Information mData;
    private Indicator indicatorLayout;
    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;

    public InfoDetailFragment() {
    }

    public static InfoDetailFragment newInstance(String itemID) {
        InfoDetailFragment fragment = new InfoDetailFragment();
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
        View contentView = inflater.inflate(R.layout.fragment_info_detail, container, false);
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

        List<Information> information = InformationProxy.getInstance().findById(itemID);
        mActivity.runOnUiThread(() -> {
            loading.dismiss();
            if (information != null && information.size() > 0) {
                loadData(information.get(0));
            } else {
                Toast.makeText(mActivity, R.string.no_data, Toast.LENGTH_SHORT).show();
            }
        });

    }

    List<UrlBean> urls = new ArrayList<>();

    private void loadData(Information data) {
        this.mData = data;
        infoTitle.setText(data.getTitle());
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
        infoTitle = (FontEditText) view.findViewById(R.id.info_title);
        infoCoverVpager = (ViewPager) view.findViewById(R.id.info_cover_vpager);
        indicatorLayout = (Indicator) view.findViewById(R.id.indicator_layout);
        indicatorViewPager = new IndicatorViewPager(indicatorLayout, infoCoverVpager);

        inflate = LayoutInflater.from(mActivity);
        indicatorViewPager.setAdapter(imagePageAdapter);

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
