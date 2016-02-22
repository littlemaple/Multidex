package imagination.ga.marsh;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 44260 on 2016/2/4.
 */
public class NetworkClient {
    public static final String UNIFIED_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String LOCAL_API = "http://192.168.0.1/api/marsh/";

    public static String RequestOrder(HashMap<String, Object> params) {
        return perform(HttpMethod.POST, Config.IS_NATIVE ? LOCAL_API : UNIFIED_ORDER, params);
    }

    public static String RequestOrder(String params) {
        return perform(HttpMethod.POST, Config.IS_NATIVE ? LOCAL_API : UNIFIED_ORDER, params);
    }

    public static String perform(HttpMethod method, String requestUrl, HashMap<String, Object> params) {
        return perform(method, requestUrl, transParams(params));
    }

    private static String perform(HttpMethod method, String requestUrl, String params) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod(method.toString());
            httpURLConnection.getOutputStream().write(params.getBytes("UTF-8"));
            ISerializer serializer = new SerializerXML();
            String stream =  readStream(httpURLConnection.getInputStream());
            Map<String, Object> map = (Map<String, Object>) serializer.deserialize(stream);
            httpURLConnection.disconnect();
            return stream;
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private static String readStream(InputStream inputStream) {
        if (inputStream == null) {
            return "";
        }
        final byte[] buffer = new byte[1024];
        final StringBuilder stringBuffer = new StringBuilder();
        try {
            while (inputStream.read(buffer) != -1) {
                stringBuffer.append(new String(buffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    private static String transParams(HashMap<String, Object> params) {
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return stringBuilder.toString();
    }

    public enum HttpMethod {
        PUT, GET, POST, DELETE, TRACE
    }
}
