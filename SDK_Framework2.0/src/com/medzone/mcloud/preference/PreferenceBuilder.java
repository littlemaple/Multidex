package com.medzone.mcloud.preference;

import android.content.Context;


final class PreferenceBuilder {

    public PreferenceBuilder() {
    }

    public String mPreferenceName = "com.medzone.mcloud.defPreference";
    public Context mContext;

    public PreferenceBuilder setPreferenceName(String preferenceName) {
        this.mPreferenceName = preferenceName;
        return this;
    }

    public PreferenceBuilder setContext(Context context) {
        this.mContext = context.getApplicationContext();
        return this;
    }

    public PreferenceImpl build() {
        return new PreferenceImpl(this);
    }
}
