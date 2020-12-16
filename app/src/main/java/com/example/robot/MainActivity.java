package com.example.robot;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.example.robot.content.EventBusMessage;
import com.example.robot.content.GsonUtils;
import com.example.robot.map.FirstFragment;
import com.example.robot.map.MainFragment;
import com.example.robot.map.MapManagerFragment;
import com.example.robot.map.SecoundFragment;
import com.example.robot.util.NormalDialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static String TAG = "MainActivity";
    public static EmptyClient emptyClient;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.net_time)
    TextView net_Time;
    private GsonUtils gsonUtils;
    private FirstFragment firstFragment;
    private SecoundFragment secoundFragment;
    private MainFragment mainFragment;
    private MapManagerFragment mapManagerFragment;


    private NormalDialogUtil disconnectDialog;
    private static final int msgKey = 1;
    private ProgressDialog waitingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        hideBottomUIMenu();
        ButterKnife.bind(this);
        gsonUtils = new GsonUtils();
        connect();
        disconnectDialog = new NormalDialogUtil();

        firstFragment = new FirstFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.first_fragment, firstFragment).commit();
        secoundFragment = new SecoundFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.second_fragment, secoundFragment).commit();
        mapManagerFragment = new MapManagerFragment();

        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, mainFragment).commit();

        waitingDialog = new ProgressDialog(MainActivity.this);
        new TimeThread().start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void connect() {
        try {
            if (emptyClient == null) {
                emptyClient = new EmptyClient(new URI("ws://10.7.5.176:8887"));
                emptyClient.connect();
                Log.d(TAG, "开始连接");
            } else {
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
        } else if (messageEvent.getState() == 10006) {
            String type = (String) messageEvent.getT();
            System.out.println("type:" + type);

        } else if (messageEvent.getState() == 10001) {
            Log.d(TAG, "图片 ： " + messageEvent.getT());

        } else if (messageEvent.getState() == 11111) {
            Log.d(TAG, "connect state：connect 1111" + messageEvent.getT());
            waitingDialog.dismiss();
            showDisconnectDialog();
            disconnectDialog.dismiss();
        } else if (messageEvent.getState() == 11110) {
            Log.d(TAG, "connect state：connect 11110" + messageEvent.getT());
            waitingDialog.dismiss();
            showDisconnectDialog();
        } else if (messageEvent.getState() == 11119) {
            Log.d(TAG, "connect state：connect 11119" + messageEvent.getT());
            showDisconnectDialog();
        } else if (messageEvent.getState() == 40003) {
            String batter = (String) messageEvent.getT();
            time.setText(batter);
        } else if (messageEvent.getState() == 19191) {
            String message = (String) messageEvent.getT();
            if(message.contains("701")){
                Toast.makeText(this, "检测到机器人附近存在障碍物", Toast.LENGTH_SHORT).show();
            }

//            if (!"充电".equals(message) && !"放电".equals(message)) {
//                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//            }
        }
    }


    //Dialog
    private void showDisconnectDialog() {
        disconnectDialog.showDialog(this, "网络错误", "网络连接错误，请确认当前网络状态", "退出", "重新连接", new DialogInterface.OnClickListener() {
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

    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = DateFormat
                            .format("hh:mm", sysTime);
                    net_Time.setText(sysTimeStr);
                    break;
                default:
                    break;
            }
        }
    };


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

    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {

            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
            window.setAttributes(params);
        }
    }

    @Override
    public void onClick(View v) {

    }
}





