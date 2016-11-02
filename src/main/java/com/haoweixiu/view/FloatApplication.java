package com.haoweixiu.view;

import android.app.Application;
import android.view.WindowManager;

import com.haoweixiu.util.CrashHandler;
//import com.squareup.leakcanary.RefWatcher;




public class FloatApplication extends Application {
    private WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getWindowParams() {
        return windowParams;
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        FloatApplication application = (FloatApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }
//
//    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
//        refWatcher = LeakCanary.install(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

    }
}

           