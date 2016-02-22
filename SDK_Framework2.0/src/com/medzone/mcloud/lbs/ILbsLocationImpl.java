package com.medzone.mcloud.lbs;

import java.util.HashMap;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.medzone.framework.Log;
import com.medzone.framework.task.ITaskCallback;

/**
 * 
 * @category 处理接收定位的结果(含包装定位)
 */
public class ILbsLocationImpl implements LocationListener, ILocationSerializate<CouldLocation> {

	public static final String		TAG		= ILbsLocationImpl.class.getSimpleName();
	private ITaskCallback			mITaskCallback;
	private HashMap<String, Object>	retMap	= new HashMap<String, Object>();
	// private Context mContext;
	// @Deprecated
	// private LocationManager mlManager;
	// private static int code;
	private CouldLocation			mLocation;

	public ILbsLocationImpl(/* Context context */) {
		// this.mContext = context;
		// mlManager = (LocationManager)
		// this.mContext.getSystemService(Context.LOCATION_SERVICE);
		// mlManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		// mLocation = new CouldLocation(LocationManager.NETWORK_PROVIDER);
		// 注册网络状态改变监听器
		// this.mContext.registerReceiver(new NetstateReceiver(), new
		// IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
	}

	protected void setTaskHost(ITaskCallback mITaskCallback) {
		this.mITaskCallback = mITaskCallback;
		// if (!mlManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
		// code = LbsConstants.LBS_NET_RESULT_OFFLINE_OK;
		// mLocation.setLbsCode(code);
		// mITaskCallback.onComplete(packErrorCode(code),
		// packWrapper(mLocation));
		// }
	}

	public void setLastKnownLocation(Location location) {
		onLocationChanged(location);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (mITaskCallback != null) {
			// XXX First given a error code, but the error code
			// corresponding scenarios may not be correct.
			int lbsStatusCode = LbsConstants.LBS_GPS_RESULT_FAILED;
			if (location != null) {
				// Sets the contents of the location to the values from the
				// given location.
				if (mLocation == null) {
					mLocation = new CouldLocation(location);
				}
				else {
					mLocation.set(location);
				}
				final String provider = location.getProvider();

				if (provider == null) {
					Log.w(TAG, "onLocationChanged: provider is null ,permission not gived.");
					lbsStatusCode = LbsConstants.LBS_NO_PERMISSION;
				}
				else if (provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
					lbsStatusCode = LbsConstants.LBS_GPS_RESULT_OK;
				}
				else if (provider.equalsIgnoreCase(LocationManager.NETWORK_PROVIDER)) {
					lbsStatusCode = LbsConstants.LBS_NET_RESULT_OK;
				}
				else if (provider.equalsIgnoreCase("fused")) {
					lbsStatusCode = LbsConstants.LBS_GPS_RESULT_OK;
				}
			}
			mITaskCallback.onComplete(packErrorCode(lbsStatusCode), packWrapper(mLocation));
		}
		else {
			throw new RuntimeException("必须需要传入ITaskCallback来确保定位功能的可用性.");
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
		case LocationProvider.AVAILABLE:
			Log.v(TAG, "LocationProvider.AVAILABLE");
			break;
		case LocationProvider.OUT_OF_SERVICE:
			Log.v(TAG, "LocationProvider.OUT_OF_SERVICE");
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Log.v(TAG, "LocationProvider.TEMPORARILY_UNAVAILABLE");
			break;
		default:
			Log.e(TAG, "onStatusChanged");
			break;
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.v(TAG, provider + ">>onProviderEnabled");
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.v(TAG, provider + ">>onProviderDisabled");
	}

	@Override
	public HashMap<String, ?> packWrapper(CouldLocation location) {
		retMap.clear();
		retMap.put(LbsConstants.LOCATION_TIME, location.getTime());
		retMap.put(LbsConstants.LOCATION_LATITUDE, location.getLatitude());
		retMap.put(LbsConstants.LOCATION_LONTITUDE, location.getLongitude());
		retMap.put(LbsConstants.LOCATION_ALTITUDE, location.getAltitude());
		retMap.put(LbsConstants.LOCATION_ADDRESS, null);
		retMap.put(LbsConstants.LOCATION_ORGIN_CODE, location.getLbsCode());
		retMap.put(LbsConstants.LOCATION_LOC_CODE, packErrorCode(location.getLbsCode()));
		retMap.put(LbsConstants.LOCATION_RADIUS, location.getAccuracy());
		return retMap;
	}

	@Override
	public CouldLocation reversePackWrapper(HashMap<String, ?> map) {
		// TODO 逆向解包，一般没有需求需要与SDK进行交互
		return null;
	}

	@Override
	public int packErrorCode(int lbsCode) {
		return lbsCode; // 直接使用心云内部定位返回码
	}

	// /**
	// * 网络变化监听事件
	// *
	// * @author asusu
	// *
	// */
	// @Deprecated
	// public class NetstateReceiver extends BroadcastReceiver {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// ConnectivityManager manager = (ConnectivityManager)
	// context.getSystemService(Context.CONNECTIVITY_SERVICE);
	// NetworkInfo gprs =
	// manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	// NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	// if (!gprs.isConnected() && !wifi.isConnected()) {
	// // 网络状态不可用
	// code = LbsConstants.LBS_NET_RESULT_OFFLINE_OK;
	// // 异步定位过程中网络中断的情况
	// if (ILbsLocationImpl.this.mITaskCallback != null &&
	// ILbsLocationImpl.this.mLocation != null) {
	// // if(!mlManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
	// code = LbsConstants.LBS_NET_RESULT_OFFLINE_OK;
	// mLocation.setLbsCode(code);
	// mITaskCallback.onComplete(packErrorCode(code), packWrapper(mLocation));
	// }
	// }
	// else {
	// // 网络状态可用
	// }
	// }
	//
	// }

}
