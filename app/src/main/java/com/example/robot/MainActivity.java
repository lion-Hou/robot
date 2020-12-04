package com.example.robot;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.map.FirstFragment;
import com.example.robot.map.MapManagerFragment;
import com.example.robot.map.SecoundFragment;
import com.example.robot.util.NormalDialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URI;
import java.net.URISyntaxException;

import butterknife.ButterKnife;


public class MainActivity extends FragmentActivity {

    public static String TAG="MainActivity";
    public static EmptyClient emptyClient;
    private GsonUtils gsonUtils;
    private FirstFragment firstFragment;
    private SecoundFragment secoundFragment;
    private MapManagerFragment mapManagerFragment;


    private NormalDialogUtil disconnectDialog;

    private ProgressDialog waitingDialog;



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

            }else if (messageEvent.getState() == 11111) {
                Log.d(TAG, "connect state：connect 1111" + messageEvent.getT());
                waitingDialog.dismiss();
                if (disconnectDialog!=null){
                    disconnectDialog.dismiss();
                }
            }else if (messageEvent.getState() == 11110){
                    Log.d(TAG, "connect state：connect 11110" + messageEvent.getT());
                    waitingDialog.dismiss();
                    showDisconnectDialog();
            }else if (messageEvent.getState() == 11119){
                Log.d(TAG, "connect state：connect 11119" + messageEvent.getT());
                showDisconnectDialog();
        }
    }


    //Dialog
    private void showDisconnectDialog(){
        disconnectDialog = new NormalDialogUtil();
        disconnectDialog.showDialog(this, "网络错误","网络连接错误，请确认当前网络状态","退出","重新连接" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消
                finish();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //确定逻辑
                showWaitingDialog();


                emptyClient.reconnect();
                disconnectDialog.dismiss();
            }
        });
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
        };

}





