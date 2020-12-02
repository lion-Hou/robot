package com.example.robot.map;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.bean.RobotMapBean;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewMapFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "AddNewMapFragment";

    @BindView(R.id.new_map_mapImage)
    ImageView newMapMapImage;
    @BindView(R.id.new_map_mapName_editText)
    EditText newMapMapNameEditText;
    @BindView(R.id.new_map_scan)
    Button newMapScan;
    @BindView(R.id.new_map_save)
    Button newMapSave;
    @BindView(R.id.new_map_back)
    Button newMapBack;



    private GsonUtils gsonUtils;
    public EmptyClient emptyClient;
    private Context mContext;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_new_map, container, false);
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();
        initView();
        initListener();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {
        newMapMapNameEditText.setOnClickListener(this);
        newMapScan.setOnClickListener(this);
        newMapSave.setOnClickListener(this);
        newMapBack.setOnClickListener(this);
    }

    private void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_map_scan:
                Log.d(TAG, "onEventMsg ： " + "点击开始扫描");
                newMapMapNameEditText = new EditText(getContext());
//                if (TextUtils.isEmpty(newMapMapNameEditText.getText()) ){
//                    Log.d(TAG, "onEventMsg ： " + "字符串为空");
//                    Toast toast=Toast.makeText(getActivity(),"请输入地图名称",Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, 0);
//                    toast.show();
//                }else {
                    Log.d(TAG, "onEventMsg ： " + "开始扫描");
                    gsonUtils.setMapName(newMapMapNameEditText.getText().toString());
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.START_SCAN_MAP));
//                }
                break;
            case R.id.new_map_save:
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.CANCEL_SCAN_MAP));
                break;
            case R.id.new_map_back:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new MapManagerFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    //获取map名称
    public static void refresh(String message){
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = new JSONObject(message);

            JSONArray name = jsonObject1.getJSONArray(Content.SENDMAPNAME);
            Content.list = new ArrayList<>();
            String[] mapName = new String[name.length()];
            for (int i =0; i <name.length(); i++){
                JSONObject jsonObject = name.getJSONObject(i);
                RobotMapBean robotMapBean = new RobotMapBean();
                robotMapBean.setMap_Name(jsonObject.getString(Content.MAP_NAME));
                robotMapBean.setGridHeight(jsonObject.getInt(Content.GRID_HEIGHT));
                robotMapBean.setGridWidth(jsonObject.getInt(Content.GRID_WIDTH));
                robotMapBean.setOriginX(jsonObject.getDouble(Content.ORIGIN_X));
                robotMapBean.setOriginY(jsonObject.getDouble(Content.ORIGIN_Y));
                Log.d("zdzd 666", ""+jsonObject.getDouble(Content.RESOLUTION));
                robotMapBean.setResolution(jsonObject.getDouble(Content.RESOLUTION));
                Content.list.add(robotMapBean);
                mapName[i] = jsonObject.getString(Content.MAP_NAME);
            }
            System.out.println("map_name: " + Content.list.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}