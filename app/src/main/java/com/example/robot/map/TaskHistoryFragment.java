package com.example.robot.map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robot.EmptyClient;
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

public class TaskHistoryFragment extends Fragment {

    private static String TAG = "TaskHistoryFragment";
    @BindView(R.id.history_recyclerview)
    RecyclerView historyRecycler;
    private View view;
    private Context mContext;
    private HistoryAdapter  historyAdapter;
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d("hhhh",  "first_start");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d("hhhh",  "first_stop");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_history, container, false);
        mContext = getContext();
        initView();

        return view;
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        historyRecycler.setLayoutManager(linearLayoutManager);
        historyRecycler.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        historyAdapter = new HistoryAdapter(mContext, R.layout.item_recycler);
        historyRecycler.setAdapter(historyAdapter);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void  onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg ： " + messageEvent.getState());
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
                historyAdapter.refeshList(list);
                historyAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}