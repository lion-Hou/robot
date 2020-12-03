package com.example.robot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.robot.R;
import com.example.robot.bean.HistoryBean;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<RecycleHolder> {

    private Context mContext;
    private List<HistoryBean> mDatas;
    private int mLayoutId;
    private LayoutInflater mInflater;

    private OnItemClickListener onItemClickListener;


    public HistoryAdapter(Context mContext, int mLayoutId) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        mInflater = LayoutInflater.from(mContext);
    }

    public void refeshList(List<HistoryBean> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecycleHolder(mInflater.inflate(mLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecycleHolder holder, int position) {
        convert(holder, mDatas.get(position), position);
        if (onItemClickListener != null) {
            //设置背景
            //holder.itemView.setBackgroundResource(R.drawable.recycler_bg);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClickListener(holder.itemView, holder.getLayoutPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.OnItemLongClickListener(holder.itemView, holder.getLayoutPosition());
                    return true;
                }
            });

        }
    }

    public void convert(RecycleHolder holder, HistoryBean historyBean, int position) {
        holder.setText(R.id.history_map_name, historyBean.getMapName());
        holder.setText(R.id.history_task_name, historyBean.getTaskName());
        holder.setText(R.id.history_time_name, historyBean.getTime());
        holder.setText(R.id.history_date_name, historyBean.getDate());
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
