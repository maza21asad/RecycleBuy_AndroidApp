package com.zmonster.recyclebuy;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.zmonster.recyclebuy.bean.Information;
import com.zmonster.recyclebuy.bean.Shop;
import com.zmonster.recyclebuy.dao.DaoMaster;
import com.zmonster.recyclebuy.dao.DaoSession;
import com.zmonster.recyclebuy.proxy.InformationProxy;
import com.zmonster.recyclebuy.proxy.ShopProxy;
import com.zmonster.recyclebuy.utils.FileUtil;



/**
 * @author Monster_4y
 */
public class RecycleBuyApplication extends Application {
    private static RecycleBuyApplication sInstance;
    private DaoSession mDaoSession;
    public CosXmlService cosXmlService;
    //Initial user points
    public static final String USER_POINTS = "500";

    public static RecycleBuyApplication getInstance() {
        return sInstance;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }


    @RequiresApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        FileUtil.SDCARD_DIR = sInstance.getFilesDir().getAbsolutePath();
        FileUtil.ROOT_DIR = "RecycleBuy";
        setDatabase();
        initTxCloud();
    }

    private void initTxCloud() {
        String secretId = "";
        String secretKey = "";
        QCloudCredentialProvider provider =
                new ShortTimeCredentialProvider(secretId, secretKey, 300);
        String region = "ap-chongqing";
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setRegion(region)
                .isHttps(true) //Use HTTPS request, default is HTTP request
                .builder();
        cosXmlService = new CosXmlService(this,
                serviceConfig, provider);
    }


    private void setDatabase() {
        //1.Create database
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(this, "recyclebuy", null);
        //2.Get read and write objects
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.disableWriteAheadLogging();
        //3.Get the manager class
        DaoMaster mDaoMaster = new DaoMaster(db);
        //4.Get table object
        mDaoSession = mDaoMaster.newSession();


    }
}
