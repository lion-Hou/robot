package com.example.robot.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.robot.EmptyClient;
import com.example.robot.R;
import com.example.robot.SettingsActivity;
import com.example.robot.content.GsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapEditFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MapEditFragment";


    private GsonUtils gsonUtils;
    public EmptyClient emptyClient;
    private Context mContext;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map_edit, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {

    }

    private void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_settings:

                break;
            default:
                break;
        }
    }
}