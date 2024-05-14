package com.zmonster.recyclebuy.view;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.BottomPopupView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.bean.Shop;
import com.zmonster.recyclebuy.callback.OnBottomClickListener;

public class CustomPopup extends BottomPopupView {
    private Shop item;
    private int type;
    private OnBottomClickListener clickListener;

    public CustomPopup(@NonNull Context context) {
        super(context);
    }

    // Return to the layout of the custom pop-up window
    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_shop_popup;
    }

    // Perform initialization operations, such as: findView, set click, or any business logic in your pop-up window
    @Override
    protected void onCreate() {
        super.onCreate();
        RoundedImageView cover = findViewById(R.id.shop_cover);
        FontTextView price = findViewById(R.id.current_price);
        FontTextView total = findViewById(R.id.totle_count);
        FontTextView subtractCount = findViewById(R.id.subtract_count);
        FontTextView selectCount = findViewById(R.id.select_count);
        FontTextView addCount = findViewById(R.id.add_count);
        MaterialButton btnAddOrBuy = findViewById(R.id.btn_add_or_buy);
        if (item != null) {
            String imageUrl = item.getImageUrl();
            if (imageUrl!= null){
                if (imageUrl.contains(",")) {
                    String[] urls = imageUrl.split(",");
                    Picasso.get()
                            .load(urls[0])
                            .resize(cover.getLayoutParams().width, cover.getLayoutParams().height)
                            .centerInside()
                            .into(cover);
                } else {
                    Picasso.get()
                            .load(imageUrl)
                            .resize(cover.getLayoutParams().width, cover.getLayoutParams().height)
                            .centerInside()
                            .into(cover);
                }
            } else {
                Picasso.get()
                        .load(R.mipmap.ic_launcher)
                        .resize(cover.getLayoutParams().width, cover.getLayoutParams().height)
                        .centerInside()
                        .into(cover);
            }

            if (item.getDiscount_price() == 0) {
                price.setText("" + item.getPrice());
            } else {
                price.setText("" + item.getDiscount_price());

            }
            subtractCount.setOnClickListener(v -> {
                String num = selectCount.getText().toString();
                int select = Integer.parseInt(num);
                if (select > 1) {
                    select = select - 1;
                    selectCount.setText("" + select);
                }

            });
            addCount.setOnClickListener(v -> {
                String num = selectCount.getText().toString();
                int select = Integer.parseInt(num);
                int itemTotal = item.getTotal();
                if (select < itemTotal) {
                    select = select + 1;
                    selectCount.setText("" + select);
                }

            });
            total.setText("in stock:" + item.getTotal());

            if (type == 0) {
                btnAddOrBuy.setText("add to the cart");
            } else {
                btnAddOrBuy.setText("Buy now");
            }
            btnAddOrBuy.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onClick(type, Integer.parseInt(selectCount.getText().toString()));
                }
            });
        }

    }

    // Set the maximum width, depending on needs,
    @Override
    protected int getMaxWidth() {
        return super.getMaxWidth();
    }

    // Set the maximum width, depending on needs,
    @Override
    protected int getMaxHeight() {
        return super.getMaxHeight();
    }

    // Set the maximum width, depending on needs,
    @Override
    protected PopupAnimator getPopupAnimator() {
        return super.getPopupAnimator();
    }

    /**
     * The width of the pop-up window, used to dynamically set the width of the current pop-up window, limited by getMaxWidth()
     *
     * @return
     */
    protected int getPopupWidth() {
        return 0;
    }

    /**
     * The width of the pop-up window, used to dynamically set the width of the current pop-up window, limited by getMaxWidth()
     *
     * @return
     */
    protected int getPopupHeight() {
        return 0;
    }

    public void setData(Shop mData) {
        this.item = mData;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setOnBottomClickListener(OnBottomClickListener onBottomClickListener) {
        this.clickListener = onBottomClickListener;
    }
}
