package com.example.robot.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.SettingsActivity;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.run.RunFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "FirstFragment";

    @BindView(R.id.main_settings)
    Button settingsButton;
    @BindView(R.id.main_spinner_map)
    TextView mainSpinnerMap;
    @BindView(R.id.main_spinner_task)
    TextView mainSpinnerTask;
    @BindView(R.id.main_map)
    Button mainMap;
    @BindView(R.id.main_task)
    Button mainTask;
    @BindView(R.id.main_history)
    Button mainHistory;
    @BindView(R.id.main_execute)
    Button mainExecute;


    private Context mContext;
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;
    private String[] taskNameList;
    public View view;
    private String[] mapName;
    private String task_name;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("hhhh", "first_creat");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d("hhhh", "first_start");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d("hhhh", "first_stop");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, container, false);
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();
        initView();
        initListener();
        mContext = view.getContext();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private void initView() {
        settingsButton.setOnClickListener(this);
        mainSpinnerMap.setOnClickListener(this);
        mainMap.setOnClickListener(this);
        mainTask.setOnClickListener(this);
        mainHistory.setOnClickListener(this);
        mainExecute.setOnClickListener(this);
        mainSpinnerTask.setOnClickListener(this);
        //MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPLIST));

        /**
         * 加载首页判断是否有选择地图
         * 没有选择-->任务按钮不可点击
         * 反之可点
         */
        if(mainSpinnerMap.getText() == "PLEASE SELECT MAP" || mainSpinnerMap.getText() == "请选择地图"){
            Log.d("hhh", (String) mainSpinnerMap.getText());
            mainTask.setEnabled(false);
        }else {
            mainTask.setEnabled(true);
        }
    }

    /**
     * //获取map名称
     * public void refesh(String message){
     * JSONObject jsonObject1 = null;
     * try {
     * jsonObject1 = new JSONObject(message);
     * <p>
     * JSONArray name = jsonObject1.getJSONArray(Content.SENDMAPNAME);
     * Content.list = new ArrayList<>();
     * mapName = new String[name.length()];
     * for (int i =0; i <name.length(); i++){
     * JSONObject jsonObject = name.getJSONObject(i);
     * RobotMapBean robotMapBean = new RobotMapBean();
     * robotMapBean.setMap_Name(jsonObject.getString(Content.MAP_NAME));
     * robotMapBean.setGridHeight(jsonObject.getInt(Content.GRID_HEIGHT));
     * robotMapBean.setGridWidth(jsonObject.getInt(Content.GRID_WIDTH));
     * robotMapBean.setOriginX(jsonObject.getDouble(Content.ORIGIN_X));
     * robotMapBean.setOriginY(jsonObject.getDouble(Content.ORIGIN_Y));
     * Log.d("zdzd 666", ""+jsonObject.getDouble(Content.RESOLUTION));
     * robotMapBean.setResolution(jsonObject.getDouble(Content.RESOLUTION));
     * Content.list.add(robotMapBean);
     * mapName[i] = jsonObject.getString(Content.MAP_NAME);
     * }
     * System.out.println("map_name: " + Content.list.size());
     * } catch (JSONException e) {
     * e.printStackTrace();
     * }
     * }
     */

    private void initListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Content.map_Name != null) {
            mainSpinnerMap.setText(Content.map_Name);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_settings:
                Intent intent = new Intent();
                intent.setClass(getActivity(), SettingsActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.main_spinner_map:
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPLIST));
                break;
            case R.id.main_execute:
                if (task_name == null) {
                    Toast toast = Toast.makeText(mContext,"请选择任务名",Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    gsonUtils.setTaskName(task_name);
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STARTTASKQUEUE));
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.first_fragment, new RunFragment(), null)
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case R.id.main_spinner_task:
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETTASKQUEUE));//请求任务列表
                break;
            case R.id.main_map:
                Log.d(TAG, "onEventMsg ： " + "ditu");
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new MapManagerFragment(), null)
                        .addToBackStack(null)
                        .commit();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.second_fragment, new RobotControlPanelFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.main_task:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new TaskManagerFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.main_history:
                Log.d(TAG, "点击历史任务");
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new TaskHistoryFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    //首页获取所有地图名称
    public void moreMap(String[] mapName) {
        Log.d(TAG, "onEventMsg ： " + "2");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        System.out.println("which" + mapName.length);
        builder.setItems(mapName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("which" + which);
                mainSpinnerMap.setText(mapName[which]);
                Content.first_map_Name = mapName[which];
                gsonUtils.setMapName(mapName[which]);//给上位机传入地图名称
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.USE_MAP));//应用这个地图
                EventBus.getDefault().post(new EventBusMessage(30001, mapName[which]));//30001给编辑点页面传所选中的地图名
            }
        });
        builder.create().show();
    }

    //首页获取当前选定的地图的所有任务列表
    public void requestTaskList(String[] taskNameList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        System.out.println("which" + taskNameList.length);
        builder.setItems(taskNameList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("which" + which);
                task_name = taskNameList[which];
                Content.task_Name = taskNameList[which];
                mainSpinnerTask.setText(taskNameList[which]);
            }
        });
        builder.create().show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg ： " + messageEvent.getState());
        if (messageEvent.getState() == 10005) {
            mapName = new String[Content.list.size()];
            for (int i = 0; i < Content.list.size(); i++) {
                mapName[i] = Content.list.get(i).getMap_Name();
            }
            Log.d(TAG, mapName[1]);
            moreMap(mapName);
            Log.d(TAG, "onEventMsg ： " + "3");
            //EventBus.getDefault().cancelEventDelivery(10005);
        } else if (messageEvent.getState() == 10017) {
            taskNameList = (String[]) messageEvent.getT();
            Log.d("task_name", taskNameList[0]);
            requestTaskList(taskNameList);
        }
    }

}