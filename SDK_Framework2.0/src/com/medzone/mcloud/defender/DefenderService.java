/**
 *
 */
package com.medzone.mcloud.defender;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.os.RemoteException;

import com.medzone.framework.Log;
import com.medzone.mcloud.defender.DefenderServiceConnect.Stub;

/**
 * @author Robert.
 *
 */
@SuppressLint("Registered")
public class DefenderService extends Service {

	private static final String TAG = "JPush_Local_Defender_Service";
	private Defender				mDefender;
	private ConnectivityReceiver	mConnectivityReceiver;
	private BootUninstallReceiver	mBootUninstallReceiver;

	public DefenderService() {
		Log.d(TAG,"DefenderService#进程：构造DefenderService：" + hashCode());
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "DefenderService#进程：onCreate" + hashCode());
		mDefender = new Defender(this);
		registerConnectivityReceiver();
		registerInstalledReceiver();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG,"DefenderService#进程：onBind" + hashCode());
		return iBinder;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG,"DefenderService#进程：onStartCommand" + hashCode());
		try {
			Log.d(TAG,"DefenderService#进程：onStartCommand： start jpush" + hashCode());
			iBinder.startJPush();
		} catch (RemoteException e) {
			Log.d(TAG, "DefenderService#进程：start jpush error" + e.getMessage());
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG,"DefenderService#进程：onUnbind" + hashCode());
		try {
			if (iBinder != null) {
				iBinder.stopJPush();
			}
		}
		catch (RemoteException e) {
			Log.d(TAG, "DefenderService#进程：stop jpush error" + e.getMessage());
		}
		return super.onUnbind(intent);
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		super.onTaskRemoved(rootIntent);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		Log.d(TAG, "DefenderService#进程：onRebind" + hashCode());
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "DefenderService#进程：onDestroy" + hashCode());
		/**
		 * 当使用类似口口管家等第三方应用或是在setting里-应用-强制停止时，APP进程可能就直接被干掉了，onDestroy方法都进不来，
		 * 所以还是无法保证~.~
		 */
		try {
			if (iBinder != null) {
				iBinder.stopJPush();
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		unregisterConnectivityReceiver();
		unregisterInstalledReceiver();
		super.onDestroy();
	}

	private DefenderServiceConnect.Stub	iBinder	= new Stub() {

		@Override
		public void initJPush() throws RemoteException {
			mDefender.initJPush();
		}

		@Override
		public void startJPush() throws RemoteException {
			mDefender.startJPush();
		}

		@Override
		public void stopJPush() throws RemoteException {
			mDefender.stopJPush();
		}

		@Override
		public String getRegisterID() throws RemoteException {
			return mDefender.getRegisterID();
		}

		@Override
		public boolean checkJPushConnectState(boolean isAutoConnecting) throws RemoteException {
			return mDefender.checkConnectState(isAutoConnecting);
		}

	};

	private void registerConnectivityReceiver() {
		if (mConnectivityReceiver == null) {
			mConnectivityReceiver = new ConnectivityReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			// filter.addAction(ConnectivityReceiver.ACTION_BRING_TO_FRONT);
			// filter.addAction(ConnectivityReceiver.ACTION_START_DEFENDER);
			registerReceiver(mConnectivityReceiver, filter);
		}
	}

	private void registerInstalledReceiver() {
		if (mBootUninstallReceiver == null) {
			mBootUninstallReceiver = new BootUninstallReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
			registerReceiver(mBootUninstallReceiver, filter);
		}
	}

	private void unregisterConnectivityReceiver() {
		if (mConnectivityReceiver != null) {
			unregisterReceiver(mConnectivityReceiver);
		}
	}

	private void unregisterInstalledReceiver() {
		if (mBootUninstallReceiver != null) {
			unregisterReceiver(mBootUninstallReceiver);
		}
	}

	@Deprecated
	class BootUninstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
				String packageName = intent.getDataString();
				System.err.println("监听到卸载了:" + packageName + "包名的程序");
				if (packageName.equals(getPackageName())) {
					if (iBinder != null) {
						System.err.println("监听卸载自身:关闭接收推送");
						try {
							iBinder.stopJPush();
						}
						catch (RemoteException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

}
