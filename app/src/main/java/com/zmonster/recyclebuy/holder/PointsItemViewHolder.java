package com.zmonster.recyclebuy.holder;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.bean.NameValue;
import com.zmonster.recyclebuy.bean.Points;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.PointsProxy;
import com.zmonster.recyclebuy.utils.DateUtil;
import com.zmonster.recyclebuy.view.OptionListDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


/**
 * @author Monster_4y
 */
public class PointsItemViewHolder extends BaseViewHolder<Points> {

    public Points item;
    private final TextView pointsDetailTitle;
    private final TextView createTime;
    private final TextView totalPrice;

    public PointsItemViewHolder(View itemView) {
        super(itemView);
        pointsDetailTitle = itemView.findViewById(R.id.points_detail_title);
        createTime = itemView.findViewById(R.id.create_time);
        totalPrice = itemView.findViewById(R.id.total_price);
        itemView.setOnLongClickListener(v -> {
            if (item != null) {
                List<NameValue> options = NameValue.list();
                options.add(new NameValue(mActivity.getString(R.string.delete), 1));
                OptionListDialog.show((FragmentActivity) mActivity, options, id -> {
                    if (id == 1) {
                        Points points = new Points();
                        points.setId(item.getId());

                        PointsProxy.getInstance().deleteData(points);
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
    public void bindData(Points data) {
        this.item = data;
        pointsDetailTitle.setText(item.getMessage());
        totalPrice.setText(item.getPoints());
        long time = Long.parseLong(data.getCreateTime()) * 1000;
        String dateTimeAgo24 = DateUtil.getDateTimeAgo24(mActivity, time);
        createTime.setText(dateTimeAgo24);
    }

}
