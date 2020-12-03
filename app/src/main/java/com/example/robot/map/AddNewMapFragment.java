package com.example.robot.map;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.bean.RobotMapBean;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewMapFragment extends Fragment implements View.OnClickListener{

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
    public EmptyClient emptyClient;
    private Context mContext;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("hhhh",  "add_create");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d("hhhh",  "add_start");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d("hhhh",  "add_stop");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_new_map, container, false);
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();
        initView();
        initListener();
        mContext = view.getContext();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {
        newMapMapNameEditText.setOnClickListener(this);
        newMapScan.setOnClickListener(this);
        newMapSave.setOnClickListener(this);
        newMapBack.setOnClickListener(this);
    }

    private void initListener() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("hhhh",  "add_destory");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_map_scan:
                Log.d(TAG, "onEventMsg ： " + "点击开始扫描");
                Log.d(TAG, "onEventMsg ： " + "开始扫描");
                gsonUtils.setMapName(newMapMapNameEditText.getText().toString());
                Log.d(TAG, "name" + newMapMapNameEditText.getText().toString());
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.START_SCAN_MAP));
                break;
            case R.id.new_map_save:
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.CANCEL_SCAN_MAP));
                break;
            case R.id.new_map_back:
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.CANCEL_SCAN_MAP_NO));//取消保存
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new MapManagerFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg ： " + messageEvent.getState());
        if (messageEvent.getState() == 10001) {
            Log.d(TAG, "图片 ： " + messageEvent.getT());
            ByteBuffer bytes = (ByteBuffer) messageEvent.getT();
            int len = bytes.limit() - bytes.position();
            byte[] bytes1 = new byte[len];
            bytes.get(bytes1);
            Log.d(TAG, "新建地图 ： " + bytes1.length);
            Glide.with(mContext).load(bytes1).into(newMapMapImage);
        }
    }

}