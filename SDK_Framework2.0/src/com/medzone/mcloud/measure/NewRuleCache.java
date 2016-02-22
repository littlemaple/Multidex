package com.medzone.mcloud.measure;


import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.medzone.mcloud.cache.AbstractDBObjectListCache;
import com.medzone.mcloud.database.CloudDatabaseHelper;
import com.medzone.framework.Log;
import com.medzone.framework.data.bean.Account;

import com.medzone.framework.Config;
import com.medzone.mcloud.data.bean.dbtable.BaseMeasureData;
import com.medzone.mcloud.data.bean.dbtable.ContactPerson;
import com.medzone.mcloud.data.bean.dbtable.NewRule;
import com.medzone.mcloud.data.bean.dbtable.RuleItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author hyc
 */
public class NewRuleCache extends AbstractDBObjectListCache<NewRule> {
    private static Map<String, List<RuleItem>> tag2NewRuleItems = new HashMap<>();

    private NewRule getDefaultNewRule(@NonNull String measureType){
        NewRule newRule = new NewRule();
        newRule.setMeasureType(measureType);
        JSONObject jo;
        String measureRules;
        try {
            jo = new JSONObject(DEFAULT_ALL_RULES);
            measureRules = jo.getString(measureType);
        } catch (JSONException e) {
            e.printStackTrace();
            measureRules = "";
        }
        newRule.setMeasureRule(measureRules);
        return newRule;
    }

    @Override
    public List<NewRule> read() {
        try {
            Dao<NewRule, Long> dao = CloudDatabaseHelper.getInstance().getDao(NewRule.class);
            QueryBuilder<NewRule, Long> queryBuilder = dao.queryBuilder();
            PreparedQuery<NewRule> preparedQuery = queryBuilder.prepare();
            return dao.query(preparedQuery);
        } catch (SQLException e) {
            return null;
        }
    }

    public NewRule read(String type) {
        try {
            Dao<NewRule, Long> dao = CloudDatabaseHelper.getInstance().getDao(NewRule.class);
            QueryBuilder<NewRule, Long> queryBuilder = dao.queryBuilder();
            Where<NewRule, Long> where = queryBuilder.where();
            where.eq(NewRule.MEASURE_TYPE, type);
            PreparedQuery<NewRule> preparedQuery = queryBuilder.prepare();
            List<NewRule> list = dao.query(preparedQuery);
            return list != null && list.size() > 0 ? list.get(0) : null;
        } catch (SQLException e) {
            if (Config.isDeveloperMode) {
                Log.i("<<<NewRuleCache>>>", e.toString());
            }
        }
        return null;
    }

    public void readRules(@NonNull String type) {
        NewRule newRule = read(type);
        if (newRule != null)
            tag2NewRuleItems.putAll(newRule.parse());
        else {
            tag2NewRuleItems.putAll(getDefaultNewRule(type).parse());
        }
    }


