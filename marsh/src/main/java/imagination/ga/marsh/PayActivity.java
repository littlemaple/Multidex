package imagination.ga.marsh;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import imagination.ga.marsh.databinding.ActivityPayBinding;

/**
 * Created by 44260 on 2016/2/4.
 */
public class PayActivity extends AppCompatActivity {
    private ActivityPayBinding binding;
    private HashMap<String, Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pay);
        binding.setApiMode(Config.IS_NATIVE);
    }

    public void performPay(View view) {
    }

    public void requestOrder(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ISerializer serializer = new SerializerXML();
                map = (HashMap<String, Object>) serializer.deserialize(NetworkClient.RequestOrder(serializer.serialize(getCompleteParams())));
                if (map == null) {
                    return;
                }
                StringBuilder buffer = new StringBuilder();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    buffer.append(entry.getKey()).append("=>").append(entry.getValue()).append("\n");
                }
                binding.setDisplay(buffer.toString());
            }
        }).start();
    }

    public void obtainIp(View view) {
        final Button button = (Button) view;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String ip = getIp(NetworkClient.UNIFIED_ORDER, true);
                button.post(new Runnable() {
                    @Override
                    public void run() {
                        button.setText(ip);
                    }
                });
            }
        }).start();


    }

    public void switchApi(View view) {
        Config.IS_NATIVE = !Config.IS_NATIVE;
        binding.setApiMode(Config.IS_NATIVE);
    }

    public void obtain(final View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ISerializer serializer = new SerializerXML();
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.setDisplay(serializer.serialize(getCompleteParams()));
                    }
                });
            }
        }).start();
    }

    public static HashMap<String, Object> createTest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("appid", Constant.APP_ID);
        map.put("mch_id", Constant.MCH_ID);
        map.put("nonce_str", createNonceStr());
        map.put("body", "MarshMallow");
        map.put("out_trade_no", "20160204140022");
        map.put("total_fee", 1);
        map.put("spbill_create_ip", getIp(Constant.UNIFIED_ORDER, false));
        map.put("trade_type", UnifiedOrder.TradeType.APP.toString());
        map.put("notify_url", Constant.NOTIFY_URL);
        return map;
    }

    public static HashMap<String, Object> getCompleteParams() {
        HashMap<String, Object> map = createTest();
        map.put("sign", createSign(map));
        return map;
    }

    public static String createNonceStr() {
        return "wx335dd0edfa16ae15";
    }

    public static String getIp(String url, boolean hasName) {
        try {
            URL u = new URL(url);
            if (hasName)
                return String.valueOf(InetAddress.getByName(u.getHost()));
            return String.valueOf(InetAddress.getByName(u.getHost()).getHostAddress());
        } catch (MalformedURLException | UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String createSign(HashMap<String, Object> params) {
        if (params == null || params.size() == 0)
            return null;
        TreeMap<String, Object> treeMap = new TreeMap<>(params);
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Object> entry : treeMap.entrySet()) {
            if (entry.getValue() == null)
                continue;
            result.append(entry.getKey()).append("=").append(entry.getValue());
            result.append("&");
        }
        result.append("key=").append(Constant.APP_KEY);
        Log.d(UnifiedOrder.class.getSimpleName(), "origin=>" + result.toString());
        Log.d(UnifiedOrder.class.getSimpleName(), "md5=>" + MD5Util.getMD5String(result.toString()).toUpperCase());
        return MD5Util.getMD5String(result.toString()).toUpperCase();
    }
}
