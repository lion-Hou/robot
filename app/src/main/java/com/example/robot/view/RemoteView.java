package com.example.robot.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.robot.EmptyClient;
import com.example.robot.MainActivity;
import com.example.robot.R;
import com.example.robot.content.Content;
import com.example.robot.content.GsonUtils;

/**
 * 摇杆视图
 */
public class RemoteView extends View {
    Paint backPaint = new Paint();//背景画笔
    Paint bubblePaint = new Paint();//气泡画笔
    Paint rectfPaint = new Paint();
    private GsonUtils gsonUtils  = new GsonUtils();
    private boolean isUp = false;
    private boolean isDown = false;
    private boolean isLeft = false;
    private boolean isRight = false;

    public static EmptyClient emptyClient;
    /**
     * 气泡的位置
     */
    float bubbleX = 333/2, bubbleY = 333/2;
    /**
     * 背景圆的位置
     */
    float backX = 333/2, backY = 333/2;
    /**
     * 气泡和背景的半径
     */
    int radiusBack = 120, radiusBubble = 50;

    RectF mRectF = new RectF(backX-radiusBack,backY-radiusBack,backX+radiusBack,backY+radiusBack);

    Context mContext;

    /**
     * STOP  停止
     * RETURN 后退
     * LEFT 左转
     * RIGHT 右转
     * GO 前进
     * 默认为停止
     */
    //String orientation="STOP";
    String orientation;
    public RemoteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        View view = findViewById(R.id.remote_view);
        float x = view.getWidth();
        float y = view.getHeight();

        Log.d("ssss","onEventMsg setting： " + x + "voiceLevel" + y);

        canvas.drawCircle(backX, backY, radiusBack, backPaint);

