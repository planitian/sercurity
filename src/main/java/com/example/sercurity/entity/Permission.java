package com.example.sercurity.entity;

import java.io.Serializable;

public class Permission implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 权限名字
     */
    private String permissionName;

    /**
     * 备注
     */
    private String remarks;

    private static final long serialVersionUID = 1L;

    public Permission(Integer id, String permissionName, String remarks) {
        this.id = id;
        this.permissionName = permissionName;
        this.remarks = remarks;
    }

    public Permission() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName == null ? null : permissionName.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", permissionName=").append(permissionName);
        sb.append(", remarks=").append(remarks);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}