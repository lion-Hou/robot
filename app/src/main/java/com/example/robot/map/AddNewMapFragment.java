package com.example.robot.map;

import android.app.ProgressDialog;
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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
    private ProgressDialog waitingDialog;
    private String[] mapName;

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
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPLIST));
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
                boolean isRepeat = false;
                for (int i = 0; i <mapName.length ; i++) {
                    if (mapName[i].equals(newMapMapNameEditText.getText().toString())){
                        isRepeat = true;
                    }
                }

                if (newMapMapNameEditText.getText().toString().isEmpty()){
                    Toast.makeText(mContext, R.string.toast_new_map_text1, Toast.LENGTH_SHORT).show();
                }else {
                    if (isRepeat == false) {
                        Log.d(TAG, "onEventMsg ： " + "点击开始扫描");
                        addNewMapDialog = new NormalDialogUtil();
                        final CharSequence dialogNewMapText1=mContext.getString(R.string.dialog_new_map_text1);
                        final CharSequence allCancel=mContext.getString(R.string.all_cancel);
                        final CharSequence dialogNewMapText2=mContext.getString(R.string.dialog_new_map_text2);
                        addNewMapDialog.showDialog(mContext, "", (String) dialogNewMapText1, (String) allCancel, (String) dialogNewMapText2, new DialogInterface.OnClickListener() {
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
                                newMapScan.setEnabled(false);
                                newMapName = newMapMapNameEditText.getText().toString();
                                gsonUtils.setMapName(newMapName);
                                Log.d(TAG, "name" + newMapMapNameEditText.getText().toString());
                                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.START_SCAN_MAP));
                                dialog.dismiss();
                                Toast.makeText(mContext, R.string.toast_new_map_text2, Toast.LENGTH_LONG).show();
                            }
                        });
                    } else if (isRepeat==true) {
                        Toast.makeText(mContext, R.string.toast_new_map_text3, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.new_map_save:
                if (newMapMapImage.getDrawable() == null){
                    Log.d("gggggg","无图片");
                    Toast.makeText(mContext, R.string.task_edit_no_image, Toast.LENGTH_LONG).show();
                }else {
                    Log.d("gggggg","有图片");
                    addNewMapDialog = new NormalDialogUtil();
                    final CharSequence dialogNewMapText3=mContext.getString(R.string.dialog_new_map_text3);
                    final CharSequence allCancel=mContext.getString(R.string.all_cancel);
                    final CharSequence dialogNewMapText4=mContext.getString(R.string.dialog_new_map_text4);
                    addNewMapDialog.showDialog(mContext, "", (String)dialogNewMapText3,(String)allCancel,(String)dialogNewMapText4, new DialogInterface.OnClickListener() {
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

                            waitingDialog = new ProgressDialog(mContext);
                            Log.d(TAG, "onEventMsg ： "+"dialog生成");
                            final CharSequence strDialogBody=mContext.getString(R.string.dialog_new_map_text5);
                            waitingDialog.setMessage(strDialogBody);
                            waitingDialog.setIndeterminate(true);
                            waitingDialog.setCancelable(false);
                            waitingDialog.show();
                        }
                    });
                }
                break;
            case R.id.new_map_back:
                addNewMapDialog = new NormalDialogUtil();
                final CharSequence dialogNewMapText6=mContext.getString(R.string.dialog_new_map_text6);
                final CharSequence allCancel1=mContext.getString(R.string.all_cancel);
                final CharSequence dialogNewMapText7=mContext.getString(R.string.dialog_new_map_text7);
                addNewMapDialog.showDialog(mContext, "", (String) dialogNewMapText6, (String) allCancel1, (String) dialogNewMapText7, new DialogInterface.OnClickListener() {
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
        }else if (messageEvent.getState() == 10005) {
            int ori_size = Content.list.size();
            System.out.println("ZHZHSSSS: ori_size = " + ori_size);
            int null_count = 0;
            for (int i=0;i< ori_size;i++) {
                String temp1 = Content.list.get(i).getMap_Name();
                System.out.println("ZHZHZZZZ1111,temp1 =: " + temp1);
                if(TextUtils.isEmpty(temp1)){
                    //    null_count++;
                    Content.list.remove(i);
                    ori_size--;
                }
            }
            mapName = new String[Content.list.size()-null_count];
            System.out.println("ZHZHSSSS: " + Content.list.size());
            for (int i=0;i< Content.list.size();i++) {
                mapName[i] =Content.list.get(i).getMap_Name();
                System.out.println("ZHZHZZZZ: " + mapName[i]);
            }
        }else if (messageEvent.getState() == 19191) {
            String message = (String) messageEvent.getT();
            if (message.equals("取消扫描并且保存地图successed")){
                waitingDialog.dismiss();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new MapEditFragment(), null)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

}