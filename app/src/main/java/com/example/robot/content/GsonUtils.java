package com.example.robot.content;

import android.util.Log;

import com.example.robot.bean.DrawLineBean;
import com.example.robot.bean.SaveTaskBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.PropertyResourceBundle;

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
    private String oldMapName;
    private String newMapName;
    private String oldPointName;
    private String newPointName;
    private String taskType;
    private List<String> taskWeek;
    private String taskTime;
    private int ledLevel;
    private int lowBattery;
    private int voiceLevel;
    private int speedLevel;
    private int workingMode;

    public int getWorkingMode() {
        return workingMode;
    }

    public void setWorkingMode(int workingMode) {
        this.workingMode = workingMode;
    }

    public int getVoiceLevel() {
        return voiceLevel;
    }

    public void setVoiceLevel(int voiceLevel) {
        this.voiceLevel = voiceLevel;
    }

    public String getOldPointName() {
        return oldPointName;
    }

    public void setOldPointName(String oldPointName) {
        this.oldPointName = oldPointName;
    }

    public String getNewPointName() {
        return newPointName;
    }

    public void setNewPointName(String newPointName) {
        this.newPointName = newPointName;
    }

    public int getLedLevel() {
        return ledLevel;
    }

    public void setLedLevel(int ledLevel) {
        this.ledLevel = ledLevel;
    }

    public int getLowBattery() {
        return lowBattery;
    }

    public void setLowBattery(int lowBattery) {
        this.lowBattery = lowBattery;
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    public void setSpeedLevel(int speedLevel) {
        this.speedLevel = speedLevel;
    }

    public String getOldMapName() {
        return oldMapName;
    }

    public void setOldMapName(String oldMapName) {
        this.oldMapName = oldMapName;
    }

    public String getNewMapName() {
        return newMapName;
    }

    public void setNewMapName(String newMapName) {
        this.newMapName = newMapName;
    }

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
        this.taskName = taskName;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public void setTaskWeek(List<String> taskWeek) {
        this.taskWeek = taskWeek;
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
            jsonObject.put(Content.MAP_NAME, mapName);
            jsonObject.put(Content.POINT_NAME, positionName);
            jsonObject.put(Content.TASK_NAME, taskName);
            jsonObject.put(Content.OLD_MAP_NAME,oldMapName);
            jsonObject.put(Content.NEW_MAP_NAME,newMapName);
            jsonObject.put(Content.SET_LED_LEVEL,ledLevel);
            jsonObject.put(Content.SET_LOW_BATTERY,lowBattery);
            jsonObject.put(Content.SET_PLAYPATHSPEEDLEVEL,speedLevel);
            jsonObject.put(Content.SET_NAVIGATIONSPEEDLEVEL,speedLevel);
            jsonObject.put(Content.SET_VOICE_LEVEL,voiceLevel);
            jsonObject.put(Content.OLD_POINT_NAME,oldPointName);
            jsonObject.put(Content.NEW_POINT_NAME,newPointName);
            jsonObject.put(Content.WORKING_MODE,workingMode);
            if (list != null) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < list.size(); i++) {
                    JSONObject js = new JSONObject();
                    js.put(Content.POINT_NAME, list.get(i).getPoint_Name());
                    js.put(Content.SPINNERTIME, list.get(i).getSpinerIndex());
                    jsonArray.put(i, js);
                }
                jsonObject.put(Content.SAVETASKQUEUE, jsonArray);
            }
            jsonObject.put(Content.dbAlarmTime, taskTime);
            jsonObject.put(Content.dbAlarmTaskName, taskName);
            if (taskWeek != null) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < taskWeek.size(); i++) {
                    jsonArray.put(i, taskWeek.get(i));
                }
                jsonObject.put(Content.dbAlarmCycle, jsonArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("gsonutils ", "" + jsonObject.toString());
        return jsonObject.toString();
    }

    public String getType(String message) {
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
        Log.d(TAG, "getType message is " + type);
        return type;
    }

    public String putTestMsg(String type) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String putVirtualWallMsg(String type, List<List<DrawLineBean>> lists) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put(TYPE, type);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < lists.size(); i++) {
                JSONArray jsArray = new JSONArray();
                for (int j = 0; j < lists.get(i).size(); j++) {
                    JSONObject js = new JSONObject();
                    js.put(Content.VIRTUAL_X, lists.get(i).get(j).getX());
                    js.put(Content.VIRTUAL_Y, lists.get(i).get(j).getY());
                    jsArray.put(j, js);
                }
                jsonArray.put(i, jsArray);
            }
            jsonObject.put(Content.UPDATA_VIRTUAL, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
