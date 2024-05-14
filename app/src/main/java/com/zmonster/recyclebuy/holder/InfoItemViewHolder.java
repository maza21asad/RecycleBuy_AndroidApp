package com.zmonster.recyclebuy.holder;

import android.view.View;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.ContainerActivity;
import com.zmonster.recyclebuy.bean.Information;
import com.zmonster.recyclebuy.utils.SystemUtil;
import com.zmonster.recyclebuy.view.FontTextView;

import static com.zmonster.recyclebuy.activities.ContainerActivity.FRAGMENT_INFO_DETAIL;


public class InfoItemViewHolder extends BaseViewHolder<Information> {

    public Information item;
    public RoundedImageView infoCover;
    private FontTextView infoTitle;

    public InfoItemViewHolder(View itemView) {
        super(itemView);
        infoCover = itemView.findViewById(R.id.info_cover);
        infoTitle = itemView.findViewById(R.id.info_title);
        itemView.setOnClickListener(v -> ContainerActivity.go(mActivity, item.getId()+"", FRAGMENT_INFO_DETAIL));
    }

    @Override
    public void bindData(Information data) {
        this.item = data;
        infoTitle.setText(item.getTitle());
        if (item.getImageUrl() != null) {
            if (item.getImageUrl().contains(",")) {
                String[] split = item.getImageUrl().split(",");
                Picasso.get()
                        .load(split[0])
                        .resize(SystemUtil.getScreenWidth(mActivity) / 2, infoCover.getLayoutParams().height)
                        .centerInside()
                        .into(infoCover);
            } else {
                Picasso.get()
                        .load(item.getImageUrl())
                        .resize(SystemUtil.getScreenWidth(mActivity) / 2, infoCover.getLayoutParams().height)
                        .centerInside()
                        .into(infoCover);
            }
        } else {
            Picasso.get()
                    .load(R.mipmap.ic_launcher)
                    .resize(SystemUtil.getScreenWidth(mActivity) / 2, infoCover.getLayoutParams().height)
                    .centerInside()
                    .into(infoCover);
        }
    }

}
