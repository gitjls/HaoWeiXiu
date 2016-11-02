package com.haoweixiu.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.haoweixiu.R;
import com.haoweixiu.util.ParamsUtil;

import java.util.ArrayList;

/**
 * 
 * 弹出菜单
 * 
 * @author 添加菜单
 *
 */
public class PopMenu implements OnItemClickListener {
	
	public interface OnItemClickListener {
		public void onItemClick(int position);
	}

	private ArrayList<String> itemList;
	private Context context;
	private PopupWindow popupWindow;
	private ListView listView;
	private OnItemClickListener listener;
	private int checkedPosition;
    private static int whichPop=1;//记录选择的位置
	RelativeLayout view;


	public PopMenu(Context context, int resid, int checkedPosition) {
		this.context = context;
		this.checkedPosition = checkedPosition;

		itemList = new ArrayList<String>();
		view = new RelativeLayout(context);
		view.setBackgroundResource(resid);

		listView = new ListView(context);
		listView.setPadding(0, ParamsUtil.dpToPx(context, 3), 0, ParamsUtil.dpToPx(context, 3));
		view.addView(listView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		listView.setAdapter(new PopAdapter());
		listView.setOnItemClickListener(this);

		popupWindow = new PopupWindow(view, context.getResources().getDimensionPixelSize(R.dimen.popmenu_width),context.getResources().getDimensionPixelSize(R.dimen.popmenu_height));
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (listener != null) {
			listener.onItemClick(position);
            whichPop=position;
			listView.invalidate();
		}
		
		dismiss();
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	public void addItems(String[] items) {
		for (String s : items)
			itemList.add(s);
	}
	
	public void addItem(String item) {
		itemList.add(item);
	}
	public void showAtLocation(View v){
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.update();
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]-popupWindow.getHeight());

	}

	public void dismiss() {
		popupWindow.dismiss();
	}

	private final class PopAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return itemList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			RelativeLayout layoutView = new RelativeLayout(context);
			TextView textView = new TextView(context);
			textView.setTextSize(16);
			textView.setText(itemList.get(position));
			textView.setTextColor(Color.WHITE);
			textView.setTag(position);
			
			if (whichPop == position) {
				layoutView.setBackgroundColor(0x8033B5E5);
			}
			
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			layoutView.addView(textView, params);
			layoutView.setMinimumHeight(ParamsUtil.dpToPx(context, 26));
			return layoutView;

		}

	}
}
