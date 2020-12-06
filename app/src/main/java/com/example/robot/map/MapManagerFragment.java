package com.example.robot.map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.bean.DrawLineBean;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.util.NormalDialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.map_manage_relative)
    RelativeLayout mapManageRelative;
    @BindView(R.id.map_manage_relative_border)
    RelativeLayout mapManageRelativeBorder;

    
    private GsonUtils gsonUtils;
    public EmptyClient emptyClient;
    private Context mContext;
    private View view;
    private String[] mapName;
    private NormalDialogUtil mapManageNormalDialog;

    private List<ImageView> imageViewArrayList = new ArrayList<>();
    private ImageView robot_Img;
    private int index = 0;
    double mBitmapHeight;
    double mBitmapWidth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        gsonUtils.setMapName(Content.map_Name);
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
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
                final EditText input_name = new EditText(getContext());
                new AlertDialog.Builder(getContext())
                        .setView(input_name)
                        .setMessage("请输入新地图名称")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newMapName = input_name.getText().toString();
                                System.out.println("pointName1111" + input_name);
                                if (!newMapName.equals(null)&&!newMapName.equals("")){
                                    gsonUtils.setOldMapName(Content.map_Name);
                                    gsonUtils.setNewMapName(newMapName);
                                    Log.d(TAG, "onEventMsg sss： " + Content.map_Name);
                                    Content.map_Name = newMapName;
                                    Log.d(TAG, "onEventMsg sss： " + Content.map_Name);
                                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.RENAME_MAP));
                                    managerSelected.setText(Content.map_Name);
                                }else {
                                    Toast.makeText(mContext, "请输入新的地图名"+newMapName, Toast.LENGTH_SHORT).show();
                                }
                                gsonUtils.setMapName(Content.map_Name);
                                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                break;
            case R.id.manager_delete:
                Log.d(TAG, "onEventMsg sss： " + Content.map_Name);
                mapManageNormalDialog = new NormalDialogUtil();
                mapManageNormalDialog.showDialog(mContext, "","是否删除该地图","取消","确认" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消
                        dialog.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //确定逻辑
                        /**
                         * houbo
                         */
                        if (!Content.map_Name.equals(null)){
                            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.DELETE_MAP));
                            managerSelected.setText(R.string.please_select_map);
                            Content.map_Name = null;
                            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
                            Log.d(TAG, "onEventMsg sss： " + Content.map_Name);
                        }
                        dialog.dismiss();
                    }
                });
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

    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg ： " + messageEvent.getState());
        if (messageEvent.getState() == 10001) {
            Log.d(TAG, "图片 ： " + messageEvent.getT());
            ByteBuffer bytes = (ByteBuffer) messageEvent.getT();
            int len = bytes.limit() - bytes.position();
            byte[] bytes1 = new byte[len];
            bytes.get(bytes1);
            Log.d(TAG, "图片11111 ： " + bytes1);

            Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
            mBitmapHeight = mBitmap.getHeight();
            mBitmapWidth = mBitmap.getWidth();
            if (mBitmapHeight >= mBitmapWidth){
                mBitmapWidth = mapManageRelativeBorder.getHeight()/mBitmapHeight*mBitmapWidth;
                mBitmapHeight = mapManageRelativeBorder.getHeight();
            }else if (mBitmapHeight > mBitmapWidth){
                mBitmapHeight = mapManageRelativeBorder.getWidth()/mBitmapWidth*mBitmapHeight;
                mBitmapWidth = mapManageRelativeBorder.getWidth();
            }

            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETPOINTPOSITION));
            managerMapImage.setImageBitmap(mBitmap);
            mapManageRelative.setLayoutParams(new RelativeLayout.LayoutParams((int)mBitmapWidth,(int)mBitmapHeight));
            RelativeLayout.LayoutParams layoutParams = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
            managerMapImage.setLayoutParams(layoutParams);
            mapManageRelative.addView(managerMapImage);
        }else if (messageEvent.getState() == 10005) {
            mapName = new String[Content.list.size()];
            for (int i=0;i< Content.list.size();i++) {
                mapName[i] =Content.list.get(i).getMap_Name();
            }
            System.out.println("ZHZHSSSS: " + Content.list.size());
            if (Content.list.size() == 1){
                System.out.println("MG_map_nameSSSS: " + Content.list.size());
                managerSelected.setText(mapName[0]);
                gsonUtils.setMapName(mapName[0]);
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
            }else{
                moreMap(mapName);
            }
            Log.d(TAG, "onEventMsg ： " + "3");
            //EventBus.getDefault().cancelEventDelivery(10005);
        } else if (messageEvent.getState() == 10008) {
            for (int i = 0; i <imageViewArrayList.size(); i++) {
                mapManageRelative.removeView(imageViewArrayList.get(i));
            }
            String message = (String) messageEvent.getT();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray(Content.SENDPOINTPOSITION);
                    Log.d("zdzd000 ", "pointName : " + jsonArray.toString());
                    for (int k = 0; k < Content.list.size(); k++) {
                        if (Content.list.get(k).getMap_Name().equals(Content.map_Name)) {
                            Log.d("zdzd555", "" + Content.list.get(k).getResolution());
                            index = k;
                        }
                    }
                    Log.d("zdzd5111", "" + index);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonItem = jsonArray.getJSONObject(i);
                        Log.d("zdzd111 ", "pointName : " + jsonItem.toString());
                        ImageView imageView = new ImageView(mContext);
                        imageView.setImageResource(R.drawable.ic_point);
                        imageViewArrayList.add(imageView);
                        imageView.setOnClickListener(this);
                        Log.d("zdzd222", "" + (managerMapImage.getWidth() / Content.list.get(index).getGridWidth() * jsonItem.getDouble(Content.POINT_X)
                                + Content.list.get(index).getOriginX() - (Content.ROBOT_SIZE / Content.list.get(index).getResolution() * Math.cos(jsonItem.getDouble(Content.ANGLE)))));
                        Log.d("zdzd222", "" + (Content.ROBOT_SIZE / Content.list.get(index).getResolution() * Math.cos(jsonItem.getDouble(Content.ANGLE))));
                        Log.d("zdzd222", "      \n");

                        Log.d("zdzd 777", "" + managerMapImage.getWidth());
                        Log.d("zdzd 777", "" + Content.list.get(index).getGridWidth());
                        Log.d("zdzd 777", "" + jsonItem.getDouble(Content.POINT_X));
                        Log.d("zdzd 777", "" + Content.list.get(index).getOriginX());
                        Log.d("zdzd 777", "" + (Content.ROBOT_SIZE / Content.list.get(index).getResolution() * Math.cos(jsonItem.getDouble(Content.ANGLE))));
                        Log.d("zdzd222", "      \n");
                        double mapWidth = (double) managerMapImage.getWidth();
                        double mapHeight = (double) managerMapImage.getHeight();
//

                        double gridHeight = Content.list.get(index).getGridHeight();
                        double gridWidth = Content.list.get(index).getGridWidth();

                        double pointX = jsonItem.getDouble(Content.POINT_X);
                        int pointType = jsonItem.getInt(Content.POINT_TYPE);
                        double pointY = jsonItem.getDouble(Content.POINT_Y);
                        double originX = Content.list.get(index).getOriginX();
                        double originY = Content.list.get(index).getOriginY();
                        double resolution = Content.list.get(index).getResolution();
                        double angleY = Math.sin(jsonItem.getDouble(Content.ANGLE));
                        double angleX = Math.cos(jsonItem.getDouble(Content.ANGLE));

                        Log.d("zdzd9998", "gridH"+gridHeight+"        gridW"+gridWidth + "     pointX"+pointX+"       originX"+originX+"       Content.ROBOT_SIZE "+Content.ROBOT_SIZE);
                        Log.d("zdzd9998", " resolution * angleX"+  resolution*angleX);
                        if (pointType == 2) {
                            imageView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX - (Content.ROBOT_SIZE / resolution * angleX))),
                                    (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY) - (Content.ROBOT_SIZE / resolution * angleY))),
                                    0, 0);
                            Log.d("zdzd9998", "angleX"+  angleX);
                            Log.d("zdzd9998", " resolution"+  resolution);
                            mapManageRelative.addView(imageView);
                        }else if (pointType == 1){
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (messageEvent.getState() == 40002) {//获取虚拟墙
            String message = (String) messageEvent.getT();
            double gridHeight = Content.list.get(index).getGridHeight();
            double gridWidth = Content.list.get(index).getGridWidth();
            double originX = Content.list.get(index).getOriginX();
            double originY = Content.list.get(index).getOriginY();
            try {
                JSONObject jsonObject = new JSONObject(message);
                JSONArray jsonArray = jsonObject.getJSONArray(Content.SEND_VIRTUAL);
                for (int i = 0; i < jsonArray.length(); i++) {
                    List<DrawLineBean> lineBeans = new ArrayList<>();
                    for (int k = 0; k < jsonArray.getJSONArray(i).length(); k++) {
                        JSONObject js = jsonArray.getJSONArray(i).getJSONObject(k);
                        DrawLineBean drawLineBean = new DrawLineBean();
                        drawLineBean.setX(js.getInt(Content.VIRTUAL_X));
                        drawLineBean.setY(js.getInt(Content.VIRTUAL_Y));
                        lineBeans.add(drawLineBean);
                    }
                    float startX = 0, startY = 0, endX = 0, endY = 0;
                    for (int k = 0; k < lineBeans.size(); k++) {
                        if (k == 0) {
                            startX = (float) ((mBitmapWidth / (lineBeans.get(k).getX() * gridWidth)) + originX);
                            startY = (float) (mBitmapHeight / (gridHeight * (mBitmapHeight - lineBeans.get(k).getY())) + originY);
                        } else {
                            endX = (float) ((mBitmapWidth / (lineBeans.get(k).getX() * gridWidth)) + originX);
                            endY = (float) (mBitmapHeight / (gridHeight * (mBitmapHeight - lineBeans.get(k).getY())) + originY);
                        }
                    }
                    //划线：点连线 连接start和end
                    Canvas canvas = new Canvas();
                    Paint paint = new Paint();
                    paint.setColor(R.color.colorPrimaryDark);
                    canvas.drawLine(startX, startY, endX, endY, paint);
                    mapManageRelative.draw(canvas);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
