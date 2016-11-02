package com.haoweixiu.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.haoweixiu.R;


public class MyProgressDialog extends Dialog {
	private Context context = null;
	private static MyProgressDialog customProgressDialog = null;
	private TextView tvMsg;
	private AnimationDrawable animationDrawable;
	public MyProgressDialog(Context context){
		super(context);
		this.context = context;

	}

	public MyProgressDialog(Context context, int theme) {
		super(context, theme);

	}

	public static MyProgressDialog createDialog(Context context){

		customProgressDialog = new MyProgressDialog(context, R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.myprogressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        customProgressDialog.setCanceledOnTouchOutside(false);
		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus){

		if (customProgressDialog == null){
			return;
		}

		ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
		Log.i("MyProgressDialog","将drawable赋值");
		animationDrawable = (AnimationDrawable) imageView.getBackground();
		animationDrawable.start();
	}

	/**
	 * 
	 * [Summary]
	 *       setTitile 标题
	 * @param strTitle
	 * @return
	 *
	 */
	public MyProgressDialog setTitile(String strTitle){
		return customProgressDialog;
	}


	public void setshowtitle(int visibility){
		//TextView	tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
		tvMsg.setVisibility(visibility);
	}
	/**
	 * 
	 * [Summary]
	 *       setMessage 提示内容
	 * @param strMessage
	 * @return
	 *
	 */
	public MyProgressDialog setMessage(String strMessage){
		//tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
		tvMsg.setText(strMessage);
		return customProgressDialog;
	}

	@Override
	public void dismiss() {
		//防止内存溢出，将drawable置空
		if (animationDrawable!=null){
			Log.i("MyProgressDialog","将drawable置空");
			animationDrawable=null;
		}
		super.dismiss();
	}
}