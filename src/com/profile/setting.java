package com.profile;

import java.io.File;
import java.text.DecimalFormat;
import com.Login.Util.Utils;
import com.api.onekeyshare.OnekeyShare;
import com.example.hello1.R;
import com.exit.AppExit;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;

public class setting extends Activity implements OnClickListener{
    private ImageView back;
    private TextView title,version,cache;
    protected Context mContext;
    private ViewStub cacheViewStub;
    /**设置保存路径*/
    private static final String SAVE_PIC_PATH=Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath():"/tq/sdcard";//保存到SD卡
    private static final String SAVE_REAL_PATH = SAVE_PIC_PATH+"/SmartLand/savePic/";//保存的确切位置
    private static final String HZ_REAL_PATH = SAVE_PIC_PATH+"/SmartLand/saveHZ/";//保存的确切位置
    private static final String PICASSO_CACHE = "picasso-cache";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.user_setting);
	    AppExit.getInstance().addActivity(this);
		init();
	}
	private void init() {
		
		back=(ImageView)findViewById(R.id.img_back);
		title=(TextView)findViewById(R.id.txt_title);
		version=(TextView)findViewById(R.id.version);
		mContext = this;
		cache=(TextView)findViewById(R.id.cache);
		
		back.setVisibility(View.VISIBLE);
		title.setText("设置");
		version.setText("当前版本V" + GetVersion(mContext));
		back.setOnClickListener(this);
	}
	
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.setting_share:
//			Intent inte = new Intent(Intent.ACTION_SEND);    
//			inte.setType("image/*");    
//            inte.putExtra(Intent.EXTRA_SUBJECT, "Share");    
//            inte.putExtra(Intent.EXTRA_TEXT,  "I would like to share this with you...");    
//            inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
//            startActivity(inte);
			showShare();
			break;
		case R.id.setting_clean:
			if ( cacheViewStub==null ) {
				cacheViewStub = (ViewStub) this.findViewById(R.id.cache_view);
				View inflate = cacheViewStub.inflate();
				TextView ok = (TextView) inflate.findViewById(R.id.ok_button);
				TextView cancel = (TextView) inflate.findViewById(R.id.cancle_button);
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 清除缓存图片代码 
						File file = createDefaultCacheDir(mContext);
						delAllFile(file.getPath());
						cache.setText(getDiskCacheSize(mContext));
						cacheViewStub.setVisibility(View.GONE);
					}
				});
				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						cacheViewStub.setVisibility(View.GONE);
					}
				});
			}
			cacheViewStub.setVisibility(View.VISIBLE);
			break;
		case R.id.setting_check_update:
			Utils.showLongToast(setting.this, "已更新到最新版本！");
			break;
		case R.id.setting_rule:
			Intent intent = new Intent(setting.this, Rule.class);
			startActivity(intent);
			break;
		case R.id.setting_about:
			Intent intent2 = new Intent(setting.this, about.class);
			startActivity(intent2);
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			 Intent intent =new Intent(setting.this, User.class);
			 startActivity(intent);
			break;

		default:
			break;
		}
	}
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize(); 

		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
		oks.setTitle("标题");
		// titleUrl是标题的网络链接，QQ和QQ空间等使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(SAVE_REAL_PATH+"head.png");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
		}
	 /**
     *  取得版本号
     * @param context
     * @return
     */
    public static String GetVersion(Context context) {
		try {
			PackageInfo manager = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return manager.versionName;
		} catch (NameNotFoundException e) { 
			return "Unknown";  
		} 
	}
    public static File createDefaultCacheDir(Context context) {
		File cache = new File(HZ_REAL_PATH,PICASSO_CACHE);
		if (!cache.exists()) {
			cache.mkdirs();
		}
		return cache;
	}
 // 获取图片缓存大小
 	public static String getDiskCacheSize(Context context) {
 		int sizeSum = 0;
 		File diskfile = createDefaultCacheDir(context);
 		String[] diskfileList = diskfile.list();
 		for (int i = 0; i < diskfileList.length; i++) {
 			File filesize = new File(diskfile + "/" + diskfileList[i]);
 			sizeSum = sizeSum + (int) filesize.length();
 			System.out.println(sizeSum);
 		}
 		DecimalFormat df = new DecimalFormat(".00");
 		double sizeSumKB = sizeSum / 1024;
 		if (sizeSumKB < 1) {
 			return String.valueOf(sizeSum) + "B";
 		} else {
 			double sizeSumMB = sizeSumKB / 1024;
 			if (sizeSumMB < 1) {
 				return String.valueOf((int) sizeSumKB) + "KB";
 			} else {
 				return String.valueOf(df.format(sizeSumMB)) + "MB";
 			}
 		}
 	}
 	
 	/**
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path 文件夹完整绝对路径
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);		// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);		// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 删除文件夹
	 * 
	 * @param folderPath  文件夹完整绝对路径
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); 		// 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); 		// 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
