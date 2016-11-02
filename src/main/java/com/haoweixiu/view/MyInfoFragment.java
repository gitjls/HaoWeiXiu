package com.haoweixiu.view;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haoweixiu.R;
import com.haoweixiu.util.Const;

/**
 * Created by jls on 2016/8/31.
 */
public class MyInfoFragment extends Fragment{
    private View view;
    private TextView tv_name;
    private TextView tv_phone;
private SharedPreferences mSharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_myinfo, container, false);
        tv_name=(TextView)view.findViewById(R.id.tv_name);
        tv_phone=(TextView)view.findViewById(R.id.tv_phone);
        mSharedPreferences = getActivity().getSharedPreferences("user_login_setting", Context.MODE_PRIVATE);
        tv_name.setText(Const.SNAME);
        tv_phone.setText(mSharedPreferences.getString("password","1234"));
        return view;
    }
}
