package com.haoweixiu.util;

import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

public class Const {
    /* 更新下载包安装路径 */
    public static final String savePath = Environment
            .getExternalStorageDirectory() + "/Haoguanjia/update/";

    /* 更新包名称 */
    public static final String apkName = "Haoguanjia.apk";

    /* 程序重启广播 */
    public static final String REBOOT_SERVICE_BROADCAST = "com.haoguanjia.rebootservice";

    /* 程序主服务 */
    public static final String MAIN_SERVICE = "com.haoguanjia.service.MainService";


    public static final String MD5String = "4fH1w90sPpIX4z";

    //加密后的值 7ef0f603ad5f7ec655e3ab4c01d2b2ac
    public static final String MD5Already = "7ef0f603ad5f7ec655e3ab4c01d2b2ac";


    public static Boolean ISLOGIN = false;
    public static String sid = "";
    //已买课程
    public static List<String> BUYED = new ArrayList<>();


    public static String SNAME = "";

    /*
    * 网络环境
    * */
    public static String API = "http://www.hweixiu.com";
    /*
      * 网络环境2
      * */
    public static String API2 = "http://hweixiu.liluchang.com";
    public static String LOGIN = "applogin/login";

    //未接单
    public static String MyOrder = "Apporders/open_orders";
    public static String UNRECIVED_DETAIL = "Apporders/id_order";
    //已接定单列表
    public static String ECIVED_DETAIL = "apporders/outid_order";
    public static String RECIVED = "apporders/outstanding_order";
    //接单提交过程页面
    //传keys  和和订单did    姓名sname
    public static String SEND_ORDER = "apporders/j_order";
    //已接表单修改过程页面
    /*
    传keys  和和订单did    价格price   marks
    验证成功返回1，错误返回0
    成功以后跳转到http://www.hweixiu.com/Apporders/outid_order*/
    //已完成订单列表
    public static String COMPLETE_ORDER = "apporders/completed_order";
    public static String COMPLETE_ORDER_DETAIL = "apporders/completed_order";
    //地图接口
    public static String MAP_LOCATE = "apporders/map_order";
    public static long STARTTIME;
    //修改备注
    public static String UPDATE_MARKS = "apporders/up_marks";
    //修改备注
    public static String ADD_ORDER = "apporders/tjddgc";
}
