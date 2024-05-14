package com.zmonster.recyclebuy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.activities.base.DefaultActivity;
import com.zmonster.recyclebuy.bean.Order;
import com.zmonster.recyclebuy.bean.Points;
import com.zmonster.recyclebuy.bean.Shop;
import com.zmonster.recyclebuy.bean.User;
import com.zmonster.recyclebuy.event.RefreshTimeLineEvent;
import com.zmonster.recyclebuy.proxy.OrderProxy;
import com.zmonster.recyclebuy.proxy.PointsProxy;
import com.zmonster.recyclebuy.proxy.ShopProxy;
import com.zmonster.recyclebuy.proxy.UserProxy;
import com.zmonster.recyclebuy.view.FontTextView;
import com.zmonster.recyclebuy.view.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public class BuyActivity extends DefaultActivity {
    private User hostUser;

    private FontTextView nowBuy;
    private FontTextView totalPrice;
    private FontTextView selectCount;
    private FontTextView currentPrice;
    private FontTextView userName;
    private FontTextView userPhone;
    private FontTextView userLocation;
    private FontTextView totalCount;
    private FontTextView shopTitle;
    private RoundedImageView shopCover;
    private String itemID;
    private int count;
    private int totalPay;
    private Shop item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        actionBar = getSupportActionBar();
        setActionBarTitle(getString(R.string.buy_info));
        Intent intent = getIntent();
        itemID = intent.getStringExtra("itemID");
        count = intent.getIntExtra("count", 1);
        init();
    }

    private void init() {
        initView();
        refresh();
    }

    private void initView() {
        nowBuy = findViewById(R.id.now_buy);
        totalPrice = findViewById(R.id.total_price);
        selectCount = findViewById(R.id.select_count);
        currentPrice = findViewById(R.id.current_price);
        shopCover = findViewById(R.id.shop_cover);
        userName = findViewById(R.id.user_name);
        userPhone = findViewById(R.id.user_phone);
        totalCount = findViewById(R.id.total_count);
        shopTitle = findViewById(R.id.shop_title);
        userLocation = findViewById(R.id.user_location);
        SharedPreferences sp = getSharedPreferences("userids", Context.MODE_PRIVATE);
        Long userID = sp.getLong("id", 0);
        nowBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int points = Integer.parseInt(hostUser.getPoints());
                int total = item.getTotal();
                if (total < count) {
                    Toast.makeText(BuyActivity.this, getString(R.string.buy_count_max), Toast.LENGTH_SHORT).show();
                } else if (points >= totalPay) {
                    //Set loading popup
                    LoadingDialog loading = new LoadingDialog(
                            BuyActivity.this,
                            R.style.LoadingDialogStyle
                    );
                    loading.setMessage(getString(R.string.pay_for));
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();

                    //Create new order
                    Order order = new Order();
                    order.setCount("" + count);
                    order.setImageUrl(item.getImageUrl());
                    order.setVideoUrl(item.getVideoUrl());
                    order.setTitle(item.getTitle());
                    order.setMessage(item.getMessage());
                    order.setPoints("" + totalPay);
                    order.setPrice(currentPrice.getText().toString());
                    order.setShopId(item.getId()+"");
                    order.setUserId(userID+"");
                    order.setUserName(hostUser.getUserName());
                    OrderProxy.getInstance().insertData(order);

                    //Modify user points
                    User user = new User();
                    int currentPoints = points - totalPay;
                    user.setPoints(currentPoints >= 0 ? "" + currentPoints : "0");
                    user.setId(userID);

                    try {
                        UserProxy.getInstance().updateData(user);

                        //Modify product inventory information
                        Shop shop = new Shop();
                        int currentCount = item.getTotal() - count;
                        int max = Math.max(currentCount, 0);
                        shop.setTotal(max);
                        int i = item.getSales() + 1;
                        //Inventory is a non-empty attribute of this object.
                        // Note that the "replace attribute: util.reflect()" method is called when updating.
                        // It may cause the value that needs to be saved to be replaced with the default value forcibly!
                        // !!!!!!!careful!!!!!!
                        shop.setSales(i);
                        shop.setId(item.getId());
                        ShopProxy.getInstance().updateDataBy(shop);

                        //Seller points calculation
                        User seller = UserProxy.getInstance().findUser(Long.valueOf(item.getUserId()));
                        if ( seller != null){
                            int sellerPoints = Integer.parseInt(seller.getPoints());
                            int sellerNewPoints = sellerPoints + totalPay;
                            User sellerUser = new User();
                            sellerUser.setPoints("" + sellerNewPoints);
                            UserProxy.getInstance().updateData(seller);

                            loading.dismiss();

                            //Update buyer points details
                            Points buyPoints = new Points();
                            long ctime = System.currentTimeMillis() / 1000;
                            buyPoints.setCreateTime("" + ctime);
                            buyPoints.setMessage(getString(R.string.buy_tip) + item.getTitle());
                            buyPoints.setPoints("-" + totalPay);
                            buyPoints.setUserId(userID+"");
                            buyPoints.setUserName(hostUser.getUserName());
                            PointsProxy.getInstance().insertData(buyPoints);

                            //Update seller points details
                            Points points1 = new Points();
                            points1.setCreateTime("" + ctime);
                            points1.setMessage(getString(R.string.seller_shop_tip) + item.getTitle());
                            points1.setPoints("+" + totalPay);
                            points1.setUserId(seller.getId()+"");
                            points1.setUserName(seller.getUserName());
                            PointsProxy.getInstance().insertData(points1);

                            hostUser.setPoints(currentPoints >= 0 ? "" + currentPoints : "0");
                            try {
                                UserProxy.getInstance().updateData(hostUser);
                                EventBus.getDefault().post(new RefreshTimeLineEvent());
                                Toast.makeText(BuyActivity.this, getString(R.string.pay_success), Toast.LENGTH_SHORT).show();
                                finish();
                            } catch (Exception e) {
                                Toast.makeText(BuyActivity.this, getString(R.string.pay_failed), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                        }else {
                            loading.dismiss();
                            Toast.makeText(BuyActivity.this, getString(R.string.pay_failed), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        loading.dismiss();
                        Toast.makeText(BuyActivity.this, getString(R.string.pay_failed), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(BuyActivity.this, getString(R.string.points_is_not_more), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void initData(Shop item) {
        this.item = item;
        userName.setText(hostUser.getUserName());
        userPhone.setText(hostUser.getPhone());
        userLocation.setText(hostUser.getAddress());
        shopTitle.setText(item.getTitle());
        String imageUrl = item.getImageUrl();
        if (imageUrl != null){
            if (imageUrl.contains(",")) {
                String[] urls = imageUrl.split(",");
                Picasso.get()
                        .load(urls[0])
                        .resize(shopCover.getLayoutParams().width, shopCover.getLayoutParams().height)
                        .centerInside()
                        .into(shopCover);
            } else {
                Picasso.get()
                        .load(imageUrl)
                        .resize(shopCover.getLayoutParams().width, shopCover.getLayoutParams().height)
                        .centerInside()
                        .into(shopCover);
            }
        } else {
            Picasso.get()
                    .load(R.mipmap.ic_launcher)
                    .resize(shopCover.getLayoutParams().width, shopCover.getLayoutParams().height)
                    .centerInside()
                    .into(shopCover);
        }
        if (item.getDiscount_price() == 0) {
            currentPrice.setText("" + item.getPrice());
            totalPay = count * item.getPrice();
        } else {
            int discount = item.getDiscount_price();
            totalPay = count * discount;
            currentPrice.setText("" + item.getDiscount_price());
        }
        totalPrice.setText("" + totalPay);
        totalCount.setText(getString(R.string.tip_kuncun) + item.getTotal());
        selectCount.setText("" + count);


    }

    private void refresh() {
        SharedPreferences sharedPreferences = getSharedPreferences("userids", Context.MODE_PRIVATE);
        Long id = sharedPreferences.getLong("id", 0);
        hostUser = UserProxy.getInstance().findUser(id);
        LoadingDialog loading = new LoadingDialog(
                this,
                R.style.LoadingDialogStyle
        );
        loading.setMessage(getString(R.string.loading));
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        List<Shop> shop = ShopProxy.getInstance().findShopByObjectId(itemID);
        runOnUiThread(() -> {
            loading.dismiss();
            if (shop != null && shop.size() > 0) {
                initData(shop.get(0));
            } else {
                Toast.makeText(BuyActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}