package com.haoweixiu.dto;

import java.io.Serializable;

public class Order implements Serializable{
   private String Did ; //订单id号
    private String Dname; //客户姓名
    private String Phone;  //客户电话

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDid() {
        return Did;
    }

    public void setDid(String did) {
        Did = did;
    }

    public String getDname() {
        return Dname;
    }

    public void setDname(String dname) {
        Dname = dname;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    private String Time ; //时间字符串
}