/**
 * 数据解析
 */
package com.haoweixiu.service;


import android.util.Log;

import com.haoweixiu.dto.Order;
import com.haoweixiu.dto.OrderDetail;
import com.haoweixiu.dto.UserInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataService {
    public static final String TAG = "DataService";
    public static String success, zhiliaoid, tishengid;
    public static int zhenduanid;
    public static String msg;
    public static int which;//判断是知识还是应用   知识1，应用2
    //语文学科和慧医馆英语、演武场英语学科用新网络框架请求，使用本类的单例
    private static DataService dataService;

    private DataService() {

    }

    public static DataService getInstance() {
        if (dataService == null) {
            dataService = new DataService();
        }
        return dataService;
    }

    /*volley登录*/
    public UserInfo login(String info) {
        Log.i("json", "登录==" + info);
        UserInfo userInfo = new UserInfo();
        Log.i("=====", "yiguan_sendXiangXiresultlist:" + info);

        if (info != null) {
            try {
                JSONObject jo = new JSONObject(info);
                userInfo.setSname(jo.getString("sname"));
                userInfo.setSid(jo.getString("sid"));
                userInfo.setSphone(jo.getString("sphone"));
                userInfo.setSqq(jo.getString("sqq"));
                userInfo.setPassword(jo.getString("password"));
                userInfo.setCreated(jo.getString("created"));
                userInfo.setStatus(jo.getString("status"));
                Log.i(TAG, "Data service 获取成功");
            } catch (Exception e) {
                userInfo.setStatus("0");
                Log.i(TAG, "Exception");
                e.printStackTrace();
            }

        } else {
            userInfo.setStatus("0");
            Log.i(TAG, "返回值为空");
        }
        return userInfo;

    }

    /*volley未接订单*/
    public List<Order> MyOrder(String info) {
        Log.i("json", "未接订单" + info);
        List<Order> orderList = new ArrayList<>();
        Log.i("=====", "Order:" + info);

        if (info != null) {
            try {
                JSONArray joArray = new JSONArray(info);
                for (int i = 0; i < joArray.length(); i++) {
                    Order order = new Order();
                    JSONObject object = (JSONObject) joArray.opt(i);
                    order.setDid(object.getString("did"));
                    order.setDname(object.getString("dname"));
                    order.setPhone(object.getString("phone"));
                    order.setTime(object.getString("time"));
                    orderList.add(order);
                }
                Log.i(TAG, "Data service 获取成功");
            } catch (Exception e) {
                Log.i(TAG, "Exception");
                e.printStackTrace();
            }

        } else {

            Log.i(TAG, "返回值为空");
        }
        return orderList;

    }

    /*volley未接订单*/
    public String ReciveOrder(String info) {
        Log.i("json", "接受订单" + info);
        String status = "0";
        if (info != null) {
            try {
                JSONObject json = new JSONObject(info);
                status = json.getString("status");
                Log.i(TAG, "Data service 获取成功");
            } catch (Exception e) {
                Log.i(TAG, "Exception");
                e.printStackTrace();
            }

        } else {
            status="0";
            Log.i(TAG, "返回值为空");
        }
        return status;

    }

    /*volley已接订单详情*/
    public OrderDetail OrderDetail(String info) {
        OrderDetail orderDetail = new OrderDetail();
        Log.i("json", "已接订单详情" + info);
        if (info != null) {
            try {
                JSONObject object = new JSONObject(info);
                orderDetail.setAddress(object.getString("address"));
                orderDetail.setCustom_info(object.getString("info"));
                orderDetail.setCustom_name(object.getString("dname"));
                orderDetail.setCustom_phonenumber(object.getString("phone"));
                orderDetail.setModel(object.getString("model"));

                orderDetail.setPrice(object.getString("price"));
                orderDetail.setProgramme(object.getString("programme"));
                orderDetail.setTime(object.getString("time"));
                orderDetail.setMarks(object.getString("marks"));
                orderDetail.setDid(object.getString("did"));
                orderDetail.setCityid(object.getString("cityid"));
                orderDetail.setCityname(object.getString("cityname"));
                Log.i(TAG, "Data service 获取成功");

            } catch (Exception e) {
                Log.i(TAG, "Exception");
                e.printStackTrace();
            }

        } else {

            Log.i(TAG, "返回值为空");
        }
        return orderDetail;

    }

    /*volle已订单列表*/
    public List<Order> RecivedOrder(String info) {
        Log.i("json", "未完成订单" + info);
        List<Order> orderList = new ArrayList<>();
        Log.i("=====", "Order:" + info);

        if (info != null) {
            try {
                JSONArray joArray = new JSONArray(info);
                for (int i = 0; i < joArray.length(); i++) {
                    Order order = new Order();
                    JSONObject object = (JSONObject) joArray.opt(i);
                    order.setDid(object.getString("did"));
                    order.setDname(object.getString("dname"));
                    order.setPhone(object.getString("phone"));
                    order.setTime(object.getString("time"));
                    orderList.add(order);
                }
                Log.i(TAG, "Data service 获取成功");
            } catch (Exception e) {
                Log.i(TAG, "Exception");
                e.printStackTrace();
            }

        } else {

            Log.i(TAG, "返回值为空");
        }
        return orderList;

    }

    /*volley未接订单详情*/
    public OrderDetail UnRecivedOrderDetal(String info) {
        OrderDetail orderDetail = new OrderDetail();
        Log.i("json", "未接订单详情列表" + info);
        if (info != null) {
            try {
                JSONObject object = new JSONObject(info);
                orderDetail.setAddress(object.getString("address"));
                orderDetail.setCustom_info(object.getString("info"));
                orderDetail.setCustom_name(object.getString("dname"));
                orderDetail.setCustom_phonenumber(object.getString("phone"));
                orderDetail.setModel(object.getString("model"));
                orderDetail.setPrice(object.getString("price"));
                orderDetail.setProgramme(object.getString("programme"));
                orderDetail.setTime(object.getString("time"));
                orderDetail.setCityid(object.getString("cityid"));
                orderDetail.setCityname(object.getString("cityname"));
                orderDetail.setDid(object.getString("did"));
                Log.i(TAG, "Data service 获取成功");

            } catch (Exception e) {
                Log.i(TAG, "Exception");
                e.printStackTrace();
            }

        } else {

            Log.i(TAG, "返回值为空");
        }
        return orderDetail;

    }

    /*volley已完成订单*/
    public List<Order> FinishOrder(String info) {
        Log.i("json", "未完成订单" + info);
        List<Order> orderList = new ArrayList<>();
        Log.i("=====", "Order:" + info);

        if (info != null) {
            try {
                JSONArray joArray = new JSONArray(info);
                for (int i = 0; i < joArray.length(); i++) {
                    Order order = new Order();
                    JSONObject object = (JSONObject) joArray.opt(i);
                    order.setDid(object.getString("did"));
                    order.setDname(object.getString("dname"));
                    order.setPhone(object.getString("phone"));
                    order.setTime(object.getString("time"));
                    orderList.add(order);
                }
                Log.i(TAG, "Data service 获取成功");
            } catch (Exception e) {
                Log.i(TAG, "Exception");
                e.printStackTrace();
            }

        } else {

            Log.i(TAG, "返回值为空");
        }
        return orderList;

    }

    /*volley已完成订单详情*/
    public OrderDetail FinishOrderDetal(String info) {
        OrderDetail orderDetail = new OrderDetail();
        Log.i("json", "未完成订单详情列表" + info);
        if (info != null) {
            try {
                JSONObject object = new JSONObject(info);
                orderDetail.setAddress(object.getString("address"));
                orderDetail.setCustom_info(object.getString("info"));
                orderDetail.setCustom_name(object.getString("dname"));
                orderDetail.setCustom_phonenumber(object.getString("phone"));
                orderDetail.setModel(object.getString("model"));
                orderDetail.setPrice(object.getString("price"));
                orderDetail.setProgramme(object.getString("programme"));
                orderDetail.setTime(object.getString("time"));
                orderDetail.setCityname(object.getString("cityname"));
                Log.i(TAG, "Data service 获取成功");

            } catch (Exception e) {
                Log.i(TAG, "Exception");
                e.printStackTrace();
            }

        } else {

            Log.i(TAG, "返回值为空");
        }
        return orderDetail;

    }

    /*volley修改员工备注*/
    public String Update_Marks(String info) {
        Log.i("json", "未完成订单详情列表" + info);
        String status = "0";
        if (info != null) {
            try {
                JSONObject jsonObject = new JSONObject(info);
                status = jsonObject.getString("status");
                Log.i(TAG, "Data service 获取成功");

            } catch (Exception e) {
                Log.i(TAG, "Exception");
                e.printStackTrace();
            }

        } else {
            status = "0";
            Log.i(TAG, "返回值为空");
        }
        return status;

    }

    public String Add_Orders(String info) {
        Log.i("json", "未完成订单详情列表" + info);
        String did = "0";
        if (info != null) {
            try {
                JSONObject jsonObject = new JSONObject(info);
                did = jsonObject.getString("did");
                Log.i(TAG, "Data service 获取成功");

            } catch (Exception e) {
                Log.i(TAG, "Exception");
                e.printStackTrace();
            }

        } else {
            did = "0";
            Log.i(TAG, "返回值为空");
        }
        return did;

    }
    // 登录
//    public static UserInfo login(Task task) {
//        String info = HttpQuery.httpPostQuest(Const.HTML, Const.BaiDuPush,
//                task.getTaskParam());
//        UserInfo userInfo = new UserInfo();
//        Log.i("======", "登陆方法获取info：  " + info);
//
//        if (info != null) {
//            try {
//                JSONObject jo = new JSONObject(info);
//                String success = jo.getString("success");
//                String message = jo.getString("message");
//                if (success.equals("1")) {
//                    userInfo.setUser_id(jo.getString("user_id"));
//                    userInfo.setXueduan(jo.getString("xueduan"));
//                    userInfo.setDefaultIsSet(jo.getString("defaultIsSet"));
//                    userInfo.setType(jo.getString("type"));
//                    userInfo.setStudent_name(jo.getString("student_name"));
//                    userInfo.setStu_phone(jo.getString("student_phone"));
//                    userInfo.setQq(jo.getString("qq"));
//                    userInfo.setParent_phone(jo.getString("parent_phone"));
//
//                    //
////                    userInfo.setCity(jo.getString("city"));
////                    userInfo.setSchool_id(jo.getString("school_id"));
////                    userInfo.setSound_effect(jo.getString("sound_effect"));
////                    userInfo.setMusic(jo.getString("music"));
////                    userInfo.animation = jo.getString("animation");
////                    userInfo.type = jo.getString("type");
//
//
//                    Log.i(TAG, "Data service 获取成功");
//                } else if (success.equals("0")) {
//                    Log.i(TAG, "Data service 获取失败");
//                }
//
//                userInfo.success = success;
//                userInfo.message = message;
//            } catch (Exception e) {
//                Log.i(TAG, "Exception");
//                e.printStackTrace();
//            }
//
//        } else {
//            userInfo.success = "0";
//            userInfo.message = "网络异常，请检查网络";
//            Log.i(TAG, "返回值为空");
//        }
//        return userInfo;
//
//    }


    /**
     * baidu push
     *
     * @param task
     * @return
     */
//    public static BaiduPushDto baidupush(Task task) {
//        String info = HttpQuery.httpPostQuest(Const.HTML, Const.BaiDuPush,
//                task.getTaskParam());
//
//        System.out.println("百度push返回值：" + info);
//        BaiduPushDto Bdto = new BaiduPushDto();
//        Log.i("TAG", "获取版本号任务：" + info);
//        if (info != null) {
//            try {
//                JSONObject jo = new JSONObject(info);
//                String state = jo.getString("success");
//                if ("1".equals(state)) {
//                    // 取对象
//                    Bdto.setSuccess(state);
//                    Bdto.setMessage(jo.getString("message"));
//                    UserInfo user = new UserInfo();
//                    user.setUser_id(jo.getString("user_id"));
//                    user.setXueduan(jo.getString("xueduan"));
//                    user.setCity(jo.getString("city"));
//                    user.setSchool_id(jo.getString("school_id"));
//                    user.setSound_effect(jo.getString("sound_effect"));
//                    user.setMusic(jo.getString("music"));
//                    user.setAnimation(jo.getString("animation"));
//                    user.setType(jo.getString("type"));
//                    user.setDefaultIsSet(jo.getString("defaultIsSet"));
//
//
//                } else if ("0".equals(state)) {
//                    Bdto.setSuccess(state);
//                    Bdto.setMessage(jo.getString("message"));
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Bdto.setSuccess("0");
//                Bdto.setMessage("网络异常，请检查网络");
//            }
//        } else {
//            Bdto.setSuccess("0");
//            Bdto.setMessage("网络异常，请检查网络");
//        }
//        return Bdto;
//
//    }
//
//    // 登出
//    public static UserInfo xitong_userlogout_app(Task task) {
//        String info = HttpQuery.httpPostQuest(Const.HTML, Const.XITONG_USERLOGOUT_APP,
//                task.getTaskParam());
//        UserInfo userInfo = new UserInfo();
//        Log.i(TAG, "login 方法获取info" + info);
//
//        if (info != null) {
//            try {
//                JSONObject jo = new JSONObject(info);
//                String success = jo.getString("success");
//                String message = jo.getString("message");
//
//
//                userInfo.success = success;
//                userInfo.message = message;
//            } catch (Exception e) {
//                Log.i(TAG, "Exception");
//                e.printStackTrace();
//            }
//
//        } else {
//            userInfo.success = "0";
//            userInfo.message = "网络异常，请检查网络";
//            Log.i(TAG, "返回值为空");
//        }
//        return userInfo;
//
//    }
//
//    // 注册
//    public static UserInfo register(Task task) {
//        String info = HttpQuery.httpPostQuest(Const.HTML, Const.REGISTER,
//                task.getTaskParam());
//        UserInfo userInfo = new UserInfo();
//        Log.i(TAG, "register 方法获取info" + info);
//
//        if (info != null) {
//            try {
//                JSONObject jo = new JSONObject(info);
//                String success = jo.getString("success");
//                String message = jo.getString("message");
//                if (success.equals("1")) {
//
//                    userInfo.setUser_id(jo.getString("userID"));
//
//                    Log.i(TAG, "Data service 获取成功");
//                } else if (success.equals("0")) {
//
//                    Log.i(TAG, "Data service 获取失败");
//
//                }
//                userInfo.success = success;
//                userInfo.message = message;
//
//            } catch (Exception e) {
//                Log.i(TAG, "Exception");
//                e.printStackTrace();
//            }
//
//
//        } else {
//            userInfo.success = "0";
//            userInfo.message = "网络异常，请检查网络";
//            Log.i(TAG, "返回值为空");
//        }
//        return userInfo;
//
//    }
//
//    //更新应用
//    public static ReportDto xitong_updateapp(Task task) {
//        String info = HttpQuery.httpPostQuest(Const.HTML, Const.XITONG_UPDATEAPP,
//                task.getTaskParam());
//        ReportDto re = new ReportDto();
//        Log.i("======", "版本信息：" + info);
//        if (info != null) {
//            try {
//                JSONObject jo = new JSONObject(info);
//                String success = jo.getString("state");
//                //String message = jo.getString("message");
//                if (success.equals("1")) {
//
//                    re.setA_code(jo.getString("a"));
//                    re.setA_url(jo.getString("a_url"));
//                    re.setSoundon(jo.getString("soundon"));
//                    re.setPushon(jo.getString("pushon"));
//
//                    Log.i(TAG, "Data service 获取成功");
//                } else if (success.equals("0")) {
//
//                    Log.i(TAG, "Data service 获取失败");
//
//                }
//                re.success = success;
//
//
//            } catch (Exception e) {
//                Log.i(TAG, "Exception");
//                e.printStackTrace();
//            }
//
//
//        } else {
//            re.success = "0";
//            re.message = "网络异常，请检查网络";
//            Log.i(TAG, "返回值为空");
//        }
//        return re;
//
//    }
//
//    // 推送开关
//    public static PushOnDto xitong_pushon(Task task) {
//        String info = HttpQuery.httpPostQuest(Const.HTML, Const.XITONG_PUSHON,
//                task.getTaskParam());
//        PushOnDto re = new PushOnDto();
//        Log.i(TAG, "register 方法获取info" + info);
//
//        if (info != null) {
//            try {
//                JSONObject jo = new JSONObject(info);
//                String success = jo.getString("success");
//                String message = jo.getString("message");
//
//                re.success = success;
//                re.message = message;
//
//
//            } catch (Exception e) {
//                Log.i(TAG, "Exception");
//                e.printStackTrace();
//            }
//
//
//        } else {
//            re.success = "0";
//            re.message = "网络异常，请检查网络";
//            Log.i(TAG, "返回值为空");
//        }
//        return re;
//
//    }
//
//
//    // 发送mac地址
//    public static UserInfo users_checkmac(Task task) {
//        String info = HttpQuery.httpPostQuest(Const.HTML_SEND_ANWSER, Const.USERS_CHECKMAC,
//                task.getTaskParam());
//        UserInfo userInfo = null;
//        Log.i("map3_tag", "users_checkmac：" + info);
//        if (info != null) {
//            try {
//                userInfo = new UserInfo();
//                JSONObject jo = new JSONObject(info);
//                String type = jo.getString("type");
//                if (type.equals("0")) {
//                    userInfo.success = jo.getString("type");
//                    userInfo.message = jo.getString("msg");
//                } else if (type.equals("1")) {
//                    userInfo.success = jo.getString("type");
//                    if (jo.has("sID"))
//                        userInfo.setSid(jo.getString("sID"));
//                }
//
//
//            } catch (Exception e) {
//                Log.i(TAG, "Exception");
//                e.printStackTrace();
//            }
//
//
//        } else {
//
//            Log.i(TAG, "返回值为空");
//        }
//        return userInfo;
//
//    }

//    // 系统设置  重设密码
//    public static UserInfo xitongshezhi_modifypasswordinfo(Task task) {
//        String info = HttpQuery.httpPostQuest(Const.HTML, Const.XITONGSHEZHI_MODIFYPASSWORDINFO,
//                task.getTaskParam());
//        UserInfo dto = new UserInfo();
//        Log.i(TAG, "xitongshezhi_modifypasswordinfo" + info);
//        if (info != null) {
//            try {
//                JSONObject jo = new JSONObject(info);
//                dto.success = jo.getString("success");
//                dto.message = jo.getString("message");
//                Log.i(TAG, "Data service 获取成功");
//            } catch (Exception e) {
//                Log.i(TAG, "Exception");
//                e.printStackTrace();
//            }
//        } else {
//            dto.success = "0";
//            dto.message = "网络异常，请检查网络";
//            Log.i(TAG, "返回值为空");
//        }
//        return dto;
//
//    }
//


}