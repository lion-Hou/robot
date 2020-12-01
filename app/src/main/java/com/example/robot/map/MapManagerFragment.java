package com.example.robot.map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.robot.EmptyClient;
import com.example.robot.R;
import com.example.robot.content.GsonUtils;

import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapManagerFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MapManagerFragment";

    @BindView(R.id.manager_selected)
    Spinner managerSelected;
    @BindView(R.id.manager_newMap)
    Button managerNewMap;
    @BindView(R.id.manager_mapImage)
    ImageView managerMapImage;
    @BindView(R.id.manager_rename)
    Button managerRename;
    @BindView(R.id.manager_delete)
    Button managerDelete;
    @BindView(R.id.manager_edit)
    Button managerEdit;
    @BindView(R.id.manager_back)
    Button managerBack;

    private GsonUtils gsonUtils;
    public EmptyClient emptyClient;
    private Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_manager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
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
            case R.id.manager_newMap:


                break;
            default:
                break;
        }
    }
}