package com.medzone.mcloud.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;


/**
 * 对应配置文件对应的包装器，通过包装配置文件将配置文件的读写和业务逻辑的区分区分开； 达到对配置文件的读写代码去重的目的；
 * 另一个好处是此处只关注业务代码的实现；
 * 
 * @author Robert
 * 
 */
public abstract class AbstractPreferenceWrapper {

	private final static List<PreferenceImpl> mPreferenceImplPool = new ArrayList<>();
	private PreferenceImpl mPreferenceImpl;

	protected abstract String setupPreferenceName();

	protected abstract Context setupContext();

	protected AbstractPreferenceWrapper() {
		obtainPreferenceImplInstance();
	}

	protected AbstractPreferenceWrapper(Context context){
		initContext(context);
		obtainPreferenceImplInstance();
	}


	@Deprecated
	protected void initContext(Context context){}
	private PreferenceImpl findPreferenceImplFromPool() {
		PreferenceImpl ret = null;
		synchronized (mPreferenceImplPool) {
			for (PreferenceImpl item : mPreferenceImplPool) {
				if (TextUtils.equals(setupPreferenceName(),
						item.getPreferenceName())) {
					ret = item;
					break;
				}
			}
		}
		return ret;
	}

	private PreferenceImpl makePreferenceImpl() {
		PreferenceBuilder builder = new PreferenceBuilder();
		builder.setContext(setupContext());
		builder.setPreferenceName(setupPreferenceName());
		PreferenceImpl ret = builder.build();
		synchronized (mPreferenceImplPool) {
			mPreferenceImplPool.add(ret);
		}
		return ret;
	}

	protected final PreferenceImpl obtainPreferenceImplInstance() {

		mPreferenceImpl = findPreferenceImplFromPool();
		if (mPreferenceImpl == null) {
			mPreferenceImpl = makePreferenceImpl();
		}
		return mPreferenceImpl;
	}

	private SharedPreferences getSharedPreferences() {
		return mPreferenceImpl.getSharedPreferences();
	}

	public boolean contains(String key){
		return getSharedPreferences().contains(key);
	}

	public final void removeFromPreferences(HashMap<String, Object> map) {
		try {

			final int size = map.size();
			if (size > 0) {
				Editor editor = getSharedPreferences().edit();
				for (Entry<String, Object> entry : map.entrySet()) {
					final String key = entry.getKey();
					editor.remove(key);
				}
				editor.apply();
			}
		} finally {
			releaseMap(map);
		}
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("unchecked")
	public final void saveToPreferences(HashMap<String, Object> map) {
		try {
			final int size = map.size();
			if (size > 0) {
				Editor editor = getSharedPreferences().edit();
				for (Entry<String, Object> entry : map.entrySet()) {
					final Object value = entry.getValue();
					final String key = entry.getKey();
					if (value == null) {
						continue;
					}
					final Class<?> clazz = value.getClass();
					if (clazz.equals(Integer.class)) {
						editor.putInt(key, (int) value);
					} else if (clazz.equals(String.class)) {
						editor.putString(key, (String) value);
					} else if (clazz.equals(Boolean.class)) {
						editor.putBoolean(key, (boolean) value);
					} else if (clazz.equals(Float.class)) {
						editor.putFloat(key, (float) value);
					} else if (clazz.equals(Long.class)) {
						editor.putLong(key, (long) value);
					} else {

						if (clazz.equals(Set.class)) {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
								editor.putStringSet(key, (Set<String>) value);
							}
						} else {
							editor.putString(key, (String) value);
						}
					}
				}
				editor.commit();
			}
		} finally {
			releaseMap(map);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getFromPreferences(String key, T defValue) {
		Object t = null;
		final SharedPreferences sp = getSharedPreferences();
		final Class<?> clazz = defValue.getClass();

		if (clazz.equals(Integer.class)) {
			t = sp.getInt(key, (Integer) defValue);
		} else if (clazz.equals(String.class)) {
			t = sp.getString(key, (String) defValue);
		} else if (clazz.equals(Boolean.class)) {
			t = sp.getBoolean(key, (Boolean) defValue);
		} else if (clazz.equals(Float.class)) {
			t = sp.getFloat(key, (Float) defValue);
		} else if (clazz.equals(Long.class)) {
			t = sp.getLong(key, (Long) defValue);
		}
		if (t == null) {
			t = defValue;
		}
		return (T) t;

	}

	// -----------------------------参数池---------------------------------

	private static List<HashMap<String, Object>> mMapPool = new ArrayList<>();

	protected HashMap<String, Object> obtainMap() {
		synchronized (mMapPool) {
			final int size = mMapPool.size();
			if (size > 0) {
				HashMap<String, Object> map = mMapPool.remove(size - 1);
				return map;
			}
		}
		return new HashMap<>();
	}

	private void releaseMap(HashMap<String, Object> map) {
		map.clear();
		synchronized (mMapPool) {
			final int size = mMapPool.size();
			if (size < 1000) {
				mMapPool.add(map);
			}
		}
	}

}
