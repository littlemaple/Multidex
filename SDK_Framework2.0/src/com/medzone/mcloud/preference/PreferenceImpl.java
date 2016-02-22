package com.medzone.mcloud.preference;

import android.content.Context;
import android.content.SharedPreferences;

final class PreferenceImpl {

	private static PreferenceBuilder		DEF_BUILDER	= new PreferenceBuilder();
	private static volatile PreferenceImpl	defaultInstance;
	private Context							mContext;
	private SharedPreferences				mSharedPreferences;
	private String							mPreferenceName;

	PreferenceImpl(PreferenceBuilder builder) {
		mContext = builder.mContext;
		mPreferenceName = builder.mPreferenceName;
		mSharedPreferences = mContext.getSharedPreferences(mPreferenceName, Context.MODE_PRIVATE);
	}

	public SharedPreferences getSharedPreferences() {
		if (mSharedPreferences == null) {
			return getDefault().getSharedPreferences();
		}
		return mSharedPreferences;
	}

	public Context getContext() {
		return mContext;
	}

	public String getPreferenceName() {
		return mPreferenceName;
	}

	public static PreferenceImpl getDefault() {
		if (defaultInstance == null) {
			synchronized (PreferenceImpl.class) {
				if (defaultInstance == null) {
					defaultInstance = new PreferenceImpl(DEF_BUILDER);
				}
			}
		}
		return defaultInstance;
	}

}
