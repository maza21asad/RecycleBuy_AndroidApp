package com.zmonster.recyclebuy.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.zmonster.recyclebuy.R;
import com.zmonster.recyclebuy.bean.Information;
import com.zmonster.recyclebuy.bean.Shop;
import com.zmonster.recyclebuy.bean.User;
import com.zmonster.recyclebuy.proxy.InformationProxy;
import com.zmonster.recyclebuy.proxy.ShopProxy;
import com.zmonster.recyclebuy.proxy.UserProxy;


public class SplashActivity extends AppCompatActivity {

    private RelativeLayout splashView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        initData();
        initView();
        showSplash();
    }


    private void initData() {
        SharedPreferences sp = getSharedPreferences("one", Context.MODE_PRIVATE);
        String first = sp.getString("first", "");

        if (first.equals("")) {
            SharedPreferences sharedPreferences = getSharedPreferences("one", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("first", "RecycleBuy");
            editor.commit();
            //Initialization data
            Shop shop1 = new Shop("1", "admin",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/AirPods.jpg"
                    , "1638665015", " AirPods pro",
                    "AirPods Pro are Apple's wireless headphones with active noise cancellation. It’s Apple's third-generation AirPods, with a new in-ear design instead of the old EarPods look. In-ear is designed to support noise reduction to enhance the listening experience. This is the biggest difference between the previous two generations.",
                    1299, 0, 1, 0);
            Shop shop2 = new Shop("1", "admin",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/B&O.jpg"
                    , "1638665015", "B&O headset",
                    "B&O headphones was born in Denmark, a small Country in Europe. This country is world-famous for producing high-end audio-visual products, and many of the world's top audio brands come from Denmark.",
                    1999, 0, 1, 0);
            Shop shop3 = new Shop("1", "admin",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/backpack.jpg"
                    , "1638665015", "backpack",
                    "It's a lamb hair backpack that can hold books and a laptop. Of course, you can use it as an outing decoration.",
                    199, 0, 1, 0);
            Shop shop4 = new Shop("1", "admin",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/clothes.jpg"
                    , "1638665015", "clothes",
                    "This is a sweater.",
                    299, 0, 1, 0);
            Shop shop5 = new Shop("1", "admin",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/clothes2.jpg"
                    , "1638665015", "clothes",
                    "Sweaters of different colors can be selected.",
                    239, 0, 20, 0);
            Shop shop6 = new Shop("1", "admin",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/keyboard.jpg"
                    , "1638665015", "Cherry MX8.0 mechanical keyboard",
                    "White, with lighting function. Support download driver, in the driver to set up the typing light.\n" +
                            "Red axis: soft feel, low noise, quick response. Suitable for girls with small finger strength.",
                    1299, 0, 1, 0);
            Shop shop7 = new Shop("1", "admin",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/sneaker.jpg"
                    , "1638665015", "Sneaker",
                    "For people who love sports.",
                    999, 0, 3, 0);
            Shop shop8 = new Shop("1", "admin",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/switch.jpg",
                    "1638665015", " switch",
                    "The Nintendo Switch has a pair of detachable controllers called Joy-Con. Joy-con usually comes on either side of the console, and when you need to play a game on your TV, you can remove it and put it into a grip so that you can hold it like a normal gamepad. However, you can also choose to play the game in a split state without a grip.",
                    3200, 0, 2, 0);
            ShopProxy.getInstance().save(shop1);
            ShopProxy.getInstance().save(shop2);
            ShopProxy.getInstance().save(shop3);
            ShopProxy.getInstance().save(shop4);
            ShopProxy.getInstance().save(shop5);
            ShopProxy.getInstance().save(shop6);
            ShopProxy.getInstance().save(shop7);
            ShopProxy.getInstance().save(shop8);

            //video
            Information info = new Information("1", "admin",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/1.jpg",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/vedio.mp4",
                    "1638665015", "Reduce Reuse Recycle");
            Information info1 = new Information("1", "admin",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/2.jpg",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/vedio2.mp4",
                    "1638665015", "Garbage sorting nursery rhyme");
            Information info2 = new Information("1", "admin",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/3.jpg",
                    "https://monster-1251514014.cos.ap-chongqing.myqcloud.com/vedio3.mp4",
                    "1638665015", "Upcycle Shoes");
            InformationProxy.getInstance().save(info);
            InformationProxy.getInstance().save(info1);
            InformationProxy.getInstance().save(info2);
        } else {
            //Not the first time to open the software
        }
    }

    private void initView() {
        splashView = findViewById(R.id.splashView);
    }

    /**
     * Check if the user is already registered
     */
    private void showSplash() {
        SharedPreferences sp = getSharedPreferences("userids", Context.MODE_PRIVATE);
        User hostUser = UserProxy.getInstance().findUser(sp.getLong("id", 0));

        AlphaAnimation anim = new AlphaAnimation(1.0f, 1.0f);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(0);
        anim.setDuration(5000);
        anim.setFillAfter(true);
        splashView.setAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                nextStep(hostUser);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void nextStep(User user) {
        Intent intent;
        //if (user == null) {
            //intent = new Intent(this, LoginActivity.class);
        //} else {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        //}
        startActivity(intent);
        finish();

    }
}
