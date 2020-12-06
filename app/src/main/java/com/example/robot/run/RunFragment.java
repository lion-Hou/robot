package com.example.robot.run;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.robot.R;
import com.example.robot.content.Content;

import butterknife.BindView;

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
    @BindView(R.id.task_manage_edit)
    Button taskManageEdit;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_run, container, false);
        initView();
        return view;


    }

    private void initView() {
        //runMapName.setText(Content.first_map_Name);
        //runTaskName.setText(Content.task_Name);
    }
}