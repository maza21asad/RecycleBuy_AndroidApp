package com.zmonster.recyclebuy.proxy;

import android.database.sqlite.SQLiteException;

import com.zmonster.recyclebuy.RecycleBuyApplication;
import com.zmonster.recyclebuy.bean.User;
import com.zmonster.recyclebuy.dao.DaoSession;
import com.zmonster.recyclebuy.dao.UserDao;
import com.zmonster.recyclebuy.utils.Util;

import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @author Monster_4y
 */
public class UserProxy {
    private UserProxy() {
    }

    private static class UserProxyInstance {
        private static final UserProxy INSTANCE = new UserProxy();
    }

    public static UserProxy getInstance() {
        return UserProxyInstance.INSTANCE;
    }

    public void save(@NotNull User user) {
        RecycleBuyApplication.getInstance().getDaoSession().getUserDao().insertOrReplaceInTx(user);
    }

    public void clear() {
        RecycleBuyApplication.getInstance().getDaoSession().getUserDao().deleteAll();
    }

    /**
     * Check if the phone number is registered
     */
    public List<User> checkNumber(String userId) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<User> qb = daoSession.queryBuilder(User.class);
        QueryBuilder<User> studentQueryBuilder = qb.where(UserDao.Properties.Phone.eq(userId));
        try {
            //Find out the current corresponding data
            return studentQueryBuilder.list();
        } catch (SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
            return null;
        }
    }

    /**
     * Find users
     * @param userId
     * @return
     */
    public User findUser(Long userId) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        QueryBuilder<User> qb = daoSession.queryBuilder(User.class);
        QueryBuilder<User> studentQueryBuilder = qb.where(UserDao.Properties.Id.eq(userId));
        try {
            User user = studentQueryBuilder.unique();
            return user;
        } catch (SQLiteException e) {
            RecycleBuyApplication.getInstance().getDaoSession().clear();
            return null;
        }
    }

    public void insertData(User user) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.insert(user);
    }


    /**
     *
     * @param data newDATA
     * @throws Exception
     */
    public void updateData(User data) throws Exception {
        // Adjust as specified
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        User srcData = findUser(data.getId());
        //TODO  Refer to the updateDataBy method in the shopPoxy class
        // !!!!!!!careful!!!!!!
        Util util = new Util();
        util.reflect(srcData,data);
        daoSession.update(srcData);
    }

    public void updateDataInfo(User data) {
        // Adjust as specified
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        User srcData = findUser(data.getId());
        //TODO  Refer to the updateDataBy method in the shopPoxy class
        // !!!!!!!careful!!!!!!
        Util util = new Util();
        String cover = srcData.getCover();
        try {
            util.reflect(srcData,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (data.getCover() != null) srcData.setCover(cover);
        daoSession.update(srcData);
    }



    public void updatePassword(User data){
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        User srcData = findUser(data.getId());
        String cover = srcData.getCover();
        String description = srcData.getDescription();
        String sex = srcData.getSex();
        String points = srcData.getPoints();
        Util util = new Util();
        try {
            util.reflect(srcData,data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        srcData.setCover(cover);
        srcData.setDescription(description);
        srcData.setSex(sex);
        srcData.setPoints(points);
        daoSession.update(srcData);
    }
    public void deleteData(User user) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.delete(user);
    }
}