    /**
     * 将从云端取下的JSON，存进数据库。
     */
    public List<NewRule> saveToCache(String newRuleListStr) {
        List<NewRule> newRuleList = new ArrayList<>();
        JSONObject newRuleListStrJo;
        NewRule newRule;
        Iterator<String> keys;
        String key, value;

        try {
            newRuleListStrJo = new JSONObject(newRuleListStr);
            keys = newRuleListStrJo.keys();
            while (keys.hasNext()) {
                newRule = new NewRule();
                key = keys.next();
                value = newRuleListStrJo.getString(key);
                newRule.setMeasureType(key);
                newRule.setMeasureRule(value);
                newRule.invalidate();
                newRuleList.add(newRule);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        flush(newRuleList);
        return newRuleList;
    }


    /**
     * 根据状态值取得状态图
     */
    public String readImgByState(String type, Integer state) {
        String imgUri = null;
        List<RuleItem> ruleItems = tag2NewRuleItems.get(type);
        if (ruleItems != null) {
            for (RuleItem ri : ruleItems) {
                if (ri.getState() == null) {
                    continue;
                }
                if (ri.getState().compareTo(state) == 0) {
                    imgUri = ri.getImageUrlX();
                    break;
                }
            }
        }
        return imgUri;
    }

    public RuleItem readRuleByData(BaseMeasureData data, Account account, ContactPerson contactPerson) {
        Map<String, String> resultMap = new HashMap<>();
        data.toMap(resultMap);
        if (account != null)
            account.toMap(resultMap);
        if (contactPerson != null)
            contactPerson.toMap(resultMap);

        List<RuleItem> rules = tag2NewRuleItems.get(data.getMeasureName());
        for (RuleItem ri : rules) {
            if (ri == null) continue;
            ri.parse();
        }

        return match(resultMap, rules);
    }

    RuleItem match(@NonNull Map<String, String> results, List<RuleItem> rules) {
        RuleItem resultRule = null;
        if (results.size() == 0 || rules == null || rules.size() == 0)
            return null;

        for (int i = 0; i < rules.size(); i++) {
            ArrayList<ArrayList<String[]>> orCondition = rules.get(i).condition;
            int orLength = orCondition.size();
            boolean orMatch = false;
            for (int j = 0; j < orLength; j++) {
                ArrayList<String[]> andCondition = orCondition.get(j);
                boolean andMatch = true;
                int andLength = andCondition.size();
                for (int k = 0; k < andLength; k++) {
                    String map[] = andCondition.get(k);
                    String key = map[0];
                    String expect = map[1];
                    String result = results.get(key);
                    if (expect.contains(",")) {
                        String[] subExpect = expect.split(",");
                        andMatch = check(subExpect[0], result) && check(subExpect[1], result);
                    } else {
                        andMatch = check(expect, result);
                    }
                    if (!andMatch)
                        break;
                }
                if (andMatch) {
                    orMatch = true;
                    break;
                }
            }
            if (orMatch) {
                resultRule = rules.get(i);
                break;
            }
        }

        return resultRule;
    }

    private boolean check(String expect, String actual) {
        if (expect == null)
            return false;
        if (actual == null)
            return false;
        if (actual.length() < 1)
            return false;

        try {
            if (expect.startsWith("~/")) {//匹配正则表达式
                return actual.matches(expect.substring(2));
            } else if (expect.startsWith("<>")) {
                return !actual.equalsIgnoreCase(expect.substring(2));
            } else if (expect.startsWith(">=")) {
                Double actualValue = Double.parseDouble(actual);
                Double expectValue = Double.parseDouble(expect.substring(2));
                return actualValue >= expectValue;
            } else if (expect.startsWith("<=")) {
                Double actualValue = Double.parseDouble(actual);
                Double expectValue = Double.parseDouble(expect.substring(2));
                return actualValue <= expectValue;
            } else if (expect.startsWith(">")) {
                Double actualValue = Double.parseDouble(actual);
                Double expectValue = Double.parseDouble(expect.substring(1));
                return actualValue > expectValue;
            } else if (expect.startsWith("<")) {
                Double actualValue = Double.parseDouble(actual);
                Double expectValue = Double.parseDouble(expect.substring(1));
                return actualValue < expectValue;
            } else if (expect.startsWith("=")) {
                Double actualValue = Double.parseDouble(actual);
                Double expectValue = Double.parseDouble(expect.substring(1));
                return actualValue.compareTo(expectValue) == 0;
            }
        } catch (Exception ex) {
            return false;
        }
        return actual.equals(expect);
    }

    private static final String DEFAULT_ALL_RULES = "{" +
            "\"bp\":[" +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<=89\"," +
            "\"value2\":\"<=59\"" +
            "}" +
            "]," +
            "\"state\":1," +
            "\"result\":\"低血压\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bp_1.png?time=1451275894\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bp_1x.png?time=1415696901\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<=89\"," +
            "\"value2\":\">=60,<=79\"" +
            "}," +
            "{" +
            "\"value1\":\">=90,<=119\"," +
            "\"value2\":\"<=79\"" +
            "}" +
            "]," +
            "\"state\":2," +
            "\"result\":\"正常血压\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bp_2.png?time=1451275894\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bp_2x.png?time=1440569522\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\">=120,<=129\"," +
            "\"value2\":\"<=84\"" +
            "}," +
            "{" +
            "\"value1\":\"<=129\"," +
            "\"value2\":\">=80,<=84\"" +
            "}" +
            "]," +
            "\"state\":3," +
            "\"result\":\"血压正常偏高\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bp_3.png?time=1451275894\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bp_3x.png?time=1440569522\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\">=130,<=139\"," +
            "\"value2\":\"<=89\"" +
            "}," +
            "{" +
            "\"value1\":\"<=129\"," +
            "\"value2\":\">=85,<=89\"" +
            "}" +
            "]," +
            "\"state\":4," +
            "\"result\":\"血压正常偏高\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bp_4.png?time=1451275894\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bp_4x.png?time=1440569522\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\">=140,<=159\"," +
            "\"value2\":\"<=99\"" +
            "}," +
            "{" +
            "\"value1\":\"<=139\"," +
            "\"value2\":\">=90,<=99\"" +
            "}" +
            "]," +
            "\"state\":5," +
            "\"result\":\"轻度高血压\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bp_5.png?time=1451275894\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bp_5x.png?time=1415696901\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<=159\"," +
            "\"value2\":\"<=109\"" +
            "}," +
            "{" +
            "\"value1\":\"<=179\"," +
            "\"value2\":\"<=109\"" +
            "}" +
            "]," +
            "\"state\":6," +
            "\"result\":\"中度高血压\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bp_6.png?time=1451275894\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bp_6x.png?time=1415696901\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\">=180\"" +
            "}," +
            "{" +
            "\"value2\":\">=110\"" +
            "}" +
            "]," +
            "\"state\":7," +
            "\"result\":\"重度高血压\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bp_7.png?time=1451275894\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bp_7x.png?time=1415696901\"" +
            "}" +
            "]," +
            "\"oxy\":[" +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\">=95\"" +
            "}" +
            "]," +
            "\"state\":1," +
            "\"result\":\"正常\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_oxy_1.png?time=1439365046\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_oxy_1x.png?time=1439365046\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\">=90\"" +
            "}" +
            "]," +
            "\"state\":2," +
            "\"result\":\"氧失饱和\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_oxy_2.png?time=1450752105\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_oxy_2x.png?time=1439365046\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<90\"" +
            "}" +
            "]," +
            "\"state\":3," +
            "\"result\":\"低氧血症\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_oxy_3.png?time=1450752105\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_oxy_3x.png?time=1439365046\"" +
            "}" +
            "]," +
            "\"et\":[" +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<=35.9\"" +
            "}" +
            "]," +
            "\"state\":1," +
            "\"result\":\"偏低\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_et_1.png?time=1415696901\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_et_1x.png?time=1415696901\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<=37.2\"" +
            "}" +
            "]," +
            "\"state\":2," +
            "\"result\":\"正常\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_et_2.png?time=1439365046\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_et_2x.png?time=1439365046\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<=39\"" +
            "}" +
            "]," +
            "\"state\":3," +
            "\"result\":\"发热\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_et_3.png?time=1415696901\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_et_3x.png?time=1415696901\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\">39\"" +
            "}" +
            "]," +
            "\"state\":4," +
            "\"result\":\"高热\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_et_4.png?time=1415696901\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_et_4x.png?time=1415696901\"" +
            "}" +
            "]," +
            "\"bs\":[" +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<2.8\"," +
            "\"sick_diabetes\":\"Y\"" +
            "}" +
            "]," +
            "\"state\":1," +
            "\"result\":\"极低\"," +
            "\"description\":\"立即停止活动，拨打120，停用降糖药，进食含糖高的食物如：巧克力、糖果、葡萄糖液等。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bs_1.png?time=1439365046\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bs_1x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<=2.8\"," +
            "\"sick_diabetes\":\"N\"" +
            "}" +
            "]," +
            "\"state\":2," +
            "\"result\":\"低血糖\"," +
            "\"description\":\"立即进食含糖高的食物如：巧克力、糖果、葡萄糖液等，半小时后复测血糖。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bs_2.png?time=1439365046\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bs_2x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<=3.9\"," +
            "\"sick_diabetes\":\"Y\"" +
            "}" +
            "]," +
            "\"state\":2," +
            "\"result\":\"低血糖\"," +
            "\"description\":\"立即进食含糖高的食物如：巧克力、糖果、葡萄糖液等，半小时后复测血糖。存在降糖药过量的可能，如多次发生，请咨询医生是否调整用药。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bs_2.png?time=1439365046\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bs_2x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<3.9\"," +
            "\"sick_diabetes\":\"N\"" +
            "}," +
            "{" +
            "\"value1\":\"<4.4\"," +
            "\"sick_diabetes\":\"Y\"" +
            "}" +
            "]," +
            "\"state\":3," +
            "\"result\":\"偏低\"," +
            "\"description\":\"适当补充含糖食物，半小时后复测。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bs_3.png?time=1439365046\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bs_3x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<=7.0\"," +
            "\"value_period\":0," +
            "\"sick_diabetes\":\"Y\"" +
            "}," +
            "{" +
            "\"value1\":\"<=7.0\"," +
            "\"value_period\":1," +
            "\"sick_diabetes\":\"Y\"" +
            "}," +
            "{" +
            "\"value1\":\"<=10.0\"," +
            "\"value_period\":2," +
            "\"sick_diabetes\":\"Y\"" +
            "}" +
            "]," +
            "\"state\":4," +
            "\"result\":\"正常\"," +
            "\"description\":\"血糖控制良好，请注意保持。具体目标以医生建议为准。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bs_4.png?time=1439365046\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bs_4x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<=6.1\"," +
            "\"value_period\":0," +
            "\"sick_diabetes\":\"N\"" +
            "}," +
            "{" +
            "\"value1\":\"<=6.1\"," +
            "\"value_period\":1," +
            "\"sick_diabetes\":\"N\"" +
            "}," +
            "{" +
            "\"value1\":\"<=7.7\"," +
            "\"value_period\":2," +
            "\"sick_diabetes\":\"N\"" +
            "}" +
            "]," +
            "\"state\":4," +
            "\"result\":\"正常\"," +
            "\"description\":\"血糖正常。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bs_4.png?time=1439365046\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bs_4x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<16.7\"," +
            "\"sick_diabetes\":\"N\"" +
            "}" +
            "]," +
            "\"state\":5," +
            "\"result\":\"偏高\"," +
            "\"description\":\"疑似偏高，当饮食过量、人体急性感染、创伤、疼痛等应激状态下可能出现暂时性的血糖升高，请注意复测。如多次测量后仍偏高，可能存在糖调节受损或已发生糖尿病，应引起重视，咨询专业医生。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bs_5.png?time=1439365046\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bs_5x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<16.7\"," +
            "\"sick_diabetes\":\"Y\"" +
            "}" +
            "]," +
            "\"state\":5," +
            "\"result\":\"偏高\"," +
            "\"description\":\"疑似偏高，当饮食过量、人体急性感染、创伤、疼痛等应激状态下可能出现暂时性的血糖增高，请注意复测。如多次测量后仍偏高，可能需要调整用药，应引起重视，咨询专业医生。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bs_5.png?time=1439365046\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bs_5x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\"<33.3\"" +
            "}" +
            "]," +
            "\"state\":6," +
            "\"result\":\"过高\"," +
            "\"description\":\"如有多尿、多饮、乏力、恶心、呕吐、呼吸深快、呼气中有烂苹果味等症状，则可能存在糖尿病酮症酸中毒的风险，请立即去医院就诊。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bs_6.png?time=1439365046\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bs_6x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1\":\">=33.3\"" +
            "}" +
            "]," +
            "\"state\":7," +
            "\"result\":\"极高\"," +
            "\"description\":\"可能存在高血糖高渗综合征的风险，请立即去医院就诊治疗。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_bs_7.png?time=1439365046\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_bs_7x.png?time=1448607757\"" +
            "}" +
            "]," +
            "\"fh\":[" +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1_avg\":\"<=109\"," +
            "\"beyond10\":\"N\"" +
            "}" +
            "]," +
            "\"state\":1," +
            "\"result\":\"心动过缓\"," +
            "\"description\":\"胎心率偏低，建议您测量持续时间至少10分钟，如胎心率持续<110bpm,说明胎儿心动过缓，您的宝宝可能存在缺氧风险，请立即咨询您的医生。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_fh.png?time=1428401180\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_fh.png?time=1428401180\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1_avg\":\"<=109\"," +
            "\"beyond10\":\"Y\"" +
            "}" +
            "]," +
            "\"state\":1," +
            "\"result\":\"心动过缓\"," +
            "\"description\":\"胎儿心动过缓，如果您的宝宝心率一直<110bpm，则可能存在缺氧风险，请及时咨询您的医生。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_fh.png?time=1428401180\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_fh.png?time=1428401180\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1_avg\":\"<=160\"" +
            "}" +
            "]," +
            "\"state\":2," +
            "\"result\":\"正常\"," +
            "\"description\":\"胎心率正常，您的宝宝很健康，请安心等待您的宝宝降生吧！\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_fh.png?time=1428401180\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_fh.png?time=1428401180\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1_avg\":\">160\"," +
            "\"beyond10\":\"N\"" +
            "}" +
            "]," +
            "\"state\":3," +
            "\"result\":\"心动过速\"," +
            "\"description\":\"胎心率过高，建议您测量持续时间至少10分钟，如胎心率持续>160bpm,说明胎儿心动过速，您的宝宝可能存在缺氧风险，请立即咨询您的医生。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_fh.png?time=1428401180\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_fh.png?time=1428401180\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"value1_avg\":\">160\"," +
            "\"beyond10\":\"Y\"" +
            "}" +
            "]," +
            "\"state\":3," +
            "\"result\":\"心动过速\"," +
            "\"description\":\"胎儿心动过速，如果您的宝宝心率一直>160bpm，可能存在缺氧风险，请及时咨询您的医生。\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_fh.png?time=1428401180\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_fh.png?time=1428401180\"" +
            "}" +
            "]," +
            "\"ecg\":[]," +
            "\"tall\":[" +
            "{" +
            "\"conds\":[" +
            "[]" +
            "]," +
            "\"state\":1," +
            "\"result\":\"正常\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":null," +
            "\"imageUrlX\":null" +
            "}" +
            "]," +
            "\"weight\":[" +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"user.ruleState\":1" +
            "}" +
            "]," +
            "\"state\":0," +
            "\"result\":\"\"," +
            "\"description\":\"\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"bmi\":\">0,<18.5\"" +
            "}" +
            "]," +
            "\"state\":1," +
            "\"result\":\"偏轻\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_weight_1.png?time=1447237086\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_weight_1x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"bmi\":\"<=23.9\"" +
            "}" +
            "]," +
            "\"state\":2," +
            "\"result\":\"正常\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_weight_2.png?time=1447237086\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_weight_2x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"bmi\":\"<=27.9\"" +
            "}" +
            "]," +
            "\"state\":3," +
            "\"result\":\"偏重\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_weight_3.png?time=1447237086\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_weight_3x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"bmi\":\">=28\"" +
            "}" +
            "]," +
            "\"state\":5," +
            "\"result\":\"过重\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_weight_5.png?time=1448001174\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_weight_5x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"user.ruleState\":9999" +
            "}" +
            "]," +
            "\"state\":101," +
            "\"result\":\"增长过缓\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_weight_101.png?time=1448531404\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_weight_101x.png?time=1448531404\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"user.ruleState\":9999" +
            "}" +
            "]," +
            "\"state\":2," +
            "\"result\":\"正常\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_weight_2.png?time=1447237086\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_weight_2x.png?time=1448607757\"" +
            "}," +
            "{" +
            "\"conds\":[" +
            "{" +
            "\"user.ruleState\":9999" +
            "}" +
            "]," +
            "\"state\":102," +
            "\"result\":\"增长过快\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":\"http://dev.mcloudlife.com/img/chat/gm_weight_102.png?time=1448531404\"," +
            "\"imageUrlX\":\"http://dev.mcloudlife.com/img/chat/gm_weight_102x.png?time=1448531404\"" +
            "}" +
            "]," +
            "\"wl\":[" +
            "{" +
            "\"conds\":[" +
            "[]" +
            "]," +
            "\"state\":1," +
            "\"result\":\"正常\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":null," +
            "\"imageUrlX\":null" +
            "}" +
            "]," +
            "\"bt\":[" +
            "{" +
            "\"conds\":[" +
            "[]" +
            "]," +
            "\"state\":2," +
            "\"result\":\"正常\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":null," +
            "\"imageUrlX\":null" +
            "}" +
            "]," +
            "\"bbt\":[" +
            "{" +
            "\"conds\":[" +
            "[]" +
            "]," +
            "\"state\":2," +
            "\"result\":\"正常\"," +
            "\"description\":\"\"," +
            "\"imageUrl\":null," +
            "\"imageUrlX\":null" +
            "}" +
            "]," +
            "\"up\":[]," +
            "\"ua\":[]," +
            "\"check\":[]" +
            "}";
}
