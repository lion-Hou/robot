package com.example.robot;

import android.util.Log;

import com.example.robot.bean.RobotMapBean;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class EmptyClient extends WebSocketClient {

    private GsonUtils gsonUtils;
    private JSONObject jsonObject;
    private final String TAG = "EmptyClient";

    private boolean isConnected = false;

    public EmptyClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public EmptyClient(URI serverURI) {
        super(serverURI);
        gsonUtils = new GsonUtils();
    }


    @Override
    public void onOpen(ServerHandshake handshake) {
        isConnected = true;
        EventBus.getDefault().post(new EventBusMessage<>(11120,isConnected));
        //发送系统时间给下位机
        System.out.println("connect state new connection opened"+isConnected);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        isConnected = false;
        EventBus.getDefault().post(new EventBusMessage<>(11119,isConnected));
        System.out.println("connect state closed with exit code " + code + " additional info: " + reason+"boolean"+isConnected);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received message: " + message);
        try {
            differentiateType(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("received ByteBuffer"+message);
        EventBus.getDefault().post(new EventBusMessage<>(10001, message));
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }

    public void differentiateType(String message) throws JSONException {
        Log.d("gsonUtils.getType  : ", ""+ gsonUtils.getType(message));
        switch (gsonUtils.getType(message)) {
            case Content.REQUEST_MSG:
                jsonObject = new JSONObject(message);
                String request_msg = jsonObject.getString(Content.REQUEST_MSG);
                EventBus.getDefault().post(new EventBusMessage<>(19191,request_msg));
                break;
            case Content.CONN_NO:
                isConnected = false;
                EventBus.getDefault().post(new EventBusMessage<>(11110,"11110"));
                break;
            case Content.CONN_OK:
                isConnected = true;
                EventBus.getDefault().post(new EventBusMessage<>(11111,isConnected));
                //20201207.121212+
                break;
            case Content.TV_TIME:
                String string = null;
                jsonObject = new JSONObject(message);
                string = jsonObject.getString(Content.TV_TIME);
                EventBus.getDefault().post(new EventBusMessage(10002, string));
                Log.d("hhhh", ""+string);
                break;
            case Content.SENDMAPNAME:
                //地图列表
                Log.d("zdzd 666", "qqqqq");
                jsonObject = new JSONObject(message);
                JSONArray name = jsonObject.getJSONArray(Content.SENDMAPNAME);
                Log.d("zdzd 666", ""+name.toString());
                Content.list = new ArrayList<>();
                for (int i =0; i <name.length(); i++){
                    JSONObject jsonObject = name.getJSONObject(i);
                    RobotMapBean robotMapBean = new RobotMapBean();
                    robotMapBean.setMap_Name(jsonObject.getString(Content.MAP_NAME));
                    robotMapBean.setGridHeight(jsonObject.getInt(Content.GRID_HEIGHT));
                    robotMapBean.setGridWidth(jsonObject.getInt(Content.GRID_WIDTH));
                    robotMapBean.setOriginX(jsonObject.getDouble(Content.ORIGIN_X));
                    robotMapBean.setOriginY(jsonObject.getDouble(Content.ORIGIN_Y));
                    Log.d("zdzd 666", ""+jsonObject.getDouble(Content.RESOLUTION));
                    robotMapBean.setResolution(jsonObject.getDouble(Content.RESOLUTION));
                    Content.list.add(robotMapBean);
                }
                System.out.println("map_name: " + Content.list.size());
                EventBus.getDefault().post(new EventBusMessage(10005, Content.list));
                break;
            case Content.GETPOSITION:
               System.out.println("GETPOSITION: " + message);
            case Content.TEST_SENSOR_CALLBACK:
                jsonObject = new JSONObject(message);
                EventBus.getDefault().post(new EventBusMessage(20000, jsonObject.getString(Content.TEST_SENSOR_CALLBACK)));
                break;

            case Content.SENDTASKQUEUE:
                //返回任务列表
                jsonObject = new JSONObject(message);
                JSONArray task = jsonObject.getJSONArray(Content.DATATIME);
                Log.d("task_name", String.valueOf(task));
                String[] taskName = new String[task.length()];
                for (int i =0; i <task.length(); i++){
                    taskName[i] = task.getString(i);
                }
                Log.d("task_name",taskName[0]);
                if (task == null){
                    EventBus.getDefault().post(new EventBusMessage(10018, 0));
                }else {
                    EventBus.getDefault().post(new EventBusMessage(10017, taskName));
                }
            case Content.SENDPOINTPOSITION:
                //当前地图点列表
                jsonObject = new JSONObject(message);
                JSONArray point = jsonObject.getJSONArray(Content.SENDPOINTPOSITION);
                String[] pointName = new String[point.length()];
                for (int i =0; i <point.length(); i++){
                    JSONObject jsonObject = point.getJSONObject(i);
                    pointName[i] = jsonObject.getString(Content.POINT_NAME);
                }
                System.out.println("point_name" + pointName[0]);
                EventBus.getDefault().post(new EventBusMessage(10007, pointName));
                EventBus.getDefault().post(new EventBusMessage(10008, message));
                break;

            case Content.SENDGPSPOSITION:
                EventBus.getDefault().post(new EventBusMessage(10009, message));
                break;
            case Content.ROBOT_TASK_HISTORY:
                EventBus.getDefault().post(new EventBusMessage(40001, message));
                break;
            case Content.SEND_VIRTUAL:
                EventBus.getDefault().post(new EventBusMessage(40002, message));
                break;
            case Content.BATTERY_DATA:
                jsonObject = new JSONObject(message);
                String batty = jsonObject.getString(Content.BATTERY_DATA);
                EventBus.getDefault().post(new EventBusMessage(40003, batty));
                Log.d("batter",batty);
                break;

            case Content.GET_LOW_BATTERY:
//                jsonObject = new JSONObject(message);
//                int lowBattery = jsonObject.getInt(Content.GET_LOW_BATTERY);
//                EventBus.getDefault().post(new EventBusMessage(20001, lowBattery));
//                Log.d("lowBattery", String.valueOf(lowBattery));
                break;

            case Content.GET_LED_LEVEL:
                jsonObject = new JSONObject(message);
                int ledLevel = jsonObject.getInt(Content.GET_LED_LEVEL);
                EventBus.getDefault().post(new EventBusMessage(20002, ledLevel));
                Log.d("ledLevel", String.valueOf(ledLevel));
                int speedLevel = jsonObject.getInt(Content.GET_SPEED_LEVEL);
                EventBus.getDefault().post(new EventBusMessage(20003, speedLevel));
                Log.d("speedLevel", String.valueOf(speedLevel));
                int lowBattery = jsonObject.getInt(Content.GET_LOW_BATTERY);
                EventBus.getDefault().post(new EventBusMessage(20001, lowBattery));
                Log.d("lowBattery", String.valueOf(lowBattery));
                break;

            case Content.GET_SPEED_LEVEL:
//                jsonObject = new JSONObject(message);
//                int speedLevel = jsonObject.getInt(Content.GET_SPEED_LEVEL);
//                EventBus.getDefault().post(new EventBusMessage(20003, speedLevel));
//                Log.d("speedLevel", String.valueOf(speedLevel));
                break;
            case Content.editTaskQueue:
                EventBus.getDefault().post(new EventBusMessage(20004, message));
                Log.d("edit_Queue",message );
                break;

            case Content.ROBOT_TASK_STATE:
                EventBus.getDefault().post(new EventBusMessage(50001));
                Log.d("task_state",message );
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + gsonUtils.getType(message));
        }
    }

}
