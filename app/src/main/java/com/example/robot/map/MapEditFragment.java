package com.example.robot.map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.bean.DrawLineBean;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapEditFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {

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
    LinearLayout line1;
    private List<ImageView> imageViewArrayList = new ArrayList<>();


    private GsonUtils gsonUtils;
    private Context mContext;
    private ImageView robot_Img;
    private View view;
    private int index = 0;
    private String charging;

    private double mBitmapHeight;
    private double mBitmapWidth;

    private List<List<DrawLineBean>> lists = new ArrayList<>();

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
        gsonUtils.setMapName(Content.map_Name);
        wallBtn.setEnabled(false);
        markBtn.setEnabled(false);
        saveBtn.setEnabled(false);
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GET_VIRTUAL));
    }

    //保存点
    public void AddPositionDialog() {
        final EditText input_name = new EditText(getContext());
        new AlertDialog.Builder(getContext())
                .setView(input_name)
                .setMessage("请输入新建地点名称")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newPointName = input_name.getText().toString();
                        if (!newPointName.equals(null) && !newPointName.equals("") && !newPointName.isEmpty()) {
                            gsonUtils.setPositionName(newPointName);
                            System.out.println("pointName1111" + input_name);
                            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.ADD_POSITION));
                        } else {
                            Toast.makeText(mContext, "请输入新的地点名", Toast.LENGTH_SHORT).show();
                        }
                        MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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
            if (mBitmapHeight >= mBitmapWidth) {
                mBitmapWidth = mapRelativeBorder.getHeight() / mBitmapHeight * mBitmapWidth;
                mBitmapHeight = mapRelativeBorder.getHeight();
            } else if (mBitmapHeight > mBitmapWidth) {
                mBitmapHeight = mapRelativeBorder.getWidth() / mBitmapWidth * mBitmapHeight;
                mBitmapWidth = mapRelativeBorder.getWidth();
            }


            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETPOINTPOSITION));


            editMapImage.setImageBitmap(mBitmap);
            mapRelative.setLayoutParams(new RelativeLayout.LayoutParams((int) mBitmapWidth, (int) mBitmapHeight));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            editMapImage.setLayoutParams(layoutParams);
            mapRelative.addView(editMapImage);

        } else if (messageEvent.getState() == 10008) {
            for (int i = 0; i < imageViewArrayList.size(); i++) {
                mapRelative.removeView(imageViewArrayList.get(i));
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
                        Log.d("zdzd222", "" + (editMapImage.getWidth() / Content.list.get(index).getGridWidth() * jsonItem.getDouble(Content.POINT_X)
                                + Content.list.get(index).getOriginX() - (Content.ROBOT_SIZE / Content.list.get(index).getResolution() * Math.cos(jsonItem.getDouble(Content.ANGLE)))));
                        Log.d("zdzd222", "" + (Content.ROBOT_SIZE / Content.list.get(index).getResolution() * Math.cos(jsonItem.getDouble(Content.ANGLE))));
                        Log.d("zdzd222", "      \n");

                        Log.d("zdzd 777", "" + editMapImage.getWidth());
                        Log.d("zdzd 777", "" + Content.list.get(index).getGridWidth());
                        Log.d("zdzd 777", "" + jsonItem.getDouble(Content.POINT_X));
                        Log.d("zdzd 777", "" + Content.list.get(index).getOriginX());
                        Log.d("zdzd 777", "" + (Content.ROBOT_SIZE / Content.list.get(index).getResolution() * Math.cos(jsonItem.getDouble(Content.ANGLE))));
                        Log.d("zdzd222", "      \n");
                        double mapWidth = (double) editMapImage.getWidth();
                        double mapHeight = (double) editMapImage.getHeight();

                        double gridHeight = Content.list.get(index).getGridHeight();
                        double gridWidth = Content.list.get(index).getGridWidth();
                        double layoutW = (double) mapRelative.getWidth();
                        double layoutH = (double) mapRelative.getHeight();
                        Log.d("W H", "" + layoutW / mapWidth + "MW:" + layoutH / mapHeight);
//                        Log.d("zdzd999", "RW:x"+coefficientX+"Y"+coefficientY);
                        double pointX = jsonItem.getDouble(Content.POINT_X);
                        int pointType = jsonItem.getInt(Content.POINT_TYPE);
                        double pointY = jsonItem.getDouble(Content.POINT_Y);
                        double originX = Content.list.get(index).getOriginX();
                        double originY = Content.list.get(index).getOriginY();
                        double resolution = Content.list.get(index).getResolution();
                        double angleY = Math.sin(jsonItem.getDouble(Content.ANGLE));
                        double angleX = Math.cos(jsonItem.getDouble(Content.ANGLE));

                        Log.d("zdzd9998", "gridH" + gridHeight + "        gridW" + gridWidth + "     pointX" + pointX + "       originX" + originX + "       Content.ROBOT_SIZE " + Content.ROBOT_SIZE);
                        Log.d("zdzd9998", " resolution * angleX" + resolution * angleX);

                        if (pointType == 1) {
                            saveChargingBtn.setEnabled(false);
                            wallBtn.setEnabled(true);
                            markBtn.setEnabled(true);
                            saveBtn.setEnabled(true);
                            robot_Img.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX - (Content.ROBOT_SIZE / resolution * angleX))),
                                    (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY) - (Content.ROBOT_SIZE / resolution * angleY))),
                                    0, 0);
                            if (pointType == 2) {
                                imageView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX - (Content.ROBOT_SIZE / resolution * angleX))),
                                        (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY) - (Content.ROBOT_SIZE / resolution * angleY))),
                                        0, 0);
                                Log.d("zdzd9998", "angleX" + angleX);
                                Log.d("zdzd9998", " resolution" + resolution);
                                mapRelative.addView(imageView);
                            }

                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else if (messageEvent.getState() == 19191) {
            String message = (String) messageEvent.getT();
            charging = message;
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
                    Log.d("zdzd999888", "gridH" + gridHeight + "        gridW" + gridWidth + "     pointX" + pointX + "       originX" + originX + "       Content.ROBOT_SIZE " + Content.ROBOT_SIZE);
                    Log.d("zdzd999888", " resolution * angleX" + resolution * angleX);
                    Log.d("zdzd999888", "angleX" + angleX);
                    Log.d("zdzd999888", " resolution" + resolution);
                    mapRelative.addView(robot_Img);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (messageEvent.getState() == 40002) {//获取虚拟墙
            String message = (String) messageEvent.getT();
            double mapWidth = (double) editMapImage.getWidth();
            double mapHeight = (double) editMapImage.getHeight();
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
                    mapRelative.draw(canvas);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.edit_mapname, R.id.add_point_title, R.id.map_relative, R.id.wall_btn, R.id.mark_btn, R.id.save_btn, R.id.back_btn,R.id.save_charging_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edit_mapname:
                break;
            case R.id.add_point_title:
                break;
            case R.id.map_relative:
                break;
            case R.id.wall_btn:
                editMapImage.setOnTouchListener(this);
                break;
            case R.id.mark_btn:
                AddPositionDialog();
                break;
            case R.id.save_btn:
                //存储虚拟墙
                MainActivity.emptyClient.send(gsonUtils.putVirtualWallMsg(Content.UPDATA_VIRTUAL, lists));
                break;
            case R.id.back_btn:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.first_fragment, new MapManagerFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.save_charging_btn:
                Log.d("YYYYY", "save_charging_point");
                if (charging.equals("放电")){
                    Toast.makeText(mContext, "请确认机器人是否连接上充电点", Toast.LENGTH_SHORT).show();
                }else {
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.ADD_POWER_POINT));
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETMAPPIC));
                    MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.GETPOINTPOSITION));
                }
                break;
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        List<DrawLineBean> drawLineBeanList = new ArrayList<>();

        double mapWidth = (double) editMapImage.getWidth();
        double mapHeight = (double) editMapImage.getHeight();
        double gridHeight = Content.list.get(index).getGridHeight();
        double gridWidth = Content.list.get(index).getGridWidth();
        double originX = Content.list.get(index).getOriginX();
        double originY = Content.list.get(index).getOriginY();

        float startX = 0;
        float startY = 0;

        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            DrawLineBean drawLineBean = new DrawLineBean();
            startX = event.getX();
            startY = event.getY();
