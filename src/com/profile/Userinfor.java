package com.profile;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.MainPage.Activity.Main_PageActiviy;
import com.app.main.HelloApplication;
import com.example.hello1.R;
import com.exit.AppExit;
import com.profile.ActionSheetDialog.OnSheetItemClickListener;
import com.profile.ActionSheetDialog.SheetItemColor;
import com.pub.SingleGlobalVariables;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class Userinfor extends Activity implements OnClickListener {

	TextView text_title, text_submit, text_back;
	TextView setname, setsex, setschool, setcollage, setruxue, setxuehao, setmajor;// setclass,
	ImageView headimg;
	/** 编辑头像相册选取 */
	static final int REQUESTCODE_PICK = 1;
	/** 设置头像 */
	static final int REQUESTCODE_CUTTING = 2;
	/** 编辑头像拍照选取 */
	static final int PHOTO_REQUEST_TAKEPHOTO = 3;
	/** 标记是拍照还是相册0 是拍照1是相册 */
	int cameraorpic;
	static final int SCALE = 5;
	Bitmap photo;
	/** SharedPreferences服务 **/
	SharedPreferences sf_userinfo;
	String phonepath;
	// 存储路径
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.userinfo);
		AppExit.getInstance().addActivity(this);
		// 初始化
		init();

	}

	private void init() {
		// 顶部标题栏设置
		text_title = (TextView) findViewById(R.id.txt_title);
		text_title.setText("个人信息");
		text_submit = (TextView) findViewById(R.id.txt_right);
		text_submit.setText("确定");
		text_back = (TextView) findViewById(R.id.txt_left1);
		text_back.setText("返回");

		// 找到个人信息界面的各种信息
		/**
		 * headimg：头像setname：姓名setsex：性别setbrith：出生日期sethome：家乡setschool：学校setcollage：院系setruxue：入学日期setclass：班级
		 */
		headimg = (ImageView) findViewById(R.id.iv_uimage);
		setname = (TextView) findViewById(R.id.tv_name_set);
		setsex = (TextView) findViewById(R.id.tv_sex_set);
		setxuehao = (TextView) findViewById(R.id.tv_xuehao_set);
		setschool = (TextView) findViewById(R.id.tv_school_set);
		setcollage = (TextView) findViewById(R.id.tv_collage_set);
		setruxue = (TextView) findViewById(R.id.tv_ruxue_set);
		// setclass=(TextView)findViewById(R.id.tv_class_set);
		setmajor = (TextView) findViewById(R.id.tv_major_set);
		// 实例化SharedPreferences服务
		phonepath = HelloApplication.getPhonePath(SingleGlobalVariables.Phone);
		sf_userinfo=HelloApplication.PDAConfig(Userinfor.this, "userinfo", true, phonepath,sf_userinfo);
		setname.setText(sf_userinfo.getString("name", ""));
		setsex.setText(sf_userinfo.getString("sex", ""));
		setxuehao.setText(sf_userinfo.getString("xuehao", ""));
		setschool.setText(sf_userinfo.getString("school", ""));
		setcollage.setText(sf_userinfo.getString("collage", ""));
		setruxue.setText(sf_userinfo.getInt("to_schoolyear", 0) + "");
		// setclass.setText(sf.getString("class", "计133-2"));
		setmajor.setText(sf_userinfo.getString("major", ""));
		// 对控件进行监听
		text_submit.setOnClickListener(this);
		text_back.setOnClickListener(this);
		headimg.setOnClickListener(this);
		// 初始化photo
		photo =SingleGlobalVariables.photo;
//		// 初始化头像
//		Bitmap photoBitmap = ImageTools.getPhotoFromSDCard(phonepath, "head");
//		if (photoBitmap != null) {
//			headimg.setImageBitmap(photoBitmap);
//		}
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.txt_right:
			txt_right();
			break;
		case R.id.txt_left1:
			Intent intent = new Intent(Userinfor.this, User.class);
			startActivity(intent);
			break;
		case R.id.iv_uimage:
			popCheck();
			break;
		default:
			break;
		}
	}

	/**
	 * 修改按钮函数
	 */
	public void txt_right() {
		Intent intent1 = new Intent(Userinfor.this, User.class);
		Log.e("", "头像开始保存");
		ImageTools.savePhotoToSDCard(photo, phonepath, "head");
		Log.e("", "头像保存成功");
		startActivity(intent1);
	}

	/**
	 * 操作选择栏
	 */
	public void popCheck() {

		new ActionSheetDialog(Userinfor.this).builder().setCancelable(false).setCanceledOnTouchOutside(false)
				.addSheetItem("打开相册", SheetItemColor.Blue, new OnSheetItemClickListener() {
					@Override
					public void onClick(int which) {
						cameraorpic = 1;
						skipPic();
					}
				}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 判断请求码是编辑就跳到编辑
		switch (requestCode) {
		case REQUESTCODE_PICK:
			if (data == null || data.getData() == null) {
				return;
			}
			startPhotoZoom(data.getData());
			break;
		case REQUESTCODE_CUTTING:
			if (data != null) {
				setPicToView(data);
			}
			break;
		case PHOTO_REQUEST_TAKEPHOTO:
			startPhotoZoom(Uri.fromFile(outFile));
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * save the picture data 设置头像并保存头像
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			photo = (Bitmap) extras.get("data");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
		}
	
		headimg.setImageBitmap(photo);
	}

	/** 将BItmap转换成字节数组 */
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/** 设置可编辑头像 */
	public void startPhotoZoom(Uri uri) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", true);
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		// 图片格式
		intent.putExtra("outputFormat", "PNG");
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, REQUESTCODE_CUTTING);
	}

	/** 打开相册 */
	private void skipPic() {
		Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
		pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(pickIntent, REQUESTCODE_PICK);
	}

	/** 指定拍摄图片文件位置避免获取到缩略图 */
	File outFile;

	/** 打开相机 */
	private void openCamera() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File outDir = new File(phonepath);
			if (!outDir.exists()) {
				outDir.mkdirs();
			}
			outFile = new File(outDir, "head.png");
			//// 解决在部分机器缓存更新不及时问题
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
					Uri.parse("file://" + Environment.getExternalStorageDirectory())));

			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
			// intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
		} else {
			Log.e("CAMERA", "请确认已经插入SD卡");
		}
	}

}