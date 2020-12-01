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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.robot.EmptyClient;
import com.example.robot.R;
import com.example.robot.SettingsActivity;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;

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
    private Context mContext;
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;
    public static String[] mapName;
    public View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(getContext());
        view = inflater.inflate(R.layout.fragment_first, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        settingsButton.setOnClickListener(this);
    }

    private void initListener() {

    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_settings:
                Log.d(TAG, "onEventMsg ： " + "ssssss");
                Intent intent = new Intent();
                intent.setClass(getActivity(), SettingsActivity.class);
                getActivity().startActivity(intent);
                break;

            case R.id.main_spinner_map:
                emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPLIST));
                Log.d(TAG, "onEventMsg ： " + "1");
                break;
            case R.id.main_spinner_task:
                break;
            default:
                break;
        }
    }

    //地图列表
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
                emptyClient.send(gsonUtils.putJsonMessage(Content.USE_MAP));//应用这个地图
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
            moreMap(mapName);
            Log.d(TAG, "onEventMsg ： " + "3");
        }
    }


}