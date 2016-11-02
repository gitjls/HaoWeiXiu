package com.haoweixiu.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.haoweixiu.R;
import com.haoweixiu.dto.UserInfo;
import com.haoweixiu.service.Task;
import com.haoweixiu.util.Const;
import com.haoweixiu.util.NetUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "LoginActivity";
    private EditText meditUserName, meditPassword;
    private String mUserName, mPassword;
    private RelativeLayout login_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       BaseSetContentView(R.layout.activity_login);
        setContentView(R.layout.activity_login);
        //检测更新
        checkUpdate();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Const.ISLOGIN = true;
        initData();
        checkLogin();
    }

    private void checkLogin() {
        //判断是否登出  如果userID为空 则进入登录界面
        String model = mSharedPreferences.getString("deviceID", "");
        Log.i(TAG, "获取到的userID：" + userID + " model:" + model);

        if (model.equals("")) {

        } else {
            if (!userID.equals("")) {
                String utype = mSharedPreferences.getString("user_type", "");
                if (!"".equals(utype)) {
                    if ("11".equals(utype)) {
                        String stu_name = mSharedPreferences.getString("stu_name", "");
                        String stu_phone = mSharedPreferences.getString("stu_phone", "");
                        String stu_qq = mSharedPreferences.getString("stu_qq", "");
                        String parent_phone = mSharedPreferences.getString("parent_phone", "");
                        if (!"".equals(stu_name) && !"".equals(stu_phone) && !"".equals(stu_qq) && !"".equals(parent_phone)) {
                            Const.sid = mSharedPreferences.getString("sid", "");
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        }
                    } else {
                        Const.sid = mSharedPreferences.getString("sid", "");
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }
            }
        }
    }


    private void initView() {
        // mimgTitle=(TextView)findViewById(R.id.tv_title);
//        mbtnBack=(Button)findViewById(R.id.btnBack);
//        mbtnBack.setVisibility(View.GONE);
        defaultSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        meditUserName = (EditText) findViewById(R.id.editUserName);
        meditPassword = (EditText) findViewById(R.id.editPassword);
        login_rl = (RelativeLayout) findViewById(R.id.login_rl);
    }

    private void initData() {
        if (!NetUtil.isNetworking(this)) {
            showToast(getResources().getString(R.string.NoInternet));
            return;
        }
        // mimgTitle.setText("登 录");
        mUserName = "";
        mPassword = "";
        //如果存储了用户名 直接显示在编辑框中
        String username = mSharedPreferences.getString("username", "");
        String password = mSharedPreferences.getString("password", "");
        if (!password.equals("")) {
            meditPassword.setText(password);
        }
        if (!username.equals("")) {
            meditUserName.setText(username);
        }
        Log.i(TAG, "获取到的用户名：" + username);
        touchPinMuCloseJianPan(login_rl);
    }

    private void checkUpdate() {
     /*   HashMap<String, String> map = new HashMap<>();
        map.put("userID", "33497");
        map.put("type", "4");//手机端传6   pad端传4
        getData_new(map, Task.XITONG_UPDATEAPP_LOGIN);*/
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnLogin:
                login();
                break;
            default:
                break;
        }

    }

    public static String sHA1(Context context) {

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert); 
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    //登陆函数
    private void login() {
        String sha=sHA1(this);
        Log.i(TAG, "login()开始"+sha);
        Log.i(TAG, "login()开始");
        mUserName = meditUserName.getText().toString().trim();
        mPassword = meditPassword.getText().toString().trim();
//        mUserName = "18701137993";
//        mPassword = "liluchang";
        if (mUserName.equals("")) {
            showToast(getResources().getString(R.string.UserName_cannot_null));
            return;
        } else if (mPassword.equals("")) {
            showToast(getResources().getString(R.string.Password_cannot_null));
            return;
        } else if (mPassword.length() < 6) {
            showToast(getResources().getString(R.string.Password_cannot_less6));
            return;
        }

        Log.i(TAG, "login()访问网络前");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("sphone", mUserName);
        map.put("password", mPassword);
        map.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
        getData_new(map, Task.LOGIN);
        Log.i(TAG, "login() 传递完参数"+sha);


    }

    //返回数据操作
    @Override
    public void refresh(Object... param) {
        int type = (Integer) param[0];
        Object obj = (Object) param[1];
        switch (type) {
            case Task.LOGIN: {
                UserInfo userInfo = (UserInfo) obj;
                if (userInfo.getStatus().equals("1") ) {

                mSharedPreferencesEditor.putString("username", mUserName);
                mSharedPreferencesEditor.putString("password", mPassword);
                Const.SNAME = userInfo.getSname();
                // mSharedPreferencesEditor.putString("sname", userInfo.getSname());
                mSharedPreferencesEditor.commit();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
            }
                else {
                    showToast("登录失败请检查服务器！");
                    break;
                }
            }
//                Log.i(TAG,"login接受返回值");
//                UserInfo userInfo = (UserInfo) obj;
//
//
//                if (userInfo.status.equals("1"))
//                {
//                    //将若干信息存放到缓存中
//                    mSharedPreferencesEditor.putString("username", mUserName);
//                    mSharedPreferencesEditor.putString("password",mPassword);
//                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
////                    mSharedPreferencesEditor.putString("userID", userInfo.getUser_id());
////                    Log.i("map3_tag", "LoginActivity存的userid：" + userInfo.getUser_id());
////                    mSharedPreferencesEditor.putString("user_type",userInfo.getType());
////
////                    mSharedPreferencesEditor.putString("city", userInfo.getCity());
////                    mSharedPreferencesEditor.putString("sound_effect", userInfo.getSound_effect());
////                    mSharedPreferencesEditor.putString("music", userInfo.getMusic());
////
////                    String deviceID = Settings.Secure.getString(
////                            LoginActivity.this.getContentResolver(),
////                            Settings.Secure.ANDROID_ID);
////                    mSharedPreferencesEditor.putString("deviceID", deviceID);
////                    mSharedPreferencesEditor.putString("userID", userInfo.getUser_id());
////                    mSharedPreferencesEditor.putString("stu_name", userInfo.getStudent_name());
////                    mSharedPreferencesEditor.putString("stu_phone", userInfo.getStu_phone());
////                    mSharedPreferencesEditor.putString("stu_qq", userInfo.getQq());
////                    mSharedPreferencesEditor.putString("parent_phone", userInfo.getParent_phone());
////                    mSharedPreferencesEditor.commit();
////
////                    String utype=userInfo.getType();
////
////                    if("11".equals(utype)){
////                        String stu_name=mSharedPreferences.getString("stu_name","");
////                        String stu_phone=mSharedPreferences.getString("stu_phone","");
////                        String stu_qq=mSharedPreferences.getString("stu_qq","");
////                        String parent_phone=mSharedPreferences.getString("parent_phone","");
////
////                        if(!"".equals(stu_name)&& !"".equals(stu_phone) && !"".equals(stu_qq) && !"".equals(parent_phone)){
////                            getSid();
////                        }else{
////                            startActivity(new Intent(LoginActivity.this, UserInfoActivity.class));
////                        }
////
////                    }else{
////                        getSid();
////                    }
//                }
//                else
//                {
//
//                    Log.i(TAG,"返回数据失败");
////                    showToast(userInfo.message);
//                }
//
//            }
//            break;
            default:
                break;

        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public InputFilter input = new InputFilter() {
        public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {
            Log.d(TAG, "src:" + src + ";start:" + start + ";end:" + end);
            Log.d(TAG, "dest:" + dst + ";dstart:" + dstart + ";dend:" + dend);
            if (src.length() < 1) {
                return null;
            } else {
                char temp[] = (src.toString()).toCharArray();
                char result[] = new char[temp.length];
                for (int i = 0, j = 0; i < temp.length; i++) {
                    if (temp[i] == ' ') {
                        continue;
                    } else {
                        result[j++] = temp[i];
                    }
                }
                return String.valueOf(result).trim();
            }

        }
    };
}
