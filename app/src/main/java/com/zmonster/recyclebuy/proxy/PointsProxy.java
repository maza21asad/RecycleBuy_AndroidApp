package com.zmonster.recyclebuy.proxy;

import android.database.sqlite.SQLiteException;

import com.zmonster.recyclebuy.RecycleBuyApplication;
import com.zmonster.recyclebuy.bean.Points;
import com.zmonster.recyclebuy.dao.DaoSession;
import com.zmonster.recyclebuy.dao.PointsDao;

import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author Monster_4y
 * PointsAgency
 */
public class PointsProxy {
    private PointsProxy() {
    }

    private static class PointsProxyInstance {
        private static final PointsProxy INSTANCE = new PointsProxy();
    }

    public static PointsProxy getInstance() {
        return PointsProxy.PointsProxyInstance.INSTANCE;
    }


    public List<Points> findPoints(String userId) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<Points> qb = daoSession.queryBuilder(Points.class);
        QueryBuilder<Points> studentQueryBuilder = qb.where(PointsDao.Properties.UserId.eq(userId));
        try {
            List<Points> list = studentQueryBuilder.list();
            return list;
        } catch (
                SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
            return null;
        }
    }


    public void save(@NotNull Points points) {
        RecycleBuyApplication.getInstance().getDaoSession().getPointsDao().save(points);
    }


    public void insertData(Points points) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.insert(points);
    }


    public void updateData(Points points) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.update(points);
    }


    public void deleteData(Points points) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.delete(points);
    }
}
