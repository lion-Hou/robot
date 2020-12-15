package com.example.robot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.robot.R;
import com.example.robot.bean.HistoryBean;
import com.example.robot.bean.TaskStateList;

import java.util.List;

public class TaskStateListAdapter extends RecyclerView.Adapter<RecycleHolder> {

    private Context mContext;
    private List<TaskStateList> mDatas;
    private int mLayoutId;
    private LayoutInflater mInflater;

    private OnItemClickListener onItemClickListener;


    public TaskStateListAdapter(Context mContext, int mLayoutId) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        mInflater = LayoutInflater.from(mContext);
    }

    public void refeshList(List<TaskStateList> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecycleHolder(mInflater.inflate(mLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, int position) {
        convert(holder, mDatas.get(position), position);
    }

    public void convert(RecycleHolder holder, TaskStateList taskStateList, int position) {
        holder.setText(R.id.state_name, taskStateList.getPointName());
        if (taskStateList.getTaskState() == null){
            holder.setText(R.id.state_state, mContext.getResources().getStringArray(R.array.spinner_time)[taskStateList.getSpinnerTime()]);
        } else {
            holder.setText(R.id.state_state, taskStateList.getTaskState());
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClickListener(View view, int position);
        void OnItemLongClickListener(View view, int position);
    }
}
