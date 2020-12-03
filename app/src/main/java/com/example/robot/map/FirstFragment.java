package com.example.robot.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.SettingsActivity;
import com.example.robot.bean.RobotMapBean;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    private Context mContext;
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;

    public View view;
    private String[] mapName;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Log.d("hhhh",  "first_creat");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, container, false);
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();
        initView();
        initListener();
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
        //MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPLIST));
    }
/**
    //获取map名称
    public void refesh(String message){
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = new JSONObject(message);

            JSONArray name = jsonObject1.getJSONArray(Content.SENDMAPNAME);
            Content.list = new ArrayList<>();
             mapName = new String[name.length()];
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
                mapName[i] = jsonObject.getString(Content.MAP_NAME);
            }
            System.out.println("map_name: " + Content.list.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
*/

    private void initListener() {

    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        Log.d("hhhh",  "first_destory");
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_settings:
                Log.d(TAG, "onEventMsg ： " + "21");
                Intent intent = new Intent();
                intent.setClass(getActivity(), SettingsActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.main_spinner_map:
                Log.d(TAG, "onEventMsg ： " + "1");
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPLIST));
                Log.d(TAG, "onEventMsg ： " + "1");
                break;
            case R.id.main_spinner_task:
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

            default:
                break;
        }
    }

    public void moreMap(String[] mapName){
        Log.d(TAG, "onEventMsg ： " + "2");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        System.out.println("which" + mapName.length);
        builder.setItems(mapName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("which" + which);
                mainSpinnerMap.setText(mapName[which]);
                Content.map_Name = mapName[which];
                gsonUtils.setMapName(mapName[which]);//给上位机传入地图名称
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.USE_MAP));//应用这个地图
            }
        });
        builder.create().show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg ： " + messageEvent.getState());
        if (messageEvent.getState() == 10005) {
            mapName = new String[Content.list.size()];
            for (int i=0;i< Content.list.size();i++) {
                mapName[i] =Content.list.get(i).getMap_Name();
            }
            Log.d(TAG,mapName[1]);
            moreMap(mapName);
            Log.d(TAG, "onEventMsg ： " + "3");
            //EventBus.getDefault().cancelEventDelivery(10005);
        }
    }

}