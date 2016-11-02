package com.haoweixiu.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haoweixiu.R;


public class UpdateDialog extends Dialog {
	private TextView messageTextView1, messageTextView2, messageTextView3;
	private Button positiveButton, negativeButton;
	private boolean isCanDismissByKeyBack = true;
	private boolean isNeedDismiss = false;

	public UpdateDialog(Context context) {
		super(context, R.style.MyDialog);
		setContentView(R.layout.update_dialog);

		messageTextView1 = (TextView) findViewById(R.id.textmessage1);
		messageTextView2 = (TextView) findViewById(R.id.textmessage2);
		messageTextView3 = (TextView) findViewById(R.id.textmessage3);
		positiveButton = (Button) findViewById(R.id.dialog_button_cancle);
		negativeButton = (Button) findViewById(R.id.dialog_button_ok);
	}

	public void setMessage1(int messageId) {
		setMessage1(getContext().getString(messageId));
	}

	public void setMessage1(CharSequence message) {
		messageTextView1.setText(message);
	}

	public void setMessage2(int messageId) {
		setMessage2(getContext().getString(messageId));
	}

	public void setMessage2(CharSequence message) {
		messageTextView2.setText(message);
	}

	public void setMessage3(int messageId) {
		setMessage3(getContext().getString(messageId));
	}

	public void setMessage3(CharSequence message) {
		messageTextView3.setText(message);
	}

	public void setPositiveButton(String content, final OnClickListener listener) {
		positiveButton.setVisibility(View.VISIBLE);
		positiveButton.setText(content);
		positiveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				isNeedDismiss = true;
				if (listener != null) {
					listener.onClick(UpdateDialog.this, 1);
				}
				if (isNeedDismiss)
					dismiss();
			}
		});
	}

	public void setNegativeButton(String content, final OnClickListener listener) {
		negativeButton.setVisibility(View.VISIBLE);
		negativeButton.setText(content);
		negativeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				isNeedDismiss = true;
				if (listener != null) {
					listener.onClick(UpdateDialog.this, 3);
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

}
