package com.example.robot.map;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.SettingsActivity;
import com.example.robot.content.Content;
import com.example.robot.content.GsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "FirstFragment";

    @BindView(R.id.main_settings)
    Button settingsButton;





    private Context mContext;
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        settingsButton.setOnClickListener(this);

    }

    private void initListener() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_settings:
                Log.d(TAG, "onEventMsg ： " + "ssssss");
                Intent intent = new Intent();
                intent.setClass(getActivity(),SettingsActivity.class);
                getActivity().startActivity(intent);
                break;
            default:
                break;
        }
    }
}