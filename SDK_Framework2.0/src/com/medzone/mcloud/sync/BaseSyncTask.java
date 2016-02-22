package com.medzone.mcloud.sync;

import java.util.List;

import org.json.JSONObject;

import android.util.Log;

import com.medzone.framework.network.NetworkClientResult;
import com.medzone.framework.task.BaseResult;
import com.medzone.framework.task.BaseTask;
import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.LocalCode;

public abstract class BaseSyncTask<T> extends BaseTask {

	private BaseSyncTaskHost<T>	taskHost;
	protected List<T>			list;

	public BaseSyncTask() {
		super(0);
	}

	@Override
	protected void onPostExecute(BaseResult result) {
		super.onPostExecute(result);
		NetworkClientResult res = (NetworkClientResult) result;
		Log.d("espresso", "response[ code:" + res.getErrorCode() + ",message:" + res.getErrorMessage() + " ]");
		if (res.getErrorCode() != LocalCode.CODE_SUCCESS) return;
		if (res.getResponseResult() == null) {
			Log.d(getClass().getSimpleName(), "没有可用的json内容");
		}
		parse(res.getResponseResult());
		if (taskHost != null) {
			taskHost.onPostExecuteCacheChanged(list);
		}
	}

	protected abstract void parse(JSONObject jsonObject);

	public void setSyncTaskHost(BaseSyncTaskHost<T> taskHost) {
		this.taskHost = taskHost;
	}

}
