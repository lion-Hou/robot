package com.example.robot.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.content.Content;
import com.example.robot.content.GsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RobotControlPanelFragment extends Fragment implements View.OnTouchListener {

    private static final String TAG = "RobotControlPanelFragment";
    @BindView(R.id.forward_img)
    ImageView forwardImg;
    @BindView(R.id.to_left_img)
    ImageView toLeftImg;
    @BindView(R.id.to_right_img)
    ImageView toRightImg;
    @BindView(R.id.fallback_img)
    ImageView fallbackImg;


    private GsonUtils gsonUtils;
    public EmptyClient emptyClient;
    private Context mContext;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_robot_control_panel, container, false);
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();

        initView();
        initListener();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {
        forwardImg.setOnTouchListener(this);
        toLeftImg.setOnTouchListener(this);
        toRightImg.setOnTouchListener(this);
        fallbackImg.setOnTouchListener(this);
    }

    private void initListener() {

    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {


        switch (view.getId()) {
            case R.id.forward_img:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STARTUP));
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPUP));
                    Log.d(TAG, "抬起事件:上");
                }
                break;
            case R.id.to_left_img:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STARTLEFT));
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPLEFT));
                    Log.d(TAG, "抬起事件:左");
                }
                break;
            case R.id.to_right_img:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STARTRIGHT));

                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPRIGHT));
                    Log.d(TAG, "抬起事件:右");
                }
                break;
            case R.id.fallback_img:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STARTDOWN));
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPDOWN));
                    Log.d(TAG, "抬起事件:下");
                }
                break;
        }
        return true;

    }
}