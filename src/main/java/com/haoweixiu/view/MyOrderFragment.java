package com.haoweixiu.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.haoweixiu.R;
import com.haoweixiu.activity.UnReciveOrderDetailActivity;
import com.haoweixiu.adapter.MyOrderAdapter;
import com.haoweixiu.dto.Order;

import java.util.ArrayList;

/**
 * Created by jls on 2016/8/31.
 */
//我的订单
public class MyOrderFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView lv_myorder;
    private View view;
    public ArrayList<Order> orderArrayList;
    private RadioButton tv_unFinish_order, tv_cancle_order, tv_complete_order, tv_ordered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("==顺序==", "onCreateView");
        view = inflater.inflate(R.layout.finishorderfragment, container, false);

//        tv_unFinish_order = (RadioButton) view.findViewById(R.id.tv_unFinish_order);
//        tv_cancle_order = (RadioButton) view.findViewById(R.id.tv_cancle_order);
        tv_complete_order = (RadioButton) view.findViewById(R.id.tv_complete_order);
        tv_ordered = (RadioButton) view.findViewById(R.id.tv_ordered);
        lv_myorder = (ListView) view.findViewById(R.id.lv_finishorder);
        orderArrayList = (ArrayList<Order>) getArguments().get("OrderList");
        MyOrderAdapter myOrderAdapter = new MyOrderAdapter(getActivity(), orderArrayList);
        lv_myorder.setAdapter(myOrderAdapter);
        lv_myorder.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("==顺序==", "onActivityCreated");


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), UnReciveOrderDetailActivity.class);
        intent.putExtra("did", orderArrayList.get(position).getDid());
        intent.putExtra("activity", "MyOrderActivity");
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("==顺序==", "onResume");

    }

}
