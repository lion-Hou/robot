package com.example.robot;

import android.util.Log;

import com.example.robot.bean.HistoryBean;
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
import java.util.Arrays;
import java.util.List;

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
        Content.isConnected = true;
        EventBus.getDefault().post(new EventBusMessage<>(11120,isConnected));

        //发送系统时间给下位机
        long time = System.currentTimeMillis();
        gsonUtils.setTime(time);
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.SYSTEM_DATE));
        //断连监听
        Log.d("HOUH111","GET_TASK_STATE");
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_TASK_STATE));


        //获得上位机版本号
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.versionCode));

        System.out.println("connect state new connection opened"+isConnected);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        isConnected = false;
        Content.isConnected = false;
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
            case Content.GET_ULTRASONIC:
                jsonObject = new JSONObject(message);
                String get_ultrasonic = jsonObject.getString(Content.GET_ULTRASONIC);
                String world_ultrasonic = jsonObject.getString("world_ultrasonic");
                EventBus.getDefault().post(new EventBusMessage<>(19197,world_ultrasonic));
                break;
            case Content.TEST_SENSOR:
                jsonObject = new JSONObject(message);
                String test_sensor = jsonObject.getString(Content.TEST_SENSOR);
                EventBus.getDefault().post(new EventBusMessage<>(19198,test_sensor));
                break;
            case Content.ROBOT_HEALTHY:
                jsonObject = new JSONObject(message);
                String robotHealthy = jsonObject.getString(Content.ROBOT_HEALTHY);
                EventBus.getDefault().post(new EventBusMessage<>(19199,robotHealthy));
                break;
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
                jsonObject = new JSONObject(message);
                Log.d("fdsfsdfsd", String.valueOf(jsonObject));

                isConnected = true;
                EventBus.getDefault().post(new EventBusMessage<>(11111,isConnected));
                //20201207.121212+
                break;
            case Content.UP_VERSION_CODE:
                jsonObject = new JSONObject(message);
                int versionCode = jsonObject.getInt(Content.versionCode);
                Log.d("fdsfsdfsd", String.valueOf(versionCode));
                EventBus.getDefault().post(new EventBusMessage(12345, versionCode));
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
                List<String> list = Arrays.asList(pointName);
                List<String> arrayList = new ArrayList<String>(list);
                Log.d("arrayList.size", arrayList.toString());
                arrayList.remove("Origin");
                arrayList.remove("End");
                arrayList.remove("Current");
                arrayList.remove("charging");
                arrayList.remove("Initialize");
                Log.d("arrayList.size", arrayList.toString());
                String [] stockArr = arrayList.toArray(new String[arrayList.size()]);
                EventBus.getDefault().post(new EventBusMessage(10007, stockArr));
                EventBus.getDefault().post(new EventBusMessage(10008, message));
                break;
            case Content.SENDGPSPOSITION:
                EventBus.getDefault().post(new EventBusMessage(10009, message));
                break;
            case Content.ROBOT_TASK_HISTORY:
                EventBus.getDefault().post(new EventBusMessage(40001, message));

                JSONObject jsonObject = new JSONObject(message);
                JSONArray jsonArray = jsonObject.getJSONArray(Content.ROBOT_TASK_HISTORY);
                String allSize = Integer.toString(jsonArray.length());
                EventBus.getDefault().post(new EventBusMessage(11003, allSize));
                Log.d("allsize11",String.valueOf(allSize));

                int time = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    time = time + jsonArray.getJSONObject(i).getInt(Content.dbTime);
                }
                Log.d("allsize33",String.valueOf(time));
                EventBus.getDefault().post(new EventBusMessage(11004, time));
                break;
            case Content.SEND_VIRTUAL:
                EventBus.getDefault().post(new EventBusMessage(40002, message));
                break;
            case Content.BATTERY_DATA:
                jsonObject = new JSONObject(message);
                String batty = jsonObject.getString(Content.BATTERY_DATA);
                String regEx="[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
                String aa = " ";
                String str = batty.replaceAll(regEx,aa);
                EventBus.getDefault().post(new EventBusMessage(40003, batty));
                Log.d("batter",str);
                break;

            case Content.GET_VOICE_LEVEL:
                jsonObject = new JSONObject(message);
                int voiceLevel = jsonObject.getInt(Content.GET_VOICE_LEVEL);
                EventBus.getDefault().post(new EventBusMessage(20004, voiceLevel));
                break;

            case Content.GET_LOW_BATTERY:
                jsonObject = new JSONObject(message);
                int lowBattery = jsonObject.getInt(Content.GET_LOW_BATTERY);
                EventBus.getDefault().post(new EventBusMessage(20001, lowBattery));
                Log.d("lowBattery", String.valueOf(lowBattery));
                break;

            case Content.GET_LED_LEVEL:
                jsonObject = new JSONObject(message);
                int ledLevel = jsonObject.getInt(Content.GET_LED_LEVEL);
                EventBus.getDefault().post(new EventBusMessage(20002, ledLevel));
                Log.d("ledLevel", String.valueOf(ledLevel));
                break;

            case Content.GET_CHARGING_MODE:
                jsonObject = new JSONObject(message);
                String pile = jsonObject.getString(Content.GET_CHARGING_MODE);
                EventBus.getDefault().post(new EventBusMessage(20007, pile));
                Log.d("pile", pile);
                break;

            case Content.DEVICES_STATUS:
                jsonObject = new JSONObject(message);
                int speedLevel = jsonObject.getInt(Content.GET_NAVIGATIONSPEEDLEVEL);
                EventBus.getDefault().post(new EventBusMessage(20003, speedLevel));
                int speedLevel2 = jsonObject.getInt(Content.GET_PLAYPATHSPEEDLEVEL);
                EventBus.getDefault().post(new EventBusMessage(20006, speedLevel2));
                String urgencyStop = jsonObject.getString(Content.DEVICES_STATUS);
                EventBus.getDefault().post(new EventBusMessage(20020, urgencyStop));
                String robotVersionCode = jsonObject.getString(Content.ROBOT_VERSION_CODE);
                EventBus.getDefault().post(new EventBusMessage(20021, robotVersionCode));
                String upVersionCode = jsonObject.getString(Content.UP_VERSION_CODE);
                EventBus.getDefault().post(new EventBusMessage(20022, upVersionCode));
                Log.d("speedLevelhhhh", urgencyStop);

                break;
            case Content.CURRENTCOUNT:
                jsonObject = new JSONObject(message);
                String dbTaskCurrentCount = jsonObject.getString(Content.DBTASKCURRENTCOUNT);
                EventBus.getDefault().post(new EventBusMessage(88883, dbTaskCurrentCount));
                Log.d("DB88883", dbTaskCurrentCount);

                String dbTimeCurrentCount = jsonObject.getString(Content.DBTIMECURRENTCOUNT);
                EventBus.getDefault().post(new EventBusMessage(88884, dbTimeCurrentCount));
                Log.d("DB88884", dbTimeCurrentCount);

                String dbAreaCurrentCount = jsonObject.getString(Content.DBAREACURRENTCOUNT);
                EventBus.getDefault().post(new EventBusMessage(88885, dbAreaCurrentCount));
                Log.d("DB88885", dbAreaCurrentCount);
                break;
            case Content.TOTALCOUNT:
                jsonObject = new JSONObject(message);
                String dbTaskTotalCount = jsonObject.getString(Content.DBTASKTOTALCOUNT);
                EventBus.getDefault().post(new EventBusMessage(88880, dbTaskTotalCount));
                Log.d("DB88880", dbTaskTotalCount);

                String dbTimeTotalCount = jsonObject.getString(Content.DBTIMETOTALCOUNT);
                EventBus.getDefault().post(new EventBusMessage(88881, dbTimeTotalCount));
                Log.d("DB88881", dbTimeTotalCount);

                String dbAreaTotalCount = jsonObject.getString(Content.DBAREATOTALCOUNT);
                EventBus.getDefault().post(new EventBusMessage(88882, dbAreaTotalCount));
                Log.d("DB88882", dbAreaTotalCount);
                break;

            case Content.GET_WORKING_MODE:
                jsonObject = new JSONObject(message);
                int workingMode = jsonObject.getInt(Content.GET_WORKING_MODE);
                EventBus.getDefault().post(new EventBusMessage(20005, workingMode));
                Log.d("workingMode", String.valueOf(workingMode));
                break;

            case Content.editTaskQueue:
                jsonObject = new JSONObject(message);
                EventBus.getDefault().post(new EventBusMessage(20009, message));
                Log.d("edit_Queue",message );
                break;
                
           case Content.ROBOT_ERROR:
                jsonObject = new JSONObject(message);
                EventBus.getDefault().post(new EventBusMessage(112244, message));
                break;

            case Content.TOTAL_AREA:
                jsonObject = new JSONObject(message);
                String totalArea = jsonObject.getString(Content.TOTAL_AREA);
                EventBus.getDefault().post(new EventBusMessage(11005, totalArea));
                Log.d("totalArea", String.valueOf(totalArea));
                break;

            case Content.ROBOT_TASK_STATE:
                Log.d("HOUHOUHOU1",message );
                EventBus.getDefault().post(new EventBusMessage(60001, message));
                break;
            case Content.GET_TASK_STATE:
                Log.d("HOUH111",message );
                jsonObject = new JSONObject(message);
                String map_name = jsonObject.getString(Content.MAP_NAME);
                String task_name = jsonObject.getString(Content.TASK_NAME);
                Log.d("HOUH111",map_name);
                EventBus.getDefault().post(new EventBusMessage(90001, task_name));
                Log.d("HOUH111",task_name);
                EventBus.getDefault().post(new EventBusMessage(11001, map_name));
                EventBus.getDefault().post(new EventBusMessage(11002, task_name));
                break;
            case Content.UPDATE:
                EventBus.getDefault().post(new EventBusMessage(90009, message));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + gsonUtils.getType(message));
        }
    }

}
