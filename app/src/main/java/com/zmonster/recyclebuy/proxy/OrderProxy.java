package com.zmonster.recyclebuy.proxy;

import android.database.sqlite.SQLiteException;

import com.zmonster.recyclebuy.RecycleBuyApplication;
import com.zmonster.recyclebuy.bean.Order;
import com.zmonster.recyclebuy.dao.DaoSession;
import com.zmonster.recyclebuy.dao.OrderDao;

import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author Monster_4y
 * orderAgent
 */
public class OrderProxy {
    private OrderProxy() {
    }

    private static class OrderProxyInstance {
        private static final OrderProxy INSTANCE = new OrderProxy();
    }

    public static OrderProxy getInstance() {
        return OrderProxy.OrderProxyInstance.INSTANCE;
    }


    public List<Order> findOrder(String userId) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<Order> qb = daoSession.queryBuilder(Order.class);
        QueryBuilder<Order> studentQueryBuilder = qb.where(OrderDao.Properties.UserId.eq(userId));
        try {

            List<Order> list = studentQueryBuilder.list();
            return list;
        } catch (
                SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
            return null;
        }
    }


    public void save(@NotNull Order order) {
        RecycleBuyApplication.getInstance().getDaoSession().getOrderDao().save(order);
    }


    public void insertData(Order order) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.insert(order);
    }


    public void updateData(Order order) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.update(order);
    }


    public void deleteData(Order order) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.delete(order);
    }
}
