/**
 * 
 */
package com.medzone.mcloud.task;

import com.medzone.mcloud.network.NetworkClient;
import com.medzone.framework.task.BaseResult;
import com.medzone.framework.task.BaseTask;

/**
 * @author Robert.
 * 
 */
public class DoRuleMatchTask extends BaseTask {

	private Integer	syncid;
	private String	type;
	private Float	value1;
	private Float	value2;
	private Float	value3;
	private Float   avg;
	private Integer valueDuration;
	private Integer	valuePeriod;
	private Integer state;
	
	private String	eval;

	public DoRuleMatchTask(Integer syncid, String type, Float value1, Float value2, Float value3,Float avg, Integer valueDuration, Integer valuePeriod,
			Boolean eval) {
		super(0);
		this.syncid = syncid;
		this.type = type;
		this.value1 = value1;
		this.value2 = value2;
		this.value3 = value3;
		this.avg    = avg;
		this.valueDuration = valueDuration;
		this.valuePeriod = valuePeriod;
		if (eval != null) {
			this.eval = eval ? "Y" : "N";
		}
	}

	public DoRuleMatchTask(Integer syncid, String type, Float value1, Float value2, Float value3,Float avg, Integer valueDuration, Integer valuePeriod,
			Boolean eval,Integer state) {
	   this(syncid, type, value1, value2, value3, avg, valueDuration, valuePeriod, eval);
	   this.state = state;
	
	}
	
	@Override
	protected BaseResult doInBackground(Void... params) {

		return NetworkClient.getInstance().doRuleMatch(syncid, type, value1, value2, value3,avg, valueDuration,valuePeriod, eval,state);

	}

}