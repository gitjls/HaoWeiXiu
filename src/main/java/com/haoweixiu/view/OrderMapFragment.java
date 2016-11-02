package com.haoweixiu.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.haoweixiu.R;

import java.util.List;
import java.util.Map;

/**
 * Created by jls on 2016/8/22.
 */
//订单地图
public class OrderMapFragment extends Fragment {
    private ListView lv_ordermap;
    private View view;
    public List<Map> mobjectList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ordermap, container, false);
//        lv_ordermap = (ListView) view.findViewById(R.id.lv_ordermap);
//        mobjectList = (List<Map>) getArguments().get("completeMapList");
//        OrderMapAdapter orderMapAdapter = new OrderMapAdapter(getActivity(), mobjectList);
//        lv_ordermap.setAdapter(orderMapAdapter);
        return view;

    }


}
