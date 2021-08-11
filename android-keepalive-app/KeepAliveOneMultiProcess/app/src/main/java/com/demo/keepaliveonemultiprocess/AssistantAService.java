package com.demo.keepaliveonemultiprocess;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alive.core.ICat;

/**
 *@decrible 保活助手A守护后台服务
 * 1.配置在同一个应用中，配置独立进程的方式。
 * Create by jiangdongguo on 2016-11-23 上午10:57:59
 */
public class AssistantAService extends Service {
	private final String TAG = "AssistantAService";
	private final String Lcb_PackageName = "com.demo.keepaliveonemultiprocess";
	private final String Lcb_ServicePath = "com.demo.keepaliveonemultiprocess.LcbAliveService";
	private ICat mBinderFromLcb;
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
			bindLuChiBao();
		}
 
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG,"onServiceDisconnected.");
			mBinderFromLcb = ICat.Stub.asInterface(service);
			if (mBinderFromLcb != null) {
				try {
					Log.d(TAG,
							"收到路痴宝Service返回的数据：name="
									+ mBinderFromLcb.getName() + "；age="
									+ mBinderFromLcb.getAge() );
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	private ICat.Stub mBinderToLcb = new ICat.Stub() {
		@Override
		public String getName() throws RemoteException {
			return "我是保活助手A";
		}
		
		@Override
		public int getAge() throws RemoteException {
			return 2;
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG,"onBind.");
		return mBinderToLcb;
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
		//提升Service的优先级
		Notification notification = new Notification();
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_NO_CLEAR;
		notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
		startForeground(1, notification);

		Log.d(TAG,"****保活助手onCreate：绑定启动路痴宝****");
		bindLuChiBao();

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
	
	private void bindLuChiBao() {
		Intent clientIntent = new Intent();
		clientIntent.setClassName(Lcb_PackageName, Lcb_ServicePath);
		boolean bindServiceRes = bindService(clientIntent, conn, Context.BIND_AUTO_CREATE);
		Log.i(TAG,"bindLuChiBao,bindServiceRes:" + bindServiceRes);
	}
}