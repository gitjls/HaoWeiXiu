package com.haoweixiu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by Administrator on 2016/8/19.
 */
public class Waterripp extends View{
    /*
    *画笔
    * */
    private Paint mpaint;
    /*
    画布
    * */
    private Canvas mcanvas;
    /**
     * 文本
     */
    private String mTitleText;
    /**
     * 文本的颜色
     */
    private int mTextColor;
    /**
     * 文本的大小
     */
    private int mTextSize;
    /**
     * 背景色
     */
    private int mBackGround;
    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
//    public Waterripp(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        TypedArray a = context.getTseme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyleAttr, 0);
//        int n = a.getIndexCount();
//        for (int i = 0; i < n; i++)
//        {
//            int attr = a.getIndex(i);
//            switch (attr)
//            {
//                case R.styleable.text:
//                    mTitleText = a.getString(attr);
//                    break;
//                case R.styleable.background:
//                    // 默认颜色设置为黑色
//                    mBackGround = a.getColor(attr, Color.BLACK);
//                    break;
//                case R.styleable.textcolor:
//                    // 默认设置为16sp，TypeValue也可以把sp转化为px
//                    mTextColor = a.getColor(attr, Color.BLACK);
//                    break;
//                case R.styleable.textsize:
//                    // 默认设置为16sp，TypeValue也可以把sp转化为px
//                    mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
//                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
//                    break;
//
//            }
//
//        }
//        a.recycle();
//
//        /**
//         * 获得绘制文本的宽和高
//         */
//        mpaint = new Paint();
//        mpaint.setTextSize(mTitleTextSize);
//        // mPaint.setColor(mTitleTextColor);
//        mBound = new Rect();
//        mpaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
//    }

    public Waterripp(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
