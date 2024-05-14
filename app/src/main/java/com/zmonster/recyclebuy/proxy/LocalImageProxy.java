package com.zmonster.recyclebuy.proxy;

import com.zmonster.recyclebuy.RecycleBuyApplication;
import com.zmonster.recyclebuy.bean.LocalImage;
import com.zmonster.recyclebuy.bean.User;
import com.zmonster.recyclebuy.dao.DaoSession;
import com.zmonster.recyclebuy.dao.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author Monster_4y
 * pictureAddressProxy
 */
public class LocalImageProxy {
    private LocalImageProxy() {
    }

    private static class LocalImageProxyInstance {
        private static final LocalImageProxy INSTANCE = new LocalImageProxy();
    }

    public static LocalImageProxy getInstance() {
        return LocalImageProxy.LocalImageProxyInstance.INSTANCE;
    }


    public List<User> checkNumber(String userId) {

        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<User> qb = daoSession.queryBuilder(User.class);
        QueryBuilder<User> studentQueryBuilder = qb.where(UserDao.Properties.Phone.eq(userId));
        List<User> userList = studentQueryBuilder.list();
        return userList;
    }


    public void insertData(LocalImage localImage) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.insert(localImage);
    }


    public void updateData(LocalImage localImage) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.update(localImage);
    }


    public void deleteData(LocalImage localImage) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.delete(localImage);
    }
}
