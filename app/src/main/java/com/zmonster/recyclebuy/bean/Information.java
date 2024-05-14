package com.zmonster.recyclebuy.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
@Entity
public class Information {
    @Id(autoincrement = true)
    private Long id;
    private String userId;
    private String userName;
    private String imageUrl;
    private String videoUrl;
    private String createtime;
    private String title;

    public Information(String userId, String userName, String imageUrl, String videoUrl, String createtime, String title) {
        this.userId = userId;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.createtime = createtime;
        this.title = title;
    }

    @Generated(hash = 1045349276)
    public Information(Long id, String userId, String userName, String imageUrl,
            String videoUrl, String createtime, String title) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.createtime = createtime;
        this.title = title;
    }

    @Generated(hash = 1933283371)
    public Information() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
