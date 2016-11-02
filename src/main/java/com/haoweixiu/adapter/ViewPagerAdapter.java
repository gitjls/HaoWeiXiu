package com.haoweixiu.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by jls on 2016/8/22.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private List<View> mlistview;
    private Context mcontext;
    private LayoutInflater inflater;
    public ViewPagerAdapter(Context context, List<View> view) {
        super();
        this.mcontext = context;
        this.mlistview = view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public int getCount() {
        return mlistview.size();
    }
    //初始化Item实例的方法
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mlistview.get(position));
        return mlistview.get(position);
    }
    //Item销毁的方法
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        /**
         * 销毁Item的方法不使用系统定义销毁Item的方法，而是使用我们自己定义的形式，将一个View对象从ViewPager中移除
         */
        //super.destroyItem(container, position, object);
        container.removeView(mlistview.get(position));
    }

}
