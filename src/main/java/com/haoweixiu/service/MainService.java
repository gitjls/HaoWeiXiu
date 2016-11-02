package com.haoweixiu.service;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class MainService extends Service implements Runnable {
    private static final String LOGTAG = "MainService";
    public static ArrayList<Task> allTask = new ArrayList<>();
    public static ArrayList<Activity> allActivity = new ArrayList<>();// 添加Activity
    public boolean isrun = false;
    private Thread t = null;
    public static boolean is_del = false;

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(LOGTAG, "onBind()...");
        return null;
    }

    @SuppressWarnings("deprecation")
    public boolean onUnbind(Intent intent) {
        Log.d(LOGTAG, "onUnbind()...");
        t.stop();
        return false;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(LOGTAG, "onRebind()...");
        t.start();
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        Log.d(LOGTAG, "onCreate()...");
        isrun = true;
        t = new Thread(this);
        t.start();
        this.startForeground((int) System.currentTimeMillis(),
                new Notification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (t != null && t.isAlive()) {
            isrun = false;
            t.interrupt();
            t = null;
        }
        hand.removeCallbacksAndMessages(null);
        // 开启服务
//        this.startService(new Intent(Const.MAIN_SERVICE));
    }

    @Override
    public void run() {
        while (isrun) {
            try {
                Task lastTask = null;
                synchronized (allTask) {
                    if (allTask.size() > 0) {
                        for (int i = 0; i < allTask.size(); i++) {
                            Log.i("allTask", "allTask 第 " + i + "个= " + allTask.get(i).getTaskID() + ";此时的长度 = " + allTask.size());
                        }
                        lastTask = allTask.get(0);
                        doTask(lastTask);
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void newTask(Task task) {
        if (is_del) {
            Log.i("allTask", " clear>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
            allTask.clear();
            is_del = false;
        }
        allTask.add(task);
    }

    private Handler hand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            switch (msg.what) {
//                case Task.HYG_YUWEN_STARTEXAM:
//                    QueryDateInterface ia15 = (QueryDateInterface) MainService
//                            .getActivityByName("YuWenStartExamActivity");
//                    if (ia15 != null)
//                        ia15.refresh(Task.HYG_YUWEN_STARTEXAM, msg.obj);
//                    break;
//                default:
//                    break;
//            }
        }

    };

    public void doTask(Task task) {
        Message mess = hand.obtainMessage();
        mess.what = task.getTaskID();
        try {
            switch (task.getTaskID()) {
//			case Task.LOADING:
//				// mess.obj = DataService.loading(task);
//				break;


                default:
                    break;
            }
            hand.sendMessage(mess);
            allTask.remove(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Activity getActivityByName(String name) {
        for (Activity ac : allActivity) {
            if (ac.getClass().getName().indexOf("." + name) >= 0) {
                Log.d("ddd", "ac  =" + ac.getClass().getName());
                return ac;
            }
        }
        return null;
    }

    public static synchronized void addActivity(Activity activity) {
        String name = activity.getClass().getName();
        Iterator<Activity> iterator = allActivity.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            Activity ac = (Activity) iterator.next();
            if (ac.getClass().getName().indexOf(name) >= 0) {
                allActivity.remove(num);
                break;
            }
            num++;
        }
        allActivity.add(activity);
    }

    public static synchronized void removeActivity(Activity activity) {
        String name = activity.getClass().getName();
        Iterator<Activity> iterator = allActivity.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            Activity ac = (Activity) iterator.next();
            if (ac.getClass().getName().indexOf(name) >= 0) {
                allActivity.remove(num);
                break;
            }
            num++;
        }

    }

    /**
     * 用来判断当前的activity 是否是在最顶端。
     */
    private boolean isTopActivity(Activity ia) {
        boolean isTop = false;
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        Log.d("ddd", "isTopActivity = " + cn.getClassName());
        if (cn.getClassName().contains("")) {
            isTop = true;
        }
        Log.d("ddd", "isTop = " + isTop);
        return isTop;
    }

}
