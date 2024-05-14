package com.zmonster.recyclebuy.proxy;

import android.database.sqlite.SQLiteException;

import com.zmonster.recyclebuy.RecycleBuyApplication;
import com.zmonster.recyclebuy.bean.Favorite;
import com.zmonster.recyclebuy.dao.DaoSession;
import com.zmonster.recyclebuy.dao.FavoriteDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author Monster_4y
 */
public class FavoriteProxy {
    private FavoriteProxy() {
    }

    private static class FavoriteProxyInstance {
        private static final FavoriteProxy INSTANCE = new FavoriteProxy();
    }

    public static FavoriteProxy getInstance() {
        return FavoriteProxy.FavoriteProxyInstance.INSTANCE;
    }

    public List<Favorite> findFavorite(String userId, String shopId) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<Favorite> qb = daoSession.queryBuilder(Favorite.class);
        QueryBuilder<Favorite> studentQueryBuilder = qb.where(FavoriteDao.Properties.UserId.eq(userId),
                FavoriteDao.Properties.ShopId.eq(shopId));
        try {
            List<Favorite> list = studentQueryBuilder.list();
            return list;
        } catch (SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
            return null;
        }
    }


    public List<Favorite> findFavorite(String userId) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<Favorite> qb = daoSession.queryBuilder(Favorite.class);
        QueryBuilder<Favorite> studentQueryBuilder = qb.where(FavoriteDao.Properties.UserId.eq(userId));
        try {

            List<Favorite> list = studentQueryBuilder.list();
            return list;
        } catch (
                SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
            return null;
        }
    }

    public void insertData(Favorite favorite) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.insert(favorite);
    }


    public void updateData(Favorite favorite) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.update(favorite);
    }

    public void deleteData(Favorite favorite) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.delete(favorite);
    }
    public void deleteData(Long  userID,String shopID) {
        List<Favorite> favorite = findFavorite(userID+"", shopID);
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.delete(favorite.get(0));
    }
}
