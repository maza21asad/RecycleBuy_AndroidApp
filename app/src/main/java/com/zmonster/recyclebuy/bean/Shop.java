package com.zmonster.recyclebuy.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Shop {
    @Id(autoincrement = true)
    private Long id;
    private String userId;
    private String userName;
    private String imageUrl;
    private String videoUrl;
    private String createtime;
    private String title;
    private String message;
    private int price;
    private int discount_price;
    private int total;
    private int sales;

    public Shop(String userId, String userName, String imageUrl, String createtime, String title, String message, int price, int discount_price, int total, int sales) {
        this.userId = userId;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.createtime = createtime;
        this.title = title;
        this.message = message;
        this.price = price;
        this.discount_price = discount_price;
        this.total = total;
        this.sales = sales;
    }

    @Generated(hash = 1631041086)
    public Shop(Long id, String userId, String userName, String imageUrl,
                String videoUrl, String createtime, String title, String message,
                int price, int discount_price, int total, int sales) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.createtime = createtime;
        this.title = title;
        this.message = message;
        this.price = price;
        this.discount_price = discount_price;
        this.total = total;
        this.sales = sales;
    }

    @Generated(hash = 633476670)
    public Shop() {
    }


    public int getDiscount_price() {
        if (discount_price == 0) {
            return 0;
        }
        return discount_price;
    }

    public void setDiscount_price(int discount_price) {
        this.discount_price = discount_price;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPrice() {
        if (price == 0) {
            return 0;
        }
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTotal() {
        if (total == 0) {
            return 0;
        }
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSales() {
        if (sales == 0) {
            return 0;
        }
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
