/**
 * 
 */
package com.medzone.mcloud.network;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.medzone.mcloud.measure.NewRuleController;
import com.medzone.mcloud.preference.RuleListPreference;
import com.medzone.mcloud.task.GetRuleListTask;
import com.medzone.framework.Log;
import com.medzone.framework.network.NetworkClientResult;
import com.medzone.framework.task.BaseResult;
import com.medzone.framework.task.TaskHost;
import com.medzone.framework.util.NetUtil;
import com.medzone.mcloud.errorcode.CloudStatusCodeProxy.LocalCode;

/**
 * @author Robert.
 * 
 */
public final class NetworkClientHelper {

	public static boolean detectAppIsForcedUpdate(Context context,
			String serverApiVersion, Object message) {

		if (message == null || !(message instanceof JSONObject)) {
			return false;
		}
		final JSONObject actionMessage = (JSONObject) message;
//		WindowUtils.showPopupWindow(context, actionMessage);

		return true;
	}



	/**
	 * 更新规则库
	 * 
	 * @param mContext
	 */
	public static void ruleVersionForcedUpdateServer(final Context mContext,String newRuleVersion) {
		if (NetUtil.isTaskNetAvailable(mContext)) {
			if (TextUtils.isEmpty(newRuleVersion)) {
				Log.d(NetworkClientHelper.class.getSimpleName(), ">>>规则库更新失败");
				return;
			}
			// 获取全部规则
			String oldRuleVersion = RuleListPreference.getInstance(mContext).getRuleVersion();
			if (oldRuleVersion.equalsIgnoreCase(newRuleVersion)) {
				Log.d(NetworkClientHelper.class.getSimpleName(), ">>>没有新的规则库");
				return;
			}
			RuleListPreference.getInstance(mContext).saveRuleVersion(newRuleVersion);
			GetRuleListTask ruleListTask = new GetRuleListTask(0);

			final String oldVersion = oldRuleVersion;
			final String newVewsion = newRuleVersion;
			ruleListTask.setTaskHost(new TaskHost(){
				public void onPostExecute(int requestCode, BaseResult result) {
					super.onPostExecute(requestCode, result);
					switch (requestCode) {
					case LocalCode.CODE_SUCCESS:{
						Log.d(NetworkClientHelper.class.getSimpleName(), ">>>规则库更新:");
						JSONObject res = ((NetworkClientResult)result).getResponseResult();
						if(res!=null){
				           System.out.println(">>>"+res.toString());
				           RuleListPreference.getInstance(mContext).saveRuleList(res.toString());

							if (!oldVersion.equalsIgnoreCase(newVewsion)) {
								NewRuleController.getInstance().getCache().saveToCache(res.toString());
							}
			            }
						
						break;
					}
					default:
						Log.d(NetworkClientHelper.class.getSimpleName(), ">>>规则库更新失败");
						break;
					}
					
				}
			});
			ruleListTask.execute();
			
		}
	}

	/**
	 * 执行强制更新操作
	 * 
	 * @param context
	 */
//	public static void doAppForceUpdate(final Context context) {
//		UmengUpdateAgent.setUpdateOnlyWifi(false);
//		UmengUpdateAgent.forceUpdate(context);
//		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() {
//
//			@Override
//			public void onClick(int cmd) {
//				if (cmd == 5) {
//					if (Config.isDeveloperMode) {
//						ToastUtils.show(context,
//								context.getString(string.down_loading));
//					}
//				}
//				// TODO 关闭APPlication的 Activity栈 或者 关闭应用。
//				// ASK：保障下载的进程在不在跑？
//				CloudApplication.getInstance().exit(true);
//			}
//		});
//	}

}
