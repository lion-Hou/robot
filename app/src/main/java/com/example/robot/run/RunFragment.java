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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    RecyclerView recyclerView;
    @BindView(R.id.point_state_time)
    TextView pointStateTime;
    private View view;
    private GsonUtils gsonUtils;
    private TaskStateListAdapter mAdapter = null;
    private Context mContext;
    private List<TaskStateList> listPointName = new ArrayList<>();

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

    @Override
    public void onResume() {

        super.onResume();
        initView();
    }

    private void initView() {
        runMapName.setText(Content.first_map_Name);
        runTaskName.setText(Content.task_Name);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new TaskStateListAdapter(mContext, R.layout.item_list_task);
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
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
        Log.d("run", "onEventMsgrun ： " + messageEvent.getState());
        if (messageEvent.getState() == 10002) {
            String secound = mContext.getResources().getString(R.string.time_second);
            String time = (String) messageEvent.getT() + secound;
            Log.d("timehhhh", time);
            pointStateTime.setText(time);
        }
        if (messageEvent.getState() == 60001) {
            Log.d("run fragment : " , (String) messageEvent.getT());
            listPointName.clear();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject((String) messageEvent.getT());
                String taskName = jsonObject.getString(Content.TASK_NAME);
                String mapName = jsonObject.getString(Content.MAP_NAME);
                Log.d("bgbg" , taskName);
                Log.d("bgbg" , mapName);
                runMapName.setText(mapName);
                runTaskName.setText(taskName);
                JSONArray stateList = jsonObject.getJSONArray(Content.ROBOT_TASK_STATE);
                for (int i = 0; i < stateList.length(); i++) {
                    JSONObject js = stateList.getJSONObject(i);
                    TaskStateList taskStateList = new TaskStateList(js.getString(Content.POINT_NAME),
                            js.getString(Content.POINT_STATE));
                    listPointName.add(taskStateList);
                }
                mAdapter.refeshList(listPointName);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (messageEvent.getState() == 19191) {
            String message = (String) messageEvent.getT();
            if (message.equals("充电") || message.equals("放电")){

            }else {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }


        }
    }
}