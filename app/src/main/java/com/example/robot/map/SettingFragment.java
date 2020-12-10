package com.example.robot.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    //speed 0,1,2
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.electricityQuantityTV)
    TextView electricityQuantityTV;
    @BindView(R.id.settings_electricityQuantity)
    SeekBar settingsElectricityQuantity;
    @BindView(R.id.languageTV)
    TextView languageTV;
    @BindView(R.id.settings_language)
    Spinner settingsLanguage;
    @BindView(R.id.volumeTV)
    TextView volumeTV;
    @BindView(R.id.settings_volume)
    SeekBar settingsVolume;
    @BindView(R.id.robotSpeedTV)
    TextView robotSpeedTV;
    @BindView(R.id.settings_robotSpeed)
    Spinner settingsRobotSpeed;
    @BindView(R.id.ledBrightnessTV)
    TextView ledBrightnessTV;
    @BindView(R.id.settings_ledBrightness)
    Spinner settingsLedBrightness;
    @BindView(R.id.versionNumberTV)
    TextView versionNumberTV;
    @BindView(R.id.settings_versionNumber)
    TextView settingsVersionNumber;
    @BindView(R.id.settings_ok)
    Button settingsOk;
    @BindView(R.id.settings_cancel)
    Button settingsCancel;
    @BindView(R.id.settings_main_layout)
    ConstraintLayout settingsMainLayout;

    private View view;
    private GsonUtils gsonUtils;
    private Context mContext;
    private long[] mHints = new long[3];
    private long[] mHints1 = new long[2];
    private long[] mHints2 = new long[1];

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d("hhhh", "edit_start");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d("hhhh", "edit_stop");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();
        mContext = view.getContext();
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_SPEED_LEVEL));//0,1,2
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_LED_LEVEL));//0,1,2
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_VOICE_LEVEL));
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_LOW_BATTERY));//30-80
        initView();
        return view;
    }

    private void initView() {

    }


    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg setting： " + messageEvent.getState());
        if (messageEvent.getState() == 20001) {
            int lowBattery = (int) messageEvent.getT();
            Log.d(TAG, "onEventMsg setting： " + messageEvent.getState()+"low_battery"+lowBattery);
            settingsElectricityQuantity.setProgress(lowBattery);
        } else if (messageEvent.getState() == 20002) {
            int ledLevel = (int) messageEvent.getT();
            Log.d(TAG, "onEventMsg setting： " + messageEvent.getState()+"LED"+ledLevel);
            if (ledLevel == 0) {
                settingsLedBrightness.setSelection(0);
            } else if (ledLevel == 1) {
                settingsLedBrightness.setSelection(1);
            } else {
                settingsLedBrightness.setSelection(2);
            }
        } else if (messageEvent.getState() == 20003) {
            int robotSpeed = (int) messageEvent.getT();
            if (robotSpeed == 0) {
                settingsRobotSpeed.setSelection(0);
            } else if (robotSpeed == 1) {
                settingsRobotSpeed.setSelection(1);
            } else {
                settingsRobotSpeed.setSelection(2);
            }
        }else if (messageEvent.getState() == 20004) {
            int voiceLevel = (int) messageEvent.getT();
            Log.d(TAG, "onEventMsg setting： " + messageEvent.getState() + "voiceLevel" + voiceLevel);
            settingsVolume.setProgress(voiceLevel);
        }
    }


    @OnClick({R.id.settings_ok, R.id.settings_cancel,R.id.settings_versionNumber})
    public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.settings_ok:
                    Log.d(TAG, "APPLY");
                    int lowBattery = settingsElectricityQuantity.getProgress();
                    gsonUtils.setLowBattery(lowBattery);
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.SET_LOW_BATTERY));//30-80

                    int voiceLevel = settingsElectricityQuantity.getProgress();
                    gsonUtils.setVoiceLevel(voiceLevel);
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.SET_VOICE_LEVEL));//30-80

                    int ledLevel = settingsLedBrightness.getSelectedItemPosition();
                    gsonUtils.setLedLevel(ledLevel);
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.SET_LED_LEVEL));//0,1,2

                    int robotSpeed = settingsRobotSpeed.getSelectedItemPosition();
                    gsonUtils.setSpeedLevel(robotSpeed);
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.SET_SPEED_LEVEL));//0,1,2

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment, new MainFragment(), null)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.settings_cancel:
                    Log.d(TAG, "返回");
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment, new MainFragment(), null)
                            .addToBackStack(null)
                            .commit();
                    break;
                case R.id.settings_versionNumber:
                    Log.d(TAG, "banbenhao");
                    System.arraycopy(mHints1, 1, mHints1, 0, mHints1.length - 1);
                    //获得当前系统已经启动的时间
                    mHints1[mHints1.length - 1] = SystemClock.uptimeMillis();
                    if(SystemClock.uptimeMillis()-mHints1[0]<=500){
                        Toast.makeText(mContext.getApplicationContext(),"快速点击三次进入测试页面", Toast.LENGTH_SHORT).show();
                    }

                    System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);
                    //获得当前系统已经启动的时间
                    mHints[mHints.length - 1] = SystemClock.uptimeMillis();
                    if(SystemClock.uptimeMillis()-mHints[0]<=500){
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_fragment, new TestFragment(), null)
                                .addToBackStack(null)
                                .commit();
                        Toast.makeText(mContext.getApplicationContext(),"进入测试页面", Toast.LENGTH_SHORT).show();
                    }
                    break;
        }
    }
}