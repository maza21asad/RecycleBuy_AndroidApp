package com.zmonster.recyclebuy.proxy;

import android.database.sqlite.SQLiteException;

import com.zmonster.recyclebuy.RecycleBuyApplication;
import com.zmonster.recyclebuy.bean.Information;
import com.zmonster.recyclebuy.bean.Shop;
import com.zmonster.recyclebuy.dao.DaoSession;
import com.zmonster.recyclebuy.dao.InformationDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class InformationProxy {
    private InformationProxy() {
    }

    private static class ShopProxyInstance {
        private static final InformationProxy INSTANCE = new InformationProxy();
    }

    public static InformationProxy getInstance() {
        return InformationProxy.ShopProxyInstance.INSTANCE;
    }

    public List<Information> getALL() {
        try {
            return RecycleBuyApplication.getInstance().getDaoSession().getInformationDao().queryBuilder().list();
        } catch (SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
        }
        return null;
    }

    public List<Information> findInfo(String userId) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<Information> qb = daoSession.queryBuilder(Information.class);
        QueryBuilder<Information> studentQueryBuilder = qb.where(InformationDao.Properties.UserId.eq(userId));
        try{
            List<Information> list = studentQueryBuilder.list();
            return list;
        } catch (SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
            return null;
        }

    }

    public List<Information> findById(String Id) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<Information> qb = daoSession.queryBuilder(Information.class);
        QueryBuilder<Information> studentQueryBuilder = qb.where(InformationDao.Properties.Id.eq(Id));
        try{
            List<Information> list = studentQueryBuilder.list();
            return list;
        } catch (SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
            return null;
        }

    }

    public void save(Information information) {
        RecycleBuyApplication.getInstance().getDaoSession().getInformationDao().save(information);
    }

    public void insertData(Information information) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.insert(information);
    }

    public void updateData(Information information) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.update(information);
    }


    public void deleteData(Shop shop) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.delete(shop);
    }
}
