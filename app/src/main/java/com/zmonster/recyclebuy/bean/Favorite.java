package com.zmonster.recyclebuy.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Favorite {
    @Id(autoincrement = true)
    private Long id;
    private String userId;
    private String userName;
    private String imageUrl;
    private String videoUrl;
    private String title;
    private String message;
    private String shopId;
    private String price;

    @Generated(hash = 794102926)
    public Favorite(Long id, String userId, String userName, String imageUrl,
            String videoUrl, String title, String message, String shopId,
            String price) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.title = title;
        this.message = message;
        this.shopId = shopId;
        this.price = price;
    }

    @Generated(hash = 459811785)
    public Favorite() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
