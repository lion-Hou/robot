package com.example.robot.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robot.R;
import com.example.robot.bean.DialogCheckboxBean;
import com.example.robot.util.SelectDialogUtil;

import java.util.ArrayList;
import java.util.List;

public class DialogAdapter extends BaseAdapter {
    private List<DialogCheckboxBean> list = new ArrayList<>();
    private Context context;
    private AdapterCallback adapterCallback;
    private List<String> stringList = new ArrayList<>();

    public interface AdapterCallback {
        public void adapterCallback(int position, boolean isChecked, List<String> stringList, String pointName);
    }

    public void setAdapterCallback(AdapterCallback adapterCallback) {
        this.adapterCallback = adapterCallback;
    }

    public DialogAdapter(Context context) {
        this.context = context;
    }

    public void refeshList(List<DialogCheckboxBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mViewHolder holder = null;
//        if (convertView == null) {
            //无缓存时进入
            holder = new mViewHolder();
            //这里要注意有一个是上下文，一个是显示每一行的行布局文件
            convertView = LayoutInflater.from(context).inflate(R.layout.dialog_checkbox, parent, false);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
            holder.checkbox_tv = (TextView) convertView.findViewById(R.id.checkbox_tv);
            convertView.setTag(holder);
//        } else {
//            //缓存时进入
//            holder = (mViewHolder) convertView.getTag();
//        }
        //匹配数据
        holder.checkbox_tv.setText(list.get(position).getTaskName());
        holder.checkBox.setChecked(list.get(position).isClick());
        Log.d("zdzd ", "getview : " + position + list.get(position).isClick());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("dialogadapter : ", "" + position + ",   " + isChecked);
                if (isChecked) {
                    stringList.add(list.get(position).getTaskName());
                } else {
                    stringList.remove(list.get(position).getTaskName());
                }
                list.get(position).setClick(isChecked);
                adapterCallback.adapterCallback(position, isChecked, stringList,list.get(position).getTaskName());
            }
        });
        return convertView;
    }

    private static class mViewHolder {
        TextView checkbox_tv;
        CheckBox checkBox;
    }
}
