/**
 * 
 */
package com.medzone.mcloud.defender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.medzone.framework.util.ToastUtils;
import com.medzone.framework.Config;

/**
 * @author Robert.
 * 
 */
public class ConnectivityReceiver extends BroadcastReceiver {

	private ConnectivityManager	connectivityManager;
	private NetworkInfo			info;

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();

		if (Config.isDeveloperMode) {
			ToastUtils.show(context, action);
		}
		if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
			connectivityManager = (ConnectivityManager)

			context.getSystemService(Context.CONNECTIVITY_SERVICE);
			info = connectivityManager.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				String name = info.getTypeName();

				Intent mIntent = new Intent(BroadCastUtil.ACTION_NET_CONNECTED);
				mIntent.putExtra("extra", name);
				context.sendBroadcast(mIntent);

			} else {
				Intent mIntent = new Intent(BroadCastUtil.ACTION_NET_DISCONNECTED);
				context.sendBroadcast(mIntent);
			}
		}
	}
}
