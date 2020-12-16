package com.example.robot.map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.robot.R;
import com.kongqw.rockerlibrary.view.RockerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RockerFragment extends Fragment {

    public View view;
    @BindView(R.id.rockerView)
    RockerView rockerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rocker, container, false);
        ButterKnife.bind(this, view);
        initRockerView();
        return view;
    }

    private void initRockerView() {
        // https://blog.csdn.net/q4878802/article/details/52402529 参考
        // 设置回调模式
        rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        // 监听摇动方向
        rockerView.setOnShakeListener(RockerView.DirectionMode.DIRECTION_8, new RockerView.OnShakeListener() {
            @Override
            public void onStart() {
                Log.d("wdd", "onStart");
            }

            @Override
            public void direction(RockerView.Direction direction) {
                Log.d("wdd", "direction: " + direction);
            }

            @Override
            public void onFinish() {
                Log.d("wdd", "onFinish");
            }
        });
    }
}