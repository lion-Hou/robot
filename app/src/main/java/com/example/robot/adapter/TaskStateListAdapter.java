package com.example.robot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robot.R;
import com.example.robot.bean.TaskStateList;

import java.util.LinkedList;

public class TaskStateListAdapter extends BaseAdapter {

    private LinkedList<TaskStateList> mData;
    private Context mContext;

    public TaskStateListAdapter(LinkedList<TaskStateList> mData,Context mContext ){
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_task,parent,false);
        ImageView img_icon = convertView.findViewById(R.id.state_img);
        TextView state_name = convertView.findViewById(R.id.state_name);
        TextView state_state = convertView.findViewById(R.id.state_state);
        img_icon.setBackgroundResource(mData.get(position).getaIcon());
        state_name.setText(mData.get(position).getPointName());
        state_state.setText(mData.get(position).getTaskState());
        return convertView;
    }


}
