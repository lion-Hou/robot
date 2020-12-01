package com.example.robot;

import android.app.Activity;
import android.content.Context;
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

    private Context mContext;
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;

    String[] languageItems;
    String[] robotSpeedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = SettingsActivity.this;
        initView();
        bindView();
//        EventBus.getDefault().register(this);
        gsonUtils = new GsonUtils();

    }

    private void initView() {
        languageSpinner = new Spinner(this);
        languageItems = new String[]{"中文", "English"};
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languageItems);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);

        robotSpeedSpinner = new Spinner(this);
        robotSpeedItems = new String[]{"低", "高"};
        ArrayAdapter<String> robotSpeedAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languageItems);
        robotSpeedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        robotSpeedSpinner.setAdapter(robotSpeedAdapter);

        electricityQuantitySeekBar = new SeekBar(this);
        volumeSeekBar = new SeekBar(this);
        ledBrightnessSeekBar = new SeekBar(this);
    }

    private void bindView() {
        //electricityQuantity
        electricityQuantitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //volume
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //ledBrightnessSeekBar
        ledBrightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }


}
