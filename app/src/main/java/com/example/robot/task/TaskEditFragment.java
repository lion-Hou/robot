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
import com.example.robot.bean.TaskStateList;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.map.TaskManagerFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TaskEditFragment extends Fragment implements View.OnClickListener, MyItemTouchHelperCallback.StartDragListener {

    private static final String TAG = "TaskEditFragment";

    @BindView(R.id.list_task_edit)
    RecyclerView mRecyclerView;
    @BindView(R.id.add_task_name_edit)
    TextView addTaskName;
    @BindView(R.id.title_edit)
    RelativeLayout title1;
    @BindView(R.id.task_type_edit)
    TextView taskType;
    @BindView(R.id.title2_edit)
    RelativeLayout title2;
    @BindView(R.id.addtask_select_point_edit)
    Button addtaskSelectPoint;
    @BindView(R.id.title3_edit)
    RelativeLayout title3;
    @BindView(R.id.task_new_save_edit)
    Button taskNewSave;
    @BindView(R.id.new_map_mapName_editText_edit)
    EditText newMapMapNameEditText;
    @BindView(R.id.task_type_select_edit)
    TextView taskTypeSelect;
    @BindView(R.id.task_type_selectTime_edit)
    TextView taskTypeSelectTime;
    @BindView(R.id.task_type_selectWeek_edit)
    TextView taskTypeSelectWeek;
    @BindView(R.id.task_new_back_edit_edit)
    Button taskNewBackEditEdit;

    private View view;
    private GsonUtils gsonUtils;
    public EmptyClient emptyClient;
    private Context mContext;
    private ArrayList<SaveTaskBean> mList = new ArrayList<SaveTaskBean>();
    private MyAdapter mAdapter;
    private String[] point_name;
    private ItemTouchHelper itemTouchHelper;
    private String[] type = new String[]{"Once", "Pre Day", "Pre Week"};
    private String selectWeek = "";
    private String typeValue;
    private String typeTime;
    private String[] weeks = new String[]{"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
    private List<String> myWeek = new ArrayList<>();
    private AlertDialog.Builder week;

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
        view = inflater.inflate(R.layout.fragment_task_edit, container, false);
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();
        mContext = view.getContext();
        initView();
        initListener();
        return view;
    }

    private void initView() {

        newMapMapNameEditText.setText(Content.fixTaskName);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mAdapter = new MyAdapter(mList, this);
        mRecyclerView.setAdapter(mAdapter);
        //条目触摸帮助类
        ItemTouchHelper.Callback callback = new MyItemTouchHelperCallback(mAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        gsonUtils.setMapName(Content.first_map_Name);
        gsonUtils.setTaskName(Content.fixTaskName);
        Log.d("ffff",Content.first_map_Name);
        Log.d("ffff",Content.fixTaskName);
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.editTaskQueue));

        taskNewSave.setOnClickListener(this);
        addtaskSelectPoint.setOnClickListener(this);
        taskTypeSelect.setOnClickListener(this);
        taskTypeSelectTime.setOnClickListener(this);
        taskTypeSelectWeek.setOnClickListener(this);
        taskNewBackEditEdit.setOnClickListener(this);
    }

    private void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.task_new_save_edit:

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
                                    //删除之前的任务
                                    gsonUtils.setMapName(Content.first_map_Name);
                                    gsonUtils.setTaskName(Content.fixTaskName);
                                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.DELETETASKQUEUE));
                                    //保存新的任务
                                    gsonUtils.setMapName(Content.first_map_Name);
                                    gsonUtils.setTaskName(newMapMapNameEditText.getText().toString());//任务名
                                    gsonUtils.setList(mList);//导航点和所对于时间
                                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.SAVETASKQUEUE));//保存任务
                                    Log.d("zdzd week :", "" + myWeek.size() + ",    " + typeValue);
                                    if (typeValue.equals(type[1])) {
                                        for (int i = 0; i < weeks.length; i++) {
                                            myWeek.add(weeks[i]);
                                        }
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
            case R.id.addtask_select_point_edit:
                Log.d("tasklog", "select");
                //gsonUtils.setMapName(Content.map_Name);//请求导航点前发送当前地图名
                gsonUtils.setMapName(Content.first_map_Name);
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETPOINTPOSITION));//请求导航点
                break;

            /**
             * 选择任务类型
             */
            case R.id.task_type_select_edit:
                Log.d("tasklog", "type");
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setItems(type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        typeValue = type[which];//任务类型
                        if (which == 0) {
                            typeTime = "FF:FF";
                            taskTypeSelectTime.setText("FF:FF");
                            myWeek.clear();
                            selectWeek = "";
                            taskTypeSelect.setText(typeValue);
                            taskTypeSelectWeek.setVisibility(View.GONE);
                        }else if (which == 1) {
                            myWeek.clear();
                            selectWeek = "";
                            taskTypeSelect.setText(typeValue);
                            taskTypeSelectWeek.setVisibility(View.VISIBLE);
                        } else {
                            taskTypeSelect.setText(typeValue);
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
            case R.id.task_type_selectTime_edit:
                new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if ((hourOfDay >= 0 && hourOfDay <= 9) && (minute >= 0 && minute <= 9)) {
                            typeTime = String.format("0%d:0%d", hourOfDay, minute);
                        } else if ((hourOfDay >= 0 && hourOfDay <= 9) && (minute >= 10 && minute <= 60)) {
                            typeTime = String.format("0%d:%d", hourOfDay, minute);
                        } else if ((hourOfDay >= 10 && hourOfDay <= 12) && (minute >= 0 && minute <= 9)) {
                            typeTime = String.format("%d:0%d", hourOfDay, minute);
                        } else {
                            typeTime = String.format("%d:%d", hourOfDay, minute);//任务时间
                        }
                        //typeTime = String.format("%d:%d",hourOfDay,minute);//任务时间
                        //typeTime = String.format("0%d:0%d",hourOfDay,minute);
                        //typeTime = String.format("%H:%M",hourOfDay,minute);
                        taskTypeSelectTime.setText(typeTime);
                        Log.d("taskvalue", typeTime);
                    }
                }, 00, 00, true).show();

                break;

            /**
             * 选择任务周期（星期）
             */
            case R.id.task_type_selectWeek_edit:
                Log.d("tasklog", "week");
                boolean[] booleans = new boolean[weeks.length];
                for (int i = 0; i < weeks.length; i++) {
                    boolean flag = false;
                    for (int j = 0; j < myWeek.size(); j++) {
                        if (weeks[i].equals(myWeek.get(j))) {
                            flag = true;
                        }
                    }
                    booleans[i] = flag;
                }
                week = new AlertDialog.Builder(mContext);
                week.setMultiChoiceItems(weeks, booleans, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            myWeek.add(weeks[which]);
                            Log.d("taskvalue", myWeek.toString());
                        } else {
                            myWeek.remove(weeks[which]);
                        }
                    }
                });

                //设置正面按钮
                week.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectWeek = "";
                        for (int i = 0; i < myWeek.size(); i++) {
                            selectWeek = selectWeek + myWeek.get(i);
                        }
                        taskTypeSelectWeek.setText(selectWeek);
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
            case R.id.task_new_back_edit_edit:
                AlertDialog.Builder back = new AlertDialog.Builder(mContext);
                back.setMessage("当前修改未保存是否退出");
                //设置正面按钮
                back.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.first_fragment, new TaskManagerFragment(), null)
                                .addToBackStack(null)
                                .commit();
                        dialog.dismiss();
                    }
                });
                //设置反面按钮
                back.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                back.show();
                break;
            default:
                break;
        }
    }

    public void pointList(String[] pointName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        Log.d(TAG, "whichedit" + pointName.length);
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
        Log.d(TAG, "onEventMsgEditeTask ： " + messageEvent.getState());
        if (messageEvent.getState() == 10007) {
            String[] point = (String[]) messageEvent.getT();
            point_name = new String[point.length];
            pointList(point);
            Log.d(TAG, "收到点的数据大小 ： " + point.length);
        } else if (messageEvent.getState() == 20009) {
            Log.d(TAG, "onEventMsg20009 ： " + (String) messageEvent.getT());
            try {
                JSONObject jsonObject = new JSONObject((String) messageEvent.getT());
                //类型
                JSONArray typeArray = jsonObject.getJSONArray(Content.editTaskQueueType);
                Log.d("type event :", "" + TextUtils.isEmpty(typeArray.getString(0)));
                Log.d("type event :", "" + typeArray.getString(0).length());
                if (TextUtils.isEmpty(typeArray.getString(0))) {
                    //Once
                    taskTypeSelectWeek.setVisibility(View.GONE);
                    taskTypeSelect.setText(type[0]);
                    typeValue = type[0];
                } else if (typeArray.length() == 7) {
                    taskTypeSelect.setText(type[1]);
                    taskTypeSelectWeek.setVisibility(View.GONE);
                    typeValue = type[1];
                } else {
                    typeValue = type[2];
                    taskTypeSelect.setText(type[2]);
                    for (int i = 0; i < typeArray.length(); i++) {
                        myWeek.add(typeArray.getString(i));
                        selectWeek = selectWeek + typeArray.getString(i) + "  ";
                    }
                    taskTypeSelectWeek.setText(selectWeek);
                }
                taskTypeSelectTime.setText(jsonObject.getString(Content.editTaskQueueTime));


                //数据
                JSONArray jsonArray = jsonObject.getJSONArray(Content.editTaskQueue);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject js = jsonArray.getJSONObject(i);
                    SaveTaskBean saveTaskBean = new SaveTaskBean();
                    saveTaskBean.setPoint_Name(jsonArray.getJSONObject(i).getString(Content.dbPointName));
                    saveTaskBean.setSpinerIndex(jsonArray.getJSONObject(i).getInt(Content.dbSpinnerTime));
                    Log.d("details_hh", "" + js.getString(Content.dbSpinnerTime));
                    Log.d("details_hh", "" + js.getString(Content.dbPointName));
                    mList.add(saveTaskBean);
                }
                mAdapter.refeshList(mList);
                mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}