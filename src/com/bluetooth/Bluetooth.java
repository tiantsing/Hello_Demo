package com.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class Bluetooth {
	BluetoothAdapter bluetoochadapter;
	Context context;
	String Address;
	String Name ;
	int Type;
	int BluetoothClass;
	public Bluetooth(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		bluetoochadapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 找到设备
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				// 添加进一个设备列表，进行显示。
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					Log.i("TAG", "find device:" + device.getName() + device.getAddress());
					Log.i("MAC地址", "" + device.getAddress());
					Log.i("蓝牙名称", "" + device.getName());
					Log.i("蓝牙Type", "" + device.getType());
					Log.i("蓝牙类别", "" + device.getBluetoothClass());
				}
			}
			// 搜索完成
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
			}
		}
	};
	//开启蓝牙签到模式
	public void StartBlueToothSign(){
		OpenBlueTooth();
		SearchBluetooch(context);
	}
	//关闭蓝牙签到模式
	public void StopBlueToothSign(){
		bluetoochadapter.disable(); //关闭蓝牙
	    context.unregisterReceiver(receiver);//关闭广播
	}
	// 检查设备是否支持蓝牙
	public void OpenBlueTooth() {
		// 是否支持蓝牙
		if (bluetoochadapter == null) {
			System.out.print("该设备不支持蓝牙!");
		}
		// 如果蓝牙没有开启，打开蓝牙
		if (!bluetoochadapter.isEnabled()) {
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// 设置蓝牙可见性，最多300秒
			context.startActivity(intent);
		}
	}
	// 搜索周围的蓝牙
	public void SearchBluetooch(Context context) {
		// 设置广播信息过滤
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		// 注册广播接收器，接收并处理搜索结果
		context.registerReceiver(receiver, intentFilter);
		// 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
		bluetoochadapter.startDiscovery();
	}
	//返回蓝牙的信息
	public String putAddress()
	{
		return Address;
	}
	public String putName(){
		return  Name;
	}
	public int putType(){
		return Type;
	}
	public int putBluetoothClass(){
		return BluetoothClass;
	}
}
