package com.example.robot.run;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.adapter.TaskStateListAdapter;
import com.example.robot.bean.TaskStateList;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.map.FirstFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RunFragment extends Fragment {

    @BindView(R.id.run_map)
    TextView runMap;
    @BindView(R.id.run_map_name)
    TextView runMapName;
    @BindView(R.id.run_task)
    TextView runTask;
    @BindView(R.id.run_task_name)
    TextView runTaskName;
    @BindView(R.id.run)
    RelativeLayout run;
    @BindView(R.id.task_run_edit)
    Button taskRunEdit;
    @BindView(R.id.count_list)
    ListView countList;
    @BindView(R.id.point_state_time)
    TextView pointStateTime;
    private View view;
    private GsonUtils gsonUtils;
    private List<TaskStateList> mData = null;
    private TaskStateListAdapter mAdapter = null;
    private Context mContext;

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
        view = inflater.inflate(R.layout.fragment_run, container, false);
        mContext = view.getContext();
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();
        initView();
        return view;

    }

    private void initView() {
        runMapName.setText(Content.first_map_Name);
        runTaskName.setText(Content.task_Name);

        mData = new LinkedList<TaskStateList>();
        mData.add(new TaskStateList("hhh", "hhh", R.drawable.ok));
        mAdapter = new TaskStateListAdapter((LinkedList<TaskStateList>) mData, mContext);
        countList.setAdapter(mAdapter);
    }

    @OnClick(R.id.task_run_edit)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.task_run_edit:
                Log.d("hhh", "STOPTASKQUEUE");
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPTASKQUEUE));//按stop停止任务
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new FirstFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d("run", "onEventMsgEditeTask ： " + messageEvent.getState());
        if (messageEvent.getState() == 10002) {
            String time = (String) messageEvent.getT();
            Log.d("timehhhh",time);
            pointStateTime.setText(time);
        }else if (messageEvent.getState() == 50001){

        }
    }
}