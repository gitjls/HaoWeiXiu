package com.haoweixiu.util;


import com.haoweixiu.service.DataService;
import com.haoweixiu.service.Task;

import java.io.File;

/**
 * Created by zcc on 2015/12/3.
 */
public class UrlUtil {
    public String url;
    public Object result;
    private DataService dataService;

    public UrlUtil() {
        dataService = DataService.getInstance();
    }

    //请求地址
    public String getUrl(int taskId) {
        switch (taskId) {
            //登录
            case Task.LOGIN:
                url = Const.API + File.separator + Const.LOGIN;
//                url = Const.API2 + File.separator + Const.LOGIN;
                break;
            //未接订单列表
            case Task.MyOrder:
                url = Const.API + File.separator + Const.MyOrder;
                break;
            //已接订单详情
            case Task.ORDERDETAIL:
                url = Const.API + File.separator + Const.ECIVED_DETAIL;
                break;
            //已接订单列表
            case Task.RECIVED:
                url = Const.API + File.separator + Const.RECIVED;
                break;
            //未接订单详情
            case Task.UNRECIVED_DETAIL:
                url = Const.API + File.separator + Const.UNRECIVED_DETAIL;
                break;
            //接受订单
            case Task.SEND_ORDER:
                url = Const.API + File.separator + Const.SEND_ORDER;
                break;
            //已完成订单列表
            case Task.COMPLETE_ORDER:
                url = Const.API + File.separator + Const.COMPLETE_ORDER;
                break;
            //已完成订单详情
            case Task.COMPLETE_ORDER_DETAIL:
                url = Const.API + File.separator + Const.COMPLETE_ORDER_DETAIL;
                break;
            //未接订单修改备注
            case Task.UPDATE_MARKS:
                url = Const.API + File.separator + Const.UPDATE_MARKS;
                break;
            case Task.ADD_ORDER:
                url = Const.API + File.separator + Const.ADD_ORDER;
                break;
            default:
                break;
        }
        return url;
    }

    //Json解析后的结果
    public Object getJsonResult(int taskId, String volleyResult) {
        switch (taskId) {
            case Task.LOGIN://获取登录后的信息
                result = dataService.login(volleyResult);
                break;
            //接受订单
            case Task.SEND_ORDER:
                result = dataService.ReciveOrder(volleyResult);
                break;
            case Task.MyOrder://未接订单列表
                result = dataService.MyOrder(volleyResult);
                break;
            case Task.ORDERDETAIL://已接单详情
                result = dataService.OrderDetail(volleyResult);
                break;

            case Task.RECIVED://已接订单列表
                result = dataService.RecivedOrder(volleyResult);
                break;
            case Task.UNRECIVED_DETAIL://未接订单列表详情
                result = dataService.UnRecivedOrderDetal(volleyResult);
                break;

            case Task.COMPLETE_ORDER://已完成订单列表
                result = dataService.FinishOrder(volleyResult);
                break;
            case Task.COMPLETE_ORDER_DETAIL://已完成订单列表详情
                result = dataService.FinishOrderDetal(volleyResult);
                break;
            case Task.UPDATE_MARKS://修改员工备注
                result = dataService.Update_Marks(volleyResult);
                break;
            case Task.ADD_ORDER://添加订单
                result = dataService.Add_Orders(volleyResult);
                break;

            default:
                break;
        }
        return result;
    }
}
