package com.haoweixiu.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushSettings;
import com.haoweixiu.R;
import com.haoweixiu.dto.Fragment_Bundle;
import com.haoweixiu.dto.Order;
import com.haoweixiu.gaodemap.activities.ImageMarkerAty;
import com.haoweixiu.service.Task;
import com.haoweixiu.util.Const;
import com.haoweixiu.util.NetUtil;
import com.haoweixiu.util.Utils;
import com.haoweixiu.view.MyOrderFragment;
import com.haoweixiu.view.OrderMapFragment;
import com.haoweixiu.view.HomePageFragment;
import com.haoweixiu.view.MyInfoFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    Bitmap source, target;
    private List<Fragment> fragmentList;
    private int count = 0;//back键按下的次数
    private Fragment_Bundle fragment_bundle;
    private Timer mTimer = new Timer();
    public boolean isRun = false;
    private RadioButton btn_homepage;
    private MyOrderFragment myOrderFragment;
    private OrderMapFragment orderMapFragment;
    private HomePageFragment homePageFragment;
    private MyInfoFragment myInfoFragment;
    public String TAG = "2016-09-2,MainActivity";
    public static Boolean isDownLodingApk = false;//默认没有在下载更新
    private TextView tv_title;

    @Override
    protected void onPause() {
        //Log.d("***======***","MainActivity--->onPause()");
        super.onPause();
        mTimer.cancel();
        mTimer = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d("***======***","MainActivity--->onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        PushSettings.enableDebugMode(this, true);
//        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY,
//                Utils.getMetaValue(MainActivity.this, "api_key"));
        tv_title = (TextView) findViewById(R.id.tv_title);
        Const.STARTTIME = System.currentTimeMillis();
        //清空缓存
        myLruCache.clearCache();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        StartBaiduPush();
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        fragmentList = new ArrayList<Fragment>();
        Log.i("bug", "width =" + width + ";height =" + height + ";屏幕密度 =" + density + ";屏幕密度DPI =" + densityDpi);

        btn_homepage = (RadioButton) findViewById(R.id.btn_homePage);
//        fm = getFragmentManager();
//        // 开启Fragment事务
//        transaction = fm.beginTransaction();

        fragment_bundle = new Fragment_Bundle();
        setDefaultFragment();
        isRun = true;
        initView();
        btn_homepage.setChecked(true);
    }

    private void StartBaiduPush() {
        try {
            //baidu_push_start
            Utils.logStringCache = Utils.getLogText(getApplicationContext());
            Resources resource = this.getResources();
            String pkgName = this.getPackageName();

            // Push: 以apikey的方式登录，一般放在主Activity的onCreate中。
            // 这里把apikey存放于manifest文件中，只是一种存放方式，
            // 您可以用自定义常量等其它方式实现，来替换参数中的Utils.getMetaValue(PushDemoActivity.this,
            // "api_key")

//        PushManager.startWork(getApplicationContext(),PushConstants.LOGIN_TYPE_API_KEY, "u00l0Yxm7ryWr8LZFICZzxuG");
//       while(!PushManager.isConnected(MainActivity.this)){
            String apikey = Utils.getMetaValue(this, "api_key");
            PushSettings.enableDebugMode(this, true);
            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, Utils.getMetaValue(this, "api_key"));
//        }


            // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
            // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
            // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
            CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                    resource.getIdentifier(
                            "notification_custom_builder", "layout", pkgName),
                    resource.getIdentifier("notification_icon", "id", pkgName),
                    resource.getIdentifier("notification_title", "id", pkgName),
                    resource.getIdentifier("notification_text", "id", pkgName));
            cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
            cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_VIBRATE);
            cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
            cBuilder.setLayoutDrawable(resource.getIdentifier(
                    "simple_notification_icon", "drawable", pkgName));
            PushManager.setNotificationBuilder(this, 1, cBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setDefaultFragment() {

        if (homePageFragment == null) {
            homePageFragment = new HomePageFragment();
        }
        tv_title.setText("未接单");
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle bundle = new Bundle();
        ArrayList<Map> list = new ArrayList<Map>();
        for (int i = 0; i < 5; i++) {
            Map map = new HashMap();
            map.put("tvcname", "tvcname");
            map.put("tvphoneNumber", "155555585");
            map.put("tvaddress", "tvaddress");
            map.put("tv_type", "tv_type");
            list.add(map);
        }
        if (!"".equals(fragment_bundle.getAcceptMapList())) {
            fragment_bundle.setAcceptMapList(list);
            bundle.putSerializable("unFinishMapList", fragment_bundle.getAcceptMapList());
            homePageFragment.setArguments(bundle);
        }
        fragmentList.add(homePageFragment);
        transaction.replace(R.id.id_content, homePageFragment);
        transaction.commit();
    }

    //隐藏掉其他的碎片
    private void hideOtherFragment(FragmentTransaction transaction, Fragment fragment) {

        for (int i = 0; i < fragmentList.size(); i++) {
            if (fragmentList.get(i).equals(fragment)) {
                continue;
            }
            transaction.hide(fragmentList.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        if (!NetUtil.isNetworking(mContext)) {
            Log.i("MY", "网络异常");
            showToast(getResources().getString(R.string.NoInternet));
            return;
        }
        FragmentManager fragmentManagerm = getFragmentManager();
        FragmentTransaction transactions = fragmentManagerm.beginTransaction();
        switch (v.getId()) {
            case R.id.btn_homePage:
                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();
                }
                tv_title.setText("未接单");
                // 第一次replace 否则 hide show
                if (fragmentList.contains(homePageFragment)) {
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction transaction = fm.beginTransaction();
                    hideOtherFragment(transactions, homePageFragment);
                    transactions.show(homePageFragment);
                    transactions.commit();
                } else {
                    transactions.add(R.id.id_content, homePageFragment);
                    transactions.commit();
                    Bundle bundle = new Bundle();
                    ArrayList<Map> list = new ArrayList<Map>();
                    for (int i = 0; i < 5; i++) {
                        Map map = new HashMap();
                        map.put("tvcname", "tvcname");
                        map.put("tvphoneNumber", "155555585");
                        map.put("tvaddress", "tvaddress");
                        map.put("tv_type", "tv_type");
                        list.add(map);
                    }
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("sphone", "18701137993");
                    map.put("password", "liluchang");
                    map.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
                    getData_new(map, Task.LOGIN);
                    fragment_bundle.setAcceptMapList(list);
                    bundle.putSerializable("unFinishMapList", fragment_bundle.getAcceptMapList());
                    homePageFragment.setArguments(bundle);
                }
                fragmentList.add(homePageFragment);
                break;
            case R.id.btn_myorder:
                Intent intent = new Intent(this, MyOrderActivity.class);
                startActivity(intent);
//                if (myOrderFragment == null) {
//                    myOrderFragment = new MyOrderFragment();
//                }
//                tv_title.setText("已接单");
//                if (fragmentList.contains(myOrderFragment)) {
////                    FragmentManager fm = getFragmentManager();
////                    FragmentTransaction transaction = fm.beginTransaction();
//                    hideOtherFragment(transactions, myOrderFragment);
//                    transactions.show(myOrderFragment);
//                    transactions.commit();
//                } else {
//
//                    HashMap<String, String> maps = new HashMap<>();
//                    maps.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
//                    maps.put("page", "1");
//                    Log.i("getData", "===" + maps);
//                    getData_new(maps, Task.MyOrder);
//                }
//                fragmentList.add(myOrderFragment);
                // Bundle bundle1 = new Bundle();

                break;
            case R.id.btn_ordermap:
//                if (orderMapFragment == null) {
//                    orderMapFragment = new OrderMapFragment();
//                }
//                tv_title.setText("已完成的单");
//                //便于切换的时候保存当前状态
//                if (fragmentList.contains(orderMapFragment)) {
////                    FragmentManager fm = getFragmentManager();
////                    FragmentTransaction transaction = fm.beginTransaction();
//                    hideOtherFragment(transactions, orderMapFragment);
//                    transactions.show(orderMapFragment);
//                    transactions.commit();
//                } else {
//                    transactions.add(R.id.id_content, orderMapFragment);
//                    transactions.commit();
//                    Bundle bundle2 = new Bundle();
//                    ArrayList<Map> completelist = new ArrayList<Map>();
//                    for (int i = 0; i < 5; i++) {
//                        Map map = new HashMap();
//                        map.put("complete_order_number", "complete_order_number");
//                        map.put("complete_order_time", "complete_order_time");
//                        map.put("complete_phone_type", "complete_phone_type");
//                        map.put("complete_tv_ticheng", "complete_tv_ticheng");
//                        completelist.add(map);
//                    }
//                    fragment_bundle.setCompleteMapList(completelist);
//                    bundle2.putSerializable("completeMapList", fragment_bundle.getCompleteMapList());
//                    orderMapFragment.setArguments(bundle2);
//
//                }
//
//                fragmentList.add(orderMapFragment);
                Intent intents = new Intent(this, ImageMarkerAty.class);
                intents.putExtra("activity", "MAINACTIVITY");
                startActivity(intents);
                break;
            case R.id.btn_myinfo:
                if (myInfoFragment == null) {
                    myInfoFragment = new MyInfoFragment();
                }
                tv_title.setText("个人中心");
                if (fragmentList.contains(myInfoFragment)) {
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction transaction = fm.beginTransaction();
                    hideOtherFragment(transactions, myInfoFragment);
                    transactions.show(myInfoFragment);
                    transactions.commit();
                } else {
                    transactions.add(R.id.id_content, myInfoFragment);
                    transactions.commit();
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("sphone", "18701137993");
                    map.put("password", "liluchang");
                    map.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
                    getData_new(map, Task.LOGIN);
                }

                fragmentList.add(myInfoFragment);
                break;
        }
//        transactions.commit();
        // transaction.addToBackStack();

    }


    private void startTimer() {
        //Log.d("***======***","MainActivity--->startTimer()");
        mTimer = new Timer();

        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                try {

                    handleProgress.sendEmptyMessage(0);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };
        mTimer.schedule(mTimerTask, 0, 7 * 1000);
    }

    //更新状态栏
    public void updateContent(Context context, HashMap<String, String> map, String tag) {
        try {

            if (tag.equals("onBind")) {

                MyTask myTask = new MyTask(map);

                myTask.execute(null, null, null);


            } else if (tag.equals("onMessage")) {
                //消息栏通知
                //定义NotificationManager

                String ns = Context.NOTIFICATION_SERVICE;

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

                //定义通知栏展现的内容信息

                int icon = R.drawable.ic_launcher;

                CharSequence tickerText = "分豆教育";

                long when = System.currentTimeMillis();

                Notification notification = new Notification(icon, tickerText, when);


                //定义下拉通知栏时要展现的内容信息

                Context context2 = getApplicationContext();

                CharSequence contentTitle = map.get("message");

                CharSequence contentText = map.get("customContentString");

                Intent notificationIntent = new Intent(this, LoginActivity.class);

                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

                notification.setLatestEventInfo(context2, contentTitle, contentText, contentIntent);

                notification.defaults = notification.DEFAULT_SOUND | notification.DEFAULT_VIBRATE;

                //用mNotificationManager的notify方法通知用户生成标题栏消息通知

                mNotificationManager.notify(1, notification);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {
            Log.d("***===!!!===***", "获取任务塔消息数->userID:" + userID);
            HashMap<String, String> map3 = new HashMap<String, String>();
            map3.put("userID", userID);
            map3.put("student_id", userID);
            //getData(map3, Task.RENWUTA_GET_MESSAGECOUNT);
        }

        ;
    };

    //*****************************************************

    @Override
    protected void onResume() {
        //Log.d("***======***","MainActivity--->onResume()");
        super.onResume();
        Log.i(TAG, "onResume()");
        Const.ISLOGIN = false;
//        initData();
        startTimer();
    }

    private void initView() {
        hideMrlTitle();

    }

    @Override
    public void refresh(Object... param) {
        Log.i("==顺序==", "refresh");
        int type = (Integer) param[0];
        Object obj = param[1];
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transactions = fm.beginTransaction();
        if (obj == null)
            return;
        switch (type) {
            case Task.MyOrder:
                transactions.add(R.id.id_content, myOrderFragment);
//                    transactions.commit();
                ArrayList<Order> orderList = (ArrayList<Order>) obj;
                Bundle bundle = new Bundle();
                fragment_bundle.setOrderList(orderList);
                bundle.putSerializable("OrderList", fragment_bundle.getOrderList());
                myOrderFragment.setArguments(bundle);
                break;
            case Task.LOGIN:
                break;
            default:
                break;
        }
        if (progressDialog != null && progressDialog.isShowing()) {

            progressDialog.dismiss();
        }
        transactions.commit();
    }

    //创建圆框图片
    public Bitmap createCircleImage(Bitmap resource, int mins) {
        try {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            int width = resource.getWidth();
            int height = resource.getHeight();
            int min = width < height ? width : height;

            float s = mins / min;
            Matrix matrix = new Matrix();
            matrix.postScale(s, s);
            source = Bitmap.createScaledBitmap(resource, mins, mins, true);

            target = Bitmap.createBitmap(mins, mins, Bitmap.Config.ARGB_8888);
            /**
             * 产生一个同样大小的画布
             */
            Canvas canvas = new Canvas(target);
            /**
             * 首先绘制圆形
             */
            canvas.drawCircle(mins / 2, mins / 2, mins / 2, paint);
            /**
             * 使用SRC_IN
             */
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            /**
             * 绘制图片
             */
            canvas.drawBitmap(source, 0, 0, paint);

            myLruCache.addBitmapCache("user_image", target);

            return target;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    private class MyTask extends AsyncTask<String, Integer, String> {
        HashMap<String, String> map1 = new HashMap<String, String>();

        public MyTask(HashMap<String, String> map1) {
            this.map1 = map1;
        }


        @Override
        protected String doInBackground(String... params) {
//            Looper.prepare();

            try {
                //百度push的sp
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);


                String userId = map1.get("userId");

                mSharedPreferencesEditor.putString("baidu_userId", userId);
                mSharedPreferencesEditor.commit();

                String channelId = map1.get("channelId");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", userId);
                map.put("channelId", channelId);
                map.put("username", mSharedPreferences.getString("username", ""));
                map.put("password", mSharedPreferences.getString("password", ""));

                map.put("terminal", "a");

                map.put("model", mSharedPreferences.getString("deviceID", "")); //获取手机型号id
                map.put("usertype", "1");//usertype这个字段，pad端传1 手机端传2
                System.out.println("设备的id;" + mSharedPreferences.getString("deviceID", ""));
//            System.out.println("userId;"+userId);
//            System.out.println("channelId："+channelId);
                System.out.println("username" + mSharedPreferences.getString("username", ""));
                System.out.println("password：" + mSharedPreferences.getString("password", ""));

                map.put("release", android.os.Build.VERSION.SDK_INT + ","
                        + android.os.Build.VERSION.RELEASE);//RELEASE获取版本号
//            System.out.println("百度push传的设备："+device);
                getData(map, Task.BAIDUPUSH);

                return "";
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("======", "主界面销毁");
        if (source != null && !source.isRecycled()) {
            source.recycle();
            source = null;
        }
        if (target != null && !target.isRecycled()) {
            target.recycle();
            target = null;
        }
        System.gc();
    }

    @Override
    public void onBackPressed() {
        count++;
        if (count == 1) {
            Toast.makeText(mContext, getResources().getString(R.string.exittip), Toast.LENGTH_SHORT).show();
        } else if (count == 2) {
            for (Activity ac : allActivity1) {
                if (!ac.isFinishing()) {
                    ac.finish();
                }
            }
        }

    }

}
