package com.haoweixiu.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.haoweixiu.R;
import com.haoweixiu.dto.OrderDetail;
import com.haoweixiu.service.Task;
import com.haoweixiu.util.Const;

import java.util.HashMap;
//*
// 已接完成订单详情页面
// */
public class CompleteDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_custom_name, tv_custom_phonenumber, tv_custom_info, tv_time, tv_model;
    private TextView tv_address, tv_programme, tv_price, tv_city_name;
    private EditText etmrak;
    private String did;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_detail);
        initView();
        getData();
    }

    public void initView() {
        tv_custom_name = (TextView) findViewById(R.id.tv_custom_name);
        tv_custom_phonenumber = (TextView) findViewById(R.id.tv_custom_phonenumber);
        tv_custom_info = (TextView) findViewById(R.id.tv_custom_info);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_model = (TextView) findViewById(R.id.tv_model);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_programme = (TextView) findViewById(R.id.tv_programme);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_city_name = (TextView) findViewById(R.id.tv_city_name);
    }

    public void getData() {
        did = getIntent().getStringExtra("did");
        HashMap hashMap = new HashMap();
        hashMap.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
        hashMap.put("did", did.trim());
        Log.i("getData", "===" + hashMap);
        getData_new(hashMap, Task.COMPLETE_ORDER_DETAIL);
    }

    public void initData(OrderDetail orderDetail) {
        tv_custom_name.setText("客户姓名: " + orderDetail.getCustom_name());
        tv_custom_phonenumber.setText(orderDetail.getCustom_phonenumber());
        tv_custom_info.setText("客户信息: " + orderDetail.getCustom_info());
        tv_time.setText("订单时间: " + orderDetail.getTime());
        tv_model.setText("手机型号: " + orderDetail.getModel());
        tv_address.setText("地址: " + orderDetail.getAddress());
        tv_programme.setText("问题描述: " + orderDetail.getProgramme());
        tv_price.setText("价格: " + orderDetail.getPrice());
        tv_city_name.setText("城市名称: " + orderDetail.getCityname());
    }

    @Override
    public void refresh(Object... param) {
        Log.i("==顺序==", "refresh");
        int type = (Integer) param[0];
        Object obj = param[1];

        if (obj == null)
            return;
        switch (type) {
            case Task.COMPLETE_ORDER_DETAIL:
                OrderDetail orderDetail = (OrderDetail) obj;
                initData(orderDetail);
                break;
            default:
                break;
        }
        if (progressDialog != null && progressDialog.isShowing()) {

            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_receive_order:
                HashMap hashMap = new HashMap();
                hashMap.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
                hashMap.put("sname", Const.SNAME);
                hashMap.put("did", did.trim());
                Log.i("getData", "===" + hashMap);
                getData_new(hashMap,Task.SEND_ORDER);
                break;
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }
    }

}
