package com.haoweixiu.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;


/*
* 向fragment传递Bundle数据，需要序列化
* */
public class Fragment_Bundle implements Serializable {



    private ArrayList<Order> orderList;

    public ArrayList<Map> getCompleteMapList() {
        return CompleteMapList;
    }

    public void setCompleteMapList(ArrayList<Map> completeMapList) {
        CompleteMapList = completeMapList;
    }

    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    public ArrayList<Map> getAcceptMapList() {
        return AcceptMapList;
    }

    public void setAcceptMapList(ArrayList<Map> acceptMapList) {
        AcceptMapList = acceptMapList;
    }

    private ArrayList<Map> AcceptMapList;



    private ArrayList<Map> CompleteMapList;

}