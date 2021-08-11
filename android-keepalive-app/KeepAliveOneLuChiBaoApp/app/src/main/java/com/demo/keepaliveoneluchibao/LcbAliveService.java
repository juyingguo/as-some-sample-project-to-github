package com.demo.keepaliveoneluchibao;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alive.core.ICat;

import java.util.List;

/**
 *@decrible 路痴宝保活后台服务，绑定启动保活助手A的服务
 *
 * Create by jiangdongguo on 2016-12-6 上午9:41:36
 */
public class LcbAliveService extends Service {
	private final String TAG = "LcbAliveService";
	private final String A_PackageName = "com.demo.keepaliveoneassistanta";
	private final String A_ServicePath = "com.demo.keepaliveoneassistanta.AssistantAService";
	private ICat mBinderFromA;
	private int count;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(@NonNull Message msg) {
			super.handleMessage(msg);
			Log.i(TAG,"handleMessage,count:" + (count++));
			mHandler.sendEmptyMessageDelayed(100,5000);
		}
	};
	private ServiceConnection conn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i(TAG,"onServiceDisconnected.");
			bindAliveA();
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG,"onServiceConnected.");
			mBinderFromA = ICat.Stub.asInterface(service);
			if (mBinderFromA != null) {
				try {
					Log.d(TAG,
							"收到保活助手A的数据：name="
									+ mBinderFromA.getName() + "；age="
									+ mBinderFromA.getAge() + "----");
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	private ICat.Stub mBinderToA = new ICat.Stub() {	
		@Override
		public String getName() throws RemoteException {
			return "我是路痴宝";
		}
		
		@Override
		public int getAge() throws RemoteException {
			return 3;
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG,"onBind.");
		return mBinderToA;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG,"onUnbind.");
		return super.onUnbind(intent);
	}
 
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG,"onCreate.");
		if(!isApkInstalled(A_PackageName)){
			Log.d(TAG,"onCreate,----保活助手A未安装----");
			stopSelf();
			return;
		}
		bindAliveA();

		mHandler.sendEmptyMessageDelayed(100,5000);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG,"onStartCommand.");
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG,"onDestroy.");
		unbindService(conn);
	}
 
	private void bindAliveA() {
		Intent serverIntent = new Intent();
		serverIntent.setClassName(A_PackageName, A_ServicePath);
		bindService(serverIntent, conn, Context.BIND_AUTO_CREATE);
	}
	
	private boolean isApkInstalled(String packageName){
		PackageManager mPackageManager = getPackageManager();
		//获得所有已经安装的包信息
		List<PackageInfo> infos = mPackageManager.getInstalledPackages(0);
		for(int i=0;i<infos.size();i++){
			if(infos.get(i).packageName.equalsIgnoreCase(packageName)){
				return true;
			}
		}
		return false;
	}
}