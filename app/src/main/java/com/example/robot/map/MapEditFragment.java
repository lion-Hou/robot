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
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import androidx.annotation.Nullable;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapEditFragment extends Fragment {

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
    private List<List<DrawLineBean>> listsUpdate = new ArrayList<>();
    private int listSize;

    private Set<SwipeListLayout> sets = new HashSet();
    private List<Point> listPoint = new ArrayList<>();
    private ListAdapter adapter = new ListAdapter();
    private String pointNameList[];

    //Zhihan add for change map
    private final Matrix matrix = new Matrix();
    private final Matrix savedMatrix = new Matrix();
    private DisplayMetrics mDisplyMetrcs;
    private float cScale = 1;
    private float minScaleR;// 最小缩放比例
    private static final float MAX_SCALE = 16f;// 最大缩放比例
    private static final int NONE = 0;// 初始状态
    private static final int DRAG = 1;// 拖动
    private static final int ZOOM = 2;// 缩放
    private int mode = NONE;
    private final PointF prev = new PointF();
    private final PointF mid = new PointF();
    private float dist = 1f;
    private float hScale;
    private float wScale;

    private double showWindowCorer[];
    private float tScale = 1;

    private double translationX;
    private double translationY;
    private double partialTranslationX;
    private double partialTranslationY;
    float deltaX = 0, deltaY = 0;
    private JSONObject jsonPointObject;
    private int pointIndex = 2;
    private boolean isOnePoint = true;

    private boolean isOnClickVW = false;
    private PointF pointZore = new PointF(0, 0);
    private boolean isInit = false;

    //即时点坐标
    ArrayList<PointF> arrayPointList;


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        getActivity().findViewById(R.id.main_image).setVisibility(View.GONE);
        getActivity().findViewById(R.id.robot_img).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.second_fragment).setVisibility(View.VISIBLE);
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
        //
        updateVW();

        robot_Img = new ImageView(mContext);
        editMapname.setText(Content.map_Name);
        wallBtn.setEnabled(false);
        wallBtn.setBackgroundResource(R.drawable.dituxiangqing_btn_disable_xuniqiang);
        markBtn.setEnabled(false);
        markBtn.setBackgroundResource(R.drawable.dituxiangqing_btn_disable_xuniqiang);
        saveBtn.setEnabled(false);
        saveBtn.setBackgroundResource(R.drawable.dituxiangqing_btn_disable_xuniqiang);
        saveChargingBtn.setEnabled(false);
        saveChargingBtn.setBackgroundResource(R.drawable.dituxiangqing_btn_disable_xuniqiang);
        Toast.makeText(mContext, R.string.toast_edit_map_text7, Toast.LENGTH_LONG).show();
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPLIST));
        gsonUtils.setMapName(Content.map_Name);
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
        gsonUtils.setMapName(Content.map_Name);
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.USE_MAP));

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
                Log.d("SourireG", "pointNameList" + pointNameList + newPointName);
                for (int i = 0; i < pointNameList.length; i++) {
                    Log.d("SourireG", "pointNameList" + pointNameList.length + pointNameList[i]);
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
                        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
                        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETPOINTPOSITION));
                        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_VIRTUAL));
                        Toast.makeText(mContext, R.string.toast_edit_map_text5, Toast.LENGTH_SHORT).show();
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
            if (mBitmapHeight / mapRelativeBorder.getHeight() >= mBitmapWidth / mapRelativeBorder.getWidth()) {
                mBitmapWidth = mapRelativeBorder.getHeight() / mBitmapHeight * mBitmapWidth;
                mBitmapHeight = mapRelativeBorder.getHeight();
            } else {
                mBitmapHeight = mapRelativeBorder.getWidth() / mBitmapWidth * mBitmapHeight;
                mBitmapWidth = mapRelativeBorder.getWidth();
            }
            mapRelative.removeAllViews();
