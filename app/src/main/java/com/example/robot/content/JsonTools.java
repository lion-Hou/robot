package com.example.robot.content;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonTools {

    public JsonTools() {
    }

    public static String creatJson(String key, Object value){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
