package com.example.robot.content;

import com.example.robot.bean.RobotMapBean;

import java.util.List;

public final class Content {

    public static final double ROBOT_SIZE = 0.215;
    public static String map_Name = null;
    public static String first_map_Name = null;
    public static String task_Name = null;
    public static String fixTaskName = null;
    public static String manageTaskName = null;

    /**
     * robotState：
     * 0:关机
     * 1:静止
     * 2:
     * 3:移动
     * 4:充电
     * 5:uvc警告
     * 6:低电量
     */
    public static int robotState = 0;
    public static int time = 0;//灯带时间间隔
    public static List<RobotMapBean> list;//地图所有点数据
    /**
     * 0：任务结束
     * 1：执行任务，恢复任务
     * 2: 暂停任务
     * 3:没电暂停
    */
    public static int taskState = 0;//机器人执行任务的状态
    public static int taskIndex = 0;
    public static int version = 1;//初始版本号

    public static final String MAP_NAME = "map_Name";//地图名字

    public static final String BATTERY_LOW = "battery_low";//地图名字
    public static final String OLD_MAP_NAME = "old_map_name";//老地图名字
    public static final String NEW_MAP_NAME = "new_map_name";//新地图名字
    public static final String TASK_NAME = "task_Name";//任务名字


    public static final String OLD_POINT_NAME = "old_point_name";//老地图名字
    public static final String NEW_POINT_NAME = "new_point_name";//新地图名字
    public static final String CHARGING = "request_msg";
    //type id
    public static final String STARTUP = "startUp";//前进
    public static final String STOPUP = "stopUp";//停止后退
    public static final String STARTDOWN = "startDown";//后退
    public static final String STOPDOWN = "stopDown";//停止后退
    public static final String STARTLEFT = "startLeft";//向左
    public static final String STOPLEFT = "stopLeft";//停止左转
    public static final String STARTRIGHT = "startRight";//向右
    public static final String STOPRIGHT = "stopRight";//停止右转
    public static final String STARTLIGHT = "startLight";//开灯
    public static final String STOPLIGHT = "stopLight";//关灯
    public static final String SYSTEM_DATE = "system_date";//
    public static final String GETMAPLIST = "getMapList";//请求地图列表
    public static final String SENDMAPNAME = "sendMapName";//返回地图列表名称
    public static final String GETMAPPIC = "getMapPic";//请求地图图片
    public static final String SENDMAPICON = "sendMapIcon";//返回地图图片
    public static final String GETPOSITION = "getPosition";//请求机器人位置
    public static final String SENDGPSPOSITION = "sendGpsPosition";//返回机器人位置
    public static final String GETINITIALIZE = "getInitialize";//请求机器人转圈初始化
    public static final String SENDINITIALIZE = "sendInitialize";//返回机器人转圈初始化
    public static final String GETTASKQUEUE = "getTaskQueue";//请求机器人任务列表
    public static final String SENDTASKQUEUE = "sendTaskQueue";//返回任务列表
    public static final String GETPOINTPOSITION = "getPointPosition";//返回点数据
    public static final String SENDPOINTPOSITION = "sendPointPosition";//返回点数据
    public static final String ADD_POSITION = "add_position";//添加点
    public static final String ADD_POWER_POINT = "add_power_point";//添加点
    public static final String SPINNERTIME = "spinnerTime";//请求的时间
    public static final String TV_TIME = "tv_time";//返回时间
    public static final String SAVETASKQUEUE = "saveTaskQueue";//存储任务
    public static final String DELETETASKQUEUE = "deleteTaskQueue";//删除任务队列
    public static final String STARTTASKQUEUE = "startTaskQueue";//开始任务队列
    public static final String STOPTASKQUEUE = "stopTaskQueue";//停止任务队列
    public static final String DELETE_TASK = "delete_task";//删除任务
    public static final String editTaskQueue = "editTaskQueue";//编辑任务
    public static final String editTaskQueueType = "edittaskqueuetype";//编辑任务类型
    public static final String editTaskQueueTime = "edittaskqueuetime";//编辑任务类型
    public static final String versionCode = "versionCode";//编辑任务类型
    public static final String UPDATE = "update";//编辑任务类型

    //settings
    public static final String GET_LED_LEVEL = "get_led_level";//获取LED亮度
    public static final String SET_LED_LEVEL = "set_led_level";//设置LED亮度
    public static final String SET_SPEED_LEVEL = "set_speed_level";//设置导航速度
    public static final String GET_LOW_BATTERY = "get_low_battery";//获取回充电量
    public static final String SET_LOW_BATTERY = "set_low_battery";//设置回充电量
    public static final String GET_VOICE_LEVEL = "get_voice_level";//获取声音
    public static final String SET_VOICE_LEVEL = "set_voice_level";
    public static final String GET_SPEED_LEVEL = "get_speed_level";//导航速度
    public static final String GET_NAVIGATIONSPEEDLEVEL = "get_navigationSpeedLevel";//任务导航速度
    public static final String GET_PLAYPATHSPEEDLEVEL = "get_playPathSpeedLevel";//跟线导航速度

    public static final String SET_PLAYPATHSPEEDLEVEL = "set_playPathSpeedLevel";//设置导航速度
    public static final String SET_NAVIGATIONSPEEDLEVEL = "set_navigationSpeedLevel";//设置导航速度

    public static final String WORKING_MODE= "working_mode";
    public static final String GET_WORKING_MODE= "get_working_mode";
    public static final String DATATIME = "dataTime";//地图名称的列表array的key
    //task key
    public static final String TASK_X = "x";//x坐标
    public static final String TASK_Y = "y";//y坐标
    public static final String TASK_DISINFECT_TIME = "disinfect_Time";//执行任务的点的时间
    public static final String TASK_ANGLE = "angle";//机器人角度
    public static final String TASK_ALARM ="task_alarm";//定时任务

