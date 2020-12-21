package com.example.robot.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.robot.R;


public class SelectDialog extends Dialog {

    public SelectDialog(Context context) {
        super(context);
    }

    public SelectDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private Drawable drawable = null;
        private int topImageId;
        private String positiveButton;
        private String negativeButton;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置ImageView
         * @param id
         * @return
         */
        public Builder setTopImage(int id) {
            this.topImageId = id;
            return this;
        }

        /**
         * 设置LiestView
         * @param view
         * @return
         */
        public Builder setListView(View view){
            this.contentView = view;
            return this;
        }

        /**
         * 设置积极按钮
         * @param positiveButton
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButton,
                                         OnClickListener listener) {
            this.positiveButton = positiveButton;
            this.positiveButtonClickListener = listener;
            return this;
        }

        /**
         * 设置消极按钮
         *
         * @param negativeButton
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButton,
                                         OnClickListener listener) {
            this.negativeButton = negativeButton;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * 创建一个AlertDialog
         * @return
         */

        public AlertDialog create(){

            return null;
        }

    }
}
