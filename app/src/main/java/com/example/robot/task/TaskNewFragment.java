package com.example.robot.task;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.adapter.MyAdapter;
import com.example.robot.adapter.MyItemTouchHelperCallback;
import com.example.robot.bean.SaveTaskBean;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.map.TaskManagerFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TaskNewFragment extends Fragment implements View.OnClickListener, MyItemTouchHelperCallback.StartDragListener {

    private static final String TAG = "TaskNewFragment";

    @BindView(R.id.list_task)
    RecyclerView listTask;
    @BindView(R.id.add_task_name)
    TextView addTaskName;
    @BindView(R.id.title1)
    RelativeLayout title1;
    @BindView(R.id.task_type)
    TextView taskType;
    @BindView(R.id.title2)
    RelativeLayout title2;
    @BindView(R.id.addtask_select_point)
    Button addtaskSelectPoint;
    @BindView(R.id.title3)
    RelativeLayout title3;
    @BindView(R.id.task_new_save)
    Button taskNewSave;
    @BindView(R.id.task_new_back)
    Button taskNewBack;
    @BindView(R.id.new_map_mapName_editText)
    EditText newMapMapNameEditText;
    @BindView(R.id.task_type_select)
    TextView taskTypeSelect;
    @BindView(R.id.task_type_selectTime)
    TextView taskTypeSelectTime;
    @BindView(R.id.task_type_selectWeek)
    TextView taskTypeSelectWeek;

    private View view;
    private GsonUtils gsonUtils;
    public EmptyClient emptyClient;
    private Context mContext;
    private ArrayList<SaveTaskBean> mList = new ArrayList<SaveTaskBean>();
    private MyAdapter mAdapter;
    private String[] point_name;
    private ItemTouchHelper itemTouchHelper;
    private RecyclerView mRecyclerView;
    private String[] type;
    private String[] selectWeek;
    private String typeValue;
    private String typeTime;
    private String[] weeks = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    private List<String> myWeek = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("hhhh", "newtask_create");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d("hhhh", "newtask_start");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d("hhhh", "newtask_stop");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task_new, container, false);
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();
        mContext = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_task);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mAdapter = new MyAdapter(mList, this);
        mRecyclerView.setAdapter(mAdapter);
        //条目触摸帮助类
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(mAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        initView();
        initListener();
        return view;
    }

    private void initView() {
        taskNewSave.setOnClickListener(this);
        addtaskSelectPoint.setOnClickListener(this);
        taskTypeSelect.setOnClickListener(this);
        taskTypeSelectTime.setOnClickListener(this);
        taskTypeSelectWeek.setOnClickListener(this);
    }

    private void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.task_new_save:
                Log.d("tasklog", "taskname");
                if (TextUtils.isEmpty(newMapMapNameEditText.getText().toString())) {
                    Toast.makeText(mContext, "先输入任务名字", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(mContext)
                            .setTitle("提示")
                            .setMessage("任务保存成功")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    /**
                                     * 保存任务
                                     */
                                    gsonUtils.setTaskName(newMapMapNameEditText.getText().toString());//任务名
                                    gsonUtils.setList(mList);//导航点和所对于时间
                                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.SAVETASKQUEUE));//保存任务

                                    if (typeValue.equals(weeks[2]) || typeValue.equals(weeks[0])) {
                                        gsonUtils.setTaskWeek(myWeek);//任务周期
                                    } else {
                                        gsonUtils.setTaskWeek(myWeek);//任务周期
                                    }
                                    gsonUtils.setTaskTime(typeTime);//任务时间 “20：20”
                                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.TASK_ALARM));//定时任务

                                    getActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.first_fragment, new TaskManagerFragment(), null)
                                            .addToBackStack(null)
                                            .commit();
                                }
                            }).show();
                }
                break;
            case R.id.addtask_select_point:
                Log.d("tasklog", "select");
                gsonUtils.setMapName(Content.map_Name);//请求导航点前发送当前地图名
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETPOINTPOSITION));//请求导航点
                break;

            /**
             * 选择任务类型
             */
            case R.id.task_type_select:
                Log.d("tasklog", "type");
                type = new String[]{"Once", "Pre Day", "Pre Week"};
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setItems(type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeValue = type[which];//任务类型
                        if (which == 0 || which == 1) {
                            taskTypeSelectWeek.setVisibility(View.GONE);
                        } else {
                            taskTypeSelectWeek.setVisibility(View.VISIBLE);
                        }
                        Log.d("taskvalue", typeValue);
                    }
                });
                builder.create().show();
                break;

            /**
             * 选择任务时间
             */
            case R.id.task_type_selectTime:
                new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if( (hourOfDay >= 0 && hourOfDay <=9) && (minute >= 0 && minute <=9) ){
                            typeTime = String.format("0%d:0%d",hourOfDay,minute);
                        }else if ( (hourOfDay >= 0 && hourOfDay <=9) && (minute >= 10 && minute <=60) ){
                            typeTime = String.format("0%d:%d",hourOfDay,minute);
                        }else if( (hourOfDay >= 10 && hourOfDay <=12) && (minute >= 0 && minute <=9) ){
                            typeTime = String.format("%d:0%d",hourOfDay,minute);
                        }else {
                            typeTime = String.format("%d:%d",hourOfDay,minute);//任务时间
                        }
                        //typeTime = String.format("%d:%d",hourOfDay,minute);//任务时间
                        //typeTime = String.format("0%d:0%d",hourOfDay,minute);
                        //typeTime = String.format("%H:%M",hourOfDay,minute);
                        Log.d("taskvalue", typeTime);
                    }
                },00,00,true).show();
                break;

            /**
             * 选择任务周期（星期）
             */
            case R.id.task_type_selectWeek:
                myWeek.clear();
                Log.d("tasklog", "week");
                AlertDialog.Builder week = new AlertDialog.Builder(mContext);
                week.setMultiChoiceItems(weeks, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            myWeek.add(weeks[which]);
                            Log.d("taskvalue",myWeek.toString());
                        } else {
                            myWeek.remove(weeks[which]);
                        }
                    }
                });

                //设置正面按钮
                week.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //设置反面按钮
                week.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myWeek.clear();
                        dialog.dismiss();
                    }
                });
                week.show();
                break;
        }
    }

    public void pointList(String[] pointName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        Log.d(TAG, "which" + pointName.length);
        builder.setItems(pointName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SaveTaskBean saveTaskBean = new SaveTaskBean();
                saveTaskBean.setPoint_Name(pointName[which]);
                saveTaskBean.setSpinerIndex(0);
                mList.add(saveTaskBean);
                mAdapter.refeshList(mList);
                mAdapter.notifyDataSetChanged();

            }
        });
        builder.create().show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg ： " + messageEvent.getState());
        if (messageEvent.getState() == 10007) {
            String[] point = (String[]) messageEvent.getT();
            point_name = new String[point.length];
            pointList(point);
            //Log.d(TAG, "收到点的数据大小 ： " + point.length);
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}