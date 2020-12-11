package com.example.robot.map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.adapter.HistoryAdapter;
import com.example.robot.bean.HistoryBean;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;

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

public class TaskHistoryFragment extends Fragment implements View.OnClickListener {

    private static String TAG = "TaskHistoryFragment";
    @BindView(R.id.history_recyclerview)
    RecyclerView historyRecyclerview;
    @BindView(R.id.hitory_back)
    Button hitoryBack;
    private View view;
    private Context mContext;
    private HistoryAdapter historyAdapter;
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;

    @Override
    public void onStart() {
        super.onStart();

        Log.d("hhhh", "first_start");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d("hhhh", "first_stop");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_history, container, false);
        ButterKnife.bind(this, view);
        mContext = getContext();
        gsonUtils = new GsonUtils();
        initView();
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.ROBOT_TASK_HISTORY));
        return view;
    }

    private void initView() {
        hitoryBack.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        historyRecyclerview.setLayoutManager(linearLayoutManager);
        historyRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        historyAdapter = new HistoryAdapter(mContext, R.layout.item_recycler);
        //historyRecyclerview.setAdapter(historyAdapter);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsghistory ： " + messageEvent.getState());
        if (messageEvent.getState() == 40001) {
            String message = (String) messageEvent.getT();
            List<HistoryBean> list = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(message);
                JSONArray jsonArray = jsonObject.getJSONArray(Content.ROBOT_TASK_HISTORY);
                for (int i = 0; i < jsonArray.length(); i++) {
                    HistoryBean historyBean = new HistoryBean(jsonArray.getJSONObject(i).getString(Content.dbTaskMapName),
                            jsonArray.getJSONObject(i).getString(Content.dbTaskName),
                            jsonArray.getJSONObject(i).getString(Content.dbTime),
                            jsonArray.getJSONObject(i).getString(Content.dbData));
                    list.add(historyBean);
                }
                historyRecyclerview.setAdapter(historyAdapter);
                historyAdapter.refeshList(list);
                historyAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.hitory_back:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new FirstFragment(), null)
                        .addToBackStack(null)
                        .commit();
        }
    }
}