package com.haoweixiu.view;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.haoweixiu.R;
import com.haoweixiu.adapter.HomePageAdapter;
import com.haoweixiu.util.TimeRender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jls on 2016/8/31.
 */
//首页
public class HomePageFragment extends Fragment {

    private ListView lv_homepage;
    private View view;
    private List<Map> mapList;
    private TextView tv_current_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage, container, false);
        lv_homepage = (ListView) view.findViewById(R.id.lv_homepage);
        tv_current_time = (TextView) view.findViewById(R.id.tv_current_time);
        tv_current_time.setText(TimeRender.getDate());
       /* mapList=(List<Map>) getArguments().get("unFinishMapList");*/
        mapList = new ArrayList<Map>();
        for (int i = 0; i < 5; i++) {
            Map map = new HashMap();
            map.put("tvcname", "cname");
            map.put("tvphoneNumber", "188");
            map.put("tvaddress", "address");
            map.put("tv_type", "type");
            mapList.add(map);
        }
        HomePageAdapter homePageAdapter = new HomePageAdapter(getActivity(), mapList);
        lv_homepage.setAdapter(homePageAdapter);
        return view;

    }

}
