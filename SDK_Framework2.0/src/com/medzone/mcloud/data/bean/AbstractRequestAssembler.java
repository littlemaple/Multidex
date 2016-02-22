package com.medzone.mcloud.data.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Created by 44260 on 2016/2/4.
 */
public abstract class AbstractRequestAssembler implements RequestAssembler<String, Object> {

    @Override
    public HashMap<String, Object> assembleMap() {
        HashMap<String, Object> result = new HashMap<>();
        Field[] fields = getClass().getDeclaredFields();
        if (fields == null || fields.length == 0)
            return null;
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                RestParam unifiedOrder = field.getAnnotation(RestParam.class);
                if (unifiedOrder == null)
                    continue;
                Object object = field.get(this);
                boolean valid = (unifiedOrder.required() && object != null) || !unifiedOrder.required();
                if (!valid)
                    throw new IllegalArgumentException("be sure the " + unifiedOrder.field_name() + " not null");
                if (object == null)
                    continue;
                result.put(unifiedOrder.field_name(), object);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public JSONObject assembleJson() {
        JSONObject result = new JSONObject();
        Field[] fields = getClass().getDeclaredFields();
        if (fields == null || fields.length == 0)
            return null;
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                RestParam unifiedOrder = field.getAnnotation(RestParam.class);
                if (unifiedOrder == null)
                    continue;
                Object object = field.get(this);
                boolean valid = (unifiedOrder.required() && object != null) || !unifiedOrder.required();
                if (!valid)
                    throw new IllegalArgumentException("be sure the " + unifiedOrder.field_name() + " not null");
                result.put(unifiedOrder.field_name(), object);
            } catch (IllegalAccessException | JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
