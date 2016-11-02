package com.haoweixiu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.haoweixiu.R;
import com.haoweixiu.dto.OrderDetail;
import com.haoweixiu.gaodemap.activities.ImageMarkerAty;
import com.haoweixiu.service.Task;

import java.util.ArrayList;
import java.util.HashMap;

//已接订单详情页面
public class ReciveOrderDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_custom_name, tv_custom_phonenumber, tv_custom_info, tv_time, tv_model;
    private TextView tv_address, tv_programme, tv_price, tv_city_name, tv_marks;
    private EditText etmrak;
    private String did;
    private ArrayList<String> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recived_order_detail);
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
        tv_marks = (TextView) findViewById(R.id.tv_marks);
        etmrak = (EditText) findViewById(R.id.etmrak);
        did = getIntent().getStringExtra("did");
    }

    public void getData() {

        HashMap hashMap = new HashMap();
        hashMap.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
        hashMap.put("did", did.trim());
        Log.i("getData", "===" + hashMap);
        getData_new(hashMap, Task.ORDERDETAIL);
    }

    public void initData(OrderDetail orderDetail) {
        tv_custom_name.setText("客户姓名: " + orderDetail.getCustom_name());
        tv_custom_phonenumber.setText(orderDetail.getCustom_phonenumber());
        list.add(""+did);
        list.add(orderDetail.getCityid());
        list.add(orderDetail.getCityname());
        list.add(orderDetail.getAddress());
        tv_custom_info.setText("客户信息: " + orderDetail.getCustom_info());
        tv_time.setText("订单时间: " + orderDetail.getTime());
        tv_model.setText("手机型号: " + orderDetail.getModel());
        tv_address.setText("地址: " + orderDetail.getAddress());
        tv_programme.setText("问题描述: " + orderDetail.getProgramme());
        tv_price.setText("价格: " + orderDetail.getPrice());
        tv_marks.setText("备注: " + orderDetail.getMarks());
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
            case Task.ORDERDETAIL:
                OrderDetail orderDetail = (OrderDetail) obj;
                initData(orderDetail);
                break;
            case Task.UPDATE_MARKS:
                String state = (String) obj;
                if (state.equals("1")) {
                    getData();
                } else {
                    showToast("修改失败！");
                }
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
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_custom_location:
                Intent intent = new Intent(this, ImageMarkerAty.class);
                intent.putExtra("activity", "ReciveOrderDetailActivity");
                intent.putStringArrayListExtra("list",list);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_commit:
                HashMap hashMap = new HashMap();
                hashMap.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
                hashMap.put("did", did.trim());
                hashMap.put("marks", etmrak.getText().toString().trim());
                Log.i("getData", "===" + hashMap);
                getData_new(hashMap, Task.UPDATE_MARKS);
                break;
        }
    }
}
