package com.example.robot;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.robot.content.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class SettingsActivity extends Activity {

    @BindView(R.id.settings_electricityQuantity)
    SeekBar electricityQuantitySeekBar;
    @BindView(R.id.settings_language)
    Spinner languageSpinner;
    @BindView(R.id.settings_volume)
    SeekBar volumeSeekBar;
    @BindView(R.id.settings_robotSpeed)
    Spinner robotSpeedSpinner;
    @BindView(R.id.settings_ledBrightness)
    SeekBar ledBrightnessSeekBar;
    @BindView(R.id.settings_versionNumber)
    TextView versionNumberTextView;

    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;

    String[] languageItems;
    String[] robotSpeedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        EventBus.getDefault().register(this);
        gsonUtils = new GsonUtils();

    }

    private void initView() {
        languageItems = new String[]{"中文", "English"};
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languageItems);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);

        robotSpeedItems = new String[]{"低", "高"};
        ArrayAdapter<String> robotSpeedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languageItems);
        robotSpeedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(robotSpeedAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
