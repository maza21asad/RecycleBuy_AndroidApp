package com.zmonster.recyclebuy.proxy;

import android.database.sqlite.SQLiteException;

import com.zmonster.recyclebuy.RecycleBuyApplication;
import com.zmonster.recyclebuy.bean.Cart;
import com.zmonster.recyclebuy.dao.CartDao;
import com.zmonster.recyclebuy.dao.DaoSession;
import com.zmonster.recyclebuy.utils.Util;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author Monster_4y
 * shoppingCartAgent：singletonMode
 */
public class CartProxy {
    private CartProxy() {
    }

    private static class CartProxyInstance {
        private static final CartProxy INSTANCE = new CartProxy();
    }

    public static CartProxy getInstance() {
        return CartProxy.CartProxyInstance.INSTANCE;
    }

    public List<Cart> findCart(String userId,String shopId) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<Cart> qb = daoSession.queryBuilder(Cart.class);
        QueryBuilder<Cart> studentQueryBuilder = qb.where(CartDao.Properties.UserId.eq(userId),CartDao.Properties.ShopId.eq(shopId));
        try{
            List<Cart> list = studentQueryBuilder.list(); //Find out the current corresponding data
            return list;
        } catch (SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
            return null;
        }
    }


    public List<Cart> findCart(String userId) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<Cart> qb = daoSession.queryBuilder(Cart.class);
        QueryBuilder<Cart> studentQueryBuilder = qb.where(CartDao.Properties.UserId.eq(userId));
        try{
            List<Cart> list = studentQueryBuilder.list(); //Find out the current corresponding data
            return list;
        } catch (SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
            return null;
        }
    }

    public void save(Cart cart){
        RecycleBuyApplication.getInstance().getDaoSession().getCartDao().save(cart);
    }

    public void insertData(Cart cart) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.insert(cart);
    }


    public void updateData(Cart srcData,Cart data) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        Util util = new Util();
        try {
            util.reflect(srcData,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        daoSession.update(srcData);
    }

    public void updateData(Cart data) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.update(data);
    }



    public void deleteData(Cart data) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.delete(data);
    }

}
