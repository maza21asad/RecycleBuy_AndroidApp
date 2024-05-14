package com.zmonster.recyclebuy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
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
import com.zmonster.recyclebuy.adapter.CoverSelectAdapter;
import com.zmonster.recyclebuy.bean.LocalImage;
import com.zmonster.recyclebuy.bean.Shop;
import com.zmonster.recyclebuy.bean.User;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.ShopProxy;
import com.zmonster.recyclebuy.proxy.UserProxy;
import com.zmonster.recyclebuy.view.FontEditText;
import com.zmonster.recyclebuy.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.zmonster.recyclebuy.activities.PickPhotoActivity.INTENT_PICK_PHOTO;
import static com.zmonster.recyclebuy.activities.PickVideoActivity.INTENT_PICK_VIDEO;


public class ShopCreateActivity extends DefaultActivity {
    private CoverSelectAdapter listAdapter;
    private String videoPath;
    private RoundedImageView shopVideoSelect;
    private LoadingDialog progress;
    private FontEditText messageTitle;
    private FontEditText messageContent;
    private FontEditText priceEdit;
    private FontEditText kucunEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_create);
        setActionBarTitle(getString(R.string.shop_post));
        init();
    }

    private void init() {
        initView();

    }

    private void initView() {
        messageTitle = findViewById(R.id.message_title);
        messageContent = findViewById(R.id.message_content);
        priceEdit = findViewById(R.id.price_edit);
        kucunEdit = findViewById(R.id.kucun_edit);
        RecyclerView listView = findViewById(R.id.shop_cover_layout);
        shopVideoSelect = findViewById(R.id.shop_video_select);
        AppCompatButton btnShopPost = findViewById(R.id.btn_shop_post);
        listAdapter = new CoverSelectAdapter(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        listView.setLayoutManager(mLayoutManager);
        listView.setAdapter(listAdapter);
        listAdapter.setDatas(null);
        btnShopPost.setOnClickListener(v -> {
            if (messageTitle.getText() == null || messageTitle.getText().toString().trim().isEmpty()) {
                Toast.makeText(ShopCreateActivity.this, getString(R.string.title_cannot_empty), Toast.LENGTH_SHORT).show();
            } else if (messageContent.getText() == null || messageContent.getText().toString().trim().isEmpty()) {
                Toast.makeText(ShopCreateActivity.this, getString(R.string.message_cannot_empty), Toast.LENGTH_SHORT).show();
            } else if (priceEdit.getText() == null || priceEdit.getText().toString().trim().isEmpty()) {
                Toast.makeText(ShopCreateActivity.this, getString(R.string.price_cannot_empty), Toast.LENGTH_SHORT).show();
            } else if (kucunEdit.getText() == null || kucunEdit.getText().toString().trim().isEmpty() || kucunEdit.getText().toString().equals("0")) {
                Toast.makeText(ShopCreateActivity.this, getString(R.string.kucun_cannot_empty), Toast.LENGTH_SHORT).show();
            } else {
                progress = new LoadingDialog(
                        this,
                        R.style.LoadingDialogStyle
                );
                progress.setMessage(getString(R.string.shop_post_ing));
                progress.setCanceledOnTouchOutside(false);
                progress.setCancelable(false);
                progress.show();
                SharedPreferences sp = getSharedPreferences("userids", Context.MODE_PRIVATE);
                Long userID = sp.getLong("id", 0);
                User hostUser = UserProxy.getInstance().findUser(userID);
                List<String> datas = listAdapter.getDatas();
                List<String> paths = new ArrayList<>();
                if (datas.size() > 0) {
                    for (String path : datas) {
                        File file = new File(path);
                        if (file.exists()) {
                            TransferConfig transferConfig = new TransferConfig.Builder().build();
                            TransferManager transferManager = new TransferManager(RecycleBuyApplication.getInstance().cosXmlService,
                                    transferConfig);
                            String bucket = "monster-1251514014"; //Bucket, format: BucketName-APPID
                            String cosPath = hostUser.getPhone() + System.currentTimeMillis() + "-cover"; //The position identifier of the object in the bucket, which is called the object key
                            COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath,
                                    file.getAbsolutePath(), null);
                            cosxmlUploadTask.setCosXmlProgressListener((complete, target) -> {
                            });
                            cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                                @Override
                                public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                                    COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult =
                                            (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                                    String coverUrl = cOSXMLUploadTaskResult.accessUrl;
                                    paths.add(coverUrl);
                                    if (paths.size() == datas.size()) {
                                        if (videoPath == null || videoPath.isEmpty()) {
                                            postShopItem(paths, null);
                                        } else {
                                            File videoFile = new File(videoPath);
                                            if (videoFile.exists()) {
                                                TransferConfig transferConfig = new TransferConfig.Builder().build();
                                                TransferManager transferManager = new TransferManager(RecycleBuyApplication.getInstance().cosXmlService,
                                                        transferConfig);
                                                String bucket = "monster-1251514014";
                                                String cosPath = hostUser.getPhone() + System.currentTimeMillis() + "-video";
                                                COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath,
                                                        videoFile.getAbsolutePath(), null);
                                                cosxmlUploadTask.setCosXmlProgressListener((complete, target) -> {
                                                });
                                                cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                                                    @Override
                                                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                                                        COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult =
                                                                (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                                                        String videoUrl = cOSXMLUploadTaskResult.accessUrl;
                                                        postShopItem(paths, videoUrl);
                                                    }

                                                    @Override
                                                    public void onFail(CosXmlRequest request,
                                                                       CosXmlClientException clientException,
                                                                       CosXmlServiceException serviceException) {
                                                        runOnUiThread(() -> {
                                                            progress.dismiss();
                                                            Toast.makeText(ShopCreateActivity.this, getString(R.string.file_upload_failed), Toast.LENGTH_SHORT).show();
                                                        });
                                                    }
                                                });

                                            } else {
                                                progress.dismiss();
                                                Toast.makeText(ShopCreateActivity.this, getString(R.string.file_upload_failed), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onFail(CosXmlRequest request,
                                                   CosXmlClientException clientException,
                                                   CosXmlServiceException serviceException) {
                                    runOnUiThread(() -> {
                                        progress.dismiss();
                                        Toast.makeText(ShopCreateActivity.this, getString(R.string.file_upload_failed), Toast.LENGTH_SHORT).show();
                                    });
                                }
                            });

                        } else {
                            progress.dismiss();
                            Toast.makeText(ShopCreateActivity.this, getString(R.string.file_upload_failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (videoPath != null && !videoPath.isEmpty()) {
                    File videoFile = new File(videoPath);
                    if (videoFile.exists()) {
                        TransferConfig transferConfig = new TransferConfig.Builder().build();
                        TransferManager transferManager = new TransferManager(RecycleBuyApplication.getInstance().cosXmlService,
                                transferConfig);
                        String bucket = "monster-1251514014";
                        String cosPath = hostUser.getPhone() + System.currentTimeMillis() + "-video";
                        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath,
                                videoFile.getAbsolutePath(), null);
                        cosxmlUploadTask.setCosXmlProgressListener((complete, target) -> {
                        });
                        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                            @Override
                            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                                COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult =
                                        (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                                String videoUrl = cOSXMLUploadTaskResult.accessUrl;
                                postShopItem(null, videoUrl);
                            }

                            @Override
                            public void onFail(CosXmlRequest request,
                                               CosXmlClientException clientException,
                                               CosXmlServiceException serviceException) {
                                runOnUiThread(() -> {
                                    progress.dismiss();
                                    Toast.makeText(ShopCreateActivity.this, getString(R.string.file_upload_failed), Toast.LENGTH_SHORT).show();
                                });
                            }
                        });

                    } else {
                        progress.dismiss();
                        Toast.makeText(ShopCreateActivity.this, getString(R.string.file_upload_failed), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    postShopItem(null, null);
                }
            }
        });
        shopVideoSelect.setOnClickListener(v -> {
            PickVideoActivity.pickVideo(ShopCreateActivity.this);
        });
    }

    private void postShopItem(List<String> coverUrls, String videoUrl) {

        long time = System.currentTimeMillis() / 1000;
        SharedPreferences sp = getSharedPreferences("userids", Context.MODE_PRIVATE);
        Long userID = sp.getLong("id", 0);
        User hostUser = UserProxy.getInstance().findUser(userID);
        Shop shop = new Shop();
        shop.setUserId(userID+"");
        shop.setCreatetime(time + "");
        shop.setDiscount_price(0);
        shop.setPrice(Integer.parseInt(priceEdit.getText().toString()));
        shop.setTotal(Integer.parseInt(kucunEdit.getText().toString()));
        shop.setMessage(messageContent.getText().toString());
        shop.setTitle(messageTitle.getText().toString());
        if (coverUrls != null && coverUrls.size() > 0) {
            String path = "";
            for (String cover : coverUrls) {
                if (path.isEmpty()) {
                    path = cover;
                } else {
                    path = path + "," + cover;
                }
            }
            shop.setImageUrl(path);
        } else {
            shop.setImageUrl("https://monster-1251514014.cos.ap-chongqing.myqcloud.com/def-cover");
        }
        if (videoUrl != null && !videoUrl.isEmpty()) {
            shop.setVideoUrl(videoUrl);
        }
        shop.setUserName(hostUser.getUserName());

        ShopProxy.getInstance().save(shop);

        runOnUiThread(() -> {
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
            EventBus.getDefault().post(new RefreshTimeLineEvent());
            Toast.makeText(ShopCreateActivity.this, getString(R.string.shop_post_success), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == INTENT_PICK_PHOTO && intent != null) {
                String[] pathes = intent.getStringArrayExtra("photos");
                if (pathes.length > 0) {
                    List<LocalImage> items = new ArrayList<>();
                    for (String path : pathes) {
                        LocalImage it = new LocalImage();
                        it.setPath(path);
                        it.setAdd(false);
                        items.add(it);
                    }
                    listAdapter.setDatas(items);
                }
            }
            if (requestCode == INTENT_PICK_VIDEO && intent != null) {
                String[] videos = intent.getStringArrayExtra("videos");
                if (videos.length > 0) {
                    videoPath = videos[0];
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MICRO_KIND);
                    shopVideoSelect.setImageBitmap(bitmap);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}