package com.haoweixiu.dto;

public class OrderDetail {
    private String custom_name; //订单id号
    private String custom_phonenumber; //客户姓名
    private String custom_info;  //客户电话
    private String Time; //时间字符串
    private String model; //订单id号
    private String address; //客户姓名
    private String programme;  //客户电话
    private String marks;  //备注
    private String cityid;  //城市ID

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    private String did;  //城市ID

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    private String cityname;  //客户电话

    public String getCustom_name() {
        return custom_name;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public void setCustom_name(String custom_name) {
        this.custom_name = custom_name;
    }

    public String getCustom_phonenumber() {
        return custom_phonenumber;
    }

    public void setCustom_phonenumber(String custom_phonenumber) {
        this.custom_phonenumber = custom_phonenumber;
    }

    public String getCustom_info() {
        return custom_info;
    }

    public void setCustom_info(String custom_info) {
        this.custom_info = custom_info;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    private String price; //时间字符串
}