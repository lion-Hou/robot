package com.example.robot.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.robot.R;
import com.example.robot.bean.SaveTaskBean;

import java.util.Collections;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements MyItemTouchHelperCallback.ItemTouchMoveListener {

    private List<SaveTaskBean> mList;
    private MyItemTouchHelperCallback.StartDragListener mDragListener;

    public MyAdapter(List<SaveTaskBean> list, MyItemTouchHelperCallback.StartDragListener dragListener){
        this.mList = list;
        this.mDragListener = dragListener;
    }

    public void refeshList(List<SaveTaskBean> list){
        this.mList = list;
        for (int i = 0; i < mList.size(); i++ ){
            Log.d("ADAPTER LIST ： ", ""+ mList.get(i).getSpinerIndex());
            System.out.println(mList.get(i).getSpinerIndex());
        }
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView iv;
        private Spinner tv;


        public MyViewHolder(View itemView) {
            super(itemView);
            iv = (TextView) itemView.findViewById(R.id.point_tv);
            tv = (Spinner) itemView.findViewById(R.id.spinner_time);
        }

    }

    public int getItemCount() {
        return mList.size();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dg_hint, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {
        Log.d("zdzd", "adapter : " + mList.get(position).getPoint_Name());
        holder.iv.setText(mList.get(position).getPoint_Name());
        holder.tv.setSelection(mList.get(position).getSpinerIndex());
        holder.iv.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    //传递触摸情况给谁？
                    mDragListener.onStartDrag(holder);
                }
                return false;
            }
        });

        holder.tv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int index, long id) {
                mList.get(position).setSpinerIndex(index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        // 1.数据交换；2.刷新
        Collections.swap(mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public boolean onItemRemove(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
        return true;
    }

}