package com.example.robot;

import android.util.Log;

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

    public EmptyClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public EmptyClient(URI serverURI) {
        super(serverURI);
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("Hello, it is me. Mario");
        System.out.println("new connection opened");
        gsonUtils = new GsonUtils();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
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
        switch (gsonUtils.getType(message)) {
            case Content.TV_TIME:
                String string = null;
                jsonObject = new JSONObject(message);
                string = jsonObject.getString(Content.SPINNERTIME);
                EventBus.getDefault().post(new EventBusMessage(10002, string));
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

            case Content.CONN_TYPE:
                EventBus.getDefault().post(new EventBusMessage(10006, message));
            case Content.SENDGPSPOSITION:
                EventBus.getDefault().post(new EventBusMessage(10009, message));

            case Content.BATTERY_DATA:
                System.out.println("GETPOSITION: " + message);
                jsonObject = new JSONObject(message);
                String batty = jsonObject.getString(Content.BATTERY_DATA);
                System.out.println("batty:"+batty.toString());


            default:
                throw new IllegalStateException("Unexpected value: " + gsonUtils.getType(message));
        }
    }

}
