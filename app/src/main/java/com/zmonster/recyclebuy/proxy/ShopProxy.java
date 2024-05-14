package com.zmonster.recyclebuy.proxy;

import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.zmonster.recyclebuy.RecycleBuyApplication;
import com.zmonster.recyclebuy.bean.Shop;
import com.zmonster.recyclebuy.dao.DaoSession;
import com.zmonster.recyclebuy.dao.ShopDao;
import com.zmonster.recyclebuy.utils.Util;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class ShopProxy {
    private ShopProxy() {
    }

    private static class ShopProxyInstance {
        private static final ShopProxy INSTANCE = new ShopProxy();
    }

    public static ShopProxy getInstance() {
        return ShopProxy.ShopProxyInstance.INSTANCE;
    }

    public List<Shop> getALL() {
        try {
            return RecycleBuyApplication.getInstance().getDaoSession().getShopDao().queryBuilder().list();
        } catch (SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
        }
        return null;
    }

    public List<Shop> findShop(String userId) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<Shop> qb = daoSession.queryBuilder(Shop.class);
        QueryBuilder<Shop> studentQueryBuilder = qb.where(ShopDao.Properties.UserId.eq(userId));
        try{
            List<Shop> list = studentQueryBuilder.list();
            return list;
        } catch (SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
            return null;
        }

    }

    public List<Shop> findShopByObjectId(String objectId) {
        Log.d("ShopProxy:" ,"findShopByObjectId:    "+ objectId );
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<Shop> qb = daoSession.queryBuilder(Shop.class);
        QueryBuilder<Shop> studentQueryBuilder = qb.where(ShopDao.Properties.Id.eq(objectId));
        try{
            List<Shop> list = studentQueryBuilder.list();
            return list;
        } catch (SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
            return null;
        }

    }
    public List<Shop> findShopById(Long objectId) {
        Log.d("ShopProxy:" ,"findShopByObjectId:    "+ objectId );
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<Shop> qb = daoSession.queryBuilder(Shop.class);
        QueryBuilder<Shop> studentQueryBuilder = qb.where(ShopDao.Properties.Id.eq(objectId));
        try{
            List<Shop> list = studentQueryBuilder.list();
            return list;
        } catch (SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
            return null;
        }

    }

    public void save(Shop shop) {
        RecycleBuyApplication.getInstance().getDaoSession().getShopDao().save(shop);
    }

    public void insertData(Shop shop) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.insert(shop);
    }

    public void updateData(Shop shop) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.update(shop);
    }

    /**
     *Purchase and update product inventory
     * @param data new DATA
     *
     *        Note that the default value needs to be adjusted manually
     */
    public void updateDataBy(Shop data) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        Shop shopById = findShopById(data.getId()).get(0);
        //TODO The price has a default value and may be replaced
        int price = shopById.getPrice();
        Util util = new Util();
        try {
            util.reflect(shopById,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        shopById.setPrice(price);
        daoSession.update(shopById);
    }

    public void updateData(Shop srcData, Shop data) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        Shop shopById = findShopById(data.getId()).get(0);
        int price = shopById.getPrice();
        int sales = shopById.getSales();
        Util util = new Util();
        try {
            util.reflect(srcData,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        shopById.setPrice(price);
        shopById.setSales(sales);
        daoSession.update(srcData);
    }

    public void deleteData(Shop shop) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.delete(shop);
    }
}
