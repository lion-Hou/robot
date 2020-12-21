package com.example.robot.map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
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
    private String[] taskNameList;

    private List<ImageView> imageViewArrayList = new ArrayList<>();
    private ImageView robot_Img;
    private int index = 0;
    double mBitmapHeight;
    double mBitmapWidth;
    private Bitmap mBitmap;



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
        gsonUtils.setMapName(Content.map_Name);
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETTASKQUEUE));
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

        if (Content.map_Name == null){
            managerEdit.setEnabled(false);
            managerDelete.setEnabled(false);
            managerRename.setEnabled(false);
        }

    }

    private void initListener() {

    }

    @Override
    public void onClick(View view) {
        final CharSequence allCancel=mContext.getString(R.string.all_cancel);
        final CharSequence allOK=mContext.getString(R.string.all_ok);
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
                        .setMessage(R.string.dat_map_manage_dialog_text1)
                        .setPositiveButton(R.string.all_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newMapName = input_name.getText().toString();
                                System.out.println("pointName1111" + input_name);

                                boolean isRepeat = false;
                                for (int i = 0; i <mapName.length ; i++) {
                                    if (mapName[i].equals(newMapName)){
                                        isRepeat = true;
                                    }
                                }
                                if (!newMapName.equals(null)&&!newMapName.equals("")&&!newMapName.isEmpty()){
                                    if (isRepeat==false){
                                    gsonUtils.setOldMapName(Content.map_Name);
                                    gsonUtils.setNewMapName(newMapName);
                                    Log.d(TAG, "onEventMsg sss： " + Content.map_Name);
                                    Content.map_Name = newMapName;
                                    Log.d(TAG, "onEventMsg sss： " + Content.map_Name);
                                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.RENAME_MAP));
                                    managerSelected.setText(Content.map_Name);
                                        Toast.makeText(mContext, R.string.dat_map_manage_toast_text1, Toast.LENGTH_SHORT).show();
                                    }else if (isRepeat==true){
                                        Toast.makeText(mContext, R.string.dat_map_manage_toast_text2, Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(mContext,R.string.dat_map_manage_toast_text3, Toast.LENGTH_SHORT).show();
                                }
//                                gsonUtils.setMapName(Content.map_Name);
//                                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
                            }
                        })
                        .setNegativeButton(R.string.all_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                break;
            case R.id.manager_delete:
                Log.d(TAG, "onEventMsg sss： " + Content.map_Name);
                mapManageNormalDialog = new NormalDialogUtil();
                final CharSequence datMapManageDialogText2=mContext.getString(R.string.dat_map_manage_dialog_text2);
                mapManageNormalDialog.showDialog(mContext, "", (String) datMapManageDialogText2, (String) allCancel, (String)allOK, new DialogInterface.OnClickListener() {
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
                            if (taskNameList.length>0){
                            for (int i = 0; i < taskNameList.length ; i++) {
                                gsonUtils.setTaskName(taskNameList[i]);
                                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.DELETE_TASK));
                            }
                            }
                            gsonUtils.setMapName(Content.map_Name);
                            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.DELETE_MAP));
                            managerSelected.setText(R.string.please_select_map);
                            Content.map_Name = null;
                            mapManageRelative.removeAllViews();
                            managerEdit.setEnabled(false);
                            managerDelete.setEnabled(false);
                            managerRename.setEnabled(false);
                            Log.d(TAG, "onEventMsg sss： " + Content.map_Name);
                            Toast.makeText(mContext,R.string.dat_map_manage_toast_text4, Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.manager_edit:
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
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.second_fragment, new SecoundFragment(), null)
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
                Log.d(TAG, "onEventMsg ： " + "mapName11"+ Content.map_Name);
                if (Content.map_Name!=null) {
                    managerEdit.setEnabled(true);
                    managerDelete.setEnabled(true);
                    managerRename.setEnabled(true);
                }
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

            //Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
            mBitmap = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
            mBitmapHeight = mBitmap.getHeight();
            mBitmapWidth = mBitmap.getWidth();
            if (mBitmapHeight >= mBitmapWidth){
                mBitmapWidth = mapManageRelativeBorder.getHeight()/mBitmapHeight*mBitmapWidth;
                mBitmapHeight = mapManageRelativeBorder.getHeight();
            }else if (mBitmapHeight < mBitmapWidth){
                mBitmapHeight = mapManageRelativeBorder.getWidth()/mBitmapWidth*mBitmapHeight;
                mBitmapWidth = mapManageRelativeBorder.getWidth();
            }


            managerMapImage.setImageBitmap(mBitmap);
            mapManageRelative.setLayoutParams(new RelativeLayout.LayoutParams((int)mBitmapWidth,(int)mBitmapHeight));
            RelativeLayout.LayoutParams layoutParams = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
            managerMapImage.setLayoutParams(layoutParams);

            ViewGroup parent = (ViewGroup) managerMapImage.getParent();
            if (parent != null) {
                parent.removeView(managerMapImage);
            }

            mapManageRelative.addView(managerMapImage);
            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETPOINTPOSITION));
            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_VIRTUAL));
        }else if (messageEvent.getState() == 10005) {
            int ori_size = Content.list.size();
            System.out.println("ZHZHSSSS: ori_size = " + ori_size);
            int null_count = 0;
            for (int i=0;i< ori_size;i++) {
                String temp1 = Content.list.get(i).getMap_Name();
                System.out.println("ZHZHZZZZ1111,temp1 =: " + temp1);
                if(TextUtils.isEmpty(temp1)){
                //    null_count++;
                    Content.list.remove(i);
                    ori_size--;
                }
            }
            mapName = new String[Content.list.size()-null_count];
            System.out.println("ZHZHSSSS: " + Content.list.size());
            for (int i=0;i< Content.list.size();i++) {
                mapName[i] =Content.list.get(i).getMap_Name();
                System.out.println("ZHZHZZZZ: " + mapName[i]);
            }
            System.out.println("ZHZHSSSS: " + Content.list.size());
            if (Content.list.size() == 1){
                System.out.println("MG_map_nameSSSS: " + Content.list.size());
                managerSelected.setText(mapName[0]);
                Content.map_Name=mapName[0];
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
                        TextView textView = new TextView(mContext);
                        imageView.setImageResource(R.drawable.ic_point);
                        imageViewArrayList.add(imageView);

                        double gridHeight = Content.list.get(index).getGridHeight();
                        double gridWidth = Content.list.get(index).getGridWidth();
                        String pointName = jsonItem.getString(Content.POINT_NAME);
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

                        if (pointType == 1) {
                            ImageView charging_Img = new ImageView(mContext);
                            charging_Img.setImageResource(R.drawable.charging);
                            imageViewArrayList.add(charging_Img);
                            charging_Img.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                    (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY))),
                                    0, 0);
                            mapManageRelative.addView(charging_Img);
                        }
                        if (pointType == 2) {
                            imageView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                    (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY))),
                                    0, 0);
                            textView.setText(pointName);
                            textView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                    (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY)) + 1),
                                    0, 0);
                            Log.d("zdzd9998", "angleX" + angleX);
                            Log.d("zdzd9998", " resolution" + resolution);
                            mapManageRelative.addView(imageView);
                            mapManageRelative.addView(textView);
                            imageViewArrayList.add(imageView);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if (messageEvent.getState() == 10017) {
            taskNameList = (String[]) messageEvent.getT();
            Log.d("task_name", taskNameList[0]);
        }else if (messageEvent.getState() == 40002) {//获取虚拟墙
            for (int k = 0; k < Content.list.size(); k++) {
                if (Content.list.get(k).getMap_Name().equals(Content.map_Name)) {
                    Log.d("zdzd555", "" + Content.list.get(k).getResolution());
                    index = k;
                }
            }
            Log.i("Henly","mapManager,update VirtualWall" + index);
            String message = (String) messageEvent.getT();
            double gridHeight = Content.list.get(index).getGridHeight();
            double gridWidth = Content.list.get(index).getGridWidth();
            double originX = Content.list.get(index).getOriginX();
            double originY = Content.list.get(index).getOriginY();
            try {
                JSONObject jsonObject = new JSONObject(message);
                JSONArray jsonArray = jsonObject.getJSONArray(Content.SEND_VIRTUAL);
                List<DrawLineBean> lineBeans = new ArrayList<>();//线段的坐标
                ArrayList<float[]> pointlist = new ArrayList<>();//保存所画线的坐标

                float startX = 0, startY = 0, endX = 0, endY = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    for (int k = 0; k < jsonArray.getJSONArray(i).length(); k++) {
                        JSONObject js = jsonArray.getJSONArray(i).getJSONObject(k);
                        DrawLineBean drawLineBean = new DrawLineBean();
                        drawLineBean.setX(js.getDouble(Content.VIRTUAL_X));
                        drawLineBean.setY(js.getDouble(Content.VIRTUAL_Y));
                        lineBeans.add(drawLineBean);
                    }
                }

                //画虚拟墙
                for (int k = 0; k < lineBeans.size(); ) {
                    startX = (float) ((mBitmapWidth / gridWidth) * lineBeans.get(k).getX());
                    startY = (float) (mBitmapHeight - (mBitmapHeight / gridHeight) * lineBeans.get(k).getY());
                    endX = (float) ((mBitmapWidth / gridWidth) * lineBeans.get(k + 1).getX());
                    endY = (float) (mBitmapHeight - (mBitmapHeight / gridHeight) * lineBeans.get(k + 1).getY());

                    float[] point = {startX,startY,endX,endY};
                    Log.i("Henly","mapManager,update VirtualWall,k = " + k);
                    pointlist.add(point);

                    k = k +2 ;
                }

                if (pointlist.size() != 0) {
                    Log.i("Henly", "updateVW,gridWidth = " + gridWidth + ",gridHeight = " + gridHeight);
                    updateVirtualWall(pointlist);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void updateVirtualWall(ArrayList<float[]> pointlist){
        Log.i("Henly","mapmanager-updateVirtualWall");
        RelativeLayout.LayoutParams layoutParams = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        // layoutParams.height = editMapImage.getHeight();
        // layoutParams.width = editMapImage.getWidth();
        //  layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        //   layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        MapManagerFragment.DrawlineFromVW bDrawlVW = new MapManagerFragment.DrawlineFromVW(mContext,pointlist);
        bDrawlVW.setLayoutParams(layoutParams);
        mapManageRelative.addView(bDrawlVW);
    }

    class DrawlineFromVW extends View{
        private float start_x,start_y;//声明起点坐标
        private float mov_x,mov_y;//滑动轨迹坐标
        private Paint paint;//声明画笔
        private Canvas canvas;//画布
        private Bitmap bitmap;//位图
        private float view_X,view_Y;

        private double scale_x = 1;
        private double scale_y = 1;

        private ArrayList<float[]> Pointlist = new ArrayList<>();//保存所画线的坐标

        public DrawlineFromVW(Context context,ArrayList<float[]>pointlist) {
            super(context);
            paint = new Paint(Paint.DITHER_FLAG);//创建一个画笔

            bitmap = Bitmap.createBitmap( (int)mBitmapWidth, (int)mBitmapHeight, mBitmap ==null?Bitmap.Config.ARGB_8888:mBitmap.getConfig());
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            canvas=new Canvas();
            canvas.setBitmap(bitmap);

            paint.setStyle(Paint.Style.STROKE);//设置非填充
            paint.setStrokeWidth(3);//笔宽3像素
            paint.setColor(Color.RED);//设置为红笔
            paint.setAntiAlias(true);//锯齿不显示

            Pointlist = pointlist;
            invalidate();

            Log.i(TAG,"DrawlineFromVW");
        }

        //画位图
        @Override
        protected void onDraw(Canvas canvas) {
            //super.onDraw(canvas);
            canvas.drawBitmap(bitmap,0,0,null);
            Log.i("Henly","mapmanager-updateVW,onDraw,mBitmapWidth = " + mBitmapWidth + ",mBitmapHeight = " + mBitmapHeight );
            for(int i = 0;i < Pointlist.size();i++){
                float[] point = Pointlist.get(i);
                Log.i("Henly","mapmanager-updateVW,drawline,start_x:" + point[0] + ",start_y:" + point[1] + ", end_x:" + point[2] + " ,end_y:" + point[3]);
                canvas.drawLine(point[0], point[1], point[2], point[3], paint);//画保存的线
            }
        }
    }
}
