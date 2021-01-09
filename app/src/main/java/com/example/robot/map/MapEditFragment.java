package com.example.robot.map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.bean.DrawLineBean;
import com.example.robot.bean.Point;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.util.SwipeListLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapEditFragment extends Fragment{

    private static final String TAG = "MapEditFragment";
    @BindView(R.id.add_point_title)
    LinearLayout addPointTitle;
    @BindView(R.id.edit_map_Image)
    ImageView editMapImage;
    @BindView(R.id.wall_btn)
    Button wallBtn;
    @BindView(R.id.mark_btn)
    Button markBtn;
    @BindView(R.id.save_btn)
    Button saveBtn;
    @BindView(R.id.back_btn)
    Button backBtn;
    @BindView(R.id.edit_mapname)
    TextView editMapname;
    @BindView(R.id.map_relative)
    RelativeLayout mapRelative;
    @BindView(R.id.map_relative_border)
    RelativeLayout mapRelativeBorder;
    @BindView(R.id.save_charging_btn)
    Button saveChargingBtn;
    @BindView(R.id.line1)
    RelativeLayout line1;
    @BindView(R.id.point_and_wall_list)
    ListView pointAndWallList;
    @BindView(R.id.map_list_border)
    RelativeLayout mapListBorder;

    private List<View> imageViewArrayList = new ArrayList<>();


    private GsonUtils gsonUtils;
    private Context mContext;
    private ImageView robot_Img;
    private View view;
    private int index = 0;
    private String charging = "放电";
    private String init;

    private double mBitmapHeight;
    private double mBitmapWidth;
    private Bitmap mBitmap;
    private List<List<DrawLineBean>> lists = new ArrayList<>();
    private int listSize;

    private Set<SwipeListLayout> sets = new HashSet();
    private List<Point> listPoint = new ArrayList<>();
    private ListAdapter adapter = new ListAdapter();
    private String pointNameList[];

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.d("hhhh", "edit_start");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.d("hhhh", "edit_stop");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map_edit, container, false);
        ButterKnife.bind(this, view);
        gsonUtils = new GsonUtils();
        mContext = view.getContext();
        initView();
        return view;
    }

    private void initView() {
        robot_Img = new ImageView(mContext);
        editMapname.setText(Content.map_Name);
        wallBtn.setEnabled(false);
        markBtn.setEnabled(false);
        saveBtn.setEnabled(false);
        saveChargingBtn.setEnabled(false);
        Toast.makeText(mContext, R.string.toast_edit_map_text7, Toast.LENGTH_LONG).show();
        gsonUtils.setMapName(Content.map_Name);
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
        gsonUtils.setMapName(Content.map_Name);
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.USE_MAP));
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_VIRTUAL));
    }

    //保存点
    public void AddPositionDialog() {
        final EditText input_name = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(input_name);
        builder.setMessage(R.string.toast_edit_map_text6);
        builder.setPositiveButton(R.string.all_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPointName = input_name.getText().toString();
                boolean isRepeat = false;
                Log.d("SourireG", "pointNameList" + pointNameList+newPointName);
                for (int i = 0; i < pointNameList.length; i++) {
                    Log.d("SourireG", "pointNameList" + pointNameList.length+pointNameList[i]);
                    if (pointNameList[i].equals(newPointName)) {
                        isRepeat = true;
                    }
                }
                if (!newPointName.equals(null) && !newPointName.equals("") && !newPointName.isEmpty()) {
                    if (isRepeat == true) {
                        Toast.makeText(mContext, R.string.toast_edit_map_text1, Toast.LENGTH_SHORT).show();
                    } else {
                        gsonUtils.setPositionName(newPointName);
                        System.out.println("pointName" + input_name);
                        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.ADD_POSITION));
                        Toast.makeText(mContext,  R.string.toast_edit_map_text5, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, R.string.toast_edit_map_text3, Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton(R.string.all_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg edit ： " + messageEvent.getState());
        if (messageEvent.getState() == 10001) {
            Log.d(TAG, "picture Edit ： " + messageEvent.getT());
            ByteBuffer bytes = (ByteBuffer) messageEvent.getT();
            int len = bytes.limit() - bytes.position();
            byte[] bytes1 = new byte[len];
            bytes.get(bytes1);
            Log.d(TAG, "picture edit ： " + bytes1);

            //Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
            mBitmap = BitmapFactory.decodeByteArray(bytes1, 0, bytes1.length);
            mBitmapHeight = mBitmap.getHeight();
            mBitmapWidth = mBitmap.getWidth();
            if (mBitmapHeight/mapRelativeBorder.getHeight() >= mBitmapWidth/mapRelativeBorder.getWidth()){
                mBitmapWidth = mapRelativeBorder.getHeight() / mBitmapHeight * mBitmapWidth;
                mBitmapHeight = mapRelativeBorder.getHeight();
            }else {
                mBitmapHeight = mapRelativeBorder.getWidth() / mBitmapWidth * mBitmapHeight;
                mBitmapWidth = mapRelativeBorder.getWidth();
            }
            mapRelative.removeAllViews();
            editMapImage.setImageBitmap(mBitmap);
            mapRelative.setLayoutParams(new RelativeLayout.LayoutParams((int) mBitmapWidth, (int) mBitmapHeight));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            editMapImage.setLayoutParams(layoutParams);

            ViewGroup parent = (ViewGroup) editMapImage.getParent();
            if (parent != null) {
                parent.removeView(editMapImage);
            }
//            Drawable drawable = new BitmapDrawable(mBitmap);

//            mapRelative.setBackground(drawable);
            mapRelative.addView(editMapImage);
//            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETPOINTPOSITION));
        } else if (messageEvent.getState() == 10008) {
            for (int i = 0; i < imageViewArrayList.size(); i++) {
                mapRelative.removeView(imageViewArrayList.get(i));
            }
            String message = (String) messageEvent.getT();
            try {
                JSONObject jsonObject = new JSONObject(message);
                listPoint.clear();
                if (jsonObject != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray(Content.SENDPOINTPOSITION);
                    Log.d("zdzd000 ", "pointName : " + jsonArray.toString());
                    for (int k = 0; k < Content.list.size(); k++) {
                        if (Content.list.get(k).getMap_Name().equals(Content.map_Name)) {
                            Log.d("zdzd555", "" + Content.list.get(k).getResolution());
                            index = k;
                        }
                    }
                    pointNameList = new String[jsonArray.length()];
                    Log.d("zdzd5111", "" + index);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonItem = jsonArray.getJSONObject(i);
                        Log.d("zdzd111 ", "pointName : " + jsonItem.toString());
                        ImageView imageView = new ImageView(mContext);
                        TextView textView = new TextView(mContext);
                        imageView.setImageResource(R.drawable.ic_point);

                        double gridHeight = Content.list.get(index).getGridHeight();
                        double gridWidth = Content.list.get(index).getGridWidth();

                        String pointName = jsonItem.getString(Content.POINT_NAME);
                        pointNameList[i] =pointName;
                        Log.d("zhzh111", "point" + pointName);
                        double pointX = jsonItem.getDouble(Content.POINT_X);

                        int pointType = jsonItem.getInt(Content.POINT_TYPE);
                        double pointY = jsonItem.getDouble(Content.POINT_Y);
                        double originX = Content.list.get(index).getOriginX();
                        double originY = Content.list.get(index).getOriginY();
                        double resolution = Content.list.get(index).getResolution();
                        double angleY = Math.sin(jsonItem.getDouble(Content.ANGLE));
                        double angleX = Math.cos(jsonItem.getDouble(Content.ANGLE));

                        Log.d("zdzd9998asd", "gridH" + gridHeight + "        gridW" + gridWidth + "     pointX" + pointX + "    pointy"+pointY+"       originX" + originX + "       Content.ROBOT_SIZE " + Content.ROBOT_SIZE);
                        Log.d("zdzd9998", " resolution * angleX" + resolution * angleX);

                        if (pointType == 1) {
                            ImageView charging_Img = new ImageView(mContext);
                            charging_Img.setImageResource(R.drawable.charging);
                            imageViewArrayList.add(charging_Img);
                            saveChargingBtn.setEnabled(false);
                            wallBtn.setEnabled(true);
                            markBtn.setEnabled(true);
                            saveBtn.setEnabled(true);
                            charging_Img.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX )),
                                    (int)(mBitmapHeight - (mBitmapHeight / gridHeight * (pointY))),
                                    0, 0);
                            mapRelative.addView(charging_Img);
                            Log.d("zdzd999222", "gridH" + gridHeight + "  gridW" + gridWidth + "  pointX" + pointX + "  pointY" + pointY + "   originX" + originX + "   originY" + originY + "   Content.ROBOT_SIZE " + Content.ROBOT_SIZE);
                            Log.d("zdzd999222", " resolution * angleX" + resolution * angleX);
                            Log.d("zdzd999222", "angleX" + angleX);
                        }
                            if (pointType == 2) {
                                Log.d("Sourire1", "gridH"+gridHeight+"        gridW"+gridWidth + "     pointX"+pointX+"    pointy"+pointY+ "   originX"+originX);
                                Log.d("SourireG", "width:"+mBitmapWidth+"height:"+mBitmapHeight);
                                imageView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                        (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY))),
                                        0, 0);
                                textView.setText(pointName);
                                Point point = new Point(pointName);
                                point.setName(pointName);
                                Log.d("SourireG", "add point name" + point.getName());
                                listPoint.add(point);

                                textView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                        (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY)) + 4),
                                        0, 0);
                                Log.d("zdzd999111", " resolution * angleX" + resolution * angleX);
                                Log.d("zdzd999111", "angleX" + angleX);
                                mapRelative.addView(imageView);
                                mapRelative.addView(textView);
                                imageViewArrayList.add(imageView);
                                imageViewArrayList.add(textView);
                            }
                    }
                    adapter.notifyDataSetChanged();
                    pointAndWallList.setAdapter(adapter);
                    pointAndWallList.setOnScrollListener(new AbsListView.OnScrollListener() {

                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                            switch (scrollState) {
                                //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                                case SCROLL_STATE_TOUCH_SCROLL:
                                    if (sets.size() > 0) {
                                        for (SwipeListLayout s : sets) {
                                            s.setStatus(SwipeListLayout.Status.Close, true);
                                            sets.remove(s);
                                        }
                                    }
                                    break;
                            }
                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem,
                                             int visibleItemCount, int totalItemCount) {

                        }
                    });
