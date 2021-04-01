package com.example.robot.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.adapter.DialogAdapter;
import com.example.robot.adapter.MapAdapter;
import com.example.robot.bean.DialogCheckboxBean;
import com.example.robot.bean.DrawLineBean;
import com.example.robot.content.Content;
import com.example.robot.map.FirstFragment;
import com.example.robot.map.MapManagerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SelectDialogUtil {

    private Context context;
    private Dialogcallback dialogcallback;
    private ListViewcallback listViewcallback;
    private Dialog dialog;
    private TextView sure_text;
    private TextView quit_text;
    private ListView listView;
    private TextView dialog_title;
    private ImageView manager_mapImage;
    private double mBitmapHeight;
    private double mBitmapWidth;
    private RelativeLayout map_manage_relative_border;
    private RelativeLayout map_manage_relative;
    private int index;
    private List<View> imageViewArrayList = new ArrayList<>();
    private String mapName;
    private List<String> myTaskNameList = new ArrayList<>();
    private CheckBoxCallback checkBoxCallback;
    private String selectTask;
    private List<DialogCheckboxBean> dialogCheckboxBeanList;
    private DialogAdapter dialogAdapter;


    public byte[] getMapByte() {
        return mapByte;
    }

    public void setMapByte(byte[] mapByte) {
        this.mapByte = mapByte;
        loadBitmapSize(mapByte);
    }

    private byte mapByte[];

    private Bitmap mBitmap;

    /**
     * init the dialog
     *
     * @return
     */
    public SelectDialogUtil(Context con, int layout) {
        this.context = con;
        dialog = new Dialog(context, R.style.dialog);
        String sureText = "";
        String quitText = "";
        dialog.setContentView(layout);
        listView = (ListView) dialog.findViewById(R.id.mylist);
        sure_text = (TextView) dialog.findViewById(R.id.sure_text);
        quit_text = (TextView) dialog.findViewById(R.id.quit_text);
        dialog_title = dialog.findViewById(R.id.dialog_title);
        manager_mapImage = dialog.findViewById(R.id.dialog_mapImage);
        map_manage_relative_border = dialog.findViewById(R.id.map_dialog_relative_border);
        map_manage_relative = dialog.findViewById(R.id.map_dialog_relative);
        switch (layout) {
            case R.layout.dialog_select:
                sureText = "true";
                quitText = "false";
                break;
            case R.layout.dialog_select_task:
                sureText = "Tasktrue";
                quitText = "Taskfalse";
                break;
            default:
                break;
        }
        String finalSureText = sureText;
        sure_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("true".equals(finalSureText)) {
                    dialogcallback.dialogdo(finalSureText);
                } else if ("Tasktrue".equals(finalSureText)) {
                    selectTask = "";
                    for (int i = 0; i < myTaskNameList.size(); i++) {
                        selectTask = selectTask + myTaskNameList.get(i) + "  ";
                    }
                    checkBoxCallback.checkBoxdo(selectTask, myTaskNameList);
                }
                dismiss();
            }
        });
        String finalQuitText = quitText;
        quit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialogcallback.dialogdo(finalQuitText);
                dismiss();
            }
        });

    }

    /**
     * 设定一个interfack接口,使mydialog可以處理activity定義的事情
     *
     * @author sfshine
     */
    public interface Dialogcallback {
        public void dialogdo(String string);
    }

    public void setDialogCallback(Dialogcallback dialogcallback) {
        this.dialogcallback = dialogcallback;
    }

    /**
     * @category Set The Content of the TextView
     */
    public void setContent(String content) {
        dialog_title.setText(content);
    }

    /**
     * Get the Text of the EditText
     */
    public String getText() {
        return dialog_title.getText().toString();
    }

    public void show() {
        dialog.show();
    }

    public void hide() {
        dialog.hide();
    }

    public void dismiss() {
        dialog.dismiss();
    }

    /**
     * //         * 设置ImageView
     * //         * @param bitmap
     * //         * @return
     * //
     */

    private void loadBitmapSize(byte[] bytes) {

        mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();

        System.out.println("SourireG SSSS1: " + "  h:" + mBitmap.getHeight() + "  w:" + mBitmap.getWidth());
        System.out.println("SourireG SSSS1: " + "  h:" + mBitmapHeight + "  w:" + mBitmapWidth);
        System.out.println("SourireG SSSS2: " + "  h:" + map_manage_relative_border.getHeight() + "  w:" + map_manage_relative_border.getWidth());
        if ((mBitmapHeight / map_manage_relative_border.getHeight()) >= (mBitmapWidth / map_manage_relative_border.getWidth())) {
            mBitmapWidth = map_manage_relative_border.getHeight() / mBitmapHeight * mBitmapWidth;
            mBitmapHeight = map_manage_relative_border.getHeight();
        } else {
            mBitmapHeight = map_manage_relative_border.getWidth() / mBitmapWidth * mBitmapHeight;
            mBitmapWidth = map_manage_relative_border.getWidth();
        }
        System.out.println("SourireG SSSS3: " + "  h:" + mBitmapHeight + "  w:" + mBitmapWidth);

        map_manage_relative.removeAllViews();
        manager_mapImage.setImageBitmap(mBitmap);
        map_manage_relative.setLayoutParams(new RelativeLayout.LayoutParams((int) mBitmapWidth, (int) mBitmapHeight));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        manager_mapImage.setLayoutParams(layoutParams);


        int mBitmapHeight1 = manager_mapImage.getHeight();
        int mBitmapWidth1 = manager_mapImage.getWidth();

        int mBitmapHeight2 = map_manage_relative.getHeight();
        int mBitmapWidth2 = map_manage_relative.getWidth();


        System.out.println("SourireG SSSS4: " + "  h:" + mBitmapHeight2 + "  w:" + mBitmapWidth2);
        System.out.println("SourireG SSSS5: " + "  h:" + mBitmapHeight1 + "  w:" + mBitmapWidth1);
        ViewGroup parent = (ViewGroup) manager_mapImage.getParent();
        if (parent != null) {
            parent.removeView(manager_mapImage);
        }

        map_manage_relative.addView(manager_mapImage);


    }

    public void setStrings(ArrayList<String> strings) {
        mapName = strings.get(0);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, strings);
        MapAdapter mapAdapter = new MapAdapter(context);
        mapAdapter.refeshList(strings);
        listView.setAdapter(mapAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewcallback.ListViewClick(position, strings.get(position), strings.get(position), "");
                mapName = strings.get(position);
            }
        });

    }
    
    public void setMapString(String string) {
        mapName = string;
    }


    /**
     * 设定一个interfack接口,使mydialog可以處理activity定義的事情
     *
     * @author sfshine
     */
    public interface ListViewcallback {
        void ListViewClick(int position, String mapName, String taskName, String pointName);
    }

    public void setListViewCallback(ListViewcallback listViewCallback) {
        this.listViewcallback = listViewCallback;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setPointIcon(String message) {
        drawPoint(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void drawPoint(String message) {
        for (int i = 0; i < imageViewArrayList.size(); i++) {
            map_manage_relative.removeView(imageViewArrayList.get(i));
        }
        try {
            JSONObject jsonObject = new JSONObject(message);
            imageViewArrayList.clear();
            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray(Content.SENDPOINTPOSITION);
                Log.d("zdzd000 ", "pointName : " + jsonArray.toString());
                for (int k = 0; k < Content.list.size(); k++) {
                    if (Content.list.get(k).getMap_Name().equals(mapName)) {
                        Log.d("zdzd555", "" + Content.list.get(k).getResolution());
                        index = k;
                    }
                }
                Log.d("zdzd5111", "" + index);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    Log.d("zdzd111 ", "pointName : " + jsonItem.toString());
                    ImageView imageView = new ImageView(context);
                    TextView textView = new TextView(context);
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

                    Log.d("zdzd9998qwe", "gridH" + gridHeight + "        gridW" + gridWidth + "     pointX" + pointX + "    pointy" + pointY + "   originX" + originX);
                    Log.d("zdzd9998", " resolution * angleX" + resolution * angleX);

                    if (pointType == 1) {
                        ImageView charging_Img = new ImageView(context);
                        charging_Img.setImageResource(R.drawable.charging);
                        imageViewArrayList.add(charging_Img);
                        charging_Img.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY))),
                                0, 0);
                        map_manage_relative.addView(charging_Img);
                    }
                    if (pointType == 2) {
                        Log.d("Sourire1", "gridH" + gridHeight + "        gridW" + gridWidth + "     pointX" + pointX + "    pointy" + pointY + "   originX" + originX);
                        Log.d("SourireG", "width:" + mBitmapWidth + "height:" + mBitmapHeight);
                        imageView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY))),
                                0, 0);
                        textView.setText(pointName);
                        textView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY)) + 1),
                                0, 0);

                        map_manage_relative.addView(imageView);
                        map_manage_relative.addView(textView);
                        imageViewArrayList.add(imageView);
                        imageViewArrayList.add(imageView);
                        imageViewArrayList.add(textView);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void taskDrawPoint(String message) {
        for (int i = 0; i < imageViewArrayList.size(); i++) {
            map_manage_relative.removeView(imageViewArrayList.get(i));
        }
        try {
            JSONObject jsonObject = new JSONObject(message);
            imageViewArrayList.clear();
            if (jsonObject != null) {
                String type = jsonObject.getString("type");
                if (TextUtils.isEmpty(type)){
                    return;
                }
                JSONArray jsonArray = jsonObject.getJSONArray(type);
                Log.d("zdzd000 ", "pointName : " + jsonArray.toString());
                for (int k = 0; k < Content.list.size(); k++) {
                    if (Content.list.get(k).getMap_Name().equals(mapName)) {
                        Log.d("zdzd555", "" + Content.list.get(k).getResolution());
                        index = k;
                    }
                }
                Log.d("zdzd5111", "" + index);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonItem = jsonArray.getJSONObject(i);
                    Log.d("zdzd111 ", "pointName : " + jsonItem.toString());
                    ImageView imageView = new ImageView(context);
                    TextView textView = new TextView(context);
                    imageView.setImageResource(R.drawable.ic_point);

                    double gridHeight = Content.list.get(index).getGridHeight();
                    double gridWidth = Content.list.get(index).getGridWidth();
                        String pointName = jsonItem.getString(Content.dbPointName);
                        double pointX = jsonItem.getDouble(Content.dbPointX);
                        double pointY = jsonItem.getDouble(Content.dbPointY);

                    Log.d("zdzd9998qwe", "gridH" + gridHeight + "        gridW" + gridWidth + "     pointX" + pointX + "    pointy" + pointY  );


                        Log.d("Sourire1", "gridH" + gridHeight + "        gridW" + gridWidth + "     pointX" + pointX + "    pointy" + pointY  );
                        Log.d("SourireG", "width:" + mBitmapWidth + "height:" + mBitmapHeight);
                        imageView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY))),
                                0, 0);
                        textView.setText(pointName);
                        textView.setPaddingRelative((int) (mBitmapWidth / gridWidth * (pointX)),
                                (int) (mBitmapHeight - (mBitmapHeight / gridHeight * (pointY)) + 1),
                                0, 0);

                        map_manage_relative.addView(imageView);
                        map_manage_relative.addView(textView);
                        imageViewArrayList.add(imageView);
                        imageViewArrayList.add(imageView);
                        imageViewArrayList.add(textView);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setTaskPointIcon(String message) {
        taskDrawPoint(message);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void drawWall(String message) {
        //获取虚拟墙
        for (int k = 0; k < Content.list.size(); k++) {
            if (Content.list.get(k).getMap_Name().equals(Content.map_Name)) {
                Log.d("zdzd555", "" + Content.list.get(k).getResolution());
                index = k;
            }
        }
        Log.i("Henly", "mapManager,update VirtualWall" + index);
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

                float[] point = {startX, startY, endX, endY};
                Log.i("Henly", "mapManager,update VirtualWall,k = " + k);
                pointlist.add(point);

                k = k + 2;
            }

            if (pointlist.size() != 0) {
                Log.i("Henly", "updateVW,gridWidth = " + gridWidth + ",gridHeight = " + gridHeight);
                updateVirtualWall(pointlist);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    void updateVirtualWall(ArrayList<float[]> pointlist) {
        Log.i("Henly", "mapmanager-updateVirtualWall");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        // layoutParams.height = editMapImage.getHeight();
        // layoutParams.width = editMapImage.getWidth();
        //  layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        //   layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        SelectDialogUtil.DrawlineFromVW bDrawlVW = new SelectDialogUtil.DrawlineFromVW(context, pointlist);
        bDrawlVW.setLayoutParams(layoutParams);
        map_manage_relative.addView(bDrawlVW);
    }


    class DrawlineFromVW extends View {
        private float start_x, start_y;//声明起点坐标
        private float mov_x, mov_y;//滑动轨迹坐标
        private Paint paint;//声明画笔
        private Canvas canvas;//画布
        private Bitmap bitmap;//位图
        private float view_X, view_Y;

        private double scale_x = 1;
        private double scale_y = 1;

        private ArrayList<float[]> Pointlist = new ArrayList<>();//保存所画线的坐标

        public DrawlineFromVW(Context context, ArrayList<float[]> pointlist) {
            super(context);
            paint = new Paint(Paint.DITHER_FLAG);//创建一个画笔

            bitmap = Bitmap.createBitmap((int) mBitmapWidth, (int) mBitmapHeight, mBitmap == null ? Bitmap.Config.ARGB_8888 : mBitmap.getConfig());
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            canvas = new Canvas();
            canvas.setBitmap(bitmap);

            paint.setStyle(Paint.Style.STROKE);//设置非填充
            paint.setStrokeWidth(3);//笔宽3像素
            paint.setColor(Color.RED);//设置为红笔
            paint.setAntiAlias(true);//锯齿不显示

            Pointlist = pointlist;
            invalidate();
        }

        //画位图
        @Override
        protected void onDraw(Canvas canvas) {
            //super.onDraw(canvas);
            canvas.drawBitmap(bitmap, 0, 0, null);
            Log.i("Henly", "mapmanager-updateVW,onDraw,mBitmapWidth = " + mBitmapWidth + ",mBitmapHeight = " + mBitmapHeight);
            for (int i = 0; i < Pointlist.size(); i++) {
                float[] point = Pointlist.get(i);
                Log.i("Henly", "mapmanager-updateVW,drawline,start_x:" + point[0] + ",start_y:" + point[1] + ", end_x:" + point[2] + " ,end_y:" + point[3]);
                canvas.drawLine(point[0], point[1], point[2], point[3], paint);//画保存的线
            }
        }
    }

    public void setCheckboxList(String[] taskNameList) {
        if (dialogCheckboxBeanList != null) {
            dialogCheckboxBeanList.clear();
            dialogCheckboxBeanList = null;
        }
        dialogCheckboxBeanList = new ArrayList<>();
        if (taskNameList.length == 0) {
            Log.d("hhhhh", "hhhhhjjj");
            Toast toast = Toast.makeText(context, "当前地图没有任务", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            boolean[] booleans = new boolean[taskNameList.length];
            for (int i = 0; i < taskNameList.length; i++) {
                DialogCheckboxBean dialogCheckboxBean = new DialogCheckboxBean();
                dialogCheckboxBean.setTaskName(taskNameList[i]);
                dialogCheckboxBean.setClick(false);
                dialogCheckboxBeanList.add(dialogCheckboxBean);
            }
            Log.d("tasklistlog", "tasklist");
            myTaskNameList.clear();
            dialogAdapter = new DialogAdapter(context);
            dialogAdapter.refeshList(dialogCheckboxBeanList);
            listView.setAdapter(dialogAdapter);
            dialogAdapter.setAdapterCallback(adapterCallback);
        }
    }

    DialogAdapter.AdapterCallback adapterCallback = new DialogAdapter.AdapterCallback() {
        @Override
        public void adapterCallback(int position,boolean isChecked, List<String> stringList,String pointName) {
            Log.d("dialogadapter : ", "" + position + ",   " + Content.first_map_Name+"   ,   " + dialogCheckboxBeanList.size());
            listViewcallback.ListViewClick(position, Content.first_map_Name, dialogCheckboxBeanList.get(position).getTaskName(), pointName);
            myTaskNameList = stringList;

        }
    };

    public interface CheckBoxCallback {
        public void checkBoxdo(String string, List<String> myTaskNameList);
    }

    public void setCheckBCallback(CheckBoxCallback checkBoxCallback) {
        this.checkBoxCallback = checkBoxCallback;
    }

}
