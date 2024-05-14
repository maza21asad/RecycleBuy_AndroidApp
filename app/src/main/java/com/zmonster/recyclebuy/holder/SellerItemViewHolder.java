package com.zmonster.recyclebuy.holder;

import android.view.View;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.ContainerActivity;
import com.zmonster.recyclebuy.bean.Shop;

import static com.zmonster.recyclebuy.activities.ContainerActivity.FRAGMENT_SELLER_DETAIL;


public class SellerItemViewHolder extends BaseViewHolder<Shop> {

    public Shop item;
    private TextView shopTitle;
    public RoundedImageView coverView;
    private TextView shopDescription;
    private TextView totleCount;
    private TextView priceInfo;
    private TextView currentPrice;

    public SellerItemViewHolder(View itemView) {
        super(itemView);
        coverView = itemView.findViewById(R.id.cover_view);
        shopTitle = itemView.findViewById(R.id.shop_title);
        shopDescription = itemView.findViewById(R.id.shop_description);
        totleCount = itemView.findViewById(R.id.totle_count);
        priceInfo = itemView.findViewById(R.id.price_info);
        currentPrice = itemView.findViewById(R.id.current_price);
        itemView.setOnClickListener(v -> ContainerActivity.go(mActivity, item.getId()+"", FRAGMENT_SELLER_DETAIL));
    }

    @Override
    public void bindData(Shop data) {
        this.item = data;
        String imgUrl;
        if (item.getImageUrl() != null){
            if (item.getImageUrl().contains(",")) {
                imgUrl = item.getImageUrl().split(",")[0];

            } else {
                imgUrl = item.getImageUrl();
            }
            Picasso.get()
                    .load(imgUrl)
                    .resize(coverView.getLayoutParams().width, coverView.getLayoutParams().height)
                    .centerInside()
                    .into(coverView);
        } else {
            Picasso.get()
                    .load(R.mipmap.ic_launcher)
                    .resize(coverView.getLayoutParams().width, coverView.getLayoutParams().height)
                    .centerInside()
                    .into(coverView);
        }

        shopTitle.setText(item.getTitle());
        shopDescription.setText(item.getMessage());
        priceInfo.setText(mActivity.getString(R.string.orig_price) + item.getPrice());
        if (item.getDiscount_price()==0) {
            currentPrice.setText(mActivity.getString(R.string.current_price) + item.getPrice());
        } else {
            currentPrice.setText(mActivity.getString(R.string.current_price) + item.getDiscount_price());
        }
        totleCount.setText(mActivity.getString(R.string.kucun) + item.getTotal());

    }

}
