package com.pub.Dialog;

import java.util.ArrayList;
import java.util.List;
import com.example.hello1.R;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MyNumberPiackerDialog extends Dialog {
	private Context mContext = null;
	private TextView mTvTitle = null;
	private NumberPicker mNp = null;
	private Button mBtnCancle = null;
	private Button mBtnDone = null;
	private List<String> list = new ArrayList<String>();
	private OnValuesPickerListener onValuesPickerListener = null;
	private View view = null;
	public MyNumberPiackerDialog(Context context) {
		this(context, 0);
		// TODO Auto-generated constructor stub
	}

	public MyNumberPiackerDialog(Context context,int style) {
		super(context, R.style.customDialog);
		this.mContext = context;
		view = LayoutInflater.from(mContext).inflate(R.layout.dialog_numpicker_layout, null);
		initView(view);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(view);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		Resources resources = mContext.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		params.height = (int) (dm.heightPixels * 0.5);
		params.width = (int) (dm.widthPixels * 0.9);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
	}

	private void initView(View mView) {
		mTvTitle = (TextView) mView.findViewById(R.id.mynp_title);
		mNp = (NumberPicker) mView.findViewById(R.id.mynp_vp);

		for (int i = 0; i < mNp.getChildCount(); i++) {
			View view = mNp.getChildAt(i);
			if (view instanceof EditText) {
				((EditText) view).setFocusable(false);
			}
		}
		mBtnCancle = (Button) mView.findViewById(R.id.btn_mynp_cancle);
		mBtnDone = (Button) mView.findViewById(R.id.btn_mynp_ok);
		mBtnCancle.setOnClickListener(onClickListener);
		mBtnDone.setOnClickListener(onClickListener);
	}

	android.view.View.OnClickListener onClickListener = new android.view.View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_mynp_cancle) {
				MyNumberPiackerDialog.this.dismiss();
			} else if (v.getId() == R.id.btn_mynp_ok) {
				if (onValuesPickerListener != null) {
					onValuesPickerListener.onPickerValues((mNp.getValue() + 1 ));
				}
				MyNumberPiackerDialog.this.dismiss();
			}
		}
	};

	public  void setShowValues(List<String> _list) {
		if ((_list == null) || !(_list.size() > 0)) {
			return;
		}
		String[] strs = new String[_list.size()];
		for (int i = 0; i < _list.size(); i++) {
			list.add(_list.get(i));
			strs[i] = _list.get(i);
		}
		mNp.setMinValue(0);
		mNp.setMaxValue(list.size() - 1);
		mNp.setDisplayedValues(strs);
	}

	public void setTitleText(String str) {
		mTvTitle.setText(str);
	}

	public interface OnValuesPickerListener {
		public void onPickerValues(int value);
	}

	public void setOnValuesPickerListener(OnValuesPickerListener onValuesPickerListener2) {
		// TODO Auto-generated method stub
		this.onValuesPickerListener = onValuesPickerListener2;
	}
}