        if (orientation=="GO") {
            canvas.drawArc(mRectF, -45, -90, true, rectfPaint);

            if (isUp == false){
                Log.d("ssss","前进");
            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STARTUP));
            isUp = true;
            }
            if (isRight==true){
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPRIGHT));
                isRight = false;
            }
            if (isDown==true){
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPDOWN));
                isDown = false;
            }
            if (isLeft==true){
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPLEFT));
                isLeft = false;
            }
        }else if (orientation=="RETURN"){
            canvas.drawArc(mRectF, 45, 90, true, rectfPaint);

            if (isDown == false) {
                Log.d("ssss","后退");
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STARTDOWN));
                isDown = true;
            }
            if (isUp==true){
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPUP));
                isUp = false;
            }
            if (isRight==true){
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPRIGHT));
                isRight = false;
            }
            if (isLeft==true){
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPLEFT));
                isLeft = false;
            }
        }else if (orientation=="LEFT"){
            canvas.drawArc(mRectF, 135, 90, true, rectfPaint);

            if (isLeft == false){
                Log.d("ssss","左转");
            MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STARTLEFT));
            isLeft = true;
            }
            if (isUp==true){
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPUP));
                isUp = false;
            }
            if (isDown==true){
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPDOWN));
                isDown = false;
            }
            if (isRight==true){
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPRIGHT));
                isRight = false;
            }
        }else if (orientation=="RIGHT"){
            canvas.drawArc(mRectF, -45, 90, true, rectfPaint);

            if (isRight == false) {
                Log.d("ssss","右转");
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STARTRIGHT));
                isRight = true;
            }
            if (isUp==true){
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPUP));
                isUp = false;
            }
            if (isDown==true){
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPDOWN));
                isDown = false;
            }
            if (isLeft==true){
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPLEFT));
                isLeft = false;
            }

        }else if (orientation=="STOP"){
            Log.d("ssss","停止");
            if (isUp==true){
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPUP));
                isUp = false;
            }
            if (isLeft==true) {
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPLEFT));
                isLeft = false;
            }
            if (isRight==true) {
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPRIGHT));
                isRight = false;
            }
            if (isDown==true) {
                MainActivity.emptyClient.send(gsonUtils.putJsonMessage(Content.STOPDOWN));
                isDown = false;
            }

            rectfPaint.setAlpha(0);
            canvas.drawArc(mRectF, -90, 360, true, rectfPaint);
        }

        canvas.drawCircle(bubbleX, bubbleY, radiusBubble, bubblePaint);

    }

    private void initPaint() {
        backPaint.setAntiAlias(true);
        backPaint.setColor(Color.parseColor("#F6D8CE"));

        bubblePaint.setAntiAlias(true);
        bubblePaint.setColor(Color.parseColor("#F5D0A9"));

        rectfPaint.setAntiAlias(true);
        rectfPaint.setColor(Color.parseColor("#ffffff"));
        rectfPaint.setAlpha(144);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float x = (int) event.getX();
                float y = (int) event.getY();

                if (getDistance(x, y, backX, backY) < radiusBack) {
                    bubbleX = x;
                    bubbleY = y;
                } else if (getDistance(x, y, backX, backY) >= radiusBack) {
                    float xAndy[];
                    xAndy = getXY(x, y, backX, backY, getDistance(x, y, backX, backY));
                    bubbleX = xAndy[0];
                    bubbleY = xAndy[1];
                    getOrientation(x,y);
                }
                break;
            case MotionEvent.ACTION_UP:
                bubbleX = backX;
                bubbleY = backY;
                orientation="STOP";
                break;
        }
        invalidate();

        return true;
    }

    /**
     * 得到手指触控点与圆点中心的距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private float getDistance(float x1, float y1, float x2, float y2) {
        float dis;
        dis = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        return dis;
    }

    /**
     * 当手指触控点在大圆外面时
     * 需要重新得到气泡的位置
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param dis
     * @return
     */
    private float[] getXY(float x1, float y1, float x2, float y2, float dis) {
        float[] xAndy = new float[2];
        float scaleDis;
        float xDis;
        float yDis;


        /**
         * 表示在第一象限之内
         */
        if (x1 > x2 && y1 < y2) {
            scaleDis = radiusBack / dis;
            xDis = Math.abs(x1 - x2);
            yDis = Math.abs(y1 - y2);
            xAndy[0] = x2 + xDis * scaleDis;
            xAndy[1] = y2 - yDis * scaleDis;

        }
        /**
         * 表示在第二象限之内
         */
        else if (x1 < x2 && y1 < y2) {
            scaleDis = radiusBack / dis;
            xDis = Math.abs(x1 - x2);
            yDis = Math.abs(y1 - y2);
            xAndy[0] = x2 - xDis * scaleDis;
            xAndy[1] = y2 - yDis * scaleDis;
        }
        /**
         *表示在第三象限之内
         */
        else if (x1 < x2 && y1 > y2) {
            scaleDis = radiusBack / dis;
            xDis = Math.abs(x1 - x2);
            yDis = Math.abs(y1 - y2);
            xAndy[0] = x2 - xDis * scaleDis;
            xAndy[1] = y2 + yDis * scaleDis;
        }

        /**
         * 表示在第四象限之内
         */
        else if (x1 > x2 && y1 > y2) {
            scaleDis = radiusBack / dis;
            xDis = Math.abs(x1 - x2);
            yDis = Math.abs(y1 - y2);
            xAndy[0] = x2 + xDis * scaleDis;
            xAndy[1] = y2 + yDis * scaleDis;
        }

        /**
         * 角度为零度
         */
        else if (x1 > x2 && y1 == y2) {
            xAndy[0] = x2 + radiusBack;
            xAndy[1] = y2;
        }

        /**
         * 角度为90度
         */
        else if (x1 == x2 && y1 < y2) {
            xAndy[0] = x2;
            xAndy[1] = y2 - radiusBack;
        }

        /**
         * 角度为180度
         */
        else if (x1 < x2 && y1 == y2) {
            xAndy[0] = x2 - radiusBack;
            xAndy[1] = y2;
        }

        /**
         * 表示为270度
         */
        else if (x1 == x2 && y1 > y2) {
            xAndy[0] = x2;
            xAndy[1] = y2 + radiusBack;
        }
        return xAndy;
    }
    /**
     * 更具摇杆操作的方向来控制小车的运动方向
     */
    private void getOrientation(float x,float y){
        if (y<backY&&(x<backX+backX*0.707&&x>backY-backY*0.707)){
            orientation = "GO";
        }else if (x>backX&&(y<backY+backY*0.707&&y>backY-backY*0.707)){
            orientation="RIGHT";
        }else if (y>backY&&(x<backX+backX*0.707&&x>backY-backY*0.707)){
            orientation="RETURN";
        }else if (x<backX&&(y<backY+backY*0.707&&y>backY-backY*0.707)){
            orientation="LEFT";
        }else {
            orientation="STOP";
        }

    }


}
