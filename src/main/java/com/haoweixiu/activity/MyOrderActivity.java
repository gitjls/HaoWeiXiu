package com.haoweixiu.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;

import com.haoweixiu.R;
import com.haoweixiu.dto.Fragment_Bundle;
import com.haoweixiu.dto.Order;
import com.haoweixiu.service.Task;
import com.haoweixiu.util.Const;
import com.haoweixiu.util.FragmentCallback;
import com.haoweixiu.util.NetUtil;
import com.haoweixiu.view.CompletedFragment;
import com.haoweixiu.view.ReciveOrderFragment;
import com.haoweixiu.view.UnReciveOrderFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyOrderActivity extends BaseActivity implements View.OnClickListener, FragmentCallback, PopupMenu.OnMenuItemClickListener {
    private ListView lv_myorder;
    private View view;
    public ArrayList<Order> orderArrayList;
    private UnReciveOrderFragment unReciveOrderFragment;
    private ReciveOrderFragment reciveOrderFragment;
    private CompletedFragment completedFragment;
    private List<Fragment> fragmentList;
    private Fragment_Bundle fragment_bundle;
    private RadioButton tv_unFinish_order, tv_cancle_order, tv_complete_order, tv_ordered;
    private String sname;
    private Button btn_recived_before, btn_recived_next, btn_complete_before, btn_complete_next;
    private int page, recived_size, finish_size;
    private int whichModel, pagesize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        mSharedPreferences = this.getSharedPreferences("user_login_setting", MODE_PRIVATE);
        mSharedPreferencesEditor = mSharedPreferences.edit();
        sname = Const.SNAME;
        tv_unFinish_order = (RadioButton) findViewById(R.id.tv_unRecive_order);
        tv_cancle_order = (RadioButton) findViewById(R.id.tv_recive_order);
        tv_complete_order = (RadioButton) findViewById(R.id.tv_complete_order);
        tv_ordered = (RadioButton) findViewById(R.id.tv_ordered);
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void initData() {
        fragment_bundle = new Fragment_Bundle();
        fragmentList = new ArrayList<Fragment>();
        //当前页数
        page = 1;
        //每页显示的数目
        pagesize = 10;
        //已接受的订单的页数 默认0
        recived_size = 0;
        //已完成的订单的页数 默认0
        finish_size = 0;
        setDefaultFragment();
    }

    private void setDefaultFragment() {

        if (unReciveOrderFragment == null) {
            unReciveOrderFragment = new UnReciveOrderFragment();
        }
        whichModel = Task.MyOrder;
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        HashMap<String, String> maps = new HashMap<>();
        maps.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
        maps.put("page", "" + page);
        // maps.put("sname", sname);
        Log.i("getData", "===defaultfrag" + maps);
        getData_new(maps, Task.MyOrder);
        tv_unFinish_order.setChecked(true);
        fragmentList.add(unReciveOrderFragment);
        transaction.replace(R.id.id_content, unReciveOrderFragment);

    }

    @Override
    public void refresh(Object... param) {
        Log.i("==顺序==", "refresh");
        int type = (Integer) param[0];
        Object obj = param[1];
        FragmentManager fm = getFragmentManager();
        ArrayList<Order> orderList = new ArrayList<Order>();
        FragmentTransaction transactions = fm.beginTransaction();
        if (obj == null)
            return;
        switch (type) {
            //未接单
            case Task.MyOrder:
                hideOtherFragment(transactions, unReciveOrderFragment);
                if (!unReciveOrderFragment.isAdded()) {
                    transactions.add(R.id.id_content, unReciveOrderFragment);
                }
//                    transactions.commit();
                orderList = (ArrayList<Order>) obj;
                //tv_unFinish_order.setText("未接单(" + orderList.size() + ")");
                tv_unFinish_order.setText("未接单");
                unReciveOrderFragment.orderArrayList = orderList;

//                Bundle bundle = new Bundle();
//                fragment_bundle.setOrderList(orderList);
//                bundle.putSerializable("OrderList", fragment_bundle.getOrderList());
//                unReciveOrderFragment.setArguments(bundle);
                break;
            //已接单
            case Task.RECIVED:
                hideOtherFragment(transactions, reciveOrderFragment);
                if (!reciveOrderFragment.isAdded())
                    transactions.add(R.id.id_content, reciveOrderFragment);
//                    transactions.commit();
                orderList = (ArrayList<Order>) obj;
                if (recived_size == 0) {
                    recived_size = (int) (orderList.size() / pagesize) + 1;
                }
                tv_cancle_order.setText("已接单");
                //不采用setAgrument的方式传参
//                Bundle bundles = new Bundle();
//                fragment_bundle.setOrderList(orderList);
                // if()
                reciveOrderFragment.orderArrayList = orderList;
//                bundles.putSerializable("OrderList", fragment_bundle.getOrderList());
//                reciveOrderFragment.setArguments(bundles);transactions.commit();
                break;
            case Task.COMPLETE_ORDER:
                hideOtherFragment(transactions, completedFragment);
                if (!completedFragment.isAdded())
                    transactions.add(R.id.id_content, completedFragment);
//                    transactions.commit();
                orderList = (ArrayList<Order>) obj;
                if (finish_size == 0) {
                    finish_size = (int) (orderList.size() / pagesize) + 1;
                }
                tv_complete_order.setText("已完成单");
                completedFragment.orderArrayList = orderList;
//                Bundle bundle1 = new Bundle();
//                fragment_bundle.setOrderList(orderList);
//                bundle1.putSerializable("OrderList", fragment_bundle.getOrderList());
//                completedFragment.setArguments(bundle1);transactions.commit();
                break;
            default:
                break;
        }
        if (progressDialog != null && progressDialog.isShowing()) {

            progressDialog.dismiss();
        }
        transactions.commit();
    }

    @Override
    public void onClick(View v) {
        if (!NetUtil.isNetworking(mContext)) {
            Log.i("MY", "网络异常");
            showToast(getResources().getString(R.string.NoInternet));
            return;
        }
        FragmentManager fragmentManagerm = getFragmentManager();
        FragmentTransaction transactions = fragmentManagerm.beginTransaction();
        int checkedId = v.getId();
        Log.i("aaa", "checkedId" + checkedId);
        switch (checkedId) {
            //未接订单
            case R.id.tv_unRecive_order:
                whichModel = Task.MyOrder;
                if (unReciveOrderFragment == null) {
                    unReciveOrderFragment = new UnReciveOrderFragment();
                }
                if (fragmentList.contains(unReciveOrderFragment)) {
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction transaction = fm.beginTransaction();
                    hideOtherFragment(transactions, unReciveOrderFragment);
                    Log.i("aaa", "showfinishOrderFragment");
                    transactions.show(unReciveOrderFragment);
                    transactions.commit();
                } else {
                    HashMap<String, String> maps = new HashMap<>();
                    maps.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
                    maps.put("page", "" + page);
//                    maps.put("sname", sname);
                    Log.i("getData", "onclick===MyOrder" + maps);
                    Log.i("aaa", "showfinishOrderFragment");
                    getData_new(maps, Task.MyOrder);

                }
                fragmentList.add(unReciveOrderFragment);
                break;
            //已接订单
            case R.id.tv_recive_order:
                whichModel = Task.RECIVED;
                if (reciveOrderFragment == null) {
                    reciveOrderFragment = new ReciveOrderFragment();
                }
                if (fragmentList.contains(reciveOrderFragment)) {
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction transaction = fm.beginTransaction();
                    Log.i("aaa", "showunfinishOrderFragment");
                    hideOtherFragment(transactions, reciveOrderFragment);
                    transactions.show(reciveOrderFragment);
                    transactions.commit();
                } else {
                    HashMap<String, String> maps = new HashMap<>();
                    maps.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
                    maps.put("page", "" + page);
                    maps.put("sname", sname);
                    Log.i("getData", "onclick===UNFINISH" + maps);
                    getData_new(maps, Task.RECIVED);

                }
                fragmentList.add(reciveOrderFragment);
                break;
            //已完成订单列表
            case R.id.tv_complete_order:
                whichModel = Task.COMPLETE_ORDER;
                if (completedFragment == null) {
                    completedFragment = new CompletedFragment();
                }
                if (fragmentList.contains(completedFragment)) {
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction transaction = fm.beginTransaction();
                    Log.i("aaa", "showcompletedFragment");
                    hideOtherFragment(transactions, completedFragment);
                    transactions.show(completedFragment);
                    transactions.commit();
                } else {
                    HashMap<String, String> maps = new HashMap<>();
                    maps.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
                    maps.put("page", "" + page);
                    maps.put("sname", sname);
                    Log.i("getData", "onclick===COMPLETE" + maps);
                    getData_new(maps, Task.COMPLETE_ORDER);

                }
                fragmentList.add(completedFragment);
                break;
            //已接订单
            case R.id.btn_recived_before:
                page--;
                if (page == 1) {
                    v.setVisibility(View.GONE);
                }
//                if(page==0)
//                {
//                    showToast("当前已是第一页");
//                }
//
                HashMap<String, String> maps = new HashMap<>();
                maps.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
                maps.put("page", page + "");
                maps.put("sname", sname);
                Log.i("getData", "onclick===whichModel已接单before" + maps);
                getData_new(maps, whichModel);
                break;
            case R.id.btn_recived_next:
                page++;
                v.setVisibility(View.VISIBLE);
                if (page == recived_size) {
                    v.setVisibility(View.GONE);
                }
                HashMap<String, String> mapss = new HashMap<>();
                mapss.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
                mapss.put("page", "" + page);
                mapss.put("sname", sname);
                Log.i("getData", "onclick===whichModel已接单next" + mapss);
                getData_new(mapss, whichModel);
                break;
            //完成订单
            case R.id.btn_complete_before:
                page--;
                //点完上一页之后是第一页 就不显示上一页按钮
                if (page == 1) {
                    v.setVisibility(View.GONE);
                }
                HashMap<String, String> map2 = new HashMap<>();
                map2.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
                map2.put("page", page + "");
                map2.put("sname", sname);
                Log.i("getData", "onclick===whichModel已完成before" + whichModel + map2);
                getData_new(map2, whichModel);
                break;
            case R.id.btn_complete_next:
                page++;
                v.setVisibility(View.VISIBLE);
                //点完下一页之后是第一页 就不显示下一页按钮
                if (page == finish_size) {
                    v.setVisibility(View.GONE);
                }
                HashMap<String, String> map1 = new HashMap<>();
                map1.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
                map1.put("page", "" + page);
                map1.put("sname", sname);
                Log.i("getData", "onclick===whichModel已完成next" + map1);
                getData_new(map1, whichModel);
                break;
            case R.id.btn_addorder:
                showPopMenu(v);

                break;
            case R.id.btn_back:
                finish();
                break;
            default:
                break;

        }
    }

    public void showPopMenu(View view) {
        Menu menu;
        //需要改样式自定义一个
        PopupMenu popupMenu = new PopupMenu(this, view);
        menu = popupMenu.getMenu();
//        // 通过代码添加菜单项
//        menu.add(Menu.NONE, Menu.FIRST + 0, 0, "复制");
//        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "粘贴");

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    //隐藏掉其他的碎片
    private void hideOtherFragment(FragmentTransaction transaction, Fragment fragment) {

        for (int i = 0; i < fragmentList.size(); i++) {
            if (fragmentList.get(i).equals(fragment)) {
                continue;
            }
            transaction.hide(fragmentList.get(i));
        }
    }

    @Override
    public void onclickCallback(Object... param) {
        int page = (int) param[0];
        whichModel = (int) param[1];
        HashMap<String, String> maps = new HashMap<>();
        maps.put("keys", "djlkfjdaiomluaid;f2192.830mjdCJAJPOJDA");
        maps.put("page", page + "");
        //maps.put("sname", sname);
        getData_new(maps, whichModel);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group_item1:
                Intent intents = new Intent(this, AddOrderActivity.class);
                startActivity(intents);
                break;
            case R.id.group_item2:
                showToast("item2");
                break;
            default:

                Intent intent = new Intent(MyOrderActivity.this, AddOrderActivity.class);
                startActivity(intent);
                break;
        }
        return false;
    }
}