//            editMapImage.setImageBitmap(mBitmap);
            ViewGroup parent = (ViewGroup) editMapImage.getParent();
            if (parent != null) {
                parent.removeView(editMapImage);
            }
            mapRelative.addView(editMapImage);
            loadMap(mBitmap);

        } else if (messageEvent.getState() == 10008) {
            //Zhihan change 3.18 17:07 s
            arrayPointList = new ArrayList();

            //Zhihan change 3.18 17:07 e

            for (int i = 0; i < imageViewArrayList.size(); i++) {
                mapRelative.removeView(imageViewArrayList.get(i));
            }
            String message = (String) messageEvent.getT();
            try {
                JSONObject jsonObject = new JSONObject(message);
                jsonPointObject = new JSONObject(message);
                imageViewArrayList.clear();
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
                        pointNameList[i] = pointName;
                        Log.d("zhzh111", "point" + pointName);
                        double pointX = jsonItem.getDouble(Content.POINT_X);

                        int pointType = jsonItem.getInt(Content.POINT_TYPE);
                        double pointY = jsonItem.getDouble(Content.POINT_Y);
                        double originX = Content.list.get(index).getOriginX();
                        double originY = Content.list.get(index).getOriginY();
                        double resolution = Content.list.get(index).getResolution();
                        double angleY = Math.sin(jsonItem.getDouble(Content.ANGLE));
                        double angleX = Math.cos(jsonItem.getDouble(Content.ANGLE));

                        Log.d("zdzd9998asd", "gridH" + gridHeight + "        gridW" + gridWidth + "     pointX" + pointX + "    pointy" + pointY + "       originX" + originX + "       Content.ROBOT_SIZE " + Content.ROBOT_SIZE);
                        Log.d("zdzd9998", " resolution * angleX" + resolution * angleX);

                        if (pointType == 1) {
                            ImageView charging_Img = new ImageView(mContext);
                            charging_Img.setImageResource(R.drawable.charging);
                            imageViewArrayList.add(charging_Img);

                            //zhihan change 记录所有点坐标
                            float x = (float) (mBitmapWidth / gridWidth * (pointX));
                            float y = (float) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY)));
                            Log.d(TAG, "onEventMsg: qq SSSSSS");
                            PointF pointF1 = new PointF();
                            pointF1.set(x, y);
                            arrayPointList.add(pointF1);

                            wallBtn.setEnabled(true);
                            wallBtn.setBackgroundResource(R.drawable.dituxiangqing_btn_back);
                            markBtn.setEnabled(true);
                            markBtn.setBackgroundResource(R.drawable.dituxiangqing_btn_back);
                            saveBtn.setEnabled(true);
                            saveBtn.setBackgroundResource(R.drawable.dituxiangqing_btn_back);

                            charging_Img.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                    (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY))),
                                    0, 0);
                            mapRelative.addView(charging_Img);
                            Log.d("zdzdS", "PointX:" + pointX + "  PointY:" + pointY);
                            Log.d("zdzdS", "gridH:" + gridHeight + "        gridW:" + gridWidth);
                            Log.d("zdzdS", "width:" + mBitmapWidth + "height:" + mBitmapHeight);
                        }
                        if (pointType == 2) {
//                                Log.d("zdzdS", "PointX:" + pointX + "  PointY:" + pointY);
//                                Log.d("zdzdS", "gridH:"+gridHeight+"        gridW:"+gridWidth);
//                                Log.d("zdzdS", "width:"+mBitmapWidth+"height:"+mBitmapHeight);
                            imageView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                    (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY))),
                                    0, 0);
                            textView.setText(pointName);
                            Point point = new Point(pointName);
                            point.setName(pointName);
                            Log.d("SourireG", "add point name" + point.getName());
                            listPoint.add(point);

                            //zhihan change 记录所有点坐标
                            float x = (float) (mBitmapWidth / gridWidth * (pointX));
                            float y = (float) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY)));

                            //传入初始显示坐标
                            PointF pointF1 = new PointF();
                            pointF1.set(x, y);
                            arrayPointList.add(pointF1);


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
            if (message.equals("放电")) {
                if (charging.equals("充电")) {
                    charging = message;
                }
            } else if (message.equals("充电")) {
                if (charging.equals("放电")) {
                    charging = message;
                }
            }
            Log.d("YYYYY", "save_charging_point" + charging);
            if (message.equals("初始化完成")) {
                init = message;
                if (init.equals("初始化完成")) {
                    if (saveChargingBtn.isEnabled() == false) {
                        saveChargingBtn.setEnabled(true);
                        saveChargingBtn.setBackgroundResource(R.drawable.dituxiangqing_btn_back);
                        Toast.makeText(mContext, R.string.toast_edit_map_text8, Toast.LENGTH_SHORT).show();
                        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETPOINTPOSITION));
                        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_VIRTUAL));


                        editMapImage.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {

                                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                                    // 主点按下
                                    case MotionEvent.ACTION_DOWN:
                                        isOnePoint = true;
                                        savedMatrix.set(matrix);
                                        prev.set(event.getX(), event.getY());
                                        Log.d("11112222", "onTouch: " + event.getX() + "  " + event.getY());
                                        break;
                                    // 副点按下
                                    case MotionEvent.ACTION_POINTER_DOWN:
                                        if (mode == DRAG) {
                                            translationX = partialTranslationX + deltaX;
                                            translationY = partialTranslationY + deltaY;
                                            pointZore.set((float) (pointZore.x + translationX), (float) (pointZore.y + translationY));
                                            Log.d(TAG, "onTouch: pointZore" + pointZore.x + (float) (pointZore.x + translationX) + "  " + (float) (pointZore.y + translationY));
                                            tScale = 1;
                                            updatePoint();
                                            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_VIRTUAL));
                                        }
                                        isOnePoint = false;
                                        Log.d(TAG, "onTouch: GGGGGG" + translationY + translationX);
