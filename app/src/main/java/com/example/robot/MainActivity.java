package com.example.robot;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends FragmentActivity {

    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        gsonUtils = new GsonUtils();
        conn();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void conn(){
        try {
            if(emptyClient == null){
                emptyClient = new EmptyClient(new URI("ws://10.7.5.176:8887"));
                emptyClient.connect();
            }else {
                emptyClient.reconnect();
                Toast toast = Toast.makeText(MainActivity.this,"连接失败请重新连接",Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {

        if (messageEvent.getState() == 10006) {
            String type = (String) messageEvent.getT();
            System.out.println("type111:" + type);
        }
    }

}