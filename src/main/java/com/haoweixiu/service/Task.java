package com.haoweixiu.service;

import java.util.Map;

public class Task {
    private int taskID;
    private Map taskParam;
    public static final int GET=1;
    public static final int MyOrder = 2;// 我的订单
    public static final int LOGIN = 3;// 登陆
    public static final int BAIDUPUSH = 71;//百度推送
    public static final int ORDERDETAIL = 4;// 已接订单详情页
    public static final int RECIVED = 5;// 未完成订单列表
    public static final int UNRECIVED_DETAIL = 6;// 未完成订单详情页//
    public static final int SEND_ORDER = 7;// 接受订单
    public static final int COMPLETE_ORDER = 8;// 已完成订单列表

    public static final int UPDATE_MARKS =10;// 修改员工备注
    public static final int COMPLETE_ORDER_DETAIL = 9;// 已完成订单详情列表
    public static final int ADD_ORDER = 11;// 添加订单
    public Task(int id, Map param) {
        this.taskID = id;
        this.taskParam = param;
    }

    public int getTaskID() {
        return taskID;
    }


}
