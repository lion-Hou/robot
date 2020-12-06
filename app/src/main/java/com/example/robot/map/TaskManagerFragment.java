package com.example.robot.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.task.TaskNewFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TaskManagerFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "TaskManagerFragment";
    @BindView(R.id.task_manage_title)
    TextView taskTitle;
    @BindView(R.id.task_manage_name)
    TextView taskName;
    @BindView(R.id.task_manage_new_task)
    Button newTask;
    @BindView(R.id.task_manage_map_Image)
    ImageView taskManageMapImage;
    @BindView(R.id.task_manage_map_relative)
    RelativeLayout taskManageMapRelative;
    @BindView(R.id.task_manage_edit)
    Button taskEdit;
    @BindView(R.id.task_manage_delete)
    Button taskDelete;
    @BindView(R.id.task_manage_back)
    Button taskBack;

    private Context mContext;
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;
    private String[] taskNameList;
    public View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("hhhh", "first_creat");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d("hhhh",  "manger_start");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d("hhhh",  "manger_stop");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_manager, container, false);
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();
        mContext = view.getContext();
        gsonUtils.setMapName(Content.map_Name);
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
        initView();
        initListener();
        return view;
    }

    private void initView() {
        newTask.setOnClickListener(this);
        taskName.setOnClickListener(this);
    }

    private void initListener() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.task_manage_new_task:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new TaskNewFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.task_manage_name:
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETTASKQUEUE));//请求任务列表
        }

    }

    public void requestTaskList(String[] taskNameList){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        System.out.println("which" + taskNameList.length);
        builder.setItems(taskNameList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("which" + which);
                taskName.setText(taskNameList[which]);
            }
        });
        builder.create().show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent){
        Log.d(TAG, "onEventMsg ： " + messageEvent.getState());
        if (messageEvent.getState() == 10001) {
            Log.d(TAG, "图片 ： " + messageEvent.getT());
            ByteBuffer bytes = (ByteBuffer) messageEvent.getT();
            int len = bytes.limit() - bytes.position();
            byte[] bytes1 = new byte[len];
            bytes.get(bytes1);
            Log.d(TAG, "任务界面获得地图数据" + bytes1.length);
            Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
            taskManageMapImage.setImageBitmap(mBitmap);
        }else if(messageEvent.getState() == 10017){
            taskNameList = (String[]) messageEvent.getT();
            Log.d("task_name",taskNameList[0]);
            requestTaskList(taskNameList);
        }
    }
}