package com.example.robot.map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.robot.R;
import com.kongqw.rockerlibrary.view.RockerView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RockerFragment extends Fragment {

    public View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rocker, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}