//            robot_Img.setPaddingRelative((int) (mapHeight - (mapHeight / gridHeight * (pointY - originY) - (Content.ROBOT_SIZE / resolution * angleY))),
//                    (int) (mapWidth / gridWidth * (pointX - originX - (Content.ROBOT_SIZE / resolution * angleX))),
//                    0, 0);
            drawLineBean.setX((mapWidth / (startX * gridWidth)) + originX);
            drawLineBean.setY(mapHeight / (gridHeight * (mapHeight - startY)) + originY);
            drawLineBeanList.add(drawLineBean);
        }

        if (MotionEvent.ACTION_MOVE == event.getAction()) {
            float moveX = event.getX();
            float moveY = event.getY();
            //划线    先清掉之前的线，重新画 连接start点和 move点
            //划线：点连线 连接start和end
            Canvas canvas = new Canvas();
            Paint paint = new Paint();
            paint.setColor(R.color.colorPrimaryDark);
            canvas.drawLine(startX, startY, moveX, moveY, paint);
            mapRelative.draw(canvas);

        }
        if (MotionEvent.ACTION_UP == event.getAction()) {
            DrawLineBean drawLineBean = new DrawLineBean();
            float endX = event.getX();
            float endY = event.getY();
            drawLineBean.setY(mapHeight / (gridHeight * (mapHeight - endY)) + originY);
            drawLineBean.setX((mapWidth / (endX * gridWidth)) + originX);
            drawLineBeanList.add(drawLineBean);
            lists.add(drawLineBeanList);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        for (int i = 0; i < imageViewArrayList.size(); i++) {
            if (view == imageViewArrayList.get(i)) {
                Toast.makeText(mContext, i + "", Toast.LENGTH_SHORT).show();
            }
        }
    }

}