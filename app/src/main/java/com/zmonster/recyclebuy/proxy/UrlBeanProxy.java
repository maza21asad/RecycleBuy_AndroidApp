package com.zmonster.recyclebuy.proxy;

import com.zmonster.recyclebuy.RecycleBuyApplication;
import com.zmonster.recyclebuy.bean.UrlBean;
import com.zmonster.recyclebuy.dao.DaoSession;

public class UrlBeanProxy {
    private UrlBeanProxy() {
    }

    private static class UrlBeanProxyInstance {
        private static final UrlBeanProxy INSTANCE = new UrlBeanProxy();
    }

    public static UrlBeanProxy getInstance() {
        return UrlBeanProxy.UrlBeanProxyInstance.INSTANCE;
    }


    public void insertData(UrlBean urlBean) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.insert(urlBean);
    }

    public void updateData(UrlBean urlBean) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.update(urlBean);
    }

    public void deleteData(UrlBean urlBean) {
        DaoSession daoSession = RecycleBuyApplication.getInstance().getDaoSession();
        daoSession.delete(urlBean);
    }
}
