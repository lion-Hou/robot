package com.example.robot.map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.LongDef;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapManagerFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MapManagerFragment";

    @BindView(R.id.manager_selected)
    TextView managerSelected;
    @BindView(R.id.manager_newMap)
    Button managerNewMap;
    @BindView(R.id.manager_mapImage)
    ImageView managerMapImage;
    @BindView(R.id.manager_rename)
    Button managerRename;
    @BindView(R.id.manager_delete)
    Button managerDelete;
    @BindView(R.id.manager_edit)
    Button managerEdit;
    @BindView(R.id.manager_back)
    Button managerBack;

    
    private GsonUtils gsonUtils;
    public EmptyClient emptyClient;
    private Context mContext;
    private View view;
    private String[] mapName;
    private String selectedMapName = "selectedMapName";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d("hhhh",  "manger_start");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d("hhhh",  "manger_stop");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_map_manager, container, false);
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();
        initListener();
        initView();
        mContext = view.getContext();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Content.map_Name != null) {
            managerSelected.setText(Content.map_Name);
        }
    }

    private void initView() {
        managerNewMap.setOnClickListener(this);
        managerSelected.setOnClickListener(this);
        managerEdit.setOnClickListener(this);
        managerBack.setOnClickListener(this);
        managerDelete.setOnClickListener(this);
        managerRename.setOnClickListener(this);
    }

    private void initListener() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.manager_newMap:
                Log.d(TAG, "onEventMsg ： " + "新建地图");
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new AddNewMapFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.manager_selected:
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPLIST));
                Log.d(TAG,"查看地图请求地图链表");
                break;


            case R.id.manager_rename:
                if (!Content.map_Name.equals(null)){
                    gsonUtils.setOldMapName(Content.map_Name);
                    gsonUtils.setNewMapName("gaozhihanqqqqqqqqq");
                    Log.d(TAG, "onEventMsg sss： " + Content.map_Name);
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.RENAME_MAP));
                    Content.map_Name = "gaozhihanqqqqqqqqq";
                }
                break;
            case R.id.manager_delete:
                Log.d(TAG, "onEventMsg sss： " + Content.map_Name);
                if (!Content.map_Name.equals(null)){
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.DELETE_MAP));
                    managerSelected.setText(R.string.please_select_map);
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
                    Content.map_Name = null;
                    Log.d(TAG, "onEventMsg sss： " + Content.map_Name);
                }
                break;
            case R.id.manager_edit:
                gsonUtils.setMapName(Content.map_Name);
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.USE_MAP));
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new MapEditFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.manager_back:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new FirstFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                break;
        }
    }

    //获取map名称
    public void refreshMapManage(String[] mapName){

        mapName = new String[Content.list.size()];
        for (int i =0; i <Content.list.size(); i++){
            mapName[i] =   Content.list.get(i).getMap_Name();
        }
        System.out.println("MG_map_name: " + Content.list.size());
        moreMap(mapName);
    }

    public void moreMap(String[] mapName){
        Log.d(TAG, "onEventMsg ： " + "2");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        System.out.println("which" + mapName.length);
        builder.setItems(mapName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("which" + which);
                managerSelected.setText(mapName[which]);
                Content.map_Name = mapName[which];
                gsonUtils.setMapName(mapName[which]);
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
                Log.d(TAG,"AAAAAAAA");
            }
        });
        builder.create().show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg ： " + messageEvent.getState());
        if (messageEvent.getState() == 10001) {
            Log.d(TAG, "图片 ： " + messageEvent.getT());
            ByteBuffer bytes = (ByteBuffer) messageEvent.getT();
            int len = bytes.limit() - bytes.position();
            byte[] bytes1 = new byte[len];
            bytes.get(bytes1);
            Log.d(TAG, "新建地图 ： " + bytes1.length);
            Glide.with(mContext).load(bytes1).into(managerMapImage);
        }else if (messageEvent.getState() == 10005) {
            mapName = new String[Content.list.size()];
            for (int i=0;i< Content.list.size();i++) {
                mapName[i] =Content.list.get(i).getMap_Name();
            }
            Log.d(TAG,mapName[1]);
            refreshMapManage(mapName);
            Log.d(TAG, "onEventMsg ： " + "3");
            //EventBus.getDefault().cancelEventDelivery(10005);
        }
    }
}