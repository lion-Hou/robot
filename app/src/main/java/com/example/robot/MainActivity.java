package com.example.robot;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.robot.bean.RobotMapBean;
import com.example.robot.content.Content;
import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.map.AddNewMapFragment;
import com.example.robot.map.FirstFragment;
import com.example.robot.map.MapManagerFragment;
import com.example.robot.map.SecoundFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.WebSocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.List;

import butterknife.ButterKnife;


public class MainActivity extends FragmentActivity {

    public static String TAG="MainActivity";
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;
    private FirstFragment firstFragment;
    private SecoundFragment secoundFragment;
    private MapManagerFragment mapManagerFragment;


    private ProgressDialog waitingDialog;
    private AlertDialog.Builder disConnectDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        gsonUtils = new GsonUtils();
        connect();

        firstFragment = new FirstFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.first_fragment,firstFragment).commit();
        secoundFragment  = new SecoundFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.second_fragment,secoundFragment).commit();
        mapManagerFragment = new MapManagerFragment();

        waitingDialog = new ProgressDialog(MainActivity.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void connect(){
        try {
            if(emptyClient == null){
                emptyClient = new EmptyClient(new URI("ws://10.7.5.176:8887"));
                emptyClient.connect();
                Log.d(TAG,"开始连接");
            }else {
                emptyClient.reconnect();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMsg(EventBusMessage messageEvent) {
        Log.d(TAG, "onEventMsg ： " + messageEvent.getT());
            if (messageEvent.getState() == 10005) {
                Log.d(TAG, "onEventMsg ： " + "3");
                //firstFragment.refesh((String) messageEvent.getT());
            }else if (messageEvent.getState() == 10006) {
                String type = (String) messageEvent.getT();
                System.out.println("type:" + type);

            }else if (messageEvent.getState() == 10001){
                Log.d(TAG, "图片 ： " + messageEvent.getT());

            }else if (messageEvent.getState() == 11111){
                Log.d(TAG, "connect state：connect " + messageEvent.getT());
                waitingDialog.dismiss();

            }else if (messageEvent.getState() == 11119){
                Log.d(TAG, "connect state：connect " + messageEvent.getT());
                showDisconnectDialog();
        }
    }


    //Dialog
    private void showDisconnectDialog(){
        disConnectDialog = new AlertDialog.Builder(MainActivity.this);
        disConnectDialog.setTitle("连接中断");
        disConnectDialog.setMessage("请确保网络连通并点击重新连接");
        disConnectDialog.setCancelable(false);
        disConnectDialog.setPositiveButton("退出",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        finish();
                    }
                });
        disConnectDialog.setNegativeButton("重新连接",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        showWaitingDialog();
                        connect();
                    }
                });
        // 显示
        disConnectDialog.show();
    }


    private void showWaitingDialog() {
        /* 等待Dialog具有屏蔽其他控件的交互能力
         * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
         * 下载等事件完成后，主动调用函数关闭该Dialog
         */
        waitingDialog.setTitle("连接中");
        waitingDialog.setMessage("等待中...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
    }


}


