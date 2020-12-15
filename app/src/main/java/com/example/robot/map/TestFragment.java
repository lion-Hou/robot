package com.example.robot.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.robot.R;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.pir4)
    TextView pir4;
    @BindView(R.id.test_relative2)
    RelativeLayout testRelative2;

    private View view;
    private GsonUtils gsonUtils;
    private Context mContext;



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
        gsonUtils = new GsonUtils();
        mContext = view.getContext();
        return view;
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
            String message1 = message.replace("\"","");
            String message2 = message1.replace("{","");
            String message3 = message2.replace("}","");
            String message4 = message3.replace(",","\n");
            Log.d(TAG, "onEventMsg mytest111333： " + message4);
            testSelfInspection.setText(message4);
            testSelfInspection.setMovementMethod(ScrollingMovementMethod.getInstance());

        } else if (messageEvent.getState() == 20002) {

        }
    }

    @OnClick({R.id.ultraSonic1})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ultraSonic1:
                break;
        }
    }

}