package com.zmonster.recyclebuy.bean;


import org.greenrobot.greendao.annotation.Generated;


public class LocalImage {
    private Long id;
    private String path;

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    private boolean isAdd =false;

    @Generated(hash = 1381161949)
    public LocalImage(Long id, String path, boolean isAdd) {
        this.id = id;
        this.path = path;
        this.isAdd = isAdd;
    }

    @Generated(hash = 1967361340)
    public LocalImage() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean getIsAdd() {
        return this.isAdd;
    }

    public void setIsAdd(boolean isAdd) {
        this.isAdd = isAdd;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
