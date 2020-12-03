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
            case Content.CONN_NO:
                isConnected = false;
                EventBus.getDefault().post(new EventBusMessage<>(11110,"1111"));
                break;
            case Content.CONN_OK:
                isConnected = true;
                EventBus.getDefault().post(new EventBusMessage<>(11111,isConnected));
                break;
            case Content.TV_TIME:
                String string = null;
                jsonObject = new JSONObject(message);
                string = jsonObject.getString(Content.SPINNERTIME);
                EventBus.getDefault().post(new EventBusMessage(10002, string));
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
                //当前任务列表
                EventBus.getDefault().post(new EventBusMessage(10017, message));
                break;

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
                //EventBus.getDefault().post(new EventBusMessage(10009, message));
                break;
            case Content.BATTERY_DATA:
                System.out.println("GETPOSITION: " + message);
                jsonObject = new JSONObject(message);
                String batty = jsonObject.getString(Content.BATTERY_DATA);
                System.out.println("batty:"+batty.toString());
                break;


            default:
                throw new IllegalStateException("Unexpected value: " + gsonUtils.getType(message));
        }
    }

}
