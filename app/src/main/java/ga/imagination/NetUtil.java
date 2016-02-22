package ga.imagination;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask.Status;
import android.telephony.TelephonyManager;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;


public class NetUtil {

    public static final int NETTYPE_NONE = 0;
    public static final int NETTYPE_WIFI = 1;
    public static final int NETTYPE_MOBILE_GPRS = 2;
    public static final int NETTYPE_MOBILE_EDGE = 3;
    public static final int NETTYPE_MOBILE_3G = 4;
    public static final int NETTYPE_UNKNOW = 99;
    public static final int NETOPERATOR_NONE = 0;
    public static final int NETOPERATOR_CHINA_MOBILE = 1;
    public static final int NETOPERATOR_CHINA_NET = 2;
    public static final int NETOPERATOR_CHINA_UNION = 3;
    public static final int NETOPERATOR_UNKNOW = 99;
    public static final String CONTENT_TYPE_IMAGE = "image";
    private static final int NETWORK_GET_BUFFER_SIZE = 4096;

    private static NetworkInfo getCurrentActiveNetworkInfo(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null)

            return connectivityManager.getActiveNetworkInfo();

        return null;

    }

    public static int getCurrentNetType(Context context) {

        int result = NETTYPE_NONE;

        NetworkInfo localNetworkInfo = getCurrentActiveNetworkInfo(context);

        if (localNetworkInfo == null) {

            return result;

        }

        if (localNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {

            if (localNetworkInfo.getType() == 1) {

                result = NETTYPE_WIFI;

            } else if (localNetworkInfo.getType() == 0) {

                String subTypeName = localNetworkInfo.getSubtypeName()
                        .toUpperCase(Locale.getDefault());

                if (subTypeName.indexOf("GPRS") > 1) {

                    result = NETTYPE_MOBILE_GPRS;

                } else if (subTypeName.indexOf("EDGE") > 1) {

                    result = NETTYPE_MOBILE_EDGE;

                } else {

                    result = NETTYPE_MOBILE_3G;

                }

            } else {

                result = NETTYPE_UNKNOW;

            }

        } else if (localNetworkInfo.getState() == NetworkInfo.State.CONNECTING) {

            result = NETTYPE_UNKNOW;

            System.out.println("connecting " + localNetworkInfo.getType());

        }

        return result;

    }

    public static String getIMSI(Context paramContext) {

        return ((TelephonyManager) paramContext.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();

    }

    public static int getNetworkOperator(Context paramContext) {

        int result = NETOPERATOR_NONE;

        String strIMSI = getIMSI(paramContext);

        if (strIMSI != null) {

            if ((strIMSI.startsWith("46000")) || (strIMSI.startsWith("46002"))) {

                result = NETOPERATOR_CHINA_MOBILE;

            } else if (strIMSI.startsWith("46001")) {

                result = NETOPERATOR_CHINA_UNION;

            } else if (strIMSI.startsWith("46003")) {

                result = NETOPERATOR_CHINA_NET;

            } else {

                result = NETOPERATOR_UNKNOW;

            }

        }

        result = NETOPERATOR_NONE;

        return result;

    }

    public static boolean isConnect(Context context) {

        NetworkInfo localNetworkInfo = getCurrentActiveNetworkInfo(context);

        if (localNetworkInfo != null) {

            if (localNetworkInfo.getState() == NetworkInfo.State.CONNECTED)

                return true;

        }

        return false;

    }

    public static Bitmap getImageFromURL(String url) {

        try {

            return getImageFromURL(new URL(url));

        } catch (MalformedURLException e) {

            e.printStackTrace();

        }

        return null;

    }

    public static Bitmap getImageFromURL(URL url) {

        Bitmap bitmap = null;

        HttpURLConnection connection = null;

        try {

            connection = (HttpURLConnection) url.openConnection();

            if (connection.getResponseCode() != 200) {
                return null;
            }
            if (!CONTENT_TYPE_IMAGE.equalsIgnoreCase(connection
                    .getContentType().substring(0, 5))) {
                return null;
            }

            bitmap = BitmapFactory.decodeStream(connection.getInputStream());

        } catch (IOException e) {

            e.printStackTrace();

        }

        if (connection != null) {

            connection.disconnect();

        }

        return bitmap;

    }

    public static boolean getFileFromURL(String url, String cacheFilePath) {

        try {

            return getFileFromURL(new URL(url), cacheFilePath);

        } catch (MalformedURLException e) {

            e.printStackTrace();

        }

        return false;

    }

    public static boolean getFileFromURL(URL url, String cacheFilePath) {

        HttpURLConnection connection = null;

        File cacheFile = null;

        System.out.println("start to download the image");

        try {

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            // fix bug that cannot download files in android 4.0

            // refer to:
            // http://stackoverflow.com/questions/8919948/issue-with-httpurlconnection-on-android-nexus-v4-0-2

            // connection.setDoOutput(true);

            connection.connect();

            if (connection.getResponseCode() != 200) {
                return false;
            }
            if (!CONTENT_TYPE_IMAGE.equalsIgnoreCase(connection
                    .getContentType().substring(0, 5))) {
                return false;
            }

            System.out.print("print file path" + cacheFilePath);

            cacheFile = new File(cacheFilePath);

            if (!cacheFile.getParentFile().exists()) {

                cacheFile.getParentFile().mkdirs();

            }

            FileOutputStream outputStream = new FileOutputStream(cacheFilePath);

            System.out.print("print create file success!");

            InputStream inputStream = connection.getInputStream();

            byte[] buffer = new byte[NETWORK_GET_BUFFER_SIZE];

            int len = 0;

            while ((len = inputStream.read(buffer)) > 0) {

                outputStream.write(buffer, 0, len);

            }

            outputStream.close();

            return true;

        } catch (IOException e) {

            e.printStackTrace();

            System.out.println("fail to download the image");

        }

        System.out.println("end to download the image");

        return false;

    }
}