//                                        if (partialTranslationX == 0 && partialTranslationY == 0) {
                                        dist = spacing(event);
                                        // 如果连续两点距离大于5，则判定为多点模式
//                                            if (spacing(event) > 1f) {
                                        savedMatrix.set(matrix);
                                        midPoint(mid, event);
                                        mode = ZOOM;
//                                            }
//                                        }
                                        break;
                                    case MotionEvent.ACTION_UP:
                                        if (mode == DRAG) {
                                            translationX = partialTranslationX + deltaX;
                                            translationY = partialTranslationY + deltaY;
                                            pointZore.set((float) (pointZore.x + translationX), (float) (pointZore.y + translationY));
                                            Log.d(TAG, "onTouch: pointZore" + pointZore.x + (float) (pointZore.x + translationX) + "  " + (float) (pointZore.y + translationY));
                                            tScale = 1;
                                            updatePoint();
                                            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_VIRTUAL));
                                        }
                                        mode = NONE;
                                        break;
                                    case MotionEvent.ACTION_POINTER_UP:
                                        Log.d("122", "cScale:" + cScale + "minScale" + minScaleR);
                                        if (mode == ZOOM) {
                                            if (cScale * tScale >= 1) {
                                                cScale = cScale * tScale;
                                                pointZore.set((float) (pointZore.x * tScale), (float) (pointZore.y * tScale));
                                                updatePoint();
                                            } else {
                                                pointZore.set(0, 0);
                                                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETPOINTPOSITION));
                                                cScale = 1;
                                            }
                                            mode = NONE;
                                            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_VIRTUAL));
//                            updatePoint();
                                        } else if (mode == DRAG) {
                                            translationX = partialTranslationX + deltaX;
                                            translationY = partialTranslationY + deltaY;
                                            pointZore.set((float) (pointZore.x + translationX), (float) (pointZore.y + translationY));
                                            tScale = 1;
                                            updatePoint();
                                            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_VIRTUAL));
                                        }
                                        mode = NONE;
                                        break;
                                    case MotionEvent.ACTION_MOVE:
                                        if (isOnePoint == true) {
                                            mode = DRAG;
                                            matrix.set(savedMatrix);
                                            matrix.postTranslate(event.getX() - prev.x, event.getY() - prev.y);
                                            partialTranslationX = event.getX() - prev.x;
                                            partialTranslationY = event.getY() - prev.y;
                                        } else if (isOnePoint == false) {
                                            mode = ZOOM;
                                            float newDist = spacing(event);
                                            if (newDist > 1f) {
                                                matrix.set(savedMatrix);
                                                tScale = newDist / dist;
                                                matrix.postScale(tScale, tScale, mid.x, mid.y);
                                                Log.d("1111", "tScale:" + tScale);
                                            }
                                        }
                                        Log.d("122", "tScale:" + tScale);
                                        break;
                                }
                                Log.d("1222", "onTouch: " + mode);
                                editMapImage.setImageMatrix(matrix);
                                checkView();
                                return true;
                            }
                        });
                        // 获取当前屏幕分辨率对象
                        mDisplyMetrcs = new DisplayMetrics();
                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(mDisplyMetrcs);
                        hScale = (float) editMapImage.getHeight() / (float) mBitmap.getHeight();
                        wScale = (float) editMapImage.getWidth() / (float) mBitmap.getWidth();
                        Log.d("SourireG", "setupViews: " + hScale + "  " + wScale + "   " + editMapImage.getHeight() + "  " + mBitmap.getHeight());
                        //保持bitmap宽高比的条件下让bitmap充满整个矩阵
                        if (hScale > wScale) {
                            matrix.setScale(wScale, wScale);
                            minScaleR = wScale;
                        } else {
                            matrix.setScale(hScale, hScale);
                            minScaleR = hScale;
                        }
                        setCenter();
                        editMapImage.setScaleType(ImageView.ScaleType.MATRIX);
                        editMapImage.setImageMatrix(matrix);

                    }
                }
            }
        } else if (messageEvent.getState() == 10009) {
            String message = (String) messageEvent.getT();
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject != null) {

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

                    double x = (mBitmapWidth / gridWidth * (pointX - (Content.ROBOT_SIZE / resolution * angleX)));
                    double y = (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY) - (Content.ROBOT_SIZE / resolution * angleY)));

