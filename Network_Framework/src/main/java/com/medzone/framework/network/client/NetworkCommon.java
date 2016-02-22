package com.medzone.framework.network.client;

import android.text.TextUtils;

import com.medzone.framework.network.NetworkParams;
import com.medzone.framework.network.exception.RestException;
import com.medzone.framework.network.serializer.ISerializer;

import org.json.JSONException;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class NetworkCommon<T> {

    protected static final String KEY_ACCESS_TOKEN = "access_token";
    protected String mHostURI;
    protected String mAppVersion;
    protected HashMap<String, String> mCookies;
    protected ISerializer<?> iSerializer;
    protected String credentials;

    public NetworkCommon(String hostURI) {
        this.mHostURI = hostURI;
        initISerializer();
    }

    public void setSerializer(ISerializer<T> serializer) {
        this.iSerializer = serializer;
    }

    abstract void initISerializer();

    // -----------------------------Cookies-------------------------------

    protected void setCookie(String paramString) {
        String[] arrayOfString = paramString.split("=");

        if (arrayOfString.length != 2) return;

        if (mCookies == null) {
            mCookies = new HashMap<String, String>();
        }
        if (mCookies.containsKey(arrayOfString[0])) {
            mCookies.remove(arrayOfString[0]);
        }
        mCookies.put(arrayOfString[0], arrayOfString[1]);
    }

    /**
     * @param request 为request附加Cookies
     */
    protected void addCookiesForRequest(HttpRequestBase request) {
        final String cookiesString = getCookiesString();
        request.removeHeaders("Cookie");
        if (cookiesString != null && cookiesString.length() > 0) {
            request.addHeader("Cookie", cookiesString);
        }
    }

    protected String getCookiesString() {
        StringBuilder cookiesStringBuilder = new StringBuilder();

        Iterator<?> cookieIterator;
        if ((this.mCookies != null) && (this.mCookies.size() > 0)) {
            cookieIterator = this.mCookies.entrySet().iterator();
            while (cookieIterator.hasNext()) {

                @SuppressWarnings("unchecked")
                Map.Entry<Object, Object> cookieEntry = (Map.Entry<Object, Object>) cookieIterator.next();
                cookiesStringBuilder.append((String) cookieEntry.getKey());
                cookiesStringBuilder.append("=");
                cookiesStringBuilder.append((String) cookieEntry.getValue());
                cookiesStringBuilder.append(";");
            }

        }
        return cookiesStringBuilder.toString();
    }

    protected void clearCookies() {
        if (mCookies != null) mCookies.clear();
    }

    /**
     * @param resourceURI 资源链接，通常它类似于：“/api/contacts/”
     * @return 完整的请求URI，通常他类似于：“http://www.domain.com/api/contacts/”
     */
    protected URI getURI(String resourceURI) {

        if (TextUtils.isEmpty(mHostURI)) {
            try {
                throw new RestException("Please specify the address of the api host.");
            } catch (RestException e) {
                e.printStackTrace();
            }
        }
        if (resourceURI.contains("http://") || resourceURI.contains("https://") || resourceURI.contains("ftp://")) {
            return URI.create(resourceURI);
        } else {
            String requestPath = mHostURI.concat(resourceURI);
            return URI.create(requestPath);
        }
    }

    /**
     * @param resource 如果传入的method非法，则默认使用GET。
     * @param params   资源链接，通常它类似于：“/api/contacts/”
     * @return 返回指定method，对应的request实例。
     */
    protected HttpRequestBase createHttpRequest(String resource, NetworkParams params) {

        Args.notNull(params, "params");

        HttpRequestBase request;
        URI uri = getURI(resource);
        switch (params.getHttpMethod()) {
            case OPTIONS:
                request = new HttpOptions(uri);
                break;
            case GET:
                request = new HttpGet(uri);
                break;
            case HEAD:
                request = new HttpHead(uri);
                break;
            case POST:
                request = new HttpPost(uri);
                break;
            case PUT:
                request = new HttpPut(uri);
                if (credentials != null) {
                    request.addHeader("Authorization", "mCloud " + credentials);
                    Log.w(Log.CORE_FRAMEWORK, "Authorization:mCloud " + credentials);
                }
                String host = uri.getHost();
                if (!TextUtils.isEmpty(host)) {
                    request.addHeader("Host", host);
                    // request.addHeader("Host", "db.mcloudlife.com");
                }
                break;
            case DELETE:
                request = new HttpDelete(uri);
                break;
            case TRACE:
                request = new HttpTrace(uri);
                break;
            default:
                request = new HttpPost(uri);
                break;
        }

        addCookiesForRequest(request);

        return request;
    }

    // -----------------------------HttpAction---------------------

    /**
     * 返回HttpClient对象，通常这是全局单例对象。
     *
     * @return
     */
    abstract HttpClient getHttpClient();

    /**
     * 发起一次HTTP交互，并返回交互结果。
     *
     * @param uri    资源链接，诸如：“/api/contacts/”
     * @param params 请求参数
     * @return
     * @throws RestException
     */
    abstract Object httpConnect(String uri, NetworkParams params) throws RestException;

    /**
     * @param response HttpClient执行后响应结果。
     * @return 已经被{@link ISerializer}解析后的结果。
     * @throws ParseException
     * @throws IOException
     * @throws JSONException
     */
    abstract Object processHttpResponse(HttpResponse response) throws ParseException, IOException, JSONException;

}
