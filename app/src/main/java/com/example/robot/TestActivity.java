package com.example.robot;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnTouchListener {
    private static final String TAG = "MainActivity";
    @BindView(R.id.forward_img)
    ImageView forwardImg;
    @BindView(R.id.to_left_img)
    ImageView toLeftImg;
    @BindView(R.id.to_right_img)
    ImageView toRightImg;
    @BindView(R.id.fallback_img)
    ImageView fallbackImg;
    @BindView(R.id.main_relative)
    RelativeLayout mainRelative;
    @BindView(R.id.uvc_btn)
    Switch uvcBtn;
    @BindView(R.id.light_btn)
    Switch lightBtn;
    @BindView(R.id.waring_btn)
    Switch waringBtn;
    @BindView(R.id.sensor_btn)
    Button sensorBtn;
    @BindView(R.id.sensor_text)
    TextView sensorText;
    private Context mContext;
    private GsonUtils gsonUtils;
    public static EmptyClient emptyClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        emptyClient = MainActivity.emptyClient;
        EventBus.getDefault().register(this);
        mContext = this;
        initView();
        initListener();

    }

    private void initListener() {
        uvcBtn.setOnCheckedChangeListener(this);
        lightBtn.setOnCheckedChangeListener(this);
        waringBtn.setOnCheckedChangeListener(this);
        sensorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emptyClient.send(gsonUtils.putTestMsg(Content.TEST_SENSOR));
            }
        });
        forwardImg.setOnTouchListener(this);
        toLeftImg.setOnTouchListener(this);
        toRightImg.setOnTouchListener(this);
        fallbackImg.setOnTouchListener(this);
    }

    private void initView() {
        gsonUtils = new GsonUtils();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (uvcBtn.isChecked()) {
            emptyClient.send(gsonUtils.putTestMsg(Content.TEST_UVCSTOP));
        }
        if (!lightBtn.isChecked()) {
            emptyClient.send(gsonUtils.putTestMsg(Content.TEST_LIGHTSTART));
        }
        if (waringBtn.isChecked()) {
            emptyClient.send(gsonUtils.putTestMsg(Content.TEST_WARINGSTOP));
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.forward_img:
                Toast.makeText(mContext, "前进", Toast.LENGTH_SHORT).show();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    emptyClient.send(gsonUtils.putJsonMessage(Content.STARTUP));
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    emptyClient.send(gsonUtils.putJsonMessage(Content.STOPUP));
                    Log.d(TAG, "抬起事件:上");
                }
                break;
            case R.id.to_left_img:
                Toast.makeText(mContext, "向左", Toast.LENGTH_SHORT).show();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    emptyClient.send(gsonUtils.putJsonMessage(Content.STARTLEFT));
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    emptyClient.send(gsonUtils.putJsonMessage(Content.STOPLEFT));
                    Log.d(TAG, "抬起事件:左");
                }
                break;
            case R.id.to_right_img:
                Toast.makeText(mContext, "向右", Toast.LENGTH_SHORT).show();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    emptyClient.send(gsonUtils.putJsonMessage(Content.STARTRIGHT));
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    emptyClient.send(gsonUtils.putJsonMessage(Content.STOPRIGHT));
                    Log.d(TAG, "抬起事件:右");
                }
                break;
            case R.id.fallback_img:
                Toast.makeText(mContext, "后退", Toast.LENGTH_SHORT).show();
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    emptyClient.send(gsonUtils.putJsonMessage(Content.STARTDOWN));
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    emptyClient.send(gsonUtils.putJsonMessage(Content.STOPDOWN));
                    Log.d(TAG, "抬起事件:下");
                }
                break;
        }

        return true;
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.uvc_btn:
                if (b) {
                    emptyClient.send(gsonUtils.putTestMsg(Content.TEST_UVCSTART));
                } else {
                    emptyClient.send(gsonUtils.putTestMsg(Content.TEST_UVCSTOP));
                }
                break;
            case R.id.light_btn:
                if (b) {
                    emptyClient.send(gsonUtils.putTestMsg(Content.TEST_LIGHTSTART));
                } else {
                    emptyClient.send(gsonUtils.putTestMsg(Content.TEST_LIGHTSTOP));
                }
                break;
            case R.id.waring_btn:
                if (b) {
                    emptyClient.send(gsonUtils.putTestMsg(Content.TEST_WARINGSTART));
                } else {
                    emptyClient.send(gsonUtils.putTestMsg(Content.TEST_WARINGSTOP));
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg ： " + messageEvent.getState());
        if (messageEvent.getState() == 20000) {
            sensorText.setText((String) messageEvent.getT());
        }
    }

}