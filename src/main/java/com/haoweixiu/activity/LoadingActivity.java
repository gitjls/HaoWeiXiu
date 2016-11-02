package com.haoweixiu.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.haoweixiu.R;

public class LoadingActivity extends Activity{

    private final int SPLASH_DISPLAY_LENGHT = 1800; // 延迟3秒
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Handler hanlder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        initView();
        initData();
    }

    private void initView() {

    }
    private void initData() {
        preferences = getSharedPreferences("loading", Context.MODE_PRIVATE);
        hanlder=new Handler();
        hanlder.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (preferences.getBoolean("firststart", true)==false){
                    editor = preferences.edit();
                    // 将登录标志位设置为false，下次登录时不再显示引导页面
                    editor.putBoolean("firststart", false);
                    editor.commit();
                    Intent intent = new Intent(LoadingActivity.this,AndyViewPagerActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(LoadingActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        }, SPLASH_DISPLAY_LENGHT);
    }

}
