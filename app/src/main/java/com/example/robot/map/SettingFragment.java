package com.example.robot.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.nio.ByteBuffer;

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

    private View view;
    private GsonUtils gsonUtils;
    private Context mContext;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {

    }

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        view = inflater.inflate(R.layout.fragment_map_edit, container, false);
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();
        mContext = view.getContext();
        initView();
        return view;
    }

    private void initView(){
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_LED_LEVEL));//0,1,2
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_SPEED_LEVEL));//0,1,2
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_LOW_BATTERY));//30-80
    }

    @OnClick({R.id.electricityQuantityTV, R.id.settings_electricityQuantity, R.id.languageTV, R.id.settings_language, R.id.volumeTV, R.id.settings_volume, R.id.robotSpeedTV, R.id.settings_robotSpeed, R.id.ledBrightnessTV, R.id.settings_ledBrightness, R.id.versionNumberTV, R.id.settings_versionNumber, R.id.settings_ok, R.id.settings_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.electricityQuantityTV:

                break;
            case R.id.settings_electricityQuantity:

                break;
            case R.id.languageTV:

                break;
            case R.id.settings_language:
                break;
            case R.id.volumeTV:
                break;
            case R.id.settings_volume:
                break;
            case R.id.robotSpeedTV:
                break;
            case R.id.settings_robotSpeed:
                break;
            case R.id.ledBrightnessTV:
                break;
            case R.id.settings_ledBrightness:
                break;
            case R.id.versionNumberTV:
                break;
            case R.id.settings_versionNumber:
                break;
            case R.id.settings_ok:
                break;
            case R.id.settings_cancel:
                break;
        }
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg ： " + messageEvent.getState());
        if (messageEvent.getState() == 10001) {

           }
        }
    }