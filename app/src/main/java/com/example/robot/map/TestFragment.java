package com.example.robot.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestFragment extends Fragment {


    private static final String TAG = "TestFragment";
    @BindView(R.id.test_self_inspection_title)
    TextView testSelfInspectionTitle;
    @BindView(R.id.test_self_inspection)
    TextView testSelfInspection;
    @BindView(R.id.center_view)
    TextView centerView;
    @BindView(R.id.ultraSonic1)
    TextView ultraSonic1;
    @BindView(R.id.ultraSonic2)
    TextView ultraSonic2;
    @BindView(R.id.ultraSonic3)
    TextView ultraSonic3;
    @BindView(R.id.ultraSonic4)
    TextView ultraSonic4;
    @BindView(R.id.ultraSonic5)
    TextView ultraSonic5;
    @BindView(R.id.ultraSonic6)
    TextView ultraSonic6;
    @BindView(R.id.ultraSonic7)
    TextView ultraSonic7;
    @BindView(R.id.ultraSonic8)
    TextView ultraSonic8;
    @BindView(R.id.ultraSonic)
    RelativeLayout ultraSonic;
    @BindView(R.id.test_relative1)
    RelativeLayout testRelative1;
    @BindView(R.id.upper_computer_check)
    TextView upperComputerCheck;
    @BindView(R.id.switch_voice)
    Switch switchVoice;
    @BindView(R.id.switch_led)
    Switch switchLed;
    @BindView(R.id.switch_uvc_all)
    Switch switchUvcAll;
    @BindView(R.id.switch_uvc1)
    Switch switchUvc1;
    @BindView(R.id.switch_uvc2)
    Switch switchUvc2;
    @BindView(R.id.switch_uvc3)
    Switch switchUvc3;
    @BindView(R.id.switch_uvc4)
    Switch switchUvc4;
    @BindView(R.id.test_relative3)
    RelativeLayout testRelative3;
    @BindView(R.id.pir_main)
    TextView pirMain;
    @BindView(R.id.pir1)
    TextView pir1;
    @BindView(R.id.pir2)
    TextView pir2;
    @BindView(R.id.pir3)
    TextView pir3;
    @BindView(R.id.test_relative2)
    RelativeLayout testRelative2;
    @BindView(R.id.test_fragment)
    RelativeLayout testFragment;
    @BindView(R.id.test_back)
    Button testBack;

    private View view;
    private GsonUtils gsonUtils;
    private Context mContext;


    private Handler handler = new Handler();
    private Runnable runnable;


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        //开始
        handler.postDelayed(runnable, 1000); // 开始Timer
        Log.d("hhhh", "edit_start");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        switchVoice.setChecked(false);
        switchLed.setChecked(false);
        switchUvcAll.setChecked(false);
        switchUvc1.setChecked(false);
        switchUvc2.setChecked(false);
        switchUvc3.setChecked(false);
        switchUvc4.setChecked(false);
        handler.removeCallbacks(runnable); //停止Timer
        Log.d("hhhh", "edit_stop");
    }

    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, view);
        initView();
        runnable = new Runnable() {
            public void run() {
                MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_SENSOR));
                MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.GET_ULTRASONIC));
                handler.postDelayed(this, 1000);
                //postDelayed(this,2000)方法安排一个Runnable对象到主线程队列中
            }
        };
        testFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        gsonUtils = new GsonUtils();
        mContext = view.getContext();
        return view;
    }

    private void initView() {
        switchVoice.setChecked(false);
        switchLed.setChecked(false);
        switchUvcAll.setChecked(false);
        switchUvc1.setChecked(false);
        switchUvc2.setChecked(false);
        switchUvc3.setChecked(false);
        switchUvc4.setChecked(false);
        switchVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchVoice.isChecked()) {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_WARNINGSTART));
                } else {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_WARNINGSTOP));
                }
            }
        });

        switchLed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchLed.isChecked()) {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_LIGHTSTART));
                } else {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_LIGHTSTOP));
                }
            }
        });

        switchUvcAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchUvcAll.isChecked()) {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_UVCSTART_ALL));
                } else {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_UVCSTOP_ALL));
                }
            }
        });

        switchUvc1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchUvc1.isChecked()) {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_UVCSTART_1));
                } else {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_UVCSTOP_1));
                }
            }
        });

        switchUvc2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchUvc2.isChecked()) {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_UVCSTART_2));
                } else {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_UVCSTOP_2));
                }
            }
        });

        switchUvc3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchUvc3.isChecked()) {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_UVCSTART_3));
                } else {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_UVCSTOP_3));
                }
            }
        });

        switchUvc4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchUvc4.isChecked()) {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_UVCSTART_4));
                } else {
                    MainActivity.emptyClient.send(gsonUtils.putTestMsg(Content.TEST_UVCSTOP_4));
                }
            }
        });


    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg mytest： " + messageEvent.getState());
        if (messageEvent.getState() == 19199) {
            //"{\"antiPressureFoot\":true,\"cameraNotTrigger\":true,\"cannotRotate\":false,\"deviceTopic\":true,\"driverErrorLeftDriverCommandError\":true,\"driverErrorLeftDriverCommunicationError\":true,\"driverErrorLeftDriverCurrentExceedsLimit\":true,\"driverErrorLeftDriverEmergencyStop\":true,\"driverErrorLeftDriverError\":true,\"driverErrorLeftDriverExternalStop\":true,\"driverErrorLeftDriverHoarePhaseSequenceError\":true,\"driverErrorLeftDriverInitializationError\":true,\"driverErrorLeftDriverMosError\":true,\"driverErrorLeftDriverOverheat\":true,\"driverErrorLeftDriverOverload\":true,\"driverErrorLeftDriverParameterError\":true,\"driverErrorLeftDriverShortCircuit\":true,\"driverErrorLeftDriverVoltageIsBelowFunctioningThreshold\":true,\"driverErrorLeftEEPDataError\":true,\"driverErrorLeftEncoderError\":true,\"driverErrorLeftMotorDriverVoltageExceedsLimit\":true,\"driverErrorLeftMotorExceedsSpeedLimit\":true,\"driverErrorLeftMotorFeedbackError\":true,\"driverErrorLeftMotorIsDisconnected\":true,\"driverErrorLeftMotorOverheat\":true,\"driverErrorLeftMotorPowerOnProtection\":true,\"driverErrorRightDriverCommandError\":true,\"driverErrorRightDriverCommunicationError\":true,\"driverErrorRightDriverCurrentExceedsLimit\":true,\"driverErrorRightDriverEEPDataError\":true,\"driverErrorRightDriverEmergencyStop\":true,\"driverErrorRightDriverError\":true,\"driverErrorRightDriverExternalStop\":true,\"driverErrorRightDriverHoarePhaseSequenceError\":true,\"driverErrorRightDriverInitializationError\":true,\"driverErrorRightDriverMosError\":true,\"driverErrorRightDriverOverheat\":true,\"driverErrorRightDriverOverload\":true,\"driverErrorRightDriverParameterError\":true,\"driverErrorRightDriverShortCircuit\":true,\"driverErrorRightDriverVoltageExceedsLimit\":true,\"driverErrorRightDriverVoltageIsBelowFunctioningThreshold\":true,\"driverErrorRightEncoderSignalError\":true,\"driverErrorRightMotorExceedsSpeedLimit\":true,\"driverErrorRightMotorFeedbackError\":true,\"driverErrorRightMotorIsDisconnected\":true,\"driverErrorRightMotorOverheat\":true,\"driverErrorRightMotorPowerOnProtection\":true,\"healthTopic\":true,\"imuBoard\":true,\"imuTopic\":true,\"laserNotTrigger\":true,\"laserParam\":true,\"laserTopic\":true,\"leftMotor\":true,\"localizationLost\":true,\"odomTopic\":true,\"powerBoard\":true,\"protectorNotTrigger\":true,\"protectorTopic\":true,\"rightMotor\":true,\"robotSurround\":true,\"stuckVirtualWall\":true,\"ultrasonic0\":true,\"ultrasonic1\":true,\"ultrasonic2\":true,\"ultrasonic3\":true,\"ultrasonic4\":true,\"ultrasonic5\":true,\"ultrasonic6\":true,\"ultrasonic7\":true,\"ultrasonicBoard\":true,\"usbSecurity\":true}\n"}
            String message = (String) messageEvent.getT();
            Log.d(TAG, "onEventMsg mytest111： " + message);
            String message1 = message.replace("\"", "");
            String message2 = message1.replace("{", "");
            String message3 = message2.replace("}", "");
            String message4 = message3.replace(",", "\n");
            int color1 = Color.parseColor("#00FF00");
            int color2 = Color.parseColor("#FF0000");
            Log.d(TAG, "onEventMsg mytest111333： " + color1);
            Log.d(TAG, "onEventMsg mytest111333： " + message4);
            testSelfInspection.setText(matcherSearchText(color1, message4, "true", color2, "false"));
            testSelfInspection.setMovementMethod(ScrollingMovementMethod.getInstance());

        } else if (messageEvent.getState() == 19198) {
            String message = (String) messageEvent.getT();
            String message1 = message.replace("\"", "");
            int i = message1.indexOf(",");
            int j = message1.indexOf(",", i + 1);
            String itemString1 = message1.substring(0, i);
            String itemString2 = message1.substring(i + 1, j);
            String itemString3 = message1.substring(j + 1, message1.length());
            Log.d(TAG, "onEventMsg 19198： " + "  " + i + message1 + "  " + itemString1);
            Log.d(TAG, "onEventMsg 191981：" + j + itemString2);
            Log.d(TAG, "onEventMsg 191982：" + itemString3);
            int color1 = Color.parseColor("#00FF00");
            int color2 = Color.parseColor("#FF0000");
            if (itemString1.equals("false")) {
                pir1.setTextColor(color1);
            } else {
                pir1.setTextColor(color2);
            }
            if (itemString2.equals("false")) {
                pir2.setTextColor(color1);
            } else {
                pir2.setTextColor(color2);
            }
            if (itemString2.equals("false")) {
                pir3.setTextColor(color1);
            } else {
                pir3.setTextColor(color2);
            }
        } else if (messageEvent.getState() == 19197) {
            String message = (String) messageEvent.getT();
            try {
                int color1 = Color.parseColor("#00FF00");
                int color2 = Color.parseColor("#FF0000");
                JSONArray jsonArray = new JSONArray(message);
                Log.d(TAG, "onEventMsg 191971：" + jsonArray.length());
                JSONObject jsonObject00 = jsonArray.getJSONObject(0);
                JSONObject jsonObject01 = jsonArray.getJSONObject(1);
                String X00=jsonObject00.getString("get_ultrasonic_x");
                String Y00=jsonObject00.getString("get_ultrasonic_y");
                String X01=jsonObject01.getString("get_ultrasonic_x");
                String Y01=jsonObject01.getString("get_ultrasonic_y");
                if (X00.equals(X01)&&Y00.equals(Y01)){
                    ultraSonic1.setTextColor(color2);
                }else {
                    ultraSonic1.setTextColor(color1);
                }

                JSONObject jsonObject10 = jsonArray.getJSONObject(2);
                JSONObject jsonObject11 = jsonArray.getJSONObject(3);
                String X10=jsonObject10.getString("get_ultrasonic_x");
                String Y10=jsonObject10.getString("get_ultrasonic_y");
                String X11=jsonObject11.getString("get_ultrasonic_x");
                String Y11=jsonObject11.getString("get_ultrasonic_y");
                if (X10.equals(X11)&&Y10.equals(Y11)){
                    ultraSonic2.setTextColor(color2);
                }
                else {
                    ultraSonic2.setTextColor(color1);
                }

                JSONObject jsonObject20 = jsonArray.getJSONObject(4);
                JSONObject jsonObject21 = jsonArray.getJSONObject(5);
                String X20=jsonObject20.getString("get_ultrasonic_x");
                String Y20=jsonObject20.getString("get_ultrasonic_y");
                String X21=jsonObject21.getString("get_ultrasonic_x");
                String Y21=jsonObject21.getString("get_ultrasonic_y");
                if (X20.equals(X21)&&Y20.equals(Y21)){
                    ultraSonic3.setTextColor(color2);
                }
                else {
                    ultraSonic3.setTextColor(color1);
                }

                JSONObject jsonObject30 = jsonArray.getJSONObject(6);
                JSONObject jsonObject31 = jsonArray.getJSONObject(7);
                String X30=jsonObject30.getString("get_ultrasonic_x");
                String Y30=jsonObject30.getString("get_ultrasonic_y");
                String X31=jsonObject31.getString("get_ultrasonic_x");
                String Y31=jsonObject31.getString("get_ultrasonic_y");
                if (X30.equals(X31)&&Y30.equals(Y31)){
                    ultraSonic4.setTextColor(color2);
                }
                else {
                    ultraSonic4.setTextColor(color1);
                }


                JSONObject jsonObject40 = jsonArray.getJSONObject(8);
                JSONObject jsonObject41 = jsonArray.getJSONObject(9);
                String X40=jsonObject40.getString("get_ultrasonic_x");
                String Y40=jsonObject40.getString("get_ultrasonic_y");
                String X41=jsonObject41.getString("get_ultrasonic_x");
                String Y41=jsonObject41.getString("get_ultrasonic_y");
                if (X40.equals(X41)&&Y40.equals(Y41)){
                    ultraSonic5.setTextColor(color2);
                }
                else {
                    ultraSonic5.setTextColor(color1);
                }

                JSONObject jsonObject50 = jsonArray.getJSONObject(10);
                JSONObject jsonObject51 = jsonArray.getJSONObject(11);
                String X50=jsonObject50.getString("get_ultrasonic_x");
                String Y50=jsonObject50.getString("get_ultrasonic_y");
                String X51=jsonObject51.getString("get_ultrasonic_x");
                String Y51=jsonObject51.getString("get_ultrasonic_y");
                if (X50.equals(X51)&&Y50.equals(Y51)){
                    ultraSonic6.setTextColor(color2);
                }
                else {
                    ultraSonic6.setTextColor(color1);
                }


                JSONObject jsonObject60 = jsonArray.getJSONObject(12);
                JSONObject jsonObject61 = jsonArray.getJSONObject(13);
                String X60=jsonObject60.getString("get_ultrasonic_x");
                String Y60=jsonObject60.getString("get_ultrasonic_y");
                String X61=jsonObject61.getString("get_ultrasonic_x");
                String Y61=jsonObject61.getString("get_ultrasonic_y");
                if (X60.equals(X61)&&Y60.equals(Y61)){
                    ultraSonic7.setTextColor(color2);
                }
                else {
                    ultraSonic7.setTextColor(color1);
                }

                JSONObject jsonObject70 = jsonArray.getJSONObject(14);
                JSONObject jsonObject71 = jsonArray.getJSONObject(15);
                String X70=jsonObject70.getString("get_ultrasonic_x");
                String Y70=jsonObject70.getString("get_ultrasonic_y");
                String X71=jsonObject71.getString("get_ultrasonic_x");
                String Y71=jsonObject71.getString("get_ultrasonic_y");
                if (X70.equals(X71)&&Y70.equals(Y71)){
                    ultraSonic8.setTextColor(color2);
                }
                else {
                    ultraSonic8.setTextColor(color1);
                }


                Log.d(TAG, "onEventMsg 191971：" + X00);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.test_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.test_back:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment, new SettingFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    public static SpannableString matcherSearchText(int color, String text, String keyword, int color2, String keyword2) {
        SpannableString ss = new SpannableString(text);
        Pattern pattern = Pattern.compile(keyword);
        Pattern pattern2 = Pattern.compile(keyword2);
        Matcher matcher = pattern.matcher(ss);
        Matcher matcher2 = pattern2.matcher(ss);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        while (matcher2.find()) {
            int start = matcher2.start();
            int end = matcher2.end();
            ss.setSpan(new ForegroundColorSpan(color2), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

}