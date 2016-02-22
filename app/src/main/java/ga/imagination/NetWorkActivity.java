package ga.imagination;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ga.imagination.databinding.ActivityNetworkBinding;

/**
 * Created by 44260 on 2016/1/14.
 */
public class NetWorkActivity extends Activity {
    public static final String TAG = "TAG:NetWorkActivity";
    private ActivityNetworkBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_network);
    }

    public void Post(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                try {
                    url = new URL("http://192.168.0.1/api/marsh/");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoOutput(true);
                    String prop = flushProperty(httpURLConnection.getRequestProperties());
                    writeStream(httpURLConnection.getOutputStream());
                    InputStream inputStream = httpURLConnection.getInputStream();
                    binding.setDisplay("properties:" + prop + "\n" + "method: " + httpURLConnection.getRequestMethod() + "\nContentEncoding: " + httpURLConnection.getContentEncoding() + "\nResponseMessage: " + httpURLConnection.getResponseMessage() + "\nResponseCode: " + httpURLConnection.getResponseCode() + "\nUrl: " + httpURLConnection.getURL().toString() + "\n" + "\ninputStream: " + readStream(httpURLConnection.getInputStream()) + "\nerrorStream: " + readStream(httpURLConnection.getErrorStream()));
                    readStream(inputStream);
                    readStream(httpURLConnection.getErrorStream());
                    httpURLConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    binding.setDisplay(e.toString());
                }

            }
        }).start();
    }

    private String flushProperty(Map<String, List<String>> map) {
        if (map == null)
            return "";
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, List<String>> listEntry : map.entrySet()) {
            builder.append(listEntry.getKey()).append(":").append(Arrays.toString(listEntry.getValue().toArray())).append("\n");
        }
        return builder.toString();
    }

    public String readStream(InputStream inputStream) {
        if (inputStream == null) {
            return "no Data";
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

    public void writeStream(OutputStream outputStream) {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "network");
        map.put("error", "no connection");
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        try {
            outputStream.write(stringBuilder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Clear(View view) {
        binding.setDisplay("");
    }

    public void Put(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File directory = new File(Environment.getExternalStorageDirectory() + "/medzone/log");
                if (!directory.isDirectory() || !directory.exists())
                    return;
                File[] files = directory.listFiles();
                if (files == null || files.length == 0)
                    return;
                for (File file : files) {
                    uploadFile(file);
                }
            }
        }).start();
    }

    private void uploadFile(File file) {
        try {
            URL url = new URL("http://imagination.ga/api/attachment/");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            byte[] buffer = readFile(file);
            HashMap<String, String> map = new HashMap<>();
            map.put("filename", file.getName());
            map.put("content", new String(buffer));
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            StringBuilder stringBuilder = new StringBuilder();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            try {
                httpURLConnection.getOutputStream().write(stringBuilder.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            readStream(httpURLConnection.getErrorStream());
            binding.setDisplay("method: " + httpURLConnection.getRequestMethod() + "\nContentEncoding: " + httpURLConnection.getContentEncoding() + "\nResponseMessage: " + httpURLConnection.getResponseMessage() + "\nResponseCode: " + httpURLConnection.getResponseCode() + "\nUrl: " + httpURLConnection.getURL().toString() + "\n" + "\ninputStream: " + readStream(httpURLConnection.getInputStream()) + "\nerrorStream: " + readStream(httpURLConnection.getErrorStream()));
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            binding.setDisplay(e.toString());
        }
    }

    private byte[] readFile(@NonNull File file) {
        ByteArrayOutputStream outputStream = null;
        RandomAccessFile randomAccessFile = null;
        byte[] result = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            final int MAX_BUFFER_SIZE = 1024;
            byte[] buffer = new byte[MAX_BUFFER_SIZE];
            int totalLength = (int) file.length();
            int len;
            int offset = 0;
            int readLen = 0;
            randomAccessFile.seek(offset);
            outputStream = new ByteArrayOutputStream();
            while (readLen < totalLength) {
                int byteCount = totalLength > MAX_BUFFER_SIZE ? MAX_BUFFER_SIZE : totalLength;
                len = randomAccessFile.read(buffer, 0, byteCount);
                outputStream.write(buffer, 0, len);
                readLen += len;
            }
            result = outputStream.toByteArray();
            Log.d(TAG, "result length:" + (result == null ? 0 : result.length) + ",total:" + totalLength);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException ignored) {
            }
        }
        return result;
    }
}
