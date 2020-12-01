package com.example.robot.map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.robot.EmptyClient;
import com.example.robot.R;
import com.example.robot.content.GsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewMapFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AddNewMapFragment";

    @BindView(R.id.new_map_mapImage)
    ImageView newMapMapImage;
    @BindView(R.id.new_map_mapName_editText)
    EditText newMapMapNameEditText;
    @BindView(R.id.new_map_scan)
    Button newMapScan;
    @BindView(R.id.new_map_save)
    Button newMapSave;
    @BindView(R.id.new_map_back)
    Button newMapBack;


    private GsonUtils gsonUtils;
    private EmptyClient emptyClient;
    private Context mContext;
    private  View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_new_map, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {
        newMapMapImage.setOnClickListener(this);
        newMapMapNameEditText.setOnClickListener(this);
        newMapScan.setOnClickListener(this);
        newMapSave.setOnClickListener(this);
        newMapBack.setOnClickListener(this);
    }

    private void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_map_scan:

                break;

            case R.id.new_map_save:
                break;

            case R.id.new_map_back:

                break;
            default:
                break;
        }
    }

}