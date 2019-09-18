package com.kushkumardhawan.inspections.Model;

import java.io.Serializable;

public class User implements Serializable {

    public String full_name;
    public String email;
    public String mobile;
    public String department;

    public User() {
    }

    public User(String full_name, String email, String mobile, String department, String id) {
        this.full_name = full_name;
        this.email = email;
        this.mobile = mobile;
        this.department = department;
        this.id = id;
    }

    public String id;




    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "full_name='" + full_name + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", department='" + department + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
