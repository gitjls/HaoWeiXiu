package com.haoweixiu.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.haoweixiu.R;


public class UpdateDialogSimple extends Dialog {
	private TextView messageTextView1, messageTextView2, messageTextView3;
	private Button positiveButton, negativeButton;
	private boolean isCanDismissByKeyBack = true;
	private boolean isNeedDismiss = false;

	public UpdateDialogSimple(Context context) {
		super(context, R.style.MyDialog);
		//setContentView(R.layout.update_dialog_simple);


		positiveButton = (Button) findViewById(R.id.dialog_button_ok);
		negativeButton = (Button) findViewById(R.id.dialog_button_cancle);
	}


	public void setPositiveButton(String content, final OnClickListener listener) {
		positiveButton.setVisibility(View.VISIBLE);
		positiveButton.setText(content);
		positiveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				isNeedDismiss = true;
				if (listener != null) {
					listener.onClick(UpdateDialogSimple.this, 1);
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
					listener.onClick(UpdateDialogSimple.this, 3);
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
