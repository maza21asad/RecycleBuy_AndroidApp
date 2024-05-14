package com.zmonster.recyclebuy.holder;


import android.view.View;
import android.widget.TextView;

import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.bean.GreenPoints;

/**
 * @author Monster_4y
 */
public class GreenItemViewHolder extends BaseViewHolder<GreenPoints> {

    public GreenPoints item;
    private final TextView pointsDetailTitle;
    private final TextView createTime;
    private final TextView totalPrice;

    public GreenItemViewHolder(View itemView) {
        super(itemView);
        pointsDetailTitle = itemView.findViewById(R.id.points_detail_title);
        createTime = itemView.findViewById(R.id.create_time);
        totalPrice = itemView.findViewById(R.id.total_price);

    }

    @Override
    public void bindData(GreenPoints data) {
        this.item = data;
        pointsDetailTitle.setText(item.getGreenName());
        totalPrice.setText(item.getPoints());
        createTime.setText(item.getMessage());
    }
}