//                    robot_Img.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX - (Content.ROBOT_SIZE / resolution * angleX))),
//                            (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY) - (Content.ROBOT_SIZE / resolution * angleY))),
//                            0, 0);
                    updateRobotPostion(x, y);
                    Log.d("zdzd999888", "gridH" + gridHeight + "  gridW" + gridWidth + "  pointX" + pointX + "  pointY" + pointY + "   originX" + originX + "   originY" + originY + "   Content.ROBOT_SIZE " + Content.ROBOT_SIZE);
                    Log.d("zdzd999888", " resolution * angleX" + resolution * angleX);
                    Log.d("zdzd999888", "angleX" + angleX);
                    Log.d("zdzd999888", " resolution" + resolution);
//                    mapRelativeBorder.addView(robot_Img);
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
            Log.i("Henly", "update VirtualWall" + index);
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
                lists.clear();
                listsUpdate.clear();
                for (int k = 0; k < lineBeans.size(); ) {
                    startX = (float) ((mBitmapWidth / gridWidth) * lineBeans.get(k).getX());
                    startY = (float) (mBitmapHeight - (mBitmapHeight / gridHeight) * lineBeans.get(k).getY());
                    endX = (float) ((mBitmapWidth / gridWidth) * lineBeans.get(k + 1).getX());
                    endY = (float) (mBitmapHeight - (mBitmapHeight / gridHeight) * lineBeans.get(k + 1).getY());
                    float[] point = {startX * cScale + pointZore.x, startY * cScale + pointZore.y, endX * cScale + pointZore.x, endY * cScale + pointZore.y};
                    pointlist.add(point);

                    Log.d(TAG, "onEventMsg: Sooooo" + pointZore.x + "  y" + pointZore.y + "  dd:" + cScale);
                    List<DrawLineBean> drawLineBeanList = new ArrayList<>();
                    DrawLineBean drawLineBeanStart = new DrawLineBean();
                    drawLineBeanStart.setX(lineBeans.get(k).getX());
                    drawLineBeanStart.setY(lineBeans.get(k).getY());
                    drawLineBeanList.add(drawLineBeanStart);

                    DrawLineBean drawLineBeanEnd = new DrawLineBean();
                    drawLineBeanEnd.setX(lineBeans.get(k + 1).getX());
                    drawLineBeanEnd.setY(lineBeans.get(k + 1).getY());
                    drawLineBeanList.add(drawLineBeanEnd);
                    k = k + 2;
                    lists.add(drawLineBeanList);
                    listsUpdate.add(drawLineBeanList);
                }
                Log.d("SourireL111", "onEventMsg: " + listsUpdate.size());
                if (pointlist.size() != 0) {
                    Log.i("Henly", "updateVW,gridWidth = " + gridWidth + ",gridHeight = " + gridHeight);
                    updateVirtualWall(pointlist);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.edit_mapname, R.id.add_point_title, R.id.wall_btn, R.id.mark_btn, R.id.save_btn, R.id.back_btn, R.id.save_charging_btn})
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
                Log.i(TAG, "jsondata = " + jsondata);
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
                if (isOnClickVW == false) {
                } else {
                    addVirtualWall();
                }
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
                    Log.d("SourireG", "ggg" + charging);
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.ADD_POWER_POINT));
                    saveChargingBtn.setEnabled(false);
                    Toast.makeText(mContext, R.string.toast_edit_map_text9, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, R.string.toast_edit_map_text10, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    Drawl bDrawl;

    void addVirtualWall() {
        if (isOnClickVW == false) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            // layoutParams.height = editMapImage.getHeight();
            // layoutParams.width = editMapImage.getWidth();
            //  layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            //   layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            bDrawl = new Drawl(mContext);
            bDrawl.setLayoutParams(layoutParams);
            mapRelative.addView(bDrawl);
            wallBtn.setText(R.string.map_exit_edit_virtual_wall);
            isOnClickVW = true;
        } else if (isOnClickVW == true) {
            wallBtn.setText(R.string.map_edit_virtual_wall);
            bDrawl.clearCanvas();
            mapRelative.removeView(bDrawl);
            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_VIRTUAL));
            isOnClickVW = false;
        }
    }

    class Drawl extends View {
        private float start_x, start_y;//声明起点坐标
        private float mov_x, mov_y;//滑动轨迹坐标
        private Paint paint;//声明画笔
        private Canvas canvas;//画布
        private Bitmap bitmap;//位图
        private float view_X, view_Y;

        private ArrayList<float[]> pointlist = new ArrayList<>();//保存所画线的坐标

        public Drawl(Context context) {
            super(context);
            paint = new Paint(Paint.DITHER_FLAG);//创建一个画笔

            // 建立新的bitmap，其内容是对原bitmap的copy    
            bitmap = Bitmap.createBitmap((int) mBitmapWidth, (int) mBitmapHeight, mBitmap == null ? Bitmap.Config.ARGB_8888 : mBitmap.getConfig());
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            canvas = new Canvas();
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
            canvas.drawBitmap(bitmap, 0, 0, null);

            canvas.drawLine(start_x, start_y, mov_x, mov_y, paint);//画线
            for (int i = 0; i < pointlist.size(); i++) {
                float[] point = pointlist.get(i);
                canvas.drawLine(point[0], point[1], point[2], point[3], paint);//画保存的线
            }
        }

        //触摸事件
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            List<DrawLineBean> drawLineBeanList = new ArrayList<>();

            double mapWidth = (double) mBitmapWidth;
            double mapHeight = (double) mBitmapHeight;
            double gridHeight = Content.list.get(index).getGridHeight();
            double gridWidth = Content.list.get(index).getGridWidth();
            //double originX = Content.list.get(index).getOriginX();
            //double originY = Content.list.get(index).getOriginY();

            if (event.getAction() == MotionEvent.ACTION_MOVE) {//如果滑动
                mov_x = event.getX();
                mov_y = event.getY();
                invalidate();
            }

            if (event.getAction() == MotionEvent.ACTION_DOWN) {//如果点击
                //记录起点
                start_x = event.getX();
                start_y = event.getY();
                //invalidate();
            }

            if (MotionEvent.ACTION_UP == event.getAction()) {
                float endX = event.getX();
                float endY = event.getY();

                float[] point = {start_x, start_y, endX, endY};
                pointlist.add(point);

                DrawLineBean drawLineBeanStart = new DrawLineBean();
                double x_temp = (((start_x - pointZore.x) / cScale) * gridWidth) / mapWidth;
                double y_temp = ((mapHeight - ((start_y - pointZore.y) / cScale)) * gridHeight) / mapHeight;
                drawLineBeanStart.setX(x_temp);
                drawLineBeanStart.setY(y_temp);
                drawLineBeanList.add(drawLineBeanStart);

                DrawLineBean drawLineBeanEnd = new DrawLineBean();
                double end_x_temp = (((endX - pointZore.x) / cScale) * gridWidth) / mapWidth;
                double end_y_temp = (((mapHeight - ((endY - pointZore.y) / cScale)) * gridHeight) / mapHeight);
                drawLineBeanEnd.setX(end_x_temp);
                drawLineBeanEnd.setY(end_y_temp);

                drawLineBeanList.add(drawLineBeanEnd);
                lists.add(drawLineBeanList);
            }
            return true;
        }

        void clearCanvas() {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清掉画布上的内容
            pointlist.clear();
            start_x = start_y = mov_x = mov_y = 0;
            //   lists.clear();

            invalidate();
        }
    }

    DrawlineFromVW bDrawlVW;

    void updateVirtualWall(ArrayList<float[]> pointlist) {
        Log.i("Henly", "updateVirtualWall");
        mapRelative.removeView(bDrawlVW);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        // layoutParams.height = editMapImage.getHeight();
        // layoutParams.width = editMapImage.getWidth();
        //  layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        //   layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        bDrawlVW = new DrawlineFromVW(mContext, pointlist);
        bDrawlVW.setLayoutParams(layoutParams);
        mapRelative.addView(bDrawlVW);

    }

    class DrawlineFromVW extends View {
        private float start_x, start_y;//声明起点坐标
        private float mov_x, mov_y;//滑动轨迹坐标
        private Paint paint;//声明画笔
        private Paint paint1;//声明画笔
        private Canvas canvas;//画布
        private Bitmap bitmap;//位图
        private float view_X, view_Y;

        private double scale_x = 1;
        private double scale_y = 1;

        float x;
        float y;
        int newIndex = 0;
        private ArrayList<float[]> Pointlist = new ArrayList<>();//保存所画线的坐标

        public DrawlineFromVW(Context context, ArrayList<float[]> pointlist) {
            super(context);
            paint = new Paint(Paint.DITHER_FLAG);//创建一个画笔
            paint1 = new Paint(Paint.DITHER_FLAG);//创建一个画笔用于选中地图上的线段

            bitmap = Bitmap.createBitmap((int) mBitmapWidth, (int) mBitmapHeight, mBitmap == null ? Bitmap.Config.ARGB_8888 : mBitmap.getConfig());
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            canvas = new Canvas();
            canvas.setBitmap(bitmap);

            paint.setStyle(Paint.Style.STROKE);//设置非填充
            paint.setStrokeWidth(3);//笔宽3像素
            paint.setColor(Color.RED);//设置为红笔
            paint.setAntiAlias(true);//锯齿不显示

            paint1.setStyle(Paint.Style.STROKE);//设置非填充
            paint1.setStrokeWidth(6);//笔宽6像素
            paint1.setColor(Color.BLACK);//设置为黑色
            paint1.setAntiAlias(true);//锯齿不显示

            Pointlist = pointlist;
            invalidate();

            Log.i(TAG, "DrawlineFromVW");
        }

        //画位图
        @Override
        protected void onDraw(Canvas canvas) {
            //super.onDraw(canvas);
            canvas.drawBitmap(bitmap, 0, 0, null);
            Log.i("Henly", "updateVW,onDraw,mBitmapWidth = " + mBitmapWidth + ",mBitmapHeight = " + mBitmapHeight);
            for (int i = 0; i < Pointlist.size(); i++) {
                float[] point = Pointlist.get(i);
                Log.i("Henly", "updateVW,drawline,start_x:" + point[0] + ",start_y:" + point[1] + ", end_x:" + point[2] + " ,end_y:" + point[3]);
                canvas.drawLine(point[0], point[1], point[2], point[3], paint);//画保存的线
            }
        }

        //获得touch的坐标
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            List<DrawLineBean> drawLineBeanList = new ArrayList<>();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    Log.d("hbx", String.valueOf(x));
                    Log.d("hby", String.valueOf(y));
                    for (int i = 0; i < Pointlist.size(); i++) {
                        float[] point = Pointlist.get(i);
                        Log.i("hb", "updateVW,drawline,start_x:" + point[0] + ",start_y:" + point[1] + ", end_x:" + point[2] + " ,end_y:" + point[3]);
                        float AX = point[0];
                        float BX = point[0];
                        float CX = point[2];
                        float DX = point[2];
                        float AY = point[1] - 10;
                        float BY = point[1] + 10;
                        float CY = point[3] + 10;
                        float DY = point[3] - 10;
                        float a = (BX - AX) * (y - AY) - (BY - AY) * (x - AX);
                        float b = (CX - BX) * (y - BY) - (CY - BY) * (x - BX);
                        float c = (DX - CX) * (y - CY) - (DY - CY) * (x - CX);
                        float d = (AX - DX) * (y - DY) - (AY - DY) * (x - DX);
                        if ((a >= 0 && b >= 0 && c >= 0 && d >= 0) || (a <= 0 && b <= 0 && c <= 0 && d <= 0)) {
                            canvas.drawLine(point[0], point[1], point[2], point[3], paint1);//画保存的线
                            newIndex = i;

                            Log.d("hb", "OKkkkkkk" + newIndex);
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setMessage(R.string.delete_Wall);
                            /**
                             * 确认
                             * 用于删除虚拟墙
                             */
                            builder.setPositiveButton(R.string.all_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String sdasad = gsonUtils.putVirtualWallMsg(Content.UPDATA_VIRTUAL, listsUpdate);
                                    Log.i(TAG, "jsondata111 = " + sdasad);
                                    listsUpdate.remove(newIndex);
//                            double gridHeight = Content.list.get(index).getGridHeight();
//                            double gridWidth = Content.list.get(index).getGridWidth();
//                            double fullStartX = (float) (point[0] / (mBitmapWidth / gridWidth));
//                            Log.d("SourireG", "OK"+point[0]);
//                            double fullStartY = -((float) ((point[1] - mBitmapHeight) / (mBitmapHeight / gridHeight)));
//                            double fullEndX = (float) (point[2] / (mBitmapWidth / gridWidth));
//                            double fullEndY = -((float) ((point[3] - mBitmapHeight) / (mBitmapHeight / gridHeight)));
//                            Log.d("full", String.valueOf(fullStartX));
//                            Log.d("full", String.valueOf(fullStartY));
//                            Log.d("full", String.valueOf(fullEndX));
//                            Log.d("full", String.valueOf(fullEndY));
//                            Log.d("full", String.valueOf(listFull.size()));
//
//
//                            for (int i = 0; i < listFull.size(); i++){
//                                Log.d("full1", "sa"+listFull.get(i).get(0).getX());
//                                Log.d("full1", "sa"+listFull.get(i).get(0).getY());
//                                Log.d("full1","sa"+listFull.get(i).get(1).getX());
//                                Log.d("full1", "sa"+listFull.get(i).get(1).getY());
////                                if (fullStartX == listFull.get(i).get(0).getX() || fullStartY == listFull.get(i).get(0).getY() ||fullEndX == listFull.get(i).get(1).getX() || fullEndY == listFull.get(i).get(1).getY()){
//                                Log.d("listFull", String.valueOf(listFull.get(i).get(0).getX()));
//                            }
                                    Log.d("hb", "OK" + listsUpdate.size());

                                    //更新新的list
                                    String jsondata = gsonUtils.putVirtualWallMsg(Content.UPDATA_VIRTUAL, listsUpdate);
                                    Log.i(TAG, "jsondata = " + jsondata);
                                    MainActivity.emptyClient.send(gsonUtils.putVirtualWallMsg(Content.UPDATA_VIRTUAL, listsUpdate));
                                    //重新绘制
                                    gsonUtils.setMapName(Content.map_Name);


                                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_VIRTUAL));

                                }
                            });
                            /**
                             * 取消
                             * 取消时恢复原来状态
                             */
                            builder.setNegativeButton(R.string.all_cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清掉
                                    dialog.dismiss();
                                }
                            });
                            builder.show();

                        } else {
                            Log.d("hb", "NO");
                        }
                    }
                    break;
            }
            return false;
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
            Log.i("SourireG", "GETNAME()" + pointName.getName());
            final SwipeListLayout sll_main = (SwipeListLayout) view
                    .findViewById(R.id.sll_main);
            ImageView tv_rename = (ImageView) view.findViewById(R.id.tv_top);
            ImageView tv_delete = (ImageView) view.findViewById(R.id.tv_delete);
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
                                    if (!newPointName.equals(null) && !newPointName.equals("") && !newPointName.isEmpty()) {
                                        if (isRepeat == true) {
                                            Toast.makeText(mContext, R.string.toast_edit_map_text1, Toast.LENGTH_SHORT).show();
                                        } else {
                                            gsonUtils.setOldPointName(pointName.getName());
                                            gsonUtils.setNewPointName(newPointName);
                                            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.RENAME_POSITION));
                                            tv_name.setText(newPointName);
                                            Toast.makeText(mContext, R.string.toast_edit_map_text2, Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
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
                    Log.i("SourireG", "delete" + pointName.getName());
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.DELETE_POSITION));
                    listPoint.remove(arg0);
                    notifyDataSetChanged();
                    Toast.makeText(mContext, R.string.toast_edit_map_text4, Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }

    }

    private void checkView() {
        float p[] = new float[9];
        matrix.getValues(p);
        if (mode == ZOOM) {
            if (p[0] < minScaleR)
                matrix.setScale(minScaleR, minScaleR);
            if (p[0] > MAX_SCALE)
                matrix.set(savedMatrix);
        }
        this.setCenter();
    }

    private void setCenter() {
        this.setCenter(true, true);
    }

    private void setCenter(boolean horizontal, boolean vertical) {
        Matrix mMatrix = new Matrix();
        mMatrix.set(matrix);
        RectF mRectF = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mMatrix.mapRect(mRectF);
        deltaX = 0;
        deltaY = 0;
        float height = mRectF.height();
        float width = mRectF.width();

        if (vertical) {
            // 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
//            int screenHeight = mDisplyMetrcs.heightPixels;
            int screenHeight = mapRelativeBorder.getHeight();
//            if (height < screenHeight)
//                deltaY = (screenHeight - height) / 2 - mRectF.top;
//            else if (mRectF.top > 0)
            if (mRectF.top > 0) {
                deltaY = -mRectF.top;
            }
//            else if (minScaleR == hScale && mRectF.bottom < screenHeight) {
//                deltaY = editMapImage.getHeight() - mRectF.bottom;
//            }
        }

        if (horizontal) {
//            int screenWidth = mDisplyMetrcs.widthPixels;
            int screenWidth = mapRelativeBorder.getWidth();
//            if (width < screenWidth)
//                deltaX = (screenWidth - width) / 2 - mRectF.left;
//            else if (mRectF.left > 0)
            if (mRectF.left > 0) {
                deltaX = -mRectF.left;
                Log.d(TAG, "setCenter: SourireGGGGG" + minScaleR + "  " + hScale + "   " + wScale);
            }
//            else if (minScaleR == wScale && mRectF.right < screenWidth) {
//                deltaX = screenWidth - mRectF.right;
//            }
        }

        matrix.postTranslate(deltaX, deltaY);

    }

    /**
     * 两点的距离
     *
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float xf = 0.0f, yf = 0.0f;
        try {
             xf = event.getX(0) - event.getX(1);
             yf = event.getY(0) - event.getY(1);
        } catch (IllegalArgumentException e) {
// TODO Auto-generated catch block 
            e.printStackTrace();
        }
        return (float) Math.sqrt(xf * xf + yf * yf);
    }

    /**
     * 两点的中点
     *
     * @param point
     * @param event
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
//        point.set(x / 2, y / 2);
        point.set(0, 0);
    }

    private void loadMap(Bitmap mBitmap) {

        editMapImage.setImageBitmap(mBitmap);
      //  mapRelative.setLayoutParams(new RelativeLayout.LayoutParams((int) mBitmapWidth, (int) mBitmapHeight));
      //  RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
      //  editMapImage.setLayoutParams(layoutParams);


    }

    private void updatePoint() {

        pointIndex = 1;
        for (int i = 0; i < imageViewArrayList.size(); i++) {
            mapRelative.removeView(imageViewArrayList.get(i));
        }
        try {
            imageViewArrayList.clear();
            if (jsonPointObject != null) {
                JSONArray jsonArray = jsonPointObject.getJSONArray(Content.SENDPOINTPOSITION);
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

                    if (pointType == 1) {
                        ImageView charging_Img = new ImageView(mContext);
                        charging_Img.setImageResource(R.drawable.charging);
                        imageViewArrayList.add(charging_Img);
                        PointF pointF = arrayPointList.get(0);
                        int x = (int) ((pointF.x * tScale) + translationX);
                        int y = (int) ((pointF.y * tScale) + translationY);
                        Log.d(TAG, "updatePoint: 555555" + "   " + x + "  " + y + "  " + tScale + "   " + translationX + "  " + translationY);
                        arrayPointList.get(0).set((float) x, (float) y);
                        charging_Img.setPaddingRelative(x, y,
                                0, 0);
                        mapRelative.addView(charging_Img);
                    }
                    if (pointType == 2) {
                        PointF pointF = arrayPointList.get(pointIndex);
                        int x = (int) ((pointF.x * tScale) + translationX);
                        int y = (int) ((pointF.y * tScale) + translationY);
                        arrayPointList.get(pointIndex).set((float) x, (float) y);

                        imageView.setPaddingRelative(x, y,
                                0, 0);
                        textView.setText(pointName);
                        textView.setPaddingRelative(x,
                                y + 1,
                                0, 0);
                        mapRelative.addView(imageView);
                        mapRelative.addView(textView);
                        imageViewArrayList.add(imageView);
                        imageViewArrayList.add(imageView);
                        imageViewArrayList.add(textView);
                        pointIndex = pointIndex + 1;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tScale = 1;
        translationX = 0;
        translationY = 0;
    }

    private void updateRobotPostion(double x, double y) {
        mapRelative.removeView(robot_Img);
        x = x * cScale + pointZore.x;
        y = y * cScale + pointZore.y;
        robot_Img.setPaddingRelative((int) x,
                (int) y,
                0, 0);
        mapRelative.addView(robot_Img);
        updateVW();
    }

    //更新虚拟墙
    private void updateVW() {
//        for (int i = 0; i <pointlist.size() ; i++) {
//            ArrayList
//            Log.d(TAG, "updateVW: point value"+ point[0]);
//            point[0] = point[0]*tScale +translationX;
//            point[1] = point[1]*tScale+translationX;
//            point[2] = point[2]*tScale+translationX;
//            point[3] = point[3]*tScale+translationX;
//            //point*tScale+translationY
//            Log.d(TAG, "updateVW: point value"+ point[0]);
//        }

    }

}
