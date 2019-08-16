package com.example.sercurity.entity;

import java.io.Serializable;

public class User implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 密码
     */
    private String password;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 名字
     */
    private String userName;

    private static final long serialVersionUID = 1L;

    public User(Integer id, String password, String remarks, String userName) {
        this.id = id;
        this.password = password;
        this.remarks = remarks;
        this.userName = userName;
    }

    public User() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", password=").append(password);
        sb.append(", remarks=").append(remarks);
        sb.append(", userName=").append(userName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}