package imagination.ga.marsh;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by 44260 on 2016/2/4.
 */
public abstract class AbstractRequest implements RequestParam<String, Object> {

    @Override
    public HashMap<String, Object> assembleMap() {
        return null;
    }

    @Override
    public JSONObject assembleJson() {
        return null;
    }

    protected boolean checkValid() {
        return false;
    }
}
