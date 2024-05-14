package com.zmonster.recyclebuy.holder;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.ContainerActivity;
import com.zmonster.recyclebuy.bean.NameValue;
import com.zmonster.recyclebuy.bean.Order;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.OrderProxy;
import com.zmonster.recyclebuy.view.OptionListDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.zmonster.recyclebuy.activities.ContainerActivity.FRAGMENT_SHOP_DETAIL;


public class OrderItemViewHolder extends BaseViewHolder<Order> {

    public Order item;
    private TextView shopTitle;
    public RoundedImageView coverView;
    private TextView shopDescription;
    private TextView totleCount;
    private TextView priceInfo;
    private TextView currentPrice;

    public OrderItemViewHolder(View itemView) {
        super(itemView);
        coverView = itemView.findViewById(R.id.cover_view);
        shopTitle = itemView.findViewById(R.id.shop_title);
        shopDescription = itemView.findViewById(R.id.shop_description);
        totleCount = itemView.findViewById(R.id.totle_count);
        priceInfo = itemView.findViewById(R.id.price_info);
        currentPrice = itemView.findViewById(R.id.current_price);
        itemView.setOnClickListener(v -> ContainerActivity.go(mActivity, item.getShopId(), FRAGMENT_SHOP_DETAIL));
        itemView.setOnLongClickListener(v -> {
            if (item != null) {
                List<NameValue> options = NameValue.list();
                options.add(new NameValue(mActivity.getString(R.string.delete), 1));
                OptionListDialog.show((FragmentActivity) mActivity, options, id -> {
                    if (id == 1) {
                        Order order = new Order();
                        order.setId(item.getId());
                        OrderProxy.getInstance().deleteData(order);
                        mActivity.runOnUiThread(() -> {
                                Toast.makeText(mActivity, mActivity.getString(R.string.del_success), Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new RefreshTimeLineEvent());

                        });
                    }
                });
                return true;
            }
            return false;
        });
    }

    @Override
    public void bindData(Order data) {
        this.item = data;
        String imgUrl;
        if (item.getImageUrl()!= null){
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
        priceInfo.setText(mActivity.getString(R.string.price_tip) + item.getPrice());
        totleCount.setText(mActivity.getString(R.string.buy_shop_count_tip) + item.getCount());
        currentPrice.setText(mActivity.getString(R.string.cast_points_tip) + item.getPoints());

    }

}
