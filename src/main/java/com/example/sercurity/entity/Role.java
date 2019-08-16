package com.example.sercurity.entity;

import java.io.Serializable;

public class Role implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 角色名字
     */
    private String roleName;

    private static final long serialVersionUID = 1L;

    public Role(Integer id, String remarks, String roleName) {
        this.id = id;
        this.remarks = remarks;
        this.roleName = roleName;
    }

    public Role() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", remarks=").append(remarks);
        sb.append(", roleName=").append(roleName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}