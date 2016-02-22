package com.medzone.framework.network.client;

import android.text.TextUtils;
import android.util.Log;

import com.medzone.framework.network.INetworkClientWrapper;
import com.medzone.framework.network.NetworkParams;
import com.medzone.framework.network.exception.RestException;
import com.medzone.framework.network.serializer.SerializerJsonImp;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Locale;


/**
 * @ blue
 */
public class JsonRestClient extends NetworkCommon<JSONObject> implements INetworkClientWrapper<JSONObject, Object, HttpURLConnection> {

    long timeMillisBegin;
    long timeMillisEnd;
    /**
     * 我们可以完全创建一个新的Client对象，投递处理。限制全局单例Client对象，在某些情景下受到局限性。
     * 当然目前我们依然能够使用单例场景，走通整个流程。
     */
    private HttpURLConnection mClient;

    public JsonRestClient(String uri) {
        super(uri);
    }

    @Override
    void initISerializer() {
        iSerializer = new SerializerJsonImp();
    }

    void print(String resource, NetworkParams params) {
        if (params != null) {
            Log.d(INetworkClientWrapper.TAG, "call:" + resource);
            Log.d(INetworkClientWrapper.TAG, "params:" + params.toString());
        }
    }

    @Override
    Object httpConnect(String uri, NetworkParams params) throws RestException {
        timeMillisBegin = System.currentTimeMillis();
        print(uri, params);
        // 更新credentials
        if (!TextUtils.isEmpty(params.getAccessToken())) {
            credentials = params.getAccessToken();
        }
        if (!TextUtils.isEmpty(params.getAppVersion())) {
            mAppVersion = params.getAppVersion();
        }
        // 处理请求响应结果
        final DefaultHttpClient client = (DefaultHttpClient) getHttpClient();
        client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, params.getConnectTimeOut());
        client.getParams().setParameter(HttpConnectionParams.SO_TIMEOUT, params.getSocketTimeOut());

        try {
            HttpRequestBase request = createHttpRequest(uri, params);
            request.setHeader("Accept-Language", getAcceptLanguage());

            if (request instanceof HttpPost) {
                ((HttpPost) request).setEntity(params.getHttpEntity());
            } else if (request instanceof HttpPut) {
                ((HttpPut) request).setEntity(params.getHttpEntity());
            } else {
                Log.w(Log.CORE_FRAMEWORK, "除Post或Put方法外，其他方式无法setEntity设置BodyContent");
            }

            HttpResponse response = client.execute(request);

            Object ret = processHttpResponse(response);

            return ret;
        } catch (IOException e) {
            Log.w(Log.CORE_FRAMEWORK, "response:{IOException:" + e.getMessage() + "," + e.getLocalizedMessage() + "}");
            throw new RestException(e);
        } catch (JSONException e) {
            Log.w(Log.CORE_FRAMEWORK, "response:{JSONException:" + e.getMessage() + "," + e.getLocalizedMessage() + "}");
            throw new RestException(e);
        } finally {
            timeMillisEnd = System.currentTimeMillis();
            Log.d(Log.CORE_FRAMEWORK, String.format("request api: %s cost time：" + (timeMillisEnd - timeMillisBegin), uri));
        }

    }

    @Override
    HttpURLConnection getHttpClient() {
        if (mClient == null) {
            mClient = createClient();
        }
        return mClient;
    }

    @Override
    public HttpClient createClient() {

        // Prepare basic parameters
        HttpParams params = new BasicHttpParams();
        params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        StringBuilder builder = new StringBuilder();
        builder.append(System.getProperty("http.agent"));
        if (mAppVersion != null) {
            builder.append(" mCloud/");
            builder.append(mAppVersion);
        }
        params.setParameter(CoreProtocolPNames.USER_AGENT, builder.toString());
        params.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);

        params.setParameter("Accept", "application/json");
        // params.setParameter("Accept-Language", getAcceptLanguage());
        params.setParameter("Accept-Encoding", "UTF-8");

        return new DefaultHttpClient(params);
    }

    private String getAcceptLanguage() {
        Locale lc = Locale.getDefault();
        StringBuilder builder = new StringBuilder();
        builder.append(lc.getLanguage());
        builder.append(";en;q=1, fr;q=0.9, de;q=0.8, zh-Hans;q=0.7, zh-Hant;q=0.6, ja;q=0.5");
        return builder.toString();
    }

    @Override
    public Object call(String uri, NetworkParams.Builder params) throws RestException {
        return httpConnect(uri, params.build());
    }

    @Override
    Object processHttpResponse(HttpResponse response) throws ParseException, IOException, JSONException {

        Header[] headers = response.getHeaders("Set-Cookie");
        for (Header header : headers) {
            String[] parmsStrings = header.getValue().split(";");
            if (parmsStrings.length > 0) {
                setCookie(parmsStrings[0]);
            }
        }
        final StatusLine statusLine = response.getStatusLine();
        final int statusCode = statusLine.getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            return obtainServerExceptionRet(statusCode);
        }
        HttpEntity callBackEntity = response.getEntity();
        String jsonCallBack = EntityUtils.toString(callBackEntity);
        Object ret = iSerializer.deserialize(jsonCallBack);

        if (null == ret) {
            return obtainServerExceptionRet(statusCode);
        }
        return ret;
    }

    private JSONObject obtainServerExceptionRet(int... is) throws JSONException {
        JSONObject exceptionJson = new JSONObject();
        exceptionJson.put("errcode", CodeProxy.CODE_10005);
        if (null != is) {
            exceptionJson.put("errmsg", "HttpStatus :" + is[0]);
        }
        return exceptionJson;
    }

    @Override
    public void destroyClient() {
        if (mClient != null) {
            synchronized (mClient) {
                clearCookies();
                mClient.disconnect();
                mClient = null;
            }
        }
    }

}
