package com.haoweixiu.gaodemap.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.haoweixiu.R;
import com.haoweixiu.gaodemap.bean.OrderDetailBean;
import com.haoweixiu.gaodemap.net.RequestApiImp;
import com.haoweixiu.gaodemap.net.RequestListener;
import com.haoweixiu.gaodemap.util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by ytx on 2016/9/12.
 */
public class OrderDetailAty extends Activity implements View.OnClickListener{
    private TextView tv_center;
    private TextView tv_user_name;
    private TextView tv_location;
    private TextView tv_price;
    private TextView tv_material_count;
    private TextView tv_time;
    private TextView tv_iphone;
    private TextView tv_phone_condition;
    private TextView img_phone;
    private Button img_location;
    private Button img_back;
    private String info;
    private String did;
    String mPhoneNum ;
    private String mPrice;
    private static final int MESSAGE_ONE = 0x001;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_ONE:
                    JSONObject json = (JSONObject) msg.obj;
                    OrderDetailBean orderDetailBean  = JSON.parseObject(json.toString(),OrderDetailBean.class);
                    mPhoneNum = orderDetailBean.getPhone();
                    mPrice= orderDetailBean.getPrice();
                    String price = Constants.price+mPrice;
                    int start = Constants.price.length()-1;
                    int end = price.length();
                    tv_price.setText(price);
                    SpannableStringBuilder style=new SpannableStringBuilder(price);
                    style.setSpan(new ForegroundColorSpan(Color.RED),start,end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    tv_price.setText(style);

                   String model =  orderDetailBean.getModel();
                    tv_iphone.setText(model);
                   String programme =  orderDetailBean.getProgramme();
                    tv_phone_condition.setText(programme);
                    String name = orderDetailBean.getDname();
                    tv_user_name.setText(name);
                   String info =orderDetailBean.getInfo();
                    //TODO 这个Info不知道是那些数据
                    String time = orderDetailBean.getTime();
                    tv_time.setText(Constants.strFormat(OrderDetailAty.this, R.string.time,time));
                    String city = orderDetailBean.getCityid();
                    String cityName = orderDetailBean.getCityname();
                    String cityAddr = orderDetailBean.getAddress();
                    if(TextUtils.isEmpty(city)){
                        city="";
                    }
                    if(TextUtils.isEmpty(cityName)){
                        cityName="";
                    }
                    if(TextUtils.isEmpty(cityAddr)){
                        cityAddr="";
                    }
                   String addr = city+cityName+cityAddr;
                    tv_location.setText(addr);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        initEvent();
        getReceivedOrder();
    }

    private void initEvent() {
        img_phone.setOnClickListener(this);
        img_location.setOnClickListener(this);
        img_back.setOnClickListener(this);
    }

    private void initView() {
        Intent intent =getIntent();
        info = intent.getStringExtra("info");
        did = intent.getStringExtra("did");
        tv_center = (TextView) findViewById(R.id.tv_center);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_iphone = (TextView) findViewById(R.id.tv_iphone);
        tv_phone_condition = (TextView) findViewById(R.id.tv_phone_condition);
        tv_material_count = (TextView) findViewById(R.id.tv_material_count);

        img_phone = (TextView) findViewById(R.id.tv_custom_phonenumber);
        img_location = (Button) findViewById(R.id.tv_custom_location);
        img_back = (Button) findViewById(R.id.btn_back);
    }
    protected void getReceivedOrder() {
        RequestApiImp.getInstance().received_order(Constants.MAP_KEY, did,this, new RequestListener() {
            @Override
            public void requestSuccess(JSONArray json) {
            }

            @Override
            public void requestSuccess(JSONObject json) {
                Message message = new Message();
                message.what = MESSAGE_ONE;
                message.obj = json;
                mHandler.sendMessage(message);
            }

            @Override
            public void requestError(VolleyError e) {
            }

        });
    }
    public void showBottoPopupWindow(final Context context) {
        WindowManager manager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = manager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        defaultDisplay.getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        View menuView = LayoutInflater.from(this).inflate(R.layout.show_popup_window,null);
        final PopupWindow mPopupWindow = new PopupWindow(menuView, (int)(width*0.8),
                ActionBar.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.AnimationPreview);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAtLocation(tv_center, Gravity.CENTER, 0, 0);
        mPopupWindow.setOnDismissListener(new poponDismissListener());
        backgroundAlpha(0.6f);
        TextView tv_cancle = (TextView) menuView.findViewById(R.id.tv_cancle);
        TextView tv_ensure = (TextView) menuView.findViewById(R.id.tv_ensure);
        TextView tv_pop_title = (TextView) menuView.findViewById(R.id.tv_pop_title);
        tv_pop_title.setText(Constants.setPhone(mPhoneNum));
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
        tv_ensure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+mPhoneNum));
                ((Activity)context).startActivity(intent);
                mPopupWindow.dismiss();
                backgroundAlpha(1f);
            }
        });
    }
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }
    class poponDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.img_phone:
                showBottoPopupWindow(this);
                break;
            case R.id.img_location:
                Intent intent = new Intent(OrderDetailAty.this, RouteActivity.class);
                intent.putExtra("info",info);
                startActivity(intent);
                break;
        }
    }
}
