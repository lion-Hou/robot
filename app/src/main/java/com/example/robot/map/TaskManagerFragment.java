package com.example.robot.map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.bean.DrawLineBean;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.task.TaskEditFragment;
import com.example.robot.task.TaskNewFragment;
import com.example.robot.util.NormalDialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.task_manage_map_relative_border)
    RelativeLayout taskManageMapRelativeBorder;
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

    private NormalDialogUtil mapManageNormalDialog;

    private List<ImageView> imageViewArrayList = new ArrayList<>();
    private int index = 0;
    double mBitmapHeight;
    double mBitmapWidth;
    private NormalDialogUtil delectTask;
    String a;
    String select_cn = "请选择任务";
    String select_en = "PLEASE SELECT TASK";

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
        gsonUtils.setMapName(Content.first_map_Name);
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
        initView();
        initListener();
        return view;
    }

    private void initView() {
        newTask.setOnClickListener(this);
        taskName.setOnClickListener(this);
        taskDelete.setOnClickListener(this);
        taskBack.setOnClickListener(this);
        taskEdit.setOnClickListener(this);

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
                gsonUtils.setMapName(Content.first_map_Name);
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETTASKQUEUE));//请求任务列表
                break;
            case R.id.task_manage_delete:
                a = (String) taskName.getText();
                if(a.equals(select_cn)  || a.equals(select_en)){
                    Toast toast = Toast.makeText(mContext,"请选择任务名",Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    AlertDialog.Builder delete = new AlertDialog.Builder(mContext);
                    delete.setMessage("是否删除当前任务");
                    //设置正面按钮
                    delete.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("Content.fixTaskName", Content.fixTaskName);
                            gsonUtils.setMapName(Content.first_map_Name);
                            gsonUtils.setTaskName(Content.fixTaskName);
                            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.DELETETASKQUEUE));//删除任务
                            Content.fixTaskName = null;
                            taskName.setText(R.string.map_manage_select_task);
                            dialog.dismiss();
                        }
                    });
                    //设置反面按钮
                    delete.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    delete.show();
                }
                break;
            case R.id.task_manage_back:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new FirstFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.task_manage_edit:
                a = (String) taskName.getText();
                if(a.equals(select_cn)  || a.equals(select_en)){
                    Toast toast = Toast.makeText(mContext,"请选择任务名",Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.first_fragment, new TaskEditFragment(), null)
                            .addToBackStack(null)
                            .commit();
                }
                break;
            default:
                break;
        }

    }

    public void requestTaskList(String[] taskNameList){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        System.out.println("which" + taskNameList.length);
        builder.setItems(taskNameList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("which" + which);
                Content.fixTaskName = taskNameList[which];
                taskName.setText(taskNameList[which]);
                Log.d(TAG, "ggggg ： " + taskName.getText());
            }
        });
        builder.create().show();
    }

    @SuppressLint({"ResourceAsColor", "NewApi"})
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
            mBitmapHeight = mBitmap.getHeight();
            mBitmapWidth = mBitmap.getWidth();
            if (mBitmapHeight >= mBitmapWidth){
                mBitmapWidth = taskManageMapRelativeBorder.getHeight()/mBitmapHeight*mBitmapWidth;
                mBitmapHeight = taskManageMapRelativeBorder.getHeight();
            }else if (mBitmapHeight > mBitmapWidth){
                mBitmapHeight = taskManageMapRelativeBorder.getWidth()/mBitmapWidth*mBitmapHeight;
                mBitmapWidth = taskManageMapRelativeBorder.getWidth();
            }
            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETPOINTPOSITION));
            taskManageMapImage.setImageBitmap(mBitmap);
            taskManageMapRelative.setLayoutParams(new RelativeLayout.LayoutParams((int)mBitmapWidth,(int)mBitmapHeight));
            RelativeLayout.LayoutParams layoutParams = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
            taskManageMapImage.setLayoutParams(layoutParams);
            taskManageMapRelative.addView(taskManageMapImage);
        } else if (messageEvent.getState() == 10008) {
            for (int i = 0; i <imageViewArrayList.size(); i++) {
                taskManageMapRelative.removeView(imageViewArrayList.get(i));
            }
            String message = (String) messageEvent.getT();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray(Content.SENDPOINTPOSITION);
                    Log.d("zdzd000 ", "pointName : " + jsonArray.toString());
                    for (int k = 0; k < Content.list.size(); k++) {
                        if (Content.list.get(k).getMap_Name().equals(Content.map_Name)) {
                            Log.d("zdzd555", "" + Content.list.get(k).getResolution());
                            index = k;
                        }
                    }
                    Log.d("zdzd5111", "" + index);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonItem = jsonArray.getJSONObject(i);
                        Log.d("zdzd111 ", "pointName : " + jsonItem.toString());
                        ImageView imageView = new ImageView(mContext);
                        imageView.setImageResource(R.drawable.ic_point);
                        imageViewArrayList.add(imageView);
                        imageView.setOnClickListener(this);
                        double gridHeight = Content.list.get(index).getGridHeight();
                        double gridWidth = Content.list.get(index).getGridWidth();

                        double pointX = jsonItem.getDouble(Content.POINT_X);
                        int pointType = jsonItem.getInt(Content.POINT_TYPE);
                        double pointY = jsonItem.getDouble(Content.POINT_Y);
                        double originX = Content.list.get(index).getOriginX();
                        double originY = Content.list.get(index).getOriginY();
                        double resolution = Content.list.get(index).getResolution();
                        double angleY = Math.sin(jsonItem.getDouble(Content.ANGLE));
                        double angleX = Math.cos(jsonItem.getDouble(Content.ANGLE));

                        Log.d("zdzd9998", "gridH"+gridHeight+"        gridW"+gridWidth + "     pointX"+pointX+"       originX"+originX+"       Content.ROBOT_SIZE "+Content.ROBOT_SIZE);
                        Log.d("zdzd9998", " resolution * angleX"+  resolution*angleX);

                        if (pointType == 1) {
                            ImageView charging_Img = new ImageView(mContext);
                            charging_Img.setImageResource(R.drawable.charging);
                            imageViewArrayList.add(charging_Img);
                            charging_Img.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                    (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY))),
                                    0, 0);
                            taskManageMapRelative.addView(charging_Img);
                        }
                        if (pointType == 2) {
                            imageView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                    (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY))),
                                    0, 0);
                            Log.d("zdzd9998", "angleX" + angleX);
                            Log.d("zdzd9998", " resolution" + resolution);
                            taskManageMapRelative.addView(imageView);
                            imageViewArrayList.add(imageView);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (messageEvent.getState() == 40002) {//获取虚拟墙
            String message = (String) messageEvent.getT();
            double gridHeight = Content.list.get(index).getGridHeight();
            double gridWidth = Content.list.get(index).getGridWidth();
            double originX = Content.list.get(index).getOriginX();
            double originY = Content.list.get(index).getOriginY();
            try {
                JSONObject jsonObject = new JSONObject(message);
                JSONArray jsonArray = jsonObject.getJSONArray(Content.SEND_VIRTUAL);
                for (int i = 0; i < jsonArray.length(); i++) {
                    List<DrawLineBean> lineBeans = new ArrayList<>();
                    for (int k = 0; k < jsonArray.getJSONArray(i).length(); k++) {
                        JSONObject js = jsonArray.getJSONArray(i).getJSONObject(k);
                        DrawLineBean drawLineBean = new DrawLineBean();
                        drawLineBean.setX(js.getInt(Content.VIRTUAL_X));
                        drawLineBean.setY(js.getInt(Content.VIRTUAL_Y));
                        lineBeans.add(drawLineBean);
                    }
                    float startX = 0, startY = 0, endX = 0, endY = 0;
                    for (int k = 0; k < lineBeans.size(); k++) {
                        if (k == 0) {
                            startX = (float) ((mBitmapWidth / (lineBeans.get(k).getX() * gridWidth)) + originX);
                            startY = (float) (mBitmapHeight / (gridHeight * (mBitmapHeight - lineBeans.get(k).getY())) + originY);
                        } else {
                            endX = (float) ((mBitmapWidth / (lineBeans.get(k).getX() * gridWidth)) + originX);
                            endY = (float) (mBitmapHeight / (gridHeight * (mBitmapHeight - lineBeans.get(k).getY())) + originY);
                        }
                    }
                    //划线：点连线 连接start和end
                    Canvas canvas = new Canvas();
                    Paint paint = new Paint();
                    paint.setColor(R.color.colorPrimaryDark);
                    canvas.drawLine(startX, startY, endX, endY, paint);
                    taskManageMapRelative.draw(canvas);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(messageEvent.getState() == 10017){
            taskNameList = (String[]) messageEvent.getT();
            Log.d("task_name",taskNameList[0]);
            requestTaskList(taskNameList);
        }else if(messageEvent.getState() == 10018){
            int state = (int) messageEvent.getT();
            if(state == 0){
                Toast toast = Toast.makeText(mContext,"请新建任务",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}