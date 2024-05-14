package com.zmonster.recyclebuy.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;


@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String userId;//用户ID
    private String userName;//用户名
    private String password;//密码
    private String points;//积分
    private String sex;//积分
    private String address;//住址
    private String phone;//联系方式
    private String cover;//头像
    private String description;//签名
    private String age;//签名

    @Generated(hash = 1634218614)
    public User(Long id, String userId, String userName, String password,
                String points, String sex, String address, String phone, String cover,
                String description, String age) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.points = points;
        this.sex = sex;
        this.address = address;
        this.phone = phone;
        this.cover = cover;
        this.description = description;
        this.age = age;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPoints() {
        if (points == null) {
            return "0";
        }
        return this.points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getSex() {
        if (sex == null || sex.isEmpty()) {
            return "男";
        }
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCover() {
        if (cover == null) {
            return "";
        }
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        if (description == null || description.isEmpty()) {
            description = "This guy is lazy and didn't introduce anything..";
        }
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }


}
