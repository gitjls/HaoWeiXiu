package com.haoweixiu.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haoweixiu.R;


public class MyDialog extends Dialog {
	TextView  messageTextView;
	Button positiveButton, negativeButton;
	boolean isCanDismissByKeyBack = true;
	boolean isNeedDismiss = false;

	public MyDialog(Context context) {
		super(context, R.style.MyDialog);
		setContentView(R.layout.dialog);

		//titleTextView = (TextView) findViewById(R.id.title);
		messageTextView = (TextView) findViewById(R.id.textmessage);
		negativeButton= (Button) findViewById(R.id.dialog_button_cancle);
        positiveButton = (Button) findViewById(R.id.dialog_button_ok);
	}

	public void setTitle(int titleId) {
		setTitle(getContext().getString(titleId));
	}

//	public void setTitle(CharSequence title) {
//		titleTextView.setText(title);
//	}

	public void setMessage(int messageId) {
		setMessage(getContext().getString(messageId));
	}

	public void setMessage(CharSequence message) {
		messageTextView.setText(message);
	}

	public void setMessageSize(Float Size) {
		messageTextView.setTextSize(Size);
	}

	public void setPositiveButton(String content, final OnClickListener listener) {
		positiveButton.setVisibility(View.VISIBLE);
		//positiveButton.setText(content);
		positiveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				isNeedDismiss = true;
				if (listener != null) {
					listener.onClick(MyDialog.this, 1);
				}
				if (isNeedDismiss)
					dismiss();
			}
		});
	}

	public void setNegativeButton(String content, final OnClickListener listener) {
		negativeButton.setVisibility(View.VISIBLE);
		//negativeButton.setText(content);
		negativeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isNeedDismiss = true;
				if (listener != null) {
					listener.onClick(MyDialog.this, 3);
				}
				if (isNeedDismiss)
					dismiss();
			}
		});
	}

	public void setCanDismissByKeyBack(boolean isCanDismissByKeyBack) {
		this.isCanDismissByKeyBack = isCanDismissByKeyBack;
	}

	public void setIsNeedDismiss(boolean isNeedDismiss) {
		this.isNeedDismiss = isNeedDismiss;
	}

    public void setNegativeButtonGone(){
        negativeButton.setVisibility(View.GONE);
    }
    public void setPositiveButtonGone(){
        positiveButton.setVisibility(View.GONE);
    }
}
