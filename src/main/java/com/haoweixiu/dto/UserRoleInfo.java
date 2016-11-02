package com.haoweixiu.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class UserRoleInfo implements Serializable {
    private String id;
    private String ac_code;
	private String stu_id;
    private String currentdate;
    private ArrayList<RoleItem> roleList;

    public String getCurrentdate() {
        return currentdate;
    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAc_code() {
        return ac_code;
    }

    public void setAc_code(String ac_code) {
        this.ac_code = ac_code;
    }

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public ArrayList<RoleItem> getRoleList() {
        return roleList;
    }

    public void setRoleList(ArrayList<RoleItem> roleList) {
        this.roleList = roleList;
    }
}