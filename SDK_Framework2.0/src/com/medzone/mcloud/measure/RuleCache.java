package com.medzone.mcloud.measure;

import java.util.List;

import com.medzone.mcloud.cache.AbstractDBObjectListCache;
import com.medzone.framework.Log;
import com.medzone.mcloud.util.BaseMeasureDataUtil;
import com.medzone.framework.Config;
import com.medzone.mcloud.data.bean.IRuleDetails;
import com.medzone.mcloud.data.bean.dbtable.BaseMeasureData;
import com.medzone.mcloud.data.bean.dbtable.BloodOxygen;
import com.medzone.mcloud.data.bean.dbtable.BloodPressure;
import com.medzone.mcloud.data.bean.dbtable.BloodSugar;
import com.medzone.mcloud.data.bean.dbtable.EarTemperature;
import com.medzone.mcloud.data.bean.dbtable.Rule;

/**
 * 
 * @author Robert
 * 
 */
public class RuleCache extends AbstractDBObjectListCache<Rule> {

	public Rule readRuleByData(BaseMeasureData data) {

		boolean matched = false;

		IRuleDetails ird = (IRuleDetails) data;
		Rule matchedRule = null;
		if (ird.getRulesCollects() == null) {
			if (Config.isDeveloperMode || Config.isTesterMode) {
				Log.w(getClass().getSimpleName(), "尚未录入规则库，或规则库尚未同步成功。");
			}
			return null;
		}
		for (Rule rule : ird.getRulesCollects()) {
			matchedRule = rule;
			double min1 = rule.getMin1();
			double max1 = rule.getMax1();
			double min2 = rule.getMin2();
			double max2 = rule.getMax2();

			if (data instanceof BloodPressure)

			{
				float bpHigh = ((BloodPressure) data).getHigh();
				bpHigh = BaseMeasureDataUtil.convertAccuracy(bpHigh).floatValue();
				float bpLow = ((BloodPressure) data).getLow();
				bpLow = BaseMeasureDataUtil.convertAccuracy(bpLow).floatValue();
				// int bpRate = ((BloodPressureBean) data).getRate();

				if (min1 == 0) {
					matched = bpHigh <= max1;
				}
				else if (max1 == 0) {
					matched = bpHigh >= min1;
				}
				else {
					matched = bpHigh >= min1 && bpHigh <= max1;
				}

				if (min2 == 0) {
					matched = matched && bpLow <= max2;
				}
				else if (max2 == 0) {
					matched = matched || bpLow >= min2;
				}
				else {
					matched = matched && bpLow >= min2 && bpLow <= max2;
				}
			}
			else if (data instanceof BloodOxygen) {
				int oxygen = 0;
				if (((BloodOxygen) data).getOxygen() != null)

				oxygen = ((BloodOxygen) data).getOxygen().intValue();
				// int oxRate = ((OxygenBean) data).getRate();

				if (min1 == 0) {
					matched = oxygen <= max1;
				}
				else if (max1 == 0) {
					matched = oxygen >= min1;
				}
				else {
					matched = oxygen >= min1 && oxygen <= max1;
				}
			}
			else if (data instanceof EarTemperature) {
				float temperture = ((EarTemperature) data).getTemperature();
				temperture = BaseMeasureDataUtil.convertAccuracy(temperture).floatValue();

				if (min1 == 0) {
					matched = temperture <= max1;
				}
				else if (max1 == 0) {
					matched = temperture >= min1;
				}
				else {
					matched = temperture >= min1 && temperture <= max1;
				}
			}
			else if (data instanceof BloodSugar) {
				Float sugar = ((BloodSugar) data).getSugar();
				if (sugar == null) return null;
				sugar = BaseMeasureDataUtil.convertAccuracy(sugar).floatValue();

				if (min1 == 0) {
					matched = sugar <= max1;
				}
				else if (max1 == 0) {
					matched = sugar >= min1;
				}
				else {
					matched = sugar >= min1 && sugar <= max1;
				}
			}

			if (matched) return matchedRule;
		}
		return null;
	}

	@Override
	public List<Rule> read() {
		// TODO Auto-generated method stub
		return null;
	}

}
