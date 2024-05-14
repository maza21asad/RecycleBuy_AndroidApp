package com.zmonster.recyclebuy.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Points {
    @Id(autoincrement = true)
    private Long id;
    private String userId;
    private String userName;
    private String message;
    private String points;
    private String createTime;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


    @Generated(hash = 356378991)
    public Points(Long id, String userId, String userName, String message,
            String points, String createTime) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.message = message;
        this.points = points;
        this.createTime = createTime;
    }

    @Generated(hash = 1607589943)
    public Points() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
