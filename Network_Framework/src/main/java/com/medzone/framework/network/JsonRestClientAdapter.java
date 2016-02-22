package com.medzone.framework.network;

import com.medzone.framework.network.client.JsonRestClient;
import com.medzone.framework.network.exception.RestException;

import org.apache.http.client.HttpClient;
import org.json.JSONObject;


/**
 * 
 * @author Robert.
 */
public class JsonRestClientAdapter implements INetworkClientWrapper<JSONObject, Object> {

	private JsonRestClient	mNetworkClient;

	public JsonRestClientAdapter(String hostURI) {
		mNetworkClient = new JsonRestClient(hostURI);
	}

	@Override
	public HttpClient createClient() {
		return mNetworkClient.createClient();
	}

	@Override
	public Object call(String uri, NetworkParams.Builder params) throws RestException {
		return mNetworkClient.call(uri, params);
	}

	@Override
	public void destroyClient() {
		mNetworkClient.destroyClient();
	}

}
