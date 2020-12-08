package com.example.robot.run;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.content.Content;
import com.example.robot.content.GsonUtils;
import com.example.robot.map.AddNewMapFragment;
import com.example.robot.map.FirstFragment;

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
    private View view;
    private GsonUtils gsonUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_run, container, false);
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();
        initView();
        return view;


    }

    private void initView() {
        Log.d("hhhhh:", Content.first_map_Name);
        Log.d("hhhhh:", Content.task_Name);
        runMapName.setText(Content.first_map_Name);
        runTaskName.setText(Content.task_Name);
    }

    @OnClick(R.id.task_run_edit)
    public void onViewClicked(View view) {
        switch (view.getId()){
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
}