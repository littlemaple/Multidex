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
public class GetRuleListTask extends BaseTask {

	private Integer	measuretype;

	public GetRuleListTask(Integer measuretype) {
		super(0);
		this.measuretype = measuretype;
	}

	@Override
	protected BaseResult doInBackground(Void... params) {

		return NetworkClient.getInstance().getRuleList(measuretype);
	}

}