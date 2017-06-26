package com.app.main;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Environment;
import android.util.Log;


/**
 * 整个App的Application
 * 
 * @author tianq creat time ： 2016-08-19 4:50 PM
 */
public class HelloApplication extends Application {
    
	/** SDCard路径 */
	public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	/** 文件夹路径 */
	public static String BASE_PATH = SD_PATH + "/SmartLand/";

	/** 缓存路径 */
	public static String BASE_CACHE = BASE_PATH + "/.Cahe/";

	/** 账号路径 */
	public static String PHONE_PATH ;
	/** 头像缓存路径 */
	public static String HEAD_IMAGE_CACHE = "/saveHeadPic/images/";

	public static String getSdPath() {
		return SD_PATH;
	}

	public String getBasePath() {
		return BASE_PATH;
	}

	public static void setBasePath(String basePath) {
		BASE_PATH = basePath;
	}

	public static String getBaseCache() {
		return BASE_CACHE;
	}

	public static void setBaseCache(String baseImageCache) {
		BASE_CACHE = baseImageCache;
	}

	public static String getHeadImageCache() {
		return HEAD_IMAGE_CACHE;
	}

	public static void setHeadImageCache(String headImageCache) {
		HEAD_IMAGE_CACHE = headImageCache;
	}

	public static String getPhonePath(String PhonePath) {
		PhonePath = BASE_PATH + "./" + PhonePath + "/";
		return  PhonePath;
	}
	private static final String TAG = HelloApplication.class.getSimpleName();

	private static HelloApplication mInstance = null;

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	@Override
	public void onCreate() {
		createDir();
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果app启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
		// name就立即返回

		if (processAppName == null
				|| !processAppName.equalsIgnoreCase("com.example.hello1")) {
			Log.e(TAG, "enter the service process!");
			// "com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名

			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}
		
//		EMChat.getInstance().setAutoLogin(false);
		EMChat.getInstance().init(getApplicationContext());
		// 在做代码混淆的时候需要设置成false
		EMChat.getInstance().setDebugMode(true);
		initHXOptions();
		mInstance = this;
	}
	protected void initHXOptions() {
		Log.d(TAG, "init HuanXin Options");

		// 获取到EMChatOptions对象
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// 默认添加好友时，是不需要验证的true，改成需要验证false
		options.setAcceptInvitationAlways(false);
		// 默认环信是不维护好友关系列表的，如果app依赖环信的好友关系，把这个属性设置为true
		options.setUseRoster(true);
		options.setNumberOfMessagesLoaded(1);
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}

	/**
	 * 创建APP目录
	 */
	private void createDir() {
		File fileApplication = new File(BASE_PATH);
		File fileCache = new File(BASE_CACHE);
		if (!fileApplication.exists()) {
			fileApplication.mkdirs();
		}
		if (!fileCache.exists()) {
			fileCache.mkdirs();
		}
	}
    public static SharedPreferences PDAConfig(Context context, String configName, boolean isSD, String pathDir,SharedPreferences sf) {  
        if (isSD) {  
            try {  
                Field field = ContextWrapper.class.getDeclaredField("mBase");  
                field.setAccessible(true);  
                Object obj = field.get(context);  
                field = obj.getClass().getDeclaredField("mPreferencesDir");  
                field.setAccessible(true);  
                File file = new File("."+pathDir);  
                field.set(obj, file);  
                sf = context.getSharedPreferences(configName, Activity.MODE_PRIVATE);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        } else {  
            sf = context.getSharedPreferences(configName, 0);  
        }
		return sf;  
    }  
    public static HelloApplication getInstance() {
		return mInstance;
	}
}
