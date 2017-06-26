package com.pub.Dialog;

import java.util.ArrayList;
import java.util.List;
import com.example.hello1.R;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

public class ValuesPickerDialog extends Dialog {

	private Context mContext = null;
	private TextView mTvTitle = null;
	private NumberPicker mNp = null;
	private NumberPicker mNp2 = null;
	private NumberPicker mNp3 = null;
	private Button mBtnCancle = null;
	private Button mBtnDone = null;
	private List<String> list = new ArrayList<String>();
	private List<String> list2 = new ArrayList<String>();
	private List<String> list3 = new ArrayList<String>();
	private OnValuesPickerListener onValuesPickerListener = null;
	private View view = null;
	static String firstString;
	static String secondString;
	static String thirdString;

	public ValuesPickerDialog(Context context) {
		this(context, 0);
	}

	public ValuesPickerDialog(Context context, int style) {
		super(context, R.style.customDialog);
		this.mContext = context;
		view = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_valuespicker, null);
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
		mTvTitle = (TextView) mView.findViewById(R.id.tv_vp_title);
		mNp = (NumberPicker) mView.findViewById(R.id.np_vp);
		mNp2 = (NumberPicker) mView.findViewById(R.id.np_vp1);
		mNp3 = (NumberPicker) mView.findViewById(R.id.np_vp2);
		mNp2.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				// TODO Auto-generated method stub
				mNp3.setValue(mNp2.getValue());
			}

		});
		mNp3.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				// TODO Auto-generated method stub
				if (mNp3.getValue() < mNp2.getValue())
					mNp3.setValue(mNp2.getValue());
			}

		});
		for (int i = 0; i < mNp.getChildCount(); i++) {
			View view = mNp.getChildAt(i);
			if (view instanceof EditText) {
				((EditText) view).setFocusable(false);
			}
		}
		for (int i = 0; i < mNp.getChildCount(); i++) {
			View view = mNp2.getChildAt(i);
			if (view instanceof EditText) {
				((EditText) view).setFocusable(false);
			}
		}
		for (int i = 0; i < mNp.getChildCount(); i++) {
			View view = mNp3.getChildAt(i);
			if (view instanceof EditText) {
				((EditText) view).setFocusable(false);
			}
		}
		mBtnCancle = (Button) mView.findViewById(R.id.btn_vp_cancle);
		mBtnDone = (Button) mView.findViewById(R.id.btn_vp_ok);
		mBtnCancle.setOnClickListener(onClickListener);
		mBtnDone.setOnClickListener(onClickListener);
	}
	android.view.View.OnClickListener onClickListener = new android.view.View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_vp_cancle) {
				ValuesPickerDialog.this.dismiss();
			} else if (v.getId() == R.id.btn_vp_ok) {
				if (onValuesPickerListener != null) {
					onValuesPickerListener.onPickerValues((mNp
							.getValue()+1)+" "+(mNp2.getValue()+1)+" "+(mNp3.getValue()+1));
				}
				ValuesPickerDialog.this.dismiss();
			}
		}
	};
	/**
	 * 璁剧疆dialog title
	 */
	public void setTitleText(String str) {
		mTvTitle.setText(str);
	}

	/**
	 * 璁剧疆鏄剧ず鏁版嵁
	 * 
	 * @param _list
	 */
	public void setShowValues(List<String> _list) {
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

	public void setShowValues2(List<String> _list1) {
		if ((_list1 == null) || !(_list1.size() > 0)) {
			return;
		}
		String[] strs = new String[_list1.size()];
		Log.i("2222222222222222222", "2222222222222");
		for (int i = 0; i < _list1.size(); i++) {
			list2.add(_list1.get(i));
			strs[i] = _list1.get(i);
		}
		mNp2.setMinValue(0);
		mNp2.setMaxValue(list2.size() - 1);
		mNp2.setDisplayedValues(strs);
	}

	public void setShowValues3(List<String> _list2) {
		if ((_list2 == null) || !(_list2.size() > 0)) {
			return;
		}
		String[] strs = new String[_list2.size()];
		Log.i("333333333333333", "3333333333");
		for (int i = 0; i < _list2.size(); i++) {
			list3.add(_list2.get(i));
			strs[i] = _list2.get(i);
		}
		mNp3.setMinValue(0);
		mNp3.setMaxValue(list3.size() - 1);
		mNp3.setDisplayedValues(strs);
	}

	public void setOnValuesPickerListener(
			OnValuesPickerListener _onValuesPickerListener) {
		this.onValuesPickerListener = _onValuesPickerListener;
	}

	public interface OnValuesPickerListener {
		public void onPickerValues(String value);
	}
}
