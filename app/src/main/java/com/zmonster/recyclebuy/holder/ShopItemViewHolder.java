package com.zmonster.recyclebuy.holder;

import android.graphics.Paint;
import android.view.View;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.ContainerActivity;
import com.zmonster.recyclebuy.bean.Shop;
import com.zmonster.recyclebuy.utils.SystemUtil;
import com.zmonster.recyclebuy.view.FontTextView;

import static com.zmonster.recyclebuy.activities.ContainerActivity.FRAGMENT_SHOP_DETAIL;


public class ShopItemViewHolder extends BaseViewHolder<Shop> {

    public Shop item;
    public RoundedImageView shopCover;
    private FontTextView shopTitle;
    private FontTextView currentPrice;
    private FontTextView sellerCount;
    private FontTextView outPrice;

    public ShopItemViewHolder(View itemView) {
        super(itemView);
        shopCover = itemView.findViewById(R.id.shop_cover);
        shopTitle = itemView.findViewById(R.id.shop_title);
        currentPrice = itemView.findViewById(R.id.current_price);
        sellerCount = itemView.findViewById(R.id.seller_count);
        outPrice = itemView.findViewById(R.id.out_price);
        itemView.setOnClickListener(v -> ContainerActivity.go(mActivity, item.getId()+"", FRAGMENT_SHOP_DETAIL));
    }

    @Override
    public void bindData(Shop data) {
        this.item = data;
        if (item.getDiscount_price() == 0) {
            currentPrice.setText("" + item.getPrice());
            outPrice.setVisibility(View.GONE);
        } else {
            currentPrice.setText("" + item.getDiscount_price());
            outPrice.setText("" + item.getPrice());
            outPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            outPrice.setVisibility(View.VISIBLE);
        }
        shopTitle.setText(item.getTitle());
        sellerCount.setText(mActivity.getString(R.string.sales_count_tip) + item.getSales());
        if (item.getImageUrl() != null) {
            if (item.getImageUrl().contains(",")) {
                String[] split = item.getImageUrl().split(",");
                Picasso.get()
                        .load(split[0])
                        .resize(SystemUtil.getScreenWidth(mActivity) / 2, shopCover.getLayoutParams().height)
                        .centerInside()
                        .into(shopCover);
            } else {
                Picasso.get()
                        .load(item.getImageUrl())
                        .resize(SystemUtil.getScreenWidth(mActivity) / 2, shopCover.getLayoutParams().height)
                        .centerInside()
                        .into(shopCover);
            }
        } else {
            Picasso.get()
                    .load(R.mipmap.ic_launcher)
                    .resize(SystemUtil.getScreenWidth(mActivity) / 2, shopCover.getLayoutParams().height)
                    .centerInside()
                    .into(shopCover);
        }
    }

}
