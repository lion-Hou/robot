package com.example.robot.map;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

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
import com.example.robot.view.MyDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskHistoryFragment extends Fragment implements View.OnClickListener {

    private static String TAG = "TaskHistoryFragment";
    @BindView(R.id.history_recyclerview)
    RecyclerView historyRecyclerview;
    @BindView(R.id.hitory_back)
    Button hitoryBack;
    @BindView(R.id.datePicker)
    DatePicker datePicker;
    @BindView(R.id.his_task_size)
    TextView hisTaskSize;
    @BindView(R.id.his_hours)
    TextView hisHours;
    @BindView(R.id.his_area_size)
    TextView hisAreaSize;
    private View view;
    private Context mContext;
    private HistoryAdapter historyAdapter;
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;
    private int countYear;//当前年
    private int countMonth;//当前月
    private int countDay;//当前日期

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
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.TOTALCOUNT));
        return view;
    }

    private void initView() {
        selectData();
        hitoryBack.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        historyRecyclerview.setLayoutManager(linearLayoutManager);
        MyDividerItemDecoration divider = new MyDividerItemDecoration(mContext, MyDividerItemDecoration.VERTICAL);
        // 设置分割线的颜色和高度，横向的时候使用方法一样
        divider.setDrawable(getResources().getColor(R.color.list_divider), 0.5f);
        historyRecyclerview.addItemDecoration(divider);

        historyRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        historyAdapter = new HistoryAdapter(mContext, R.layout.item_recycler);
        //historyRecyclerview.setAdapter(historyAdapter);
    }

    public void selectData(){
        Calendar calendar = Calendar.getInstance();
        countYear = calendar.get(Calendar.YEAR);
        countMonth=calendar.get(Calendar.MONTH);
        countDay=calendar.get(Calendar.DATE);
        datePicker.init(countYear,countMonth,countDay,new DatePicker.OnDateChangedListener(){
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.e("datepicker—你选择的日期是：",year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                countYear = year;
                countMonth = monthOfYear;
                countDay = dayOfMonth;
                historyRecyclerview.removeAllViews();
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.ROBOT_TASK_HISTORY));
            }
        });
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
                for (int i = jsonArray.length() - 1; i > 0; i--) {
                    String minute = mContext.getResources().getString(R.string.time_minute);
                    HistoryBean historyBean = new HistoryBean(jsonArray.getJSONObject(i).getString(Content.dbTaskMapName),
                            jsonArray.getJSONObject(i).getString(Content.dbTaskName),
                            jsonArray.getJSONObject(i).getString(Content.dbTime) + minute,
                            jsonArray.getJSONObject(i).getString(Content.dbData));
                    String m1 = jsonArray.getJSONObject(i).getString(Content.dbData);
                    Log.d("month_month1",m1);
                    int a = Integer.valueOf(m1.substring(0,4)).intValue();
                    int b = Integer.valueOf(m1.substring(5,7)).intValue();
                    int c = Integer.valueOf(m1.substring(8,10)).intValue();
                    int d = countMonth+1;
                    Log.d("month_month1","" + a + ":" +b + ":" + c);
                    Log.d("month_month2","" + countYear + ":" +d + ":" + countDay);
                    if((a == countYear)&&(b == d)&&(c == countDay)){
                        list.add(historyBean);
                    }
                }
                historyRecyclerview.setAdapter(historyAdapter);
                historyAdapter.refeshList(list);
                historyAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (messageEvent.getState() == 88880) {
            String message = (String) messageEvent.getT();
            Log.d("hisTaskSize",message);
            //历史任务数
            hisTaskSize.setText(message);
        }else if (messageEvent.getState() == 88881) {
            String message1 = (String) messageEvent.getT();
            Log.d("allTaskhours", "message"+message1);
            double message = Double.valueOf(message1);
            Log.d("allTaskhours", "message"+message);
            int time = (int) (message / 100 / 60 /60);
            Log.d("allTaskhours", "" +time);
            //当月时间
            hisHours.setText(String.valueOf(time));
        }else if (messageEvent.getState() == 88882) {
            String message = (String) messageEvent.getT();
            Log.d("hisAreaSize",message);
            //历史面积
            hisAreaSize.setText(message);
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
