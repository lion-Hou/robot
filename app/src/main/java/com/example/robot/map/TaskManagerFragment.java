package com.example.robot.map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.adapter.TaskStateListAdapter;
import com.example.robot.bean.DrawLineBean;
import com.example.robot.bean.SaveTaskBean;
import com.example.robot.bean.TaskStateList;
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
    @BindView(R.id.task_manage_details)
    Button taskManageDetails;

    private Context mContext;
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;
    private String[] taskNameList;
    public View view;

    private NormalDialogUtil mapManageNormalDialog;

    private List<View> imageViewArrayList = new ArrayList<>();
    private int index = 0;
    double mBitmapHeight;
    double mBitmapWidth;
    private Bitmap mBitmap;
    private NormalDialogUtil delectTask;
    String a;
    String select_cn = "请选择任务";
    String select_en = "PLEASE SELECT TASK";
    private String selectWeek = "";
    private List<String> myWeek = new ArrayList<>();
    private TextView detailsTaskType;
    private TextView detailsTaskTime;
    private RecyclerView recyclerView;
    private List<TaskStateList> listPointName = new ArrayList<>();
    private TaskStateListAdapter mAdapter = null;
    private String taskPoint;
    ArrayList taskPoint1 = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("hhhh", "first_creat");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d("hhhh", "manger_start");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Content.manageTaskName = null;
        Log.d("hhhh", "manger_stop");
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
        taskManageDetails.setOnClickListener(this);
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
                if (a.equals(select_cn) || a.equals(select_en)) {
                    Toast toast = Toast.makeText(mContext, "请选择任务名", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
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
                            Content.manageTaskName = null;
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
                if (a.equals(select_cn) || a.equals(select_en)) {
                    Toast toast = Toast.makeText(mContext, "请选择任务名", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.first_fragment, new TaskEditFragment(), null)
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case R.id.task_manage_details:
                /**
                 * 查看task详情
                 */
                gsonUtils.setMapName(Content.first_map_Name);
                gsonUtils.setTaskName(Content.fixTaskName);
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.editTaskQueue));
                initPopWindow(view);
                break;
            default:
                break;
        }

    }

    /**
     * 任务详情PopWindow
     *
     * @param v
     */
    private void initPopWindow(View v) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.details_popup, null, false);
        TextView detailsTaskMap = view.findViewById(R.id.details_task_map);
        TextView detailsTaskTask = view.findViewById(R.id.details_task_task);
        detailsTaskType = view.findViewById(R.id.details_task_type);
        detailsTaskTime = view.findViewById(R.id.details_task_time);
        recyclerView = view.findViewById(R.id.details_count_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new TaskStateListAdapter(mContext, R.layout.item_list_task);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        final PopupWindow popWindow = new PopupWindow(
                view,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);
        popWindow.setWidth(300);
        popWindow.setHeight(400);
        popWindow.setTouchable(true);
        popWindow.showAsDropDown(taskManageDetails);
        popWindow.setBackgroundDrawable(new ColorDrawable(0));
        detailsTaskMap.setText(Content.first_map_Name);
        detailsTaskTask.setText(Content.fixTaskName);
        //recyclerView.removeAllViews();
        //recyclerView.setItemViewCacheSize(0);
    }

    public void requestTaskList(String[] taskNameList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        System.out.println("which" + taskNameList.length);
        builder.setItems(taskNameList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("which" + which);
                Content.fixTaskName = taskNameList[which];
                Content.manageTaskName = taskNameList[which];
                taskName.setText(taskNameList[which]);
                Log.d(TAG, "ggggg ： " + taskName.getText());
                gsonUtils.setMapName(Content.first_map_Name);
                gsonUtils.setTaskName(Content.fixTaskName);
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.editTaskQueue));
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
            }
        });
        builder.create().show();
    }

    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg ： " + messageEvent.getState());
        if (messageEvent.getState() == 10001) {
            Log.d(TAG, "图片 ： " + messageEvent.getT());
            ByteBuffer bytes = (ByteBuffer) messageEvent.getT();
            int len = bytes.limit() - bytes.position();
            byte[] bytes1 = new byte[len];
            bytes.get(bytes1);
            Log.d(TAG, "任务界面获得地图数据" + bytes1.length);
            //Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
            mBitmap = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
            mBitmapHeight = mBitmap.getHeight();
            mBitmapWidth = mBitmap.getWidth();
            if (mBitmapHeight/taskManageMapRelativeBorder.getHeight() >= mBitmapWidth/taskManageMapRelativeBorder.getWidth()){
                mBitmapWidth = taskManageMapRelativeBorder.getHeight() / mBitmapHeight * mBitmapWidth;
                mBitmapHeight = taskManageMapRelativeBorder.getHeight();
            }else {
                mBitmapHeight = taskManageMapRelativeBorder.getWidth() / mBitmapWidth * mBitmapHeight;
                mBitmapWidth = taskManageMapRelativeBorder.getWidth();
            }
            taskManageMapRelative.removeAllViews();
            taskManageMapImage.setImageBitmap(mBitmap);
            taskManageMapRelative.setLayoutParams(new RelativeLayout.LayoutParams((int) mBitmapWidth, (int) mBitmapHeight));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            taskManageMapImage.setLayoutParams(layoutParams);

            ViewGroup parent = (ViewGroup) taskManageMapImage.getParent();
            if (parent != null) {
                parent.removeView(taskManageMapImage);
            }

            taskManageMapRelative.addView(taskManageMapImage);
            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETPOINTPOSITION));
            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_VIRTUAL));
        } else if (messageEvent.getState() == 10008) {
            Log.d("zdzdremove", "" + imageViewArrayList.size());
            for (int i = 0; i < imageViewArrayList.size(); i++) {
                taskManageMapRelative.removeView(imageViewArrayList.get(i));
            }
            imageViewArrayList.clear();
            String message = (String) messageEvent.getT();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject != null) {

                    for (int k = 0; k < Content.list.size(); k++) {
                        if (Content.list.get(k).getMap_Name().equals(Content.first_map_Name)) {
                            Log.d("zdzd555", "" + Content.list.get(k).getResolution());
                            index = k;
                        }
                    }
                    Log.d("zdzd5111", "" + index);
                    JSONArray jsonArray = jsonObject.getJSONArray(Content.SENDPOINTPOSITION);
                    Log.d("zdzd000 ", "pointName : " + jsonArray.toString());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonItem = jsonArray.getJSONObject(i);
                        Log.d("zdzd111 ", "pointName : " + jsonItem.toString());
                        String pointName = jsonItem.getString(Content.POINT_NAME);
                        Log.d("zdzd111 ", "pointName : " + pointName);
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

                        Log.d("zdzd9998", "gridH" + gridHeight + "        gridW" + gridWidth + "     pointX" + pointX + "       originX" + originX + "       Content.ROBOT_SIZE " + Content.ROBOT_SIZE);
                        Log.d("zdzd9998", " resolution * angleX" + resolution * angleX);

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
                            if (Content.manageTaskName == null){
                                ImageView imageView = new ImageView(mContext);
                                TextView textView = new TextView(mContext);
                                imageView.setImageResource(R.drawable.ic_point);
                                imageView.setOnClickListener(this);
                                imageView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                        (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY))),
                                        0, 0);
                                textView.setText(pointName);
                                textView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                        (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY)) + 4),
                                        0, 0);
                                Log.d("zdzd9998", "angleX" + angleX);
                                Log.d("zdzd9998", " resolution" + resolution);
                                taskManageMapRelative.addView(imageView);
                                taskManageMapRelative.addView(textView);
                                imageViewArrayList.add(imageView);
                                imageViewArrayList.add(textView);
                            }else {
                                Log.d(TAG, "onEventMsg2000498 ： " + taskPoint1 +"  "+taskPoint1.size());
                                for (int j = 0; j <taskPoint1.size() ; j++) {
                                    ImageView imageView = new ImageView(mContext);
                                    TextView textView = new TextView(mContext);
                                    imageView.setImageResource(R.drawable.ic_point);
                                    imageView.setOnClickListener(this);
                                    String a= (String) taskPoint1.get(j);
                                    Log.d(TAG, "onEventMsg2000498889 ： " + a);
                                    Log.d(TAG, "onEventMsg2000498888 ： " + pointName);
                                    if (a.equals(pointName)){
                                        imageView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                                (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY))),
                                                0, 0);
                                        textView.setText(pointName);
                                        textView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                                (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY)) + 4),
                                                0 , 0);
                                        Log.d("zdzd9998", "angleX" + angleX);
                                        Log.d("zdzd9998", " resolution" + resolution);
                                        taskManageMapRelative.addView(imageView);
                                        taskManageMapRelative.addView(textView);
                                        imageViewArrayList.add(imageView);
                                        imageViewArrayList.add(textView);
                                    }
                                }
                            }
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
                List<DrawLineBean> lineBeans = new ArrayList<>();//线段的坐标
                ArrayList<float[]> pointlist = new ArrayList<>();//保存所画线的坐标

                float startX = 0, startY = 0, endX = 0, endY = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    for (int k = 0; k < jsonArray.getJSONArray(i).length(); k++) {
                        JSONObject js = jsonArray.getJSONArray(i).getJSONObject(k);
                        DrawLineBean drawLineBean = new DrawLineBean();
                        drawLineBean.setX(js.getDouble(Content.VIRTUAL_X));
                        drawLineBean.setY(js.getDouble(Content.VIRTUAL_Y));
                        lineBeans.add(drawLineBean);
                    }
                }

                //画虚拟墙
                for (int k = 0; k < lineBeans.size(); ) {
                    startX = (float) ((mBitmapWidth / gridWidth) * lineBeans.get(k).getX());
                    startY = (float) (mBitmapHeight - (mBitmapHeight / gridHeight) * lineBeans.get(k).getY());
                    endX = (float) ((mBitmapWidth / gridWidth) * lineBeans.get(k + 1).getX());
                    endY = (float) (mBitmapHeight - (mBitmapHeight / gridHeight) * lineBeans.get(k + 1).getY());

                    float[] point = {startX, startY, endX, endY};
                    Log.i("Henly", "mapManager,update VirtualWall,k = " + k);
                    pointlist.add(point);

                    k = k + 2;
                }

                if (pointlist.size() != 0) {
                    Log.i("Henly", "updateVW,gridWidth = " + gridWidth + ",gridHeight = " + gridHeight);
                    updateVirtualWall(pointlist);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (messageEvent.getState() == 10017) {
            taskNameList = (String[]) messageEvent.getT();
            Log.d("task_name", taskNameList[0]);
            requestTaskList(taskNameList);
        } else if (messageEvent.getState() == 10018) {
            int state = (int) messageEvent.getT();
            if (state == 0) {
                Toast toast = Toast.makeText(mContext, "请新建任务", Toast.LENGTH_SHORT);
                toast.show();
            }
        }else if (messageEvent.getState() == 20009) {
            Log.d(TAG, "onEventMsg20009 ： " + (String) messageEvent.getT());
            try {
                listPointName.clear();
                taskPoint1.clear();
                JSONObject jsonObject = new JSONObject((String) messageEvent.getT());
                String message = (String) messageEvent.getT();
                JSONArray jsonArray1 = jsonObject.getJSONArray(Content.editTaskQueue);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject js = jsonArray1.getJSONObject(i);
                    if (!taskPoint1.contains(js.getString(Content.dbPointName))) {
                        taskPoint1.add(js.getString(Content.dbPointName));
                    }
                    Log.d("details_GG", "" + taskPoint1);
                }
                taskPoint = message.replace("\"","");
                Log.d(TAG, "onEventMsg2000499 ： " + taskPoint1);
                String time = jsonObject.getString(Content.editTaskQueueTime);
                Log.d(TAG, "onEventMsgtime ： " + time);
                if (time.equals("FF:FF")){
                    detailsTaskTime.setText("立即执行");
                    detailsTaskType.setText("Once");
                }else {
                    detailsTaskTime.setText(time);
                    JSONArray typeArray = jsonObject.getJSONArray(Content.editTaskQueueType);
                    if (typeArray.length()==7){
                        detailsTaskType.setText("Per day");
                    }else
                    {
                        detailsTaskType.setText("Per Week");
                    }
                }

                /**
                 * 详情->点数据
                 */
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray(Content.editTaskQueue);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject js = jsonArray.getJSONObject(i);
                        TaskStateList taskStateList = new TaskStateList(js.getString(Content.dbPointName),
                                js.getInt(Content.dbSpinnerTime));
                        Log.d("details_hh", "" + js.getString(Content.dbSpinnerTime));
                        Log.d("details_hh", "" + js.getString(Content.dbPointName));
                        listPointName.add(taskStateList);
                    }
                    mAdapter.refeshList(listPointName);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void updateVirtualWall(ArrayList<float[]> pointlist) {
        Log.i("Henly", "mapmanager-updateVirtualWall");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        // layoutParams.height = editMapImage.getHeight();
        // layoutParams.width = editMapImage.getWidth();
        //  layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        //   layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        DrawlineFromVW bDrawlVW = new DrawlineFromVW(mContext, pointlist);
        bDrawlVW.setLayoutParams(layoutParams);
        taskManageMapRelative.addView(bDrawlVW);
    }

    class DrawlineFromVW extends View {
        private float start_x, start_y;//声明起点坐标
        private float mov_x, mov_y;//滑动轨迹坐标
        private Paint paint;//声明画笔
        private Canvas canvas;//画布
        private Bitmap bitmap;//位图
        private float view_X, view_Y;

        private double scale_x = 1;
        private double scale_y = 1;

        private ArrayList<float[]> Pointlist = new ArrayList<>();//保存所画线的坐标

        public DrawlineFromVW(Context context, ArrayList<float[]> pointlist) {
            super(context);
            paint = new Paint(Paint.DITHER_FLAG);//创建一个画笔

            bitmap = Bitmap.createBitmap((int) mBitmapWidth, (int) mBitmapHeight, mBitmap == null ? Bitmap.Config.ARGB_8888 : mBitmap.getConfig());
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            canvas = new Canvas();
            canvas.setBitmap(bitmap);

            paint.setStyle(Paint.Style.STROKE);//设置非填充
            paint.setStrokeWidth(3);//笔宽3像素
            paint.setColor(Color.RED);//设置为红笔
            paint.setAntiAlias(true);//锯齿不显示

            Pointlist = pointlist;
            invalidate();

            Log.i(TAG, "DrawlineFromVW");
        }

        //画位图
        @Override
        protected void onDraw(Canvas canvas) {
            //super.onDraw(canvas);
            canvas.drawBitmap(bitmap, 0, 0, null);
            Log.i("Henly", "mapmanager-updateVW,onDraw,mBitmapWidth = " + mBitmapWidth + ",mBitmapHeight = " + mBitmapHeight);
            for (int i = 0; i < Pointlist.size(); i++) {
                float[] point = Pointlist.get(i);
                Log.i("Henly", "mapmanager-updateVW,drawline,start_x:" + point[0] + ",start_y:" + point[1] + ", end_x:" + point[2] + " ,end_y:" + point[3]);
                canvas.drawLine(point[0], point[1], point[2], point[3], paint);//画保存的线
            }
        }
    }
}