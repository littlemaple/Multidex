package com.medzone.framework.network.serializer;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SerializerJsonImp implements ISerializer<Object> {

    public SerializerJsonImp() {
    }

    @Override
    public String serialize(Object obj) {
        if (obj == null) return "";
        if (obj instanceof JSONObject) return obj.toString();
        return obj.toString();
    }

    @Override
    public Object deserialize(String json) {
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            try {
                // 请求中去检查格式，不是好的选择
                // 检查如果存在类型为Array则一并转化为JSONObject
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("root", new JSONArray(json));
                return jsonObj;
            } catch (Exception e1) {
                Log.v("", "deserialize$failed:" + json);
            }
        }
        return null;
    }

}
