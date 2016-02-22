package com.medzone.framework.network;

import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.medzone.framework.Log;
import com.medzone.framework.data.errorcode.CodeProxy;
import com.medzone.framework.network.exception.RestException;
import com.medzone.framework.task.BaseResult;
import com.medzone.framework.util.Asserts;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 
 * @author Robert.
 * 
 */
final class NetworkClientManager {

	private volatile String apiURL;
	protected String viewURL;

	private static final NetworkClientManager instance	= new NetworkClientManager();

	private static INetworkClientWrapper<JSONObject, Object>	mClient;

	private NetworkClientManager() {
	}

	public static NetworkClientManager getInstance() {
		return instance;
	}

	protected void loadClient(INetworkClientWrapper<JSONObject, Object> mINetworkClient) {
		mClient = mINetworkClient;
	}

	private static boolean testDNS(String hostname) {

		if (hostname.contains("http://")) {
			hostname = hostname.replaceAll("http://", "");
		}
		Log.d(Log.CORE_FRAMEWORK, "testDNS:" + hostname);
		try {
			DNSResolver dnsRes = new DNSResolver(hostname);
			Thread t = new Thread(dnsRes);
			t.start();
			t.join(5000);
			InetAddress inetAddr = dnsRes.get();
			return inetAddr != null;
		}
		catch (Exception e) {
			return false;
		}
	}

	private static class DNSResolver implements Runnable {
		private String domain;
		private InetAddress inetAddr;

		public DNSResolver(String domain) {
			this.domain = domain;
		}

		public void run() {

			try {
				InetAddress addr = InetAddress.getByName(domain);
				set(addr);
			}
			catch (UnknownHostException e) {
			}
		}

		public synchronized void set(InetAddress inetAddr) {
			this.inetAddr = inetAddr;
			Log.d(Log.CORE_FRAMEWORK, inetAddr.toString());
		}

		public synchronized InetAddress get() {
			return inetAddr;
		}
	}

	synchronized boolean requestDistributeUrl() throws RestException {

		if (null != apiURL) {
			if (!testDNS(apiURL)) {
				Log.w(Log.CORE_FRAMEWORK, "域名无法解析：" + apiURL);
				return false;
			}
			return true;
		}
		else {
			if (!testDNS(NetworkClientManagerProxy.API_URI)) {
				Log.w(Log.CORE_FRAMEWORK, "域名无法解析：" + NetworkClientManagerProxy.API_URI);
				return false;
			}
		}
		Asserts.check(Looper.getMainLooper() != Looper.myLooper(), "You silently has a crush on me behind my back (Thread).");

		NetworkClientManagerProxy.readyClient(NetworkClientManagerProxy.API_URI);
		JSONObject param = new JSONObject();
		try {
			param.put("syncid", NetworkClientManagerProxy.getLoginSyncid());
		}
		catch (JSONException e1) {
			e1.printStackTrace();
		}
		NetworkClientResult result = (NetworkClientResult) NetworkClientManagerProxy.callWithoutCheckState(NetworkClientManagerProxy.RESOURCE_VERSION,
				NetworkParams.obtainBuilder().setConnectTimeOut(5000).setSocketTimeOut(5000).setHttpEntity(param));
		NetworkClientManagerProxy.discardClient();
		if (null != apiURL) {
			return true;
		}
		if (result.getErrorCode() == CodeProxy.CODE_SUCCESS) {
			JSONObject json = result.getResponseResult();
			if (json.has("apihost") && !json.isNull("apihost")) {
				try {
					apiURL = json.getString("apihost");
					Log.i(Log.CORE_FRAMEWORK, "动态分派(apihost):" + apiURL);
					if (!apiURL.contains("http://")) {
						apiURL = "http://" + apiURL;
					}
					NetworkClientManagerProxy.readyClient(apiURL);
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (json.has("viewhost") && !json.isNull("viewhost")) {
				try {
					viewURL = json.getString("viewhost");
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (NetworkClientManagerProxy.mApiHostHandler != null) {
				json.remove("apihost");
				json.remove("viewhost");
				Message msg = NetworkClientManagerProxy.mApiHostHandler.obtainMessage();
				msg.obj = json;
				msg.sendToTarget();
			}
		}
		return !TextUtils.isEmpty(apiURL);
	}

	public synchronized BaseResult call(String uri, NetworkParams.Builder params, boolean isCheckAPIPath) {

		NetworkClientResult result = new NetworkClientResult();

		if (isCheckAPIPath) {
			try {
				if (!requestDistributeUrl()) {
					result.setErrorMessage("Unable to get api host.");
					return result;
				}
			}
			catch (RestException e) {
				e.printStackTrace();
			}
		}
		if (!TextUtils.isEmpty(uri)) {
			try {
				if (mClient == null) {
					result.setErrorMessage("It is necessary to load the network client.");
				}
				else {
					Object httpRet = mClient.call(uri, params);
					JSONObject ret = (JSONObject) httpRet;
					result.setResponseResult(ret);
				}
			}
			catch (RestException e) {
				e.printStackTrace();
			}
		}
		return result;

	}

}
