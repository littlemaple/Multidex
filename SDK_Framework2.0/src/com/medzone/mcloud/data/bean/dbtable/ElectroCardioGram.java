package com.medzone.mcloud.data.bean.dbtable;

import java.util.List;
import java.util.Map;

public class ElectroCardioGram extends BaseMeasureData {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3931276169065563419L;
	public static final String	TAG					= "ecg";

	@Override
	public List<Rule> getRulesCollects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isHealthState() {
		// TODO Auto-generated method stub
		return false;
	}

	public ElectroCardioGram() {
		setTag(TAG);
	}

	@Override
	public String getMeasureName() {
		return TAG;
	}

	@Override
	public void toMap(Map<String, String> result) {

	}
}
