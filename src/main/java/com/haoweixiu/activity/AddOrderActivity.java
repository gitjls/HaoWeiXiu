package com.haoweixiu.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.haoweixiu.R;
import com.haoweixiu.service.Task;
import com.haoweixiu.util.Const;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddOrderActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "AddOrderActivity";
    private EditText et_price, et_dname, et_phone, et_address;
    private Spinner sp_brand, sp_model, sp_way;//抬头标题的文字
    private Button btn_commit_order, btn_back;//添加订单按钮，左侧返回按钮
    private ArrayAdapter<String> BrandAdapter = null;  //品牌适配器
    private ArrayAdapter<String> ModelAdapter = null;    //型号适配器
    private ArrayAdapter<String> DescribleAdapter = null;    //描述适配器
    static int brankPosition = 0;
    static int modelPosition = 0;
    static int wayPosition = 0;
    private String[] province;
    private String[][] city;
    private String[][][] county;
    private TextView tv_price;
    private String price = "380";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addorder);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void initView() {
        sp_brand = (Spinner) findViewById(R.id.sp_brand);
        sp_model = (Spinner) findViewById(R.id.sp_model);
        sp_way = (Spinner) findViewById(R.id.sp_way);
        et_dname = (EditText) findViewById(R.id.et_dname);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_address = (EditText) findViewById(R.id.et_address);
        tv_price = (TextView) findViewById(R.id.tv_price);
    }

    private void initSpinner() {
        //1级手机品牌下拉框监听
        sp_brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //position为当前省级选中的值的序号
                Log.i("sp_brandposition==", "" + position);
                //将地级适配器的值改变为city[position]中的值
//                BrandAdapter = new ArrayAdapter<String>(
//                        AddOrderActivity.this, android.R.layout.simple_spinner_item, province);
//                // 设置二级下拉列表的选项内容适配器
//                sp_brand.setAdapter(BrandAdapter);
                sp_brand.setSelection(position, true);
                brankPosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
                ModelAdapter = new ArrayAdapter<String>(AddOrderActivity.this,
                        android.R.layout.simple_spinner_item, city[brankPosition]);
                sp_model.setAdapter(ModelAdapter);
                sp_model.setSelection(0, true);
                DescribleAdapter = new ArrayAdapter<String>(
                        AddOrderActivity.this, android.R.layout.simple_spinner_item, county[brankPosition][0]);
                // 设置二级下拉列表的选项内容适配器
                sp_way.setAdapter(DescribleAdapter);
                sp_way.setSelection(0, true);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        //2级手机型号下拉监听
        sp_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                Log.i("sp_modelposition==", "" + position);
//                ModelAdapter = new ArrayAdapter<String>(AddOrderActivity.this,
//                        android.R.layout.simple_spinner_item, city[brankPosition]);
//                sp_model.setAdapter(ModelAdapter);
                sp_model.setSelection(position, true);
                String[] s = county[position][0];
                modelPosition = position;
                DescribleAdapter = new ArrayAdapter<String>(
                        AddOrderActivity.this, android.R.layout.simple_spinner_item, county[modelPosition][0]);
                // 设置二级下拉列表的选项内容适配器
                sp_way.setAdapter(DescribleAdapter);
                //默认显示第一个
                sp_way.setSelection(0, true);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //3级下拉框监听
        sp_way.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long arg3) {
                //position为当前省级选中的值的序号
                Log.i("sp_wayposition==", "" + position);
                //将地级适配器的值改变为city[position]中的值
//                DescribleAdapter = new ArrayAdapter<String>(
//                        AddOrderActivity.this, android.R.layout.simple_spinner_item, city[position]);
//                // 设置二级下拉列表的选项内容适配器
//                sp_way.setAdapter(DescribleAdapter);
                sp_way.setSelection(position, true);
                wayPosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
                String context = sp_way.getSelectedItem().toString();
                price = context.substring(context.indexOf(":") + 1);
                tv_price.setText(price);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
    }

        private void initData() {

        //省级选项值
        province = new String[]{"iphone"};//,"重庆","黑龙江","江苏","山东","浙江","香港","澳门"};
        //地级选项值
        city = new String[][]
                {
                        {"iphone6splus", "iphone6s", "iphone6plus", "iphone6", "iphonese", "iphone5s", "iphone5c", "iphone5"}

                };

        //县级选项值
        county = new String[][][]
                {
                        {   //iphone6splus
                                {"外屏碎:380", "内屏碎:1350", "前置摄像头:210", "后置摄像头:290", "感应故障:210", "home键:140", "电池:180", "尾插排线:180"}
                        },
                        {    //iphone6s
                                {"外屏碎:340", "内屏碎:950", "前置摄像头:140", "后置摄像头:220", "感应故障:140", "home键:140", "电池:180", "尾插排线:180"}
                        },
                        {    //iphone6plus
                                {"外屏碎:280", "内屏碎:700", "前置摄像头:98", "后置摄像头:98", "感应故障:98", "home键:90", "电池:130", "尾插排线:110"}
                        },
                        {   //iphone6
                                {"外屏碎:280", "内屏碎:600", "前置摄像头:98", "后置摄像头:98", "感应故障:98", "home键:90", "电池:130", "尾插排线:110"}
                        },
                        {    //iphonese
                                {"外屏碎:180", "内屏碎:950", "前置摄像头:140", "后置摄像头:220", "感应故障:140", "home键:140", "电池:180", "尾插排线:180"}
                        },
                        {    //iphone5s
                                {"外屏碎:180", "内屏碎:370", "前置摄像头:80", "后置摄像头:80", "感应故障:80", "home键:80", "电池:90", "尾插排线:120"}
                        },
                        {    //iphone5c
                                {"外屏碎:180", "内屏碎:370", "前置摄像头:80", "后置摄像头:80", "感应故障:80", "home键:80", "电池:90", "尾插排线:120"}
                        },
                        {    //iphone5
                                {"外屏碎:180", "内屏碎:370", "前置摄像头:80", "后置摄像头:80", "感应故障:80", "home键:80", "电池:90", "尾插排线:120"}
                        },

                };

        //绑定适配器和值
        BrandAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, province);
        sp_brand.setAdapter(BrandAdapter);
        sp_brand.setSelection(0, true);  //设置默认选中项，此处为默认选中第1个值

        ModelAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, city[0]);
        sp_model.setAdapter(ModelAdapter);
        sp_model.setSelection(0, true);  //默认选中第1个

        DescribleAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, county[0][0]);
        sp_way.setAdapter(DescribleAdapter);
        sp_way.setSelection(0, true);

        tv_price.setText(sp_way.getSelectedItem().toString());
        initSpinner();

    }

    //正则检查手机号码
    public boolean matchPhone(String phone) {

        String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";

        Pattern p = Pattern.compile(regExp);

        Matcher m = p.matcher(phone);

        return m.matches();//boolean
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_commit_order:
                String phone = et_phone.getText().toString().trim();
                boolean ismatch=matchPhone(phone);
                if (ismatch==false||phone.length()<11)
                {
                    showToast("请输入正确的手机号码");
                    break;
                }else if(ismatch==true) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
                    hashMap.put("brand", sp_brand.getSelectedItem().toString());
                    hashMap.put("model", sp_model.getSelectedItem().toString());
                    hashMap.put("programme", sp_way.getSelectedItem().toString());
                    hashMap.put("price", price);
                    hashMap.put("dname", et_dname.getText().toString().trim());
                    hashMap.put("phone", phone);
                    hashMap.put("address", et_address.getText().toString().trim());
                    hashMap.put("sname", Const.SNAME);
                    getData_new(hashMap, Task.ADD_ORDER);
                    break;
                }
            case R.id.btn_back:
                finish();
                break;
            default:
                break;
        }

    }

    //返回数据操作
    @Override
    public void refresh(Object... param) {
        int type = (Integer) param[0];
        Object obj = (Object) param[1];
        switch (type) {
            case Task.ADD_ORDER: {
                String did = (String) obj;
                Intent intent = new Intent(this, ReciveOrderDetailActivity.class);
                Log.i("==did", did);
                intent.putExtra("did", did);
                startActivity(intent);
                finish();
                break;
            }
            default:
                break;

        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
