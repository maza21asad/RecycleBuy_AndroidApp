package com.zmonster.recyclebuy.holder;

import android.view.View;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.PickPhotoActivity;
import com.zmonster.recyclebuy.bean.LocalImage;

import java.io.File;


public class LocalImageItemViewHolder extends BaseViewHolder<LocalImage> {

    public LocalImage item;
    public RoundedImageView coverView;


    public LocalImageItemViewHolder(View itemView) {
        super(itemView);
        coverView = itemView.findViewById(R.id.cover_item);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.isAdd()) {
                    PickPhotoActivity.pickPhoto(mActivity, 4);
                }
            }
        });
    }

    @Override
    public void bindData(LocalImage data) {
        this.item = data;
        if (item.isAdd()) {
            coverView.setImageResource(R.mipmap.local_resource_add);
        } else {
            Picasso.get()
                    .load(new File(data.getPath()))
                    .resize(coverView.getLayoutParams().width, coverView.getLayoutParams().height)
                    .centerInside()
                    .into(coverView);
        }


    }

}
