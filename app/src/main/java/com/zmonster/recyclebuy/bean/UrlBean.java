package com.zmonster.recyclebuy.bean;


import org.greenrobot.greendao.annotation.Generated;

public class UrlBean {
    private Long id;
    private String path;
    private boolean isVideo;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public boolean getIsVideo() {
        return this.isVideo;
    }

    public void setIsVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }


    @Generated(hash = 1655473628)
    public UrlBean(Long id, String path, boolean isVideo) {
        this.id = id;
        this.path = path;
        this.isVideo = isVideo;
    }

    @Generated(hash = 241588977)
    public UrlBean() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
