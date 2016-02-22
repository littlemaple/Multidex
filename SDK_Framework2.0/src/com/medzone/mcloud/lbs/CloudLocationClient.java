package com.medzone.mcloud.lbs;

import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.location.LocationListener;

import com.medzone.framework.Log;
import com.medzone.framework.task.ITaskCallback;

/**
 * 
 * @category 定位的外部接口
 */
public class CloudLocationClient implements ILocation<LocationListener> {

	public static final String			TAG				= CloudLocationClient.class.getSimpleName();
	private Context						mAppContext;
	private LocationClient				mLocationClient;
	private ILbsLocationImpl			mLocationImpl;
	private HashMap<String, Object>		mLastestLocationMap;
	private boolean						isLocationing	= false;
	private static CloudLocationClient	instance;

	public static synchronized void init(Context context) {
		if (instance == null) {
			instance = new CloudLocationClient(context);
		}
	}

	public static CloudLocationClient getInstance() {
		if (instance == null) {
			throw new IllegalArgumentException("call init() method is necessary.");
		}
		return instance;
	}

	private CloudLocationClient(Context context) {
		this.mAppContext = context;
		init();
	}

	private void v(String s) {
		Log.v(TAG, s);
	}

	private void w(String s) {
		Log.w(TAG, s);
	}

	@Override
	public void init() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(mAppContext);
			mLocationClient.registerLocationListener(initLocationImpl());
			mLocationClient.start();
			v("执行:定位初始化");
		}
		else {
			v("忽略：定位初始化");
		}
	}

	@Override
	public LocationListener initLocationImpl() {

		if (mLocationImpl == null) {
			mLocationImpl = new ILbsLocationImpl();
			mLocationImpl.setTaskHost(new ITaskCallback() {

				@SuppressWarnings("unchecked")
				@Override
				public void onComplete(int code, Object object) {
					switch (code) {
					case LbsConstants.LBS_GPS_RESULT_OK:
					case LbsConstants.LBS_NET_RESULT_OK:
						w("异步定位返回：定位成功：更新地理位置");
						mLastestLocationMap = (HashMap<String, Object>) object;
						getLocationParams();
						break;
					default:
						w("异步定位返回：定位失败，原始错误信息：" + ((HashMap<String, ?>) object).get(LbsConstants.LOCATION_ORGIN_CODE));
						break;
					}
				}
			});
		}
		return mLocationImpl;
	}

	@Override
	public void start() {
		mLocationClient.start();
		isLocationing = true;
		v("执行：启动定位");
	}

	@Override
	public HashMap<String, ?> getLocationParams() {
		w("执行：获取最后新的定位信息");
		// print(mLastestLocationMap);
		return mLastestLocationMap;
	}

	protected void print(HashMap<String, ?> map) {
		if (map == null) {
			v("mLastestLocationMap is null.");
			return;
		}
		Set<String> set = map.keySet();
		for (String s : set) {
			v(s + "," + map.get(s));
		}
	}

	@Override
	public void stop() {
		mLocationClient.stop();
		isLocationing = false;
		v("执行：停止定位");
	}

	@Override
	public void unInit() {

		if (isLocationing) {
			mLocationClient.stop();
		}

		mAppContext = null;
		mLocationClient = null;
		mLocationImpl = null;
		// mLocationClientOption = null;
		v("执行：释放定位操作");
	}

}
