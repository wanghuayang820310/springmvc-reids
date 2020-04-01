package com.why.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.util.Date;

/**
 * com.cht.integration.bean
 *
 * @author chenhaiting
 * @name User
 * @description
 * @date 2018-04-30 20:11
 * <p>
 * <p>
 * Copyright (c) 2018   版权所有
 */
@Data
public class User {

    private Integer userid;

    private String userName;

    private String password;

    public User() {
    }

    private Integer age;

    @NumberFormat(pattern = "###,###.##")
    private Double height;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date udate;

    public User(Integer userid, String userName, String password, Integer age, Double height, Date udate) {
        this.userid = userid;
        this.userName = userName;
        this.password = password;
        this.age = age;
        this.height = height;
        this.udate = udate;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Date getUdate() {
        return udate;
    }

    public void setUdate(Date udate) {
        this.udate = udate;
    }

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", date=" + udate +
                '}';
    }
}
