package com.haoweixiu.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.haoweixiu.R;
import com.haoweixiu.activity.MyOrderActivity;
import com.haoweixiu.activity.UnReciveOrderDetailActivity;
import com.haoweixiu.adapter.UnfinishOrderAdapter;
import com.haoweixiu.dto.Order;
import com.haoweixiu.service.MainService;
import com.haoweixiu.service.Task;
import com.haoweixiu.util.FragmentCallback;
import com.haoweixiu.util.TimeRender;

import java.util.ArrayList;

/**
 * Created by jls on 2016/8/31.
 */
//未接的订单
public class UnReciveOrderFragment extends Fragment implements AdapterView.OnItemClickListener, XListView.IXListViewListener {
    public XListView xlv_myorder;
    private View view;
    FragmentCallback fragmentCallback;
    UnfinishOrderAdapter finishOrderAdapter;
    public static ArrayList<Order> orderArrayList;

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("==顺序==", "onfragmentPause");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("==顺序==", "onfragmentDestroyView");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("==顺序==", "onfragmentStop");
    }

    @Override
    public void onRefresh() {
        fragmentCallback = (FragmentCallback) MainService.getActivityByName("MyOrderActivity");
        Log.i("==顺序==", "onfragmentRefresh");
        fragmentCallback.onclickCallback(1, Task.MyOrder);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("==顺序==", "onfragmentcallBack");
                finishOrderAdapter = new UnfinishOrderAdapter(getActivity(), orderArrayList);
                xlv_myorder.setAdapter(finishOrderAdapter);
                onLoad();
                finishOrderAdapter.notifyDataSetChanged();
            }
        }, 2000);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("==顺序==", "onframgmentCreateView");
        view = inflater.inflate(R.layout.unrecivedorderfragment, container, false);
//        orderArrayList = (ArrayList<Order>) getArguments().get("OrderList");
        xlv_myorder = (XListView) view.findViewById(R.id.lv_finishorder);
        xlv_myorder.setPullLoadEnable(false);
        finishOrderAdapter = new UnfinishOrderAdapter(getActivity(), orderArrayList);
        xlv_myorder.setAdapter(finishOrderAdapter);
        xlv_myorder.setXListViewListener(this);
        xlv_myorder.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("==顺序==", "onfragmentActivityCreated");
    }

    private void onLoad() {
        try {
            xlv_myorder.stopRefresh();
            xlv_myorder.stopLoadMore();
            xlv_myorder.setRefreshTime(TimeRender.getDate().substring(11));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), UnReciveOrderDetailActivity.class);
        intent.putExtra("did", orderArrayList.get(position-1).getDid());
        intent.putExtra("activity", "UnReciveOrderFragment");
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("==顺序==", "onfragmentResume");

    }

}
