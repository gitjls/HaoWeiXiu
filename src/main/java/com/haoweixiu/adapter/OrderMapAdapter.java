package com.haoweixiu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haoweixiu.R;

import java.util.List;
import java.util.Map;

/**
 * Created by jls on 2016/8/22.
 * 订单地图
 */
public class OrderMapAdapter extends BaseAdapter {
    private List<Map> mlistview;
    private Context mcontext;
    private LayoutInflater inflater;

    public OrderMapAdapter(Context context, List<Map> mlistview) {
        super();
        this.mcontext = context;
        this.mlistview = mlistview;

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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.list_complete_item
                    , parent, false);
            viewHolder = new ViewHolder();

            viewHolder.order_number = (TextView) convertView.findViewById(R.id.tv_order_number);
            viewHolder.tv_order_time = (TextView) convertView.findViewById(R.id.tv_order_time);
            viewHolder.tv_phone_type = (TextView) convertView.findViewById(R.id.tv_phone_type);
            viewHolder.tv_ticheng = (TextView) convertView.findViewById(R.id.tv_ticheng);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.order_number.setText( "" + mlistview.get(position).get("complete_order_number"));
        viewHolder.tv_order_time.setText( "" + mlistview.get(position).get("complete_order_time"));
        viewHolder.tv_phone_type.setText( "" + mlistview.get(position).get("complete_phone_type"));
        viewHolder.tv_ticheng.setText( "" + mlistview.get(position).get("complete_tv_ticheng"));
        return convertView;
    } 
    class ViewHolder {
        TextView order_number;
        TextView tv_order_time;
        TextView tv_phone_type;
        TextView tv_ticheng;
    }
}
