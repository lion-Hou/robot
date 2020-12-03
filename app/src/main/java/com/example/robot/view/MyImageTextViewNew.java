package com.example.robot.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MyImageTextViewNew extends LinearLayout {

    private ImageView mImageView = null;
    private TextView mTextView = null;
    private int imageId, pressImageId;
    private int textId, textColorId, textTopId, pressTextColorId;
    private String stringText;

    public MyImageTextViewNew(Context context) {
        this(context, null);
    }

    public MyImageTextViewNew(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageTextViewNew(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(LinearLayout.VERTICAL);//设置垂直排序
        this.setGravity(Gravity.CENTER);//设置居中
        if (mImageView == null) {
            mImageView = new ImageView(context);
        }
        if (mTextView == null) {
            mTextView = new TextView(context);
        }
        if (attrs == null)
            return;
        int count = attrs.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String attrName = attrs.getAttributeName(i);//获取属性名称
//根据属性获取资源ID
            switch (attrName) {
//显示的图片
                case "image":
                    imageId = attrs.getAttributeResourceValue(i, 0);
                    break;
//按下时显示的图片
                case "pressImage":
                    pressImageId = attrs.getAttributeResourceValue(i, 0);
                    break;
//显示的文字
                case "text":
                    textId = attrs.getAttributeResourceValue(i, 0);
                    break;
//设置文字颜色
                case "textColor":
                    textColorId = attrs.getAttributeResourceValue(i, 0);
                    break;
//设置文字距离上面图片的距离
                case "textTop":
                    textTopId = attrs.getAttributeResourceValue(i, 0);
                    break;
//按下时显示的文字颜色
                case "pressTextColor":
                    pressTextColorId = attrs.getAttributeResourceValue(i, 0);
                    break;

            }
        }
        init();

    }

    /**
     * 初始化状态
     */
    private void init() {
        this.setText(stringText);
        mTextView.setGravity(Gravity.CENTER);//字体居中
        this.setTextColor(textColorId);
        this.setTextPaddingTop(textTopId);
        this.setImgResource(imageId);
        addView(mImageView);//将图片加入
        addView(mTextView);//将文字加入
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
//按下
            case MotionEvent.ACTION_DOWN:
                if (pressImageId != 0)
                    this.setImgResource(pressImageId);
                if (pressTextColorId != 0)
                    this.setTextColor(pressTextColorId);
                break;
//移动
            case MotionEvent.ACTION_MOVE:
                break;
//抬起
            case MotionEvent.ACTION_UP:
                if (imageId != 0)
                    this.setImgResource(imageId);
                if (textColorId != 0)
                    this.setTextColor(textColorId);
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置默认的图片
     *
     * @param resourceID 图片id
     */
    public void setImgResourceDefault(int resourceID) {
        imageId = resourceID;
        setImgResource(resourceID);
    }

    /**
     * 设置按下的图片
     *
     * @param resourceID 图片id
     */
    public void setImgResourcePress(int resourceID) {
        pressImageId = resourceID;
    }


    /**
     * 设置显示的图片
     *
     * @param resourceID 图片ID
     */
    private void setImgResource(int resourceID) {
        if (resourceID == 0) {
            this.mImageView.setImageResource(0);
        } else {
            this.mImageView.setImageResource(resourceID);
        }
    }


    /**
     * 设置显示的文字
     *
     * @param text
     */
    public void setText(String text) {
        this.stringText = stringText;
        this.mTextView.setText(text);
    }

    /**
     * 设置字体颜色(默认为黑色)
     *
     * @param color
     */
    private void setTextColor(int color) {
        if (color == 0) {
            this.mTextView.setTextColor(Color.BLACK);
        } else {
            this.mTextView.setTextColor(getResources().getColor(color));
        }
    }

    /**
     * 设置默认的颜色
     *
     * @param color 颜色ID
     */
    public void setTextDefaultColor(int color) {
        textColorId = color;
        setTextColor(color);
    }

    /**
     * 设置按下的颜色
     *
     * @param color 颜色ID
     */
    public void setTextPressColor(int color) {
        pressImageId = color;
    }

    /**
     * 设置字体大小
     *
     * @param size
     */
    public void setTextSize(float size) {
        this.mTextView.setTextSize(size);
    }


    /**
     * 设置文字与上面的距离
     *
     * @param top
     */
    public void setTextPaddingTop(int top) {
        if (top != 0)
            this.mTextView.setPadding(0, getResources().getDimensionPixelOffset(top), 0, 0);
    }


}
