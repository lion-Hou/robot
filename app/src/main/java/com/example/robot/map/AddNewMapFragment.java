package com.example.robot.map;

import android.content.Context;
import android.content.DialogInterface;
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
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.bean.RobotMapBean;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.util.NormalDialogUtil;

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



    private NormalDialogUtil addNewMapDialog;
    private GsonUtils gsonUtils;
    public EmptyClient emptyClient;
    private Context mContext;
    private View view;
    private String newMapName;

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
        Log.d("Destroy",  "add_destroy");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_map_scan:
                if (newMapMapNameEditText.getText().toString().isEmpty()){
                    Toast.makeText(mContext, "请输入新地图的名字", Toast.LENGTH_SHORT).show();
                }else {
                Log.d(TAG, "onEventMsg ： " + "点击开始扫描");
                addNewMapDialog = new NormalDialogUtil();
                addNewMapDialog.showDialog(mContext, "","是否开始扫描","取消","开始扫描" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //确定逻辑
                        Log.d(TAG, "onEventMsg ： " + "开始扫描");
                        newMapMapNameEditText.setEnabled(false);
                        newMapName = newMapMapNameEditText.getText().toString();
                        gsonUtils.setMapName(newMapName);
                        Log.d(TAG, "name" + newMapMapNameEditText.getText().toString());
                        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.START_SCAN_MAP));
                        dialog.dismiss();
                    }
                });
                }
                break;
            case R.id.new_map_save:
                addNewMapDialog = new NormalDialogUtil();
                addNewMapDialog.showDialog(mContext, "","是否结束扫描并保存地图","取消","保存地图" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //确定逻辑
                        /**
                         * houbo
                         */
                        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.CANCEL_SCAN_MAP));
                        dialog.dismiss();
                        Content.map_Name=newMapName;
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.first_fragment, new MapEditFragment(), null)
                                .addToBackStack(null)
                                .commit();
                    }
                });
                break;
            case R.id.new_map_back:
                addNewMapDialog = new NormalDialogUtil();
                addNewMapDialog.showDialog(mContext, "","是否结束当前扫描","取消","结束扫描" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //确定逻辑
                        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.CANCEL_SCAN_MAP_NO));
                        dialog.dismiss();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.first_fragment, new MapManagerFragment(), null)
                                .addToBackStack(null)
                                .commit();
                    }
                });
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