    public static final String POINT_NAME = "point_Name";//点的名字
    public static final String POINT_TYPE = "point_type";//点类型
    public static final String POINT_STATE ="point_state";
    public static final String START_SCAN_MAP = "start_scan_map";//开始扫描地图
    public static final String CANCEL_SCAN_MAP = "cancel_scan_map";//取消扫描地图并且保存
    public static final String CANCEL_SCAN_MAP_NO = "cancel_scan_map_no";//取消扫描地图bu保存
    public static final String DEVELOP_MAP = "develop_map";//扩展扫描地图
    public static final String DELETE_POSITION = "delete_position";//删除点
    public static final String DELETE_MAP = "delete_map";//删除地图
    public static final String RENAME_MAP = "rename_map";//删除地图
    public static final String BATTERY_DATA = "battery_data";//电量
    public static final String USE_MAP = "use_map";//应用地图
    public static final String REQUEST_MSG = "request_msg";//返回失败结果
    public static final String CONN_OK = "conn_ok";
    public static final String CONN_NO = "no_conn";
    public static final String DEVICES_STATUS = "devices_status";

    public static final String ROBOT_X = "robot_x";//x坐标
    public static final String ROBOT_Y = "robot_y";//y坐标
    public static final String GRID_HEIGHT = "grid_height";//地图高
    public static final String GRID_WIDTH = "grid_width";//地图宽
    public static final String ORIGIN_X = "origin_x";//原点x
    public static final String ORIGIN_Y = "origin_y";//原点Y
    public static final String RESOLUTION = "resolution";

    public static final String POINT_X = "point_x";//点x坐标
    public static final String POINT_Y = "point_y";//点y坐标
    public static final String ANGLE = "angle";//角度

    public static final String TEST_UVCSTART = "test_uvcstart";
    public static final String TEST_UVCSTOP = "test_uvcstop";
    public static final String TEST_SENSOR_CALLBACK = "test_sensor_callback";
    public static final String TEST_WARINGSTART = "test_waringstart";
    public static final String TEST_WARINGSTOP = "test_waringstop";


    public static final String ROBOT_HEALTHY = "robot_healthy";//机器人健康
    public static final String ROBOT_TASK_STATE = "robot_task_state";//机器人任务状态
    public static final String ROBOT_TASK_HISTORY = "robot_task_history";//机器人历史任务
    public static final String RESECT_ROBOT = "resect_robot";//恢复出厂设置

    public static final String GET_VIRTUAL = "get_virtual";//获取虚拟墙数据
    public static final String UPDATA_VIRTUAL = "updata_virtual";//更新虚拟墙
    public static final String SEND_VIRTUAL = "send_virtual";//返回虚拟墙数据
    public static final String VIRTUAL_X = "virtual_x";//虚拟墙数据
    public static final String VIRTUAL_Y = "virtual_y";//虚拟墙数据
    //db Name
    public static final String dbName = "dbName";
    public static final String tableName = "taskHistory";//历史数据
    public static final String dbTaskName = "taskName";
    public static final String dbTaskMapName = "dbTaskMapName";
    public static final String dbTime = "time";
    public static final String dbData = "data";
    public static final String alarmName = "alarmName";//定时数据
    public static final String dbAlarmName = "dbAlarmName";//表名
    public static final String dbAlarmMapName = "dbAlarmMapName";
    public static final String dbAlarmTaskName = "dbAlarmTaskName";//任务名
    public static final String dbAlarmTime = "dbAlarmTime";//时间
    public static final String dbAlarmCycle = "dbAlarmCycle";//周期
    public static final String RENAME_POSITION = "rename_position";
    public static final String RESET_ROBOT = "reset_robot";//重命名点
    public static final String OLD_POSITION_NAME = "old_position_name";//原始点
    public static final String NEW_POSITION_NAME = "new_position_name";//新点

    public static final String dbPointName = "dbPointName";//点名字
    public static final String dbSpinnerTime = "dbSpinnerTime";//点时间

    //test
    public static final String GET_ULTRASONIC = "get_ultrasonic";//声呐设备
    public static final String GET_ULTRASONIC_X = "get_ultrasonic_x";//声呐设备x
    public static final String GET_ULTRASONIC_Y = "get_ultrasonic_y";//声呐设备y

    public static final String TEST_UVCSTART_1 = "test_uvcstart_1";
    public static final String TEST_UVCSTART_2 = "test_uvcstart_2";
    public static final String TEST_UVCSTART_3 = "test_uvcstart_3";
    public static final String TEST_UVCSTART_4 = "test_uvcstart_4";
    public static final String TEST_UVCSTOP_1 = "test_uvcstop_1";
    public static final String TEST_UVCSTOP_2 = "test_uvcstop_2";
    public static final String TEST_UVCSTOP_3 = "test_uvcstop_3";
    public static final String TEST_UVCSTOP_4 = "test_uvcstop_4";
    public static final String TEST_UVCSTART_ALL = "test_uvcstart_ALL";
    public static final String TEST_UVCSTOP_ALL = "test_uvcstop_ALL";
    public static final String TEST_SENSOR = "test_sensor";
    public static final String TEST_WARNINGSTART = "test_warningstart";
    public static final String TEST_WARNINGSTOP = "test_warningstop";
    public static final String TEST_LIGHTSTART = "test_lightstart";
    public static final String TEST_LIGHTSTOP = "test_lightstop";


    public static final String GET_TASK_STATE = "get_task_state";//断连请求
    //fragment见传递
    public static final String FIRSTMAPNAME_TOEDIT = "firstmap_toedit";





}