//                    pointData = pointArrayList.toArray(new String[pointArrayList.size()]);
//                    pointAndWallList.setAdapter(new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,pointData));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (messageEvent.getState() == 19191) {
            String message = (String) messageEvent.getT();
            if (message.equals("放电")){
                if (charging.equals("充电")){
                charging = message;
                }
            }else if (message.equals("充电")){
                if (charging.equals("放电")){
                charging = message;
                }
            }
            Log.d("YYYYY", "save_charging_point"+charging);
            if (message.equals("初始化完成")){
            init =message;
                if (init.equals("初始化完成")){
                    if (saveChargingBtn.isEnabled()==false){
                        saveChargingBtn.setEnabled(true);
                        Toast.makeText(mContext, R.string.toast_edit_map_text8, Toast.LENGTH_SHORT).show();
                        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETPOINTPOSITION));
                    }
                }
            }
        } else if (messageEvent.getState() == 10009) {
            String message = (String) messageEvent.getT();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject != null) {
                    mapRelative.removeView(robot_Img);
                    robot_Img.setImageResource(R.drawable.ic_baseline_brightness_1_24);
                    double gridHeight = jsonObject.getInt(Content.GRID_HEIGHT);
                    double gridWidth = jsonObject.getInt(Content.GRID_WIDTH);
                    double pointX = jsonObject.getDouble(Content.ROBOT_X);
                    double pointY = jsonObject.getDouble(Content.ROBOT_Y);
                    double originX = jsonObject.getDouble(Content.ORIGIN_X);
                    double originY = jsonObject.getDouble(Content.ORIGIN_Y);
                    double resolution = jsonObject.getDouble(Content.RESOLUTION);
                    double angleY = Math.sin(jsonObject.getDouble(Content.ANGLE));
                    double angleX = Math.cos(jsonObject.getDouble(Content.ANGLE));
                    robot_Img.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX - (Content.ROBOT_SIZE / resolution * angleX))),
                            (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY) - (Content.ROBOT_SIZE / resolution * angleY))),
                            0, 0);
                    Log.d("zdzd999888", "gridH" + gridHeight + "  gridW" + gridWidth + "  pointX" + pointX + "  pointY" + pointY + "   originX" + originX + "   originY" + originY + "   Content.ROBOT_SIZE " + Content.ROBOT_SIZE);
                    Log.d("zdzd999888", " resolution * angleX" + resolution * angleX);
                    Log.d("zdzd999888", "angleX" + angleX);
                    Log.d("zdzd999888", " resolution" + resolution);
                    mapRelative.addView(robot_Img);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (messageEvent.getState() == 40002) {//获取虚拟墙
            for (int k = 0; k < Content.list.size(); k++) {
                if (Content.list.get(k).getMap_Name().equals(Content.map_Name)) {
                    Log.d("zdzd555", "" + Content.list.get(k).getResolution());
                    index = k;
                }
            }
            Log.i("Henly","update VirtualWall" + index);
            String message = (String) messageEvent.getT();
            double gridHeight = Content.list.get(index).getGridHeight();
            double gridWidth = Content.list.get(index).getGridWidth();
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
               // float[] point = {0, 0, 0, 0};
                for (int k = 0; k < lineBeans.size(); ){
                    startX = (float) ((mBitmapWidth / gridWidth)*lineBeans.get(k).getX());
                    startY = (float) (mBitmapHeight - (mBitmapHeight / gridHeight)*lineBeans.get(k).getY());
                    endX = (float) ((mBitmapWidth / gridWidth)*lineBeans.get(k+1).getX());
                    endY = (float) (mBitmapHeight - (mBitmapHeight / gridHeight)*lineBeans.get(k+1).getY());
                    float[] point = {startX,startY,endX,endY};
                    pointlist.add(point);

                    List<DrawLineBean> drawLineBeanList = new ArrayList<>();
                    DrawLineBean drawLineBeanStart = new DrawLineBean();
                    drawLineBeanStart.setX(lineBeans.get(k).getX());
                    drawLineBeanStart.setY(lineBeans.get(k).getY());
                    drawLineBeanList.add(drawLineBeanStart);

                    DrawLineBean drawLineBeanEnd = new DrawLineBean();
                    drawLineBeanEnd.setX(lineBeans.get(k+1).getX());
                    drawLineBeanEnd.setY(lineBeans.get(k+1).getY());
                    drawLineBeanList.add(drawLineBeanEnd);
                    k = k +2 ;
                    lists.add(drawLineBeanList);
                }

                if(pointlist.size()!=0){
                    Log.i("Henly","updateVW,gridWidth = " + gridWidth + ",gridHeight = " + gridHeight );
                    updateVirtualWall(pointlist);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.edit_mapname, R.id.add_point_title, R.id.map_relative, R.id.wall_btn, R.id.mark_btn, R.id.save_btn, R.id.back_btn, R.id.save_charging_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_mapname:
                break;
            case R.id.add_point_title:
                break;
            case R.id.map_relative:
                break;
            case R.id.wall_btn:
                //editMapImage.setOnTouchListener(this);
                addVirtualWall();
                break;
            case R.id.mark_btn:
                AddPositionDialog();
                break;
            case R.id.save_btn:
                //存储虚拟墙
                String jsondata = gsonUtils.putVirtualWallMsg(Content.UPDATA_VIRTUAL, lists);
                Log.i(TAG,"jsondata = " + jsondata);
                MainActivity.emptyClient.send(gsonUtils.putVirtualWallMsg(Content.UPDATA_VIRTUAL, lists));

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.add_wall);
                //设置正面按钮
                builder.setPositiveButton(R.string.all_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                break;
            case R.id.back_btn:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new MapManagerFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.save_charging_btn:
                if (charging.equals("充电")) {
                    Log.d("SourireG", "ggg"+charging);
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.ADD_POWER_POINT));
                    Toast.makeText(mContext, R.string.toast_edit_map_text9, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext,R.string.toast_edit_map_text10, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    Drawl bDrawl;

    void addVirtualWall(){
        if(bDrawl==null){
            RelativeLayout.LayoutParams layoutParams = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
           // layoutParams.height = editMapImage.getHeight();
           // layoutParams.width = editMapImage.getWidth();
          //  layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
         //   layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            bDrawl = new Drawl(mContext);
            bDrawl.setLayoutParams(layoutParams);
            mapRelative.addView(bDrawl);
        }else{
            bDrawl.clearCanvas();
        }
    }

    class Drawl extends View{
        private float start_x,start_y;//声明起点坐标
        private float mov_x,mov_y;//滑动轨迹坐标
        private Paint paint;//声明画笔
        private Canvas canvas;//画布
        private Bitmap bitmap;//位图
        private float view_X,view_Y;
		
        private ArrayList<float[]> pointlist = new ArrayList<>();//保存所画线的坐标
		
        public Drawl(Context context) {
            super(context);
            paint = new Paint(Paint.DITHER_FLAG);//创建一个画笔

            // 建立新的bitmap，其内容是对原bitmap的copy    
		    bitmap = Bitmap.createBitmap( (int)mBitmapWidth, (int)mBitmapHeight, mBitmap ==null?Bitmap.Config.ARGB_8888:mBitmap.getConfig());
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
			canvas=new Canvas();
            canvas.setBitmap(bitmap);

            paint.setStyle(Paint.Style.STROKE);//设置非填充
            paint.setStrokeWidth(3);//笔宽3像素
            paint.setColor(Color.RED);//设置为红笔
            paint.setAntiAlias(true);//锯齿不显示

            view_X = editMapImage.getX();
            view_Y = editMapImage.getY();
        }

        //画位图
        @Override
        protected void onDraw(Canvas canvas) {
            //super.onDraw(canvas);
            canvas.drawBitmap(bitmap,0,0,null);

            canvas.drawLine(start_x, start_y, mov_x, mov_y, paint);//画线
            for(int i = 0;i < pointlist.size();i++){
                float[] point = pointlist.get(i);
                canvas.drawLine(point[0], point[1], point[2], point[3], paint);//画保存的线
            }
        }

        //触摸事件
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            List<DrawLineBean> drawLineBeanList = new ArrayList<>();

            double mapWidth = (double) editMapImage.getWidth();
            double mapHeight = (double) editMapImage.getHeight();
            double gridHeight = Content.list.get(index).getGridHeight();
            double gridWidth = Content.list.get(index).getGridWidth();
            //double originX = Content.list.get(index).getOriginX();
            //double originY = Content.list.get(index).getOriginY();

            if (event.getAction()==MotionEvent.ACTION_MOVE) {//如果滑动
                mov_x = event.getX();
                mov_y = event.getY();
                invalidate();
            }
			
            if (event.getAction()==MotionEvent.ACTION_DOWN) {//如果点击
                //记录起点
                start_x = event.getX();
                start_y = event.getY();
               //invalidate();
            }

            if (MotionEvent.ACTION_UP == event.getAction()) {
                float endX = event.getX();
                float endY = event.getY();

                float[] point = {start_x,start_y,endX,endY};
                pointlist.add(point);

                DrawLineBean drawLineBeanStart = new DrawLineBean();
                double x_temp = (start_x*gridWidth)/mapWidth;
                double y_temp = ((mapHeight-start_y)*gridHeight)/mapHeight;
                drawLineBeanStart.setX(x_temp);
                drawLineBeanStart.setY(y_temp);
                drawLineBeanList.add(drawLineBeanStart);

                DrawLineBean drawLineBeanEnd = new DrawLineBean();
                double end_x_temp = (endX*gridWidth)/mapWidth;
                double end_y_temp = (((mapHeight-endY)*gridHeight)/mapHeight);
                drawLineBeanEnd.setX(end_x_temp);
                drawLineBeanEnd.setY(end_y_temp);

                drawLineBeanList.add(drawLineBeanEnd);
                lists.add(drawLineBeanList);
            }
            return true;
        }

        void clearCanvas(){
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清掉画布上的内容
            pointlist.clear();
            start_x =start_y=mov_x=mov_y=0;
         //   lists.clear();

            invalidate();
        }
    }

    void updateVirtualWall(ArrayList<float[]> pointlist){
        Log.i("Henly","updateVirtualWall");
        RelativeLayout.LayoutParams layoutParams = new  RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        // layoutParams.height = editMapImage.getHeight();
        // layoutParams.width = editMapImage.getWidth();
        //  layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        //   layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        DrawlineFromVW bDrawlVW = new DrawlineFromVW(mContext,pointlist);
        bDrawlVW.setLayoutParams(layoutParams);
        mapRelative.addView(bDrawlVW);
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
            Log.i("Henly","updateVW,onDraw,mBitmapWidth = " + mBitmapWidth + ",mBitmapHeight = " + mBitmapHeight );
            for(int i = 0;i < Pointlist.size();i++){
                float[] point = Pointlist.get(i);
                Log.i("Henly","updateVW,drawline,start_x:" + point[0] + ",start_y:" + point[1] + ", end_x:" + point[2] + " ,end_y:" + point[3]);
                canvas.drawLine(point[0], point[1], point[2], point[3], paint);//画保存的线
            }
        }
    }

    class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }

    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listPoint.size();
        }

        @Override
        public Object getItem(int arg0) {
            return listPoint.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int arg0, View view, ViewGroup arg2) {
            Point pointName = (Point) getItem(arg0);
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(
                        R.layout.slip_item_item, null);
            }
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setText(pointName.getName());
            Log.i("SourireG","GETNAME()"+pointName.getName());
            final SwipeListLayout sll_main = (SwipeListLayout) view
                    .findViewById(R.id.sll_main);
            TextView tv_rename = (TextView) view.findViewById(R.id.tv_top);
            TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
            sll_main.setOnSwipeStatusListener(new MyOnSlipStatusListener(
                    sll_main));
            tv_rename.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    final EditText input_name = new EditText(getContext());
                    new AlertDialog.Builder(getContext())
                            .setView(input_name)
                            .setMessage(R.string.dialog_edit_map_text1)
                            .setPositiveButton(R.string.all_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String newPointName = input_name.getText().toString();
                                    boolean isRepeat = false;
                                    for (int i = 0; i < pointNameList.length; i++) {
                                        if (pointNameList[i].equals(newPointName)) {
                                            isRepeat = true;
                                        }
                                    }
                                    System.out.println("pointName1111" + input_name);
                                    if (!newPointName.equals(null)&&!newPointName.equals("")&&!newPointName.isEmpty()){
                                        if (isRepeat = true) {
                                            Toast.makeText(mContext, R.string.toast_edit_map_text1, Toast.LENGTH_SHORT).show();
                                        }else {
                                            gsonUtils.setOldPointName(pointName.getName());
                                            gsonUtils.setNewPointName(newPointName);
                                            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.RENAME_POSITION));
                                            tv_name.setText(newPointName);
                                            Toast.makeText(mContext, R.string.toast_edit_map_text2, Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(mContext, R.string.toast_edit_map_text3, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton(R.string.all_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            });
            tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sll_main.setStatus(SwipeListLayout.Status.Close, true);
                    gsonUtils.setMapName(Content.map_Name);
                    gsonUtils.setPositionName(pointName.getName());
                    Log.i("SourireG","delete"+pointName.getName());
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.DELETE_POSITION));
                    listPoint.remove(arg0);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, R.string.toast_edit_map_text4, Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

    }
}