package com.example.robot;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.robot.bean.RobotMapBean;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.map.FirstFragment;
import com.example.robot.map.MapManagerFragment;
import com.example.robot.map.SecoundFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import butterknife.ButterKnife;


public class MainActivity extends FragmentActivity {

    public static String TAG="MainActivity";
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;
    private FirstFragment firstFragment;
    private SecoundFragment secoundFragment;
    private MapManagerFragment mapManagerFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        gsonUtils = new GsonUtils();
        connect();
        firstFragment = new FirstFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.first_fragment,firstFragment).commit();
        secoundFragment  = new SecoundFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.second_fragment,secoundFragment).commit();
        mapManagerFragment = new MapManagerFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void connect(){
        try {
            if(emptyClient == null){
                emptyClient = new EmptyClient(new URI("ws://10.7.5.176:8887"));
                emptyClient.connect();
                Log.d(TAG,"连接成功");
            }else {
                emptyClient.reconnect();
                Log.d(TAG,"连接失败");
                Toast toast = Toast.makeText(MainActivity.this,"连接失败请重新连接",Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {

        Log.d(TAG, "onEventMsg ： " + messageEvent.getT());
        if (messageEvent.getState() == 10005) {
            Log.d(TAG, "onEventMsg ： " + "3");
            firstFragment.refesh((String) messageEvent.getT());
        }else if (messageEvent.getState() == 10006) {
                String type = (String) messageEvent.getT();
                System.out.println("type:" + type);

        }else if (messageEvent.getState() == 10001){

            mapManagerFragment.refesh((String) messageEvent.getT());
        }
    }


}