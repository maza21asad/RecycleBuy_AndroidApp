package com.zmonster.recyclebuy.holder;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.BuyActivity;
import com.zmonster.recyclebuy.activities.ContainerActivity;
import com.zmonster.recyclebuy.bean.Cart;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.CartProxy;
import com.zmonster.recyclebuy.view.FontTextView;

import org.greenrobot.eventbus.EventBus;

import static com.zmonster.recyclebuy.activities.ContainerActivity.FRAGMENT_SHOP_DETAIL;


public class CartItemViewHolder extends BaseViewHolder<Cart> {

    public Cart item;
    private TextView shopTitle;
    public RoundedImageView coverView;
    private TextView shopDescription;
    private TextView totleCount;
    private TextView priceInfo;
    private TextView selectCount;

    public CartItemViewHolder(View itemView) {
        super(itemView);
        coverView = itemView.findViewById(R.id.cover_view);
        shopTitle = itemView.findViewById(R.id.shop_title);
        shopDescription = itemView.findViewById(R.id.shop_description);
        totleCount = itemView.findViewById(R.id.totle_count);
        priceInfo = itemView.findViewById(R.id.price_info);
        FontTextView subtractCount = itemView.findViewById(R.id.subtract_count);
        selectCount = itemView.findViewById(R.id.select_count);
        FontTextView addCount = itemView.findViewById(R.id.add_count);
        FontTextView removeCart = itemView.findViewById(R.id.remove_cart);
        FontTextView nowBuy = itemView.findViewById(R.id.now_buy);
        itemView.setOnClickListener(v -> ContainerActivity.go(mActivity, item.getShopId(), FRAGMENT_SHOP_DETAIL));
        removeCart.setOnClickListener(v -> {
            Cart cart = new Cart();
            cart.setId(item.getId());
            cart.setShopId(item.getShopId());
            CartProxy.getInstance().deleteData(cart);
            mActivity.runOnUiThread(() -> {
                EventBus.getDefault().post(new RefreshTimeLineEvent());
            });
        });
        nowBuy.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, BuyActivity.class);
            intent.putExtra("itemID", item.getShopId());
            intent.putExtra("count", item.getCount());
            mActivity.startActivity(intent);
        });

        addCount.setOnClickListener(v -> {
            String s = selectCount.getText().toString();
            int count = Integer.parseInt(s);
            count++;
            Cart data = new Cart();
            data.setCount("" + count);
            CartProxy.getInstance().updateData(item,data);
            mActivity.runOnUiThread(() -> {
                EventBus.getDefault().post(new RefreshTimeLineEvent());

            });
        });
        subtractCount.setOnClickListener(v -> {
            String s = selectCount.getText().toString();
            int count = Integer.parseInt(s);
            count--;
            if (count >= 1) {
                Cart data = new Cart();
                data.setCount("" + count);
                CartProxy.getInstance().updateData(item,data);
                mActivity.runOnUiThread(() -> {
                    EventBus.getDefault().post(new RefreshTimeLineEvent());

                });
            }
        });
    }

    @Override
    public void bindData(Cart data) {
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
        priceInfo.setText(mActivity.getString(R.string.price) + item.getPrice());
        selectCount.setText(item.getCount());

    }

}
