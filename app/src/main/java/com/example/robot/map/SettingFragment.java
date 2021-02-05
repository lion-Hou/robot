package com.example.robot.map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.adapter.SpinnerArrayAdapter;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.util.NormalDialogUtil;

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
    @BindView(R.id.settings_reset_robot)
    Button settingsResetRobot;
    @BindView(R.id.volumeTV)
    TextView volumeTV;
    @BindView(R.id.settings_volume)
    SeekBar settingsVolume;
    @BindView(R.id.robotSpeedTV)
    TextView robotSpeedTV;
    @BindView(R.id.settings_robotSpeed)
    Spinner settingsRobotSpeed;
//    @BindView(R.id.ledBrightnessTV)
//    TextView ledBrightnessTV;
//    @BindView(R.id.settings_ledBrightness)
//    Spinner settingsLedBrightness;
    @BindView(R.id.versionNumberTV)
    TextView versionNumberTV;
    @BindView(R.id.settings_versionNumber)
    TextView settingsVersionNumber;
    @BindView(R.id.settings_ok)
    Button settingsOk;
    @BindView(R.id.settings_cancel)
    Button settingsCancel;
    @BindView(R.id.settings_debug)
    Spinner settingsDebug;
    @BindView(R.id.electricityQuantityValueTV)
    TextView electricityQuantityValueTV;
    @BindView(R.id.volumeValueTV)
    TextView volumeValueTV;
    @BindView(R.id.switch_pile)
    Switch switchPile;

    private View view;
    private GsonUtils gsonUtils;
    private Context mContext;
    private long[] mHints = new long[2];


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SpinnerArrayAdapter mAdapter;
    private boolean getSpeed = false;
    private String robotVersionCode;
    private String upVersionCode;
    private boolean swichPile;
    public SettingFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        getSpeed = false;
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
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_WORKING_MODE));
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_CHARGING_MODE));//充电桩模式
        initView();

        return view;
    }

    private void initView() {

        settingsElectricityQuantity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 拖动条停止拖动的时候调用
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                sett.setText("拖动停止");
            }

            /**
             * 拖动条开始拖动的时候调用
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                description.setText("开始拖动");
            }

            /**
             * 拖动条进度改变的时候调用
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                electricityQuantityValueTV.setText(progress+"%");
            }
        });

        settingsVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 拖动条停止拖动的时候调用
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                sett.setText("拖动停止");
            }

            /**
             * 拖动条开始拖动的时候调用
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                description.setText("开始拖动");
            }

            /**
             * 拖动条进度改变的时候调用
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                volumeValueTV.setText(progress+"%");
            }
        });

        switchPile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchPile.isChecked()) {
                    swichPile = true;
                    Log.d("switchPile","开");
                    Log.d("switchPile",String.valueOf(swichPile));
                } else {
                    swichPile = false;
                    Log.d("switchPile","关");
                    Log.d("switchPile",String.valueOf(swichPile));
                }
            }
        });

        String[] mStringArray = getResources().getStringArray(R.array.spinner_settings_speed);
        mAdapter = new SpinnerArrayAdapter(mContext,mStringArray);
        settingsRobotSpeed.setAdapter(mAdapter);

        String[] mArray = getResources().getStringArray(R.array.spinner_settings_led);
        mAdapter = new SpinnerArrayAdapter(mContext,mArray);
        //settingsLedBrightness.setAdapter(mAdapter);

        String[] mDebugArray = getResources().getStringArray(R.array.spinner_settings_debug);
        mAdapter = new SpinnerArrayAdapter(mContext,mDebugArray);
        settingsDebug.setAdapter(mAdapter);

    }


    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg setting： " + messageEvent.getState());
        if (messageEvent.getState() == 20001) {
            int lowBattery = (int) messageEvent.getT();
            Log.d(TAG, "onEventMsg setting： " + messageEvent.getState() + "low_battery" + lowBattery);
            settingsElectricityQuantity.setProgress(lowBattery);
            electricityQuantityValueTV.setText(lowBattery+"%");
//        } else if (messageEvent.getState() == 20002) {
//            int ledLevel = (int) messageEvent.getT();
//            Log.d(TAG, "onEventMsg setting： " + messageEvent.getState() + "LED" + ledLevel);
//            if (ledLevel == 0) {
//                settingsLedBrightness.setSelection(0);
//            } else if (ledLevel == 1) {
//                settingsLedBrightness.setSelection(1);
//            } else {
//                settingsLedBrightness.setSelection(2);
//            }
        } else if (messageEvent.getState() == 20004) {
            int voiceLevel = (int) messageEvent.getT();
            Log.d(TAG, "onEventMsg setting： " + messageEvent.getState() + "voiceLevel" + voiceLevel);
            settingsVolume.setProgress(voiceLevel);
            volumeValueTV.setText(voiceLevel+"%");
        } else if (messageEvent.getState() == 20005) {
            int workingMode = (int) messageEvent.getT();
            Log.d(TAG, "onEventMsg setting： " + messageEvent.getState() + "voiceLevel" + workingMode);
            if (workingMode == 0) {
                settingsDebug.setSelection(0);
            } else if (workingMode == 1) {
                settingsDebug.setSelection(1);
            } else {
                settingsDebug.setSelection(2);
            }
        } else if (messageEvent.getState() == 20006) {
            if (!getSpeed) {
                int robotSpeed = (int) messageEvent.getT();
                if (robotSpeed == 0) {
                    settingsRobotSpeed.setSelection(0);
                } else if (robotSpeed == 1) {
                    settingsRobotSpeed.setSelection(1);
                } else {
                    settingsRobotSpeed.setSelection(2);
                }
                getSpeed = true;
            }
        }else if (messageEvent.getState() == 20021) {
            String emptRobotVersionCode = (String) messageEvent.getT();
            robotVersionCode = emptRobotVersionCode;
            Log.d(TAG, "onEventMsg setting： " + messageEvent.getState() + "voiceLevel" + robotVersionCode);
        }else if (messageEvent.getState() == 20022) {
            String emptUpVersionCode = (String) messageEvent.getT();
            upVersionCode = emptUpVersionCode;
            Log.d(TAG, "onEventMsg setting： " + messageEvent.getState() + "voiceLevel" + upVersionCode);
            String number = "V" + "." + "1" + "." + upVersionCode + "." + robotVersionCode;
            settingsVersionNumber.setText(number);
        }else if (messageEvent.getState() == 20007) {
            String pile = (String) messageEvent.getT();
            if (pile.equals("true")){
                switchPile.setChecked(true);
            }else {
                switchPile.setChecked(false);
            }

        }
    }


    @OnClick({R.id.settings_ok, R.id.settings_cancel, R.id.settings_versionNumber, R.id.settings_reset_robot})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.settings_reset_robot:
                AlertDialog.Builder back = new AlertDialog.Builder(mContext);
                back.setMessage(R.string.reset);
                //设置正面按钮
                back.setPositiveButton(R.string.all_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.RESET_ROBOT));
                        Log.d(TAG, "onEventMsg sss： " + Content.map_Name);
                        dialog.dismiss();
                    }
                });
                //设置反面按钮
                back.setNegativeButton(R.string.all_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                back.show();
                break;
            case R.id.settings_ok:
                Log.d(TAG, "APPLY");
                int lowBattery = settingsElectricityQuantity.getProgress();
                gsonUtils.setLowBattery(lowBattery);
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.SET_LOW_BATTERY));//30-80

                int voiceLevel = settingsVolume.getProgress();
                gsonUtils.setVoiceLevel(voiceLevel);
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.SET_VOICE_LEVEL));//0-15

//                int ledLevel = settingsLedBrightness.getSelectedItemPosition();
//                gsonUtils.setLedLevel(ledLevel);
//                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.SET_LED_LEVEL));//0,1,2

                int robotSpeed = settingsRobotSpeed.getSelectedItemPosition();
                gsonUtils.setSpeedLevel(robotSpeed);
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.SET_PLAYPATHSPEEDLEVEL));//0,1,2

                int settingDebug = settingsDebug.getSelectedItemPosition();
                gsonUtils.setWorkingMode(settingDebug);
                Log.d(TAG, "workingmode" + settingDebug);
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.WORKING_MODE));

                gsonUtils.setPile(swichPile);
                Log.d("switchPile1111",String.valueOf(swichPile));
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.SET_CHARGING_MODE));

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, new MainFragment(), null)
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(mContext.getApplicationContext(), R.string.toast_settings_text1, Toast.LENGTH_SHORT).show();
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
                System.arraycopy(mHints, 1, mHints, 0, mHints.length - 1);
                //获得当前系统已经启动的时间
                mHints[mHints.length - 1] = SystemClock.uptimeMillis();
                if (SystemClock.uptimeMillis() - mHints[0] <= 500) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment, new TestFragment(), null)
                            .addToBackStack(null)
                            .commit();
                    Toast.makeText(mContext.getApplicationContext(), R.string.toast_settings_text2, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}