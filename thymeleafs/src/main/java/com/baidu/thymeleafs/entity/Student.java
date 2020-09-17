package com.baidu.thymeleafs.entity;

import lombok.Data;

/**
 * @ClassName Student
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/14
 * @Version V1.09999999999999999
 **/
public class Student {
    String code;
    String pass;
    int age;
    String likeColor;

    public Student() {
        super();
    }

    public Student(String code, String pass, int age, String likeColor) {
        this.code = code;
        this.pass = pass;
        this.age = age;
        this.likeColor = likeColor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLikeColor() {
        return likeColor;
    }

    public void setLikeColor(String likeColor) {
        this.likeColor = likeColor;
    }
}