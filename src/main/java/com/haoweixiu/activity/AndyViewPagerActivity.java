package com.haoweixiu.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.haoweixiu.R;
import com.haoweixiu.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AndyViewPagerActivity extends Activity implements ViewPager.OnPageChangeListener {
    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private List<View> view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initData();
    }

    private void initView() {
        vp = (ViewPager) findViewById(R.id.viewpager);
        views = new ArrayList<View>();
        view1 = new ArrayList<View>();
        // 初始化引导图片列表
        LayoutInflater inflater = LayoutInflater.from(this);
        views.add(inflater.inflate(R.layout.guide_layout1, null));
        views.add(inflater.inflate(R.layout.guide_layout2, null));
        views.add(inflater.inflate(R.layout.guide_layout3, null));
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(this, views);


        view1.add(findViewById(R.id.point_1));
        view1.add(findViewById(R.id.point_2));
        view1.add(findViewById(R.id.point_3));
        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
        for (int i = 0; i < view1.size(); i++) {
            if (i != 0) {
                view1.get(i).setEnabled(false);
            }
        }
    }

    private void initData() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.i("position", "" + position);

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < view1.size(); i++) {
            if (i == position)
                view1.get(i).setEnabled(true);
            else
                view1.get(i).setEnabled(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
