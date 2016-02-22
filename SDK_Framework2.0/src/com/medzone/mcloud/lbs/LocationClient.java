package com.medzone.mcloud.lbs;

import java.lang.ref.WeakReference;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;

import com.medzone.framework.Log;

/**
 * 
 * @category 设定定位采取的方式,限定只能使用Google定位[适配器类]
 * 
 */

public class LocationClient {

	public static final String	TAG					= LocationClient.class.getSimpleName();
	private Context				mContext;
	private LocationManager		mlManager;
	private LocationListener	mLocationListener;
	private static Handler		mHandler;

	private static final int	HANDLER_REMOVE		= 0;
	private static final int	LC_INTERVAL_MILLIS	= 1000 * 15;							// 启动定位的周期

	@SuppressLint("HandlerLeak")
	private Handler getHandler(LocationClient client) {

		final WeakReference<LocationClient> weakClient = new WeakReference<LocationClient>(client);
		if (mHandler == null) {
			mHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					switch (msg.what) {
					case HANDLER_REMOVE:
						LocationClient client = weakClient.get();
						if (client != null) {
							client.stop();
						}
						break;
					default:
						break;
					}

				}
			};
		}
		return mHandler;
	}

	public LocationClient(Context context) {
		this.mContext = context;
		if (this.mContext != null) {
			mlManager = (LocationManager) this.mContext.getSystemService(Context.LOCATION_SERVICE);
		}
	}

	/**
	 * 开始定位,默认15秒后自动关闭定位,3秒钟请求定位一次.
	 */
	public void start() {
		start(LC_INTERVAL_MILLIS);
	}

	/**
	 * @param intervalMillis
	 *            定位周期,启动定位的功能将在intervalMillis时间后自动关闭定位功能.
	 */
	private void start(long intervalMillis) {
		if (mlManager != null) {
			Location lastKnownLocation = mlManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (lastKnownLocation != null) {
				Log.w(TAG, "NETWORK_PROVIDER:上次缓存了定位结果");
				((ILbsLocationImpl) mLocationListener).setLastKnownLocation(lastKnownLocation);
			}
			else {
				lastKnownLocation = mlManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (lastKnownLocation != null) {
					Log.w(TAG, "GPS_PROVIDER:上次缓存了定位结果");
					((ILbsLocationImpl) mLocationListener).setLastKnownLocation(lastKnownLocation);
				}
			}
			if (mlManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
				mlManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, mLocationListener);
			}
			if (mlManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
				mlManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, mLocationListener);
			}
			getHandler(this).sendEmptyMessageDelayed(HANDLER_REMOVE, intervalMillis);
		}
		else {
			Log.v(TAG, ">>定位start失败，mlManager为空");
		}
	}

	/**
	 * 停止定位
	 */
	public void stop() {
		if (mlManager != null) {
			mlManager.removeUpdates(mLocationListener);
			// mlManager.setTestProviderEnabled(LocationManager.NETWORK_PROVIDER,
			// false);
			// mlManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER,
			// false);
		}
	}

	/**
	 * 监听事件
	 */
	public void registerLocationListener(LocationListener listener) {
		this.mLocationListener = listener;
	}

}
