package imagination.ga.marsh;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by 44260 on 2016/2/4.
 */
public interface RequestParam<K, V> {
    JSONObject assembleJson();

    HashMap<K, V> assembleMap();
}
