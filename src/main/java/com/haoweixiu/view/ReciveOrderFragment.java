package com.haoweixiu.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.haoweixiu.R;
import com.haoweixiu.activity.ReciveOrderDetailActivity;
import com.haoweixiu.adapter.FinishOrderAdapter;
import com.haoweixiu.dto.Order;
import com.haoweixiu.service.Task;
import com.haoweixiu.util.FragmentCallback;

import java.util.ArrayList;

/**
 * Created by jls on 2016/8/31.
 */
//已接订单
public class ReciveOrderFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView lv_myorder;
    private View view;
    //当前页数
    private int page=1;
    //总页数
    private int pageCount = 1;
    public static ArrayList<Order> orderArrayList;
    FragmentCallback fragmentCallback;
    private Button btn_recived_before, btn_recived_next;

    @Override
    public void onClick(View v) {
        int type=v.getId();
        switch (type)
        {
            case R.id.btn_complete_before:
                page--;
                fragmentCallback.onclickCallback(page, Task.COMPLETE_ORDER);
                break;
            case R.id.btn_complete_next:
                page++;
                fragmentCallback.onclickCallback(page,Task.COMPLETE_ORDER);
                break;
        }
        fragmentCallback.onclickCallback(page);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("==顺序==", "onCreateView");
        view = inflater.inflate(R.layout.recivedhorderfragment, container, false);
        btn_recived_before = (Button) view.findViewById(R.id.btn_recived_before);
        btn_recived_before.setOnClickListener(this);
        btn_recived_next = (Button) view.findViewById(R.id.btn_recived_next);
        btn_recived_next.setOnClickListener(this);

        btn_recived_before.setVisibility(View.GONE);
        if (pageCount == 1) {
            btn_recived_next.setVisibility(View.GONE);
        }
        lv_myorder = (ListView) view.findViewById(R.id.lv_unfinish);
        //orderArrayList = (ArrayList<Order>) getArguments().get("OrderList");
        FinishOrderAdapter myOrderAdapter = new FinishOrderAdapter(getActivity(), orderArrayList);
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
        Intent intent = new Intent(getActivity(), ReciveOrderDetailActivity.class);
        intent.putExtra("did", orderArrayList.get(position).getDid());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("==顺序==", "onResume");

    }

}
