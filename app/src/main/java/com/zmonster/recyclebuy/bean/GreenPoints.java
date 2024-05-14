package com.zmonster.recyclebuy.bean;

/**
 * @author Monster_4y
 */
public class GreenPoints {
    private Long id;
    private String greenName;
    private String userId;
    private String userName;
    private String message;
    private String points;

    public GreenPoints(String greenName, String userId, String userName, String message, String points) {
        this.greenName = greenName;
        this.userId = userId;
        this.userName = userName;
        this.message = message;
        this.points = points;
    }

    public String getGreenName() {
        return greenName;
    }

    public void setGreenName(String greenName) {
        this.greenName = greenName;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
