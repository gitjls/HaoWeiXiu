package com.haoweixiu.activity;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.haoweixiu.R;
import com.haoweixiu.dto.RoleItem;
import com.haoweixiu.dto.UserRoleInfo;
import com.haoweixiu.service.MainService;
import com.haoweixiu.service.Task;
import com.haoweixiu.util.ACache;
import com.haoweixiu.util.Const;
import com.haoweixiu.util.MyLruCache;
import com.haoweixiu.util.MyProgressDialog;
import com.haoweixiu.util.NetUtil;
import com.haoweixiu.util.QueryDateInterface;
import com.haoweixiu.util.ToastUtil;
import com.haoweixiu.util.UrlUtil;
import com.haoweixiu.util.Utils;
import com.haoweixiu.util.VolleySingleton;
import com.haoweixiu.view.MyDialog;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BaseActivity extends Activity implements QueryDateInterface {

    /**
     * header布局
     */

    private ImageView mimgTitle;//抬头标题的图片
    private Button mbtnBack;//左侧返回按钮
    private ImageButton mbtnRight;//右上角按钮
    public RelativeLayout mrlContent, mrlTitle;
    public Context mContext = BaseActivity.this;
    public SharedPreferences mSharedPreferences, defaultSharedPreferences;
    public SharedPreferences.Editor mSharedPreferencesEditor;
    public String userID;//用户id
    public MyProgressDialog progressDialog;//旋转框
    private Timer baseTimer = new Timer();
    public Integer baseStatus;
    public MyLruCache myLruCache;
    public static List<Activity> allActivity1 = new ArrayList<Activity>();//这个区别于MainService的allActivity ，包含了所有的。而main里不包含注册登陆等。便于校园版用户首次登陆后退出

    public static ACache mCache;
    public static StringRequest stringRequest;
    public static RequestQueue mQueue;
    public static Response.Listener<String> mlistener;
    public static Response.ErrorListener merrorListener;
    private UrlUtil urlUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置成全屏模式

        Boolean sRunning = isServiceRunning(this, Const.MAIN_SERVICE);
        if (!sRunning) {
            new Thread() {
                @Override
                public void run() {
                    // 开启服务
                    Log.d("***======***", "开启服务啦");
                    Intent mIntent = new Intent();
                    mIntent.setAction(Const.MAIN_SERVICE);//你定义的service的action
                    mIntent.setPackage(mContext.getPackageName());//这里你需要设置你应用的包名
                    mContext.startService(mIntent);
//                    startService(new Intent(Const.MAIN_SERVICE));
                }
            }.start();
        }


        MainService.addActivity(this);
        allActivity1.add(this);

        mSharedPreferences = this.getSharedPreferences("user_login_setting", MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        progressDialog = MyProgressDialog.createDialog(mContext);
        baseStatus = 0;
        myLruCache = MyLruCache.getInstance();

        mCache = ACache.get(this);
        mQueue = VolleySingleton.getVolleySingleton(mContext.getApplicationContext()).getRequestQueue();
        urlUtil = new UrlUtil();
    }


    private void startBaseTimer() {
        baseTimer = new Timer();

        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                try {

                    handleBaseProgress.sendEmptyMessage(0);

                    Log.d("请求-------", "MMMMMMMMMMMM----又请求一次");

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };
        baseTimer.schedule(mTimerTask, 0, 60 * 1000);
    }

    Handler handleBaseProgress = new Handler() {
        public void handleMessage(Message msg) {
            LoginStateAsyncTask loginStateAsyncTask = new LoginStateAsyncTask(baseStatus);
            loginStateAsyncTask.execute();
        }

        ;
    };

    //  设置要显示的布局方法
    public void BaseSetContentView(int layoutID) {
        //获得inflater
//        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        //把继承该BaseAcitivyt的layoutID放进来 显示
        View view = inflater.inflate(layoutID, null);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        //backIntent();
        if (view != null)
            mrlContent.addView(view, layoutParams);
    }

    //获取右上角按钮
    public ImageButton getMbtnRight() {
        return mbtnRight;
    }

    //获取右上角按钮
    public MyProgressDialog getProgressDialog() {
        return progressDialog;
    }

    //设置右上角图片
    public void setMbtnRightImg(int resID) {
        if (null != mbtnRight)
            mbtnRight.setBackgroundResource(resID);
    }

    //    隐藏上方的左侧按钮
    public void hideMbtnBack() {

        if (null != mbtnBack) {
            mbtnBack.setVisibility(View.INVISIBLE);
        }
    }

    public Button getMbtnBack() {
        return mbtnBack;
    }

    //设置标题图片
    public void setMimgTitle(int resID) {
        if (null != mimgTitle)
            mimgTitle.setBackgroundResource(resID);
    }

    //    隐藏上方的标题栏
    public void hideMrlTitle() {

        if (null != mrlTitle) {
            mrlTitle.setVisibility(View.GONE);
        }
    }

    //    显示上方的标题栏
    public void ShowMrlTitle() {

        if (null != mrlTitle) {
            mrlTitle.setVisibility(View.VISIBLE);
        }
    }

    public void showToast(String msg) {
        ToastUtil.showMessage(mContext, msg, Toast.LENGTH_SHORT);

    }

    public void cancleDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void refresh(Object... param) {
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        baseTimer.cancel();
        if (mQueue != null) mQueue.cancelAll(mContext);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Utils.showToast = false;
    }

    //访问网络
    public void getData(HashMap map, int TaskId)// 0:loading 1:其它
    {

        if (NetUtil.isNetworking(mContext)) {
            if (progressDialog != null && !progressDialog.isShowing()) {
                try {

                    progressDialog.show();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            Task task = new Task(TaskId, map);
            MainService.newTask(task);//

        } else {
            Log.i("MY", "网络异常");
            showToast(getResources().getString(R.string.NoInternet));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        userID = mSharedPreferences.getString("userID", "");
        Utils.showToast = true;
        baseStatus = 1;

        startBaseTimer();


    }


    public class LoginStateAsyncTask extends AsyncTask<Integer, Integer, String> {

        Integer stateNum;

        public LoginStateAsyncTask(Integer stateNum) {
            this.stateNum = stateNum;
        }

        //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
        @Override
        protected void onPreExecute() {

        }

        /**
         * 这里的Integer参数对应AsyncTask中的第一个参数
         * 这里的String返回值对应AsyncTask的第三个参数
         * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
         * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
         */
        @Override
        protected String doInBackground(Integer... params) {

            switch (baseStatus) {
                case 1: {
//                    Log.i("BaseActivity", "单一登录前：Const.sid:" + Const.sid);
//                    if (!Const.sid.equals("")) {
//                        //单一登陆
//                        HashMap<String, String> map = new HashMap<>();
//                        map.put("userID", userID);
//                        map.put("sID", Const.sid);
//                        map.put("count", "0");
//                        String androidId = Settings.Secure.getString(getContentResolver(),
//                                Settings.Secure.ANDROID_ID);
//                        map.put("model", androidId); //获取手机型号
//                        getData(map, Task.MESSAGES_UPDATENEWS);
//                        String info = HttpQuery.httpPostQuest(Const.HTML, Const.MESSAGES_UPDATENEWS,
//                                map);
//                        Log.i("map3_tag", "userid:" + userID + " sid:" + Const.sid + " model:" + androidId);
//                        Log.i("map3_tag", "renwuta_message_count" + info);
//                        return info;
                }
                break;

                case 2: {
//                    if (stateNum == 2) {
//                        HashMap<String, String> mapp = new HashMap<>();
//                        mapp.put("userID", userID);
//                        mapp.put("userid", mSharedPreferences.getString("baidu_userId", ""));
//                        getData(mapp, Task.XITONG_USERLOGOUT_APP);
//
//                        String info2 = HttpQuery.httpPostQuest(Const.HTML, Const.XITONG_USERLOGOUT_APP,
//                                mapp);
//                        Log.i("map3_tag", "renwuta_message_count" + info2);
//                        return info2;
//                    }
                }
                break;

                default:
                    break;
            }

            return "";
        }


        /**
         * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
         * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
         */
        @Override
        protected void onPostExecute(String result) {

            switch (baseStatus) {
               /* case 2: {
                    UserInfo uto1 = new UserInfo();
                    if (result != null && !result.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (success.equals("1") || success.equals("2")) {
                                //showToast(getResources().getString(R.string.loginout_successful));

                                if (baseTimer != null)
                                    baseTimer.cancel();
                                baseStatus = 0;
                                dodoExit();
                            } else {
                                showToast(getResources().getString(R.string.NoInternet));
                                if (baseTimer != null)
                                    baseTimer.cancel();
                                baseStatus = 0;
                                dodoExit();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;*/
            }


        }
    }

    public void inidialogBase() {

        if (baseTimer != null)
            baseTimer.cancel();

        final MyDialog my = new MyDialog(mContext);

        my.setTitle(getResources().getString(R.string.notice));

        my.setMessageSize(18f);
        // 点击屏幕 不消失
        my.setCancelable(false);
        my.setNegativeButtonGone();

        my.setMessage(getResources().getString(R.string.areyousurekicked));
        my.setPositiveButton(getResources().getString(R.string.comfirm), new Dialog.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                baseStatus = 2;
                startBaseTimer();
            }

        });
        try {
            my.show();
        } catch (Exception e) {
            Log.i("======", "dialog显示有异常");
        }

    }

    //任务说明
//    public void inidialog_renwu() {
//
//        final MyDialog my = new MyDialog(mContext);
//
//        my.setTitle(getResources().getString(R.string.notice));
//
//        my.setMessageSize(18f);
//        // 点击屏幕 不消失
//        my.setCancelable(false);
//        my.setNegativeButtonGone();
//
//        my.setMessage("任务说明："+Const.RENWU_RENWUSHUOMING);
//        my.setPositiveButton(getResources().getString(R.string.comfirm), new Dialog.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (my!=null && my.isShowing()) {
//                    my.dismiss();
//                }
//            }
//
//        });
//        try {
//            my.show();
//        } catch (Exception e) {
//            Log.i("======", "dialog显示有异常");
//        }
//
//    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void finish() {
        super.finish();
        MainService.removeActivity(this);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        Log.i("======", "activity被销毁");
        // TODO Auto-generated method stub
        try {

            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();


        } catch (Exception e) {
            System.out.println("myDialog取消，失败！");
        }
        super.onDestroy();
    }

    /*
    * 单击退出,清除保存用户文件的内容,清除所有栈区Activity
    * */
    public void dodoExit() {
        mSharedPreferencesEditor.putString("password", "");
        Const.sid = "";
        mSharedPreferencesEditor.putString("userID", "");
        mSharedPreferencesEditor.commit();

        SharedPreferences haoguanjia_choose = getSharedPreferences("haoguanjia_choose", MODE_PRIVATE);
        SharedPreferences.Editor huiyiguan_choose_editor = haoguanjia_choose.edit();
        huiyiguan_choose_editor.clear();
        huiyiguan_choose_editor.commit();

        Intent intent = new Intent(mContext, LoginActivity.class);
        startActivity(intent);
        for (int i = 0; i < MainService.allActivity.size(); i++) {
            MainService.allActivity.get(i).finish();
        }
    }

    //////////////////////////////////////////////////////////////////////////////////
    public String verificationUseful(int index) {
        //0:不可用 1：可用 2：限制次数 3：限制时间 10000异常
        //0:国子监 1:封神榜 2:档案馆 3:慧医馆 4:练功房 5:状元楼 6:演武场 7:提分王任务 8:任务塔

        String useful = "10000";
        UserRoleInfo userRoleInfo = readUserRoleData();
        if (userRoleInfo != null) {
            RoleItem roleItem = userRoleInfo.getRoleList().get(index);
            String f_use = roleItem.getF_use();

            if (!f_use.equals("") && f_use != null) {
                useful = f_use;
            }
        }

        return useful;
    }

    public String verificationUsefulCount(int index) {
        //验证可用次数
        //0:国子监 1:封神榜 2:档案馆 3:慧医馆 4:练功房 5:状元楼 6:演武场 7:提分王任务 8:任务塔 index

        String usefulCount = "0";
        UserRoleInfo userRoleInfo = readUserRoleData();
        if (userRoleInfo != null) {
            RoleItem roleItem = userRoleInfo.getRoleList().get(index);
            String f_cnt = roleItem.getF_cnt();
            if (!f_cnt.equals("") && f_cnt != null) {
                usefulCount = f_cnt;
            }
        }

        return usefulCount;
    }

    public Boolean verificationUsefulTime(int index) {
        //验证可用时间
        //0:国子监 1:封神榜 2:档案馆 3:慧医馆 4:练功房 5:状元楼 6:演武场 7:提分王任务 8:任务塔

        Boolean usefulTime = false;
        UserRoleInfo userRoleInfo = readUserRoleData();
        if (userRoleInfo != null) {
            RoleItem roleItem = userRoleInfo.getRoleList().get(index);
            String f_date_from = roleItem.getF_date_from();
            String f_date_to = roleItem.getF_date_to();
            String f_date_current = userRoleInfo.getCurrentdate();

            if (!f_date_from.equals("") && f_date_from != null && !"null".equals(f_date_from) && !f_date_to.equals("") && f_date_to != null && !"null".equals(f_date_to)) {
                //设定时间的模板
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //得到指定模范的时间
                Date d1 = null;
                try {
                    d1 = sdf.parse(f_date_from);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date d2 = null;
                try {
                    d2 = sdf.parse(f_date_to);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date curDate = null;
                try {
                    curDate = sdf.parse(f_date_current);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //比较
                if (Math.abs(((curDate.getTime() - d1.getTime()) / (24 * 3600 * 1000))) > 0 && Math.abs(((d2.getTime() - curDate.getTime()) / (24 * 3600 * 1000))) > 0) {
                    usefulTime = true;
                } else {
                    usefulTime = false;
                }
            }
        }

        return usefulTime;
    }


    public UserRoleInfo readUserRoleData() {

        SharedPreferences preferences = getSharedPreferences("base64",
                MODE_PRIVATE);
        String userroleBase64 = preferences.getString("USER_ROLE_STR", "");
//        String userroleBase64 = mSharedPreferences.getString(USER_ROLE_STR, "");
        if (userroleBase64 == "") {
            return null;
        }

        UserRoleInfo userRoleInfo = null;

        //读取字节

        byte[] base64 = Base64.decodeBase64(userroleBase64.getBytes());

        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                userRoleInfo = (UserRoleInfo) bis.readObject();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return userRoleInfo;
    }

    public void saveUserRoleData(UserRoleInfo aUserRoleInfo) {

        SharedPreferences preferences = getSharedPreferences("base64",
                MODE_PRIVATE);

        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            //创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            //将对象写入字节流
            oos.writeObject(aUserRoleInfo);

            //将字节流编码成base64的字符窜
            String userroleBase64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("USER_ROLE_STR", userroleBase64);

            editor.commit();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.i("ok", "存储成功");
    }

    Activity activity;

    public void alertDialog(String message) {
        try {
            activity = this;
            if (!Utils.isTopActivy(this)) {
                activity = MainService.getActivityByName(Utils.getTopActivity(this));
            }
            final MyDialog my = new MyDialog(activity);
            final MyDialog alertDialog = new MyDialog(activity);
            alertDialog.setTitle(getResources().getString(R.string.notice));

            alertDialog.setMessageSize(18f);
            // 点击屏幕 不消失
            alertDialog.setCancelable(false);
            alertDialog.setNegativeButtonGone();

            alertDialog.setMessage(message);
            alertDialog.setPositiveButton(getResources().getString(R.string.comfirm), new Dialog.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (alertDialog != null && alertDialog.isShowing())
                        alertDialog.dismiss();
                }

            });

            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 行为接口
     */
    private long startTime;
    private long stopTime;

    public void StartTime() {
        if (startTime > 0) {
            startTime = 0;
        }

        startTime = System.currentTimeMillis();//开始计时
    }

    public void StopTime() {
        if (stopTime > 0) {
            stopTime = 0;
        }

        stopTime = System.currentTimeMillis();//结束计时
    }

    public String jisuanTime() {
        String jishiTime = (stopTime - startTime) / 1000 + "";
        return jishiTime;
    }


    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    //关闭软键盘的方法
    public boolean closeJianPan() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    //点击屏幕关闭软键盘
    public void touchPinMuCloseJianPan(View object) {
        object.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                return closeJianPan();
            }
        });
    }

    /**
     * setCache
     * 添加
     *
     * @param fileName,value
     */
    public static void setCache(String fileName, String data) {
        mCache.put(fileName, data);
        Log.d("Cache", "setCache");
    }

    /**
     * getCache
     * 读取缓存
     *
     * @param
     */
    public static String getCache(String fileName) {
        Log.d("Cache", "getCache");
        String str = mCache.getAsString(fileName);
        if (str == null) {
            Log.d("Cache", "setCache");
            return null;
        }
        return str;
    }

    /**
     * 清所有.FilesDir()下文件
     */
    public void clearDataFiles() {
        Log.d("Cache", "clearCache");
        mCache.clear();
    }

    /**
     * 语文学科
     * 慧医馆英语、演武场英语走这个网络请求
     */
    public void getData_new(HashMap map, int taskId) {
        if (NetUtil.isNetworking(mContext)) {
            progressDialog.show();
            String url = urlUtil.getUrl(taskId);
            String tag = String.valueOf(taskId);
            final JSONObject finalJsonObject = reverJson(map);
            if (taskId == Task.GET) {
                url = urlUtil.getUrl(taskId) + "?userID=" + map.get("userID") + "&did=" + map.get("did");
                Log.i("======", "GET请求的网址：" + url);
                stringRequest = new StringRequest(Request.Method.GET, url, successListener(tag), errorListener(tag));
            } else {
                Log.i("======", "POST请求的网址：" + url);
                stringRequest = new StringRequest(Request.Method.POST, url, successListener(tag), errorListener(tag)) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("json", finalJsonObject.toString());
                        return map;
                    }
                };
            }
            Log.i("BaseActivity", "请求传递的url：" + url);
            stringRequest.setTag(tag);
            VolleySingleton.getVolleySingleton(getApplicationContext()).addToRequestQueue(stringRequest);
            //mQueue.start();
        } else {
            showToast(getString(R.string.NoInternet));
        }
    }

    private static JSONObject reverJson(Map map) {
        JSONObject jsonObject = null;
        jsonObject = new JSONObject();
        try {
            if (map.isEmpty()) {
                map = new HashMap();
            }
            Iterator iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String name = iter.next().toString();
                if (name.equals("ids") && !map.get(name).equals("")) {
                    JSONObject value = (JSONObject) map.get(name);
                    jsonObject.put(name, value);
                } else {
                    jsonObject.put(name, map.get(name));
                }
            }
            //这里针对比较特殊的传值方式  需要添加一个md5值加密
            //jsonObject.put("mk", MD5Util.md5(Const.MD5String));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //请求成功的监听方法
    public Response.Listener successListener(final String tag) {
        final int task = Integer.parseInt(tag);
        mlistener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("======", "success:" + s);
                refresh(task, urlUtil.getJsonResult(task, s));
            }
        };
        return mlistener;
    }

    //请求失败的监听方法
    public Response.ErrorListener errorListener(String tag) {
        final int task = Integer.parseInt(tag);
        merrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("===fail result===", volleyError.toString());
                //提示请求失败
                refresh(task, urlUtil.getJsonResult(task, volleyError.toString()));
                finish();
                ToastUtil.showMessage(BaseActivity.this,"服务器请求失败！请重试");
            }
        };
        return merrorListener;
    }
}
