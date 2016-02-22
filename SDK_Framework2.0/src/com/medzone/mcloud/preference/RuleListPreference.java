package com.medzone.mcloud.preference;

import java.util.HashMap;

import android.content.Context;


public class RuleListPreference extends AbstractPreferenceWrapper {

    // 规则库常量字段
    public static final String RULE_VERSION = "rule_version";
    public static final String RULE_CONTENT = "rule_content";

    private static RuleListPreference instance;
    private Context context;

    protected RuleListPreference(Context context) {
        super(context);
        initContext(context);
    }

    @Override
    protected void initContext(Context context) {
        super.initContext(context);
        this.context = context;
    }

    public synchronized static RuleListPreference getInstance(Context context) {
        if (instance == null) {
            instance = new RuleListPreference(context);
        }
        return instance;
    }

    /**
     * 保存规则库
     */
    public void saveRuleList(String ruleList) {
        HashMap<String, Object> map = obtainMap();
        map.put(RULE_CONTENT, ruleList);
        saveToPreferences(map);
    }

    public String getRuleList() {
        return getFromPreferences(RULE_CONTENT, "");
    }

    /**
     * 保存规则版本
     */
    public void saveRuleVersion(String ruleVersion) {
        HashMap<String, Object> map = obtainMap();
        map.put(RULE_VERSION, ruleVersion);
        saveToPreferences(map);
    }

    public String getRuleVersion() {
        return getFromPreferences(RULE_VERSION, "");
    }

    @Override
    protected String setupPreferenceName() {
        return setupContext().getPackageName() + "_rules";
    }

    @Override
    protected Context setupContext() {
        return context.getApplicationContext();
    }

}
