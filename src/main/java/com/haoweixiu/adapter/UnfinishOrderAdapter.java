package com.haoweixiu.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haoweixiu.R;
import com.haoweixiu.dto.Order;

import java.util.ArrayList;

/**
 * 个人中心
 * Created by jls on 2016/8/22.
 */
public class UnfinishOrderAdapter extends BaseAdapter {
    private ArrayList<Order> mlistview;
    private Context mcontext;

    public UnfinishOrderAdapter(Context contex, ArrayList<Order> list) {
        super();
        this.mcontext = contex;
        this.mlistview = list;
    }

    @Override
    public boolean hasStableIds() {
        return super.hasStableIds();
    }

    @Override
    public int getCount() {
        return mlistview.size();
    }

    @Override
    public Object getItem(int position) {
        return mlistview.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.list_order_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tv_order_number = (TextView) convertView.findViewById(R.id.tv_order_number);
            viewHolder.tv_order_time = (TextView) convertView.findViewById(R.id.tv_order_time);
            viewHolder.tv_custom_number = (TextView) convertView.findViewById(R.id.tv_custom_number);
            viewHolder.tv_custom_name = (TextView) convertView.findViewById(R.id.tv_custom_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_order_number.setText(Html.fromHtml("<font color=black>订单号: </font>" + mlistview.get(position).getDid()));
        viewHolder.tv_order_time.setText(Html.fromHtml("<font color=black>订单时间: </font>" + mlistview.get(position).getTime()));
        viewHolder.tv_custom_number.setText(Html.fromHtml("<font color=black>客户电话: </font>" + mlistview.get(position).getPhone()));
        viewHolder.tv_custom_name.setText(Html.fromHtml("<font color=black>客户姓名: </font>" + mlistview.get(position).getDname()));
        return convertView;
    }

    class ViewHolder {
        //订单号、订单时间、客户电话、客户姓名
        TextView tv_order_number;
        TextView tv_order_time;
        TextView tv_custom_number;
        TextView tv_custom_name;
    }
}
