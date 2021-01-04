package com.example.robot.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.robot.R;
import com.example.robot.bean.DialogCheckboxBean;
import com.example.robot.bean.HistoryBean;

import java.util.ArrayList;
import java.util.List;

public class MapAdapter<T> extends BaseAdapter {

    private List<String> list = new ArrayList<>();
    private Context context;
    private DialogAdapter.AdapterCallback adapterCallback;
    private List<String> stringList = new ArrayList<>();

    public MapAdapter(Context context) {
        this.context = context;
    }

    public void refeshList(List<String> list) {
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
        if (convertView == null) {
            holder = new mViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.map_layout, parent, false);
            holder.mapNameText = (TextView) convertView.findViewById(R.id.map_name_text);
            convertView.setTag(holder);
        } else {
            holder = (mViewHolder) convertView.getTag();
        }
        //匹配数据
        stringList.clear();
        holder.mapNameText.setText(list.get(position));
        return convertView;
    }

    private static class mViewHolder {
        TextView mapNameText;
    }

}
