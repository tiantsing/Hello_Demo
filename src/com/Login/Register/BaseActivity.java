package com.Login.Register;

import org.apache.http.message.BasicNameValuePair;
import com.Login.Util.Utils;
import android.app.Activity;
import android.os.Bundle;


@SuppressWarnings("deprecation")
public abstract class BaseActivity extends Activity {
	protected Activity context;
	//protected NetClient netClient;
	//protected FlippingLoadingDialog mLoadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		initControl();
		initView();
		initData();
		setListener();
	}
	/**
	 * 绑定控件id
	 */
	protected abstract void initControl();

	/**
	 * 初始化控件
	 */
	protected abstract void initView();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 设置监听
	 */
	protected abstract void setListener();

	/**
	 * 打开 Activity
	 * 
	 * @param activity
	 * @param cls
	 * @param name
	 */
	public void start_Activity(Activity activity, Class<?> cls,
			@SuppressWarnings("deprecation") BasicNameValuePair... name) {
		Utils.start_Activity(activity, cls, name);
	}

	/**
	 * 关闭 Activity
	 * 
	 * @param activity
	 */
	public void finish(Activity activity) {
		Utils.finish(activity);
	}
}
