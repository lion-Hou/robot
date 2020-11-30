package com.example.robot;

import android.app.Activity;
import android.os.Bundle;

import com.example.robot.content.GsonUtils;

import org.greenrobot.eventbus.EventBus;

public class SettingsActivity extends Activity {
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EventBus.getDefault().register(this);
        gsonUtils = new GsonUtils();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
