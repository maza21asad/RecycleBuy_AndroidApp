package com.zmonster.recyclebuy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.RecycleBuyApplication;
import com.zmonster.recyclebuy.activities.base.DefaultActivity;
import com.zmonster.recyclebuy.bean.User;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.UserProxy;
import com.zmonster.recyclebuy.utils.Util;
import com.zmonster.recyclebuy.view.FontEditText;
import com.zmonster.recyclebuy.view.FontTextView;
import com.zmonster.recyclebuy.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import static com.zmonster.recyclebuy.activities.PickPhotoActivity.INTENT_PICK_PHOTO;


public class EditUserInfoActivity extends DefaultActivity {
    private FontEditText editUserName;
    private RoundedImageView useIcon;
    private String iconUrl = "";
    private LoadingDialog loading = null;

    private FontEditText editUserDescription;
    private FontEditText editUserPhone;
    private FontEditText editUserAddress;
    private User hostUser;
    private String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_userinfo);
        setActionBarTitle(getString(R.string.change_userInfo));
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initData() {
        SharedPreferences sp = getSharedPreferences("userids", Context.MODE_PRIVATE);
        Long userID = sp.getLong("id", 0);
        hostUser = UserProxy.getInstance().findUser(userID);
        editUserName.setText(hostUser.getUserName());
        editUserDescription.setText(hostUser.getDescription());
        editUserAddress.setText(hostUser.getAddress());
        editUserPhone.setText(hostUser.getPhone());
        if (!hostUser.getCover().isEmpty()) {
            Picasso.get()
                    .load(hostUser.getCover())
                    .placeholder(R.mipmap.default_user_icon)
                    .error(R.mipmap.default_user_icon)
                    .resize(useIcon.getLayoutParams().width, useIcon.getLayoutParams().height)
                    .centerCrop()
                    .into(useIcon);
        }
    }


    private void initView() {
        editUserName = findViewById(R.id.edit_user_name);
        useIcon = findViewById(R.id.user_icon);
        editUserDescription = findViewById(R.id.edit_user_description);
        RadioGroup rgpSexLayout = findViewById(R.id.rgp_sex_layout);
        editUserPhone = findViewById(R.id.edit_user_phone);
        editUserAddress = findViewById(R.id.edit_user_address);


        FontTextView update = findViewById(R.id.update_now);
        rgpSexLayout.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbtn_woman) {
                sex = getString(R.string.woman);
            } else {
                sex = getString(R.string.man);
            }
        });

        useIcon.setOnClickListener(v -> PickPhotoActivity.pickPhoto(EditUserInfoActivity.this, 1));
        update.setOnClickListener(v -> {
            if (editUserName.getText() == null) {
                Toast.makeText(EditUserInfoActivity.this, getString(R.string.username_is_empty), Toast.LENGTH_SHORT).show();
            } else if (editUserPhone.getText() == null) {
                Toast.makeText(EditUserInfoActivity.this, getString(R.string.phone_is_empty), Toast.LENGTH_SHORT).show();
            } else if (editUserAddress.getText() == null) {
                Toast.makeText(EditUserInfoActivity.this, getString(R.string.address_is_emoty), Toast.LENGTH_SHORT).show();
            } else {
                loading = new LoadingDialog(
                        EditUserInfoActivity.this,
                        R.style.LoadingDialogStyle
                );
                loading.setMessage(getString(R.string.update_ing));
                loading.setCanceledOnTouchOutside(false);
                loading.show();
                if (!iconUrl.isEmpty()) {
                    File file = new File(iconUrl);
                    if (file.exists()) {
                        TransferConfig transferConfig = new TransferConfig.Builder().build();
                        TransferManager transferManager = new TransferManager(RecycleBuyApplication.getInstance().cosXmlService,
                                transferConfig);
                        String bucket = "monster-1251514014"; //Bucket, format: BucketName-APPID
                        String cosPath = hostUser.getPhone() + System.currentTimeMillis() + "-cover";
                        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath,
                                file.getAbsolutePath(), null);
                        cosxmlUploadTask.setCosXmlProgressListener((complete, target) -> {
                        });
                        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                            @Override
                            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                                COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult =
                                        (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                                String accessUrl = cOSXMLUploadTaskResult.accessUrl;
                                updateProfile(accessUrl);
                            }

                            @Override
                            public void onFail(CosXmlRequest request,
                                               CosXmlClientException clientException,
                                               CosXmlServiceException serviceException) {
                                runOnUiThread(() -> {
                                    if (loading != null) {
                                        loading.dismiss();
                                    }
                                    Toast.makeText(EditUserInfoActivity.this, getString(R.string.tip_unknow_error), Toast.LENGTH_SHORT).show();
                                });
                            }
                        });

                    } else {
                        updateProfile(null);
                    }

                } else {
                    updateProfile(null);
                }

            }

        });
    }

    private void updateProfile(String fileUrl) {
        User user = new User();
        Util util = new Util();
        if (editUserName.getText() != null) {
            user.setUserName(editUserName.getText().toString().trim());
        }
        if (editUserAddress.getText() != null) {
            user.setAddress(editUserAddress.getText().toString().trim());
        }
        if (editUserPhone.getText() != null) {
            user.setPhone(editUserPhone.getText().toString().trim());
        }
        if (editUserDescription.getText() != null) {
            user.setDescription(editUserDescription.getText().toString().trim());
        }
        if (sex!=null) {
            user.setSex(sex);
        }
        if (fileUrl != null) {
            user.setCover(fileUrl);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("userids", Context.MODE_PRIVATE);
        Long id = sharedPreferences.getLong("id", 0);
        if (id != 0){
            if (loading != null) {
                loading.dismiss();
            }
            user.setId(id);
            UserProxy.getInstance().updateDataInfo(user);

            EventBus.getDefault().post(new RefreshTimeLineEvent());
            finish();
        } else {
            Toast.makeText(EditUserInfoActivity.this, getString(R.string.tip_unknow_error), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_PICK_PHOTO && intent != null) {
                String[] pathes = intent.getStringArrayExtra("photos");
                if (pathes.length > 0) {
                    String path = pathes[0];
                    iconUrl = path;
                    Picasso.get().load(new File(path))
                            .resize(useIcon.getLayoutParams().width, useIcon.getLayoutParams().height)
                            .centerCrop()
                            .into(useIcon);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}