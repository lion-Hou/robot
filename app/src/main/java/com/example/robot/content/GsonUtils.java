package com.example.robot.content;

import android.util.Log;

import com.example.robot.bean.SaveTaskBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GsonUtils {
    private static final String TAG = "GsonUtils";
    private static final String TYPE = "type";

    private JSONObject jsonObject;

    private int spinnerTime;
    private String tvTime = null;
    private String mapName;
    private String positionName;
    private String taskName;
    private List<SaveTaskBean> list;

    public List<SaveTaskBean> getList() {
        return list;
    }

    public void setList(List<SaveTaskBean> list) {
        this.list = list;
    }

    public void setTvTime(String tvTime) {
        this.tvTime = tvTime;
    }

    public void setSpinnerTime(int spinnerTime) {
        this.spinnerTime = spinnerTime;
    }


    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public void setTaskName(String taskName) {
        this.taskName= taskName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }



    public String putJsonMessage(String type) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
            jsonObject.put(Content.MAP_NAME,mapName);
            jsonObject.put(Content.POINT_NAME,positionName);
            jsonObject.put(Content.TASK_NAME, taskName);
            if (list != null) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0;i < list.size() ;i++) {
                    JSONObject js = new JSONObject();
                    js.put(Content.POINT_NAME, list.get(i).getPoint_Name());
                    js.put(Content.SPINNERTIME, list.get(i).getSpinerIndex());
                    jsonArray.put(i, js);
                }
                jsonObject.put(Content.SAVETASKQUEUE, jsonArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("gsonutils ", "" + jsonObject.toString());
        return jsonObject.toString();
    }

    public String getType(String message){
        String type = null;
        if (message == null) {
            Log.d(TAG, "getType message is null");
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(message);
            type = jsonObject.getString(TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return type;
    }

    public String putTestMsg(String type){
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
