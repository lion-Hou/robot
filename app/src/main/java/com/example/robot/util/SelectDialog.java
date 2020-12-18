//package com.example.robot.util;
//
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.drawable.Drawable;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.example.robot.R;
//
///**
// * 提示对话框(可以自定义布局)
// * 1，两个选项，顶部是问号图标
// * 2，单个选项
// */
//public class SelectDialog extends Dialog {
//
//    public SelectDialog(Context context) {
//        super(context);
//    }
//
//    public SelectDialog(Context context, int theme) {
//        super(context, theme);
//    }
//
//    /**
//     * 建造者类
//     *
//     * @author Administrator
//     */
//    public static class Builder {
//        private Context context;
//        private int topImageId;
//        private String title;
//        private String message;
//        private Drawable drawable = null;
//        private String positiveButtonText;
//        private String negativeButtonText;
//        private View contentView;
//        private OnClickListener positiveButtonClickListener;
//        private OnClickListener negativeButtonClickListener;
//
//        public Builder(Context context) {
//            this.context = context;
//        }
//
//        /**
//         * 设置消息内容
//         *
//         * @param message
//         * @return
//         */
//        public Builder setMessage(String message) {
//            this.message = message;
//            return this;
//        }
//
//        public Builder setDrawable(Drawable drawable_id) {
//            this.drawable = drawable_id;
//            return this;
//        }
//
//
//        public Builder setContentView(View v) {
//            this.contentView = v;
//            return this;
//        }
//
//        /**
//         * 设置LiestView
//         * @param view
//         * @return
//         */
//        public Builder setListView(View view){
//            this.contentView = view;
//            return this;
//
//        }
//
//        /**
//         * 设置ImageView
//         * @param view
//         * @return
//         */
//        public Builder setImageView(View view){
//            this.contentView = view;
//            return this;
//        }
//
//        /**
//         * 设置积极按钮
//         *
//         * @param positiveButtonText
//         * @param listener
//         * @return
//         */
//        public Builder setPositiveButton(String positiveButtonText,
//                                         OnClickListener listener) {
//            this.positiveButtonText = positiveButtonText;
//            this.positiveButtonClickListener = listener;
//            return this;
//        }
//
//        /**
//         * 设置消极按钮
//         *
//         * @param negativeButtonText
//         * @param listener
//         * @return
//         */
//        public Builder setNegativeButton(String negativeButtonText,
//                                         OnClickListener listener) {
//            this.negativeButtonText = negativeButtonText;
//            this.negativeButtonClickListener = listener;
//            return this;
//        }
//
//        /**
//         * 创建一个AlertDialog
//         *
//         * @return
//         */
//        public SelectDialog create() {
//            LayoutInflater inflater = LayoutInflater.from(context);
//
////            final SelectDialog dialog = new SelectDialog(context,
////                    R.style.DialogStyle);
//
//            View layout = null;
//            if (null != contentView) {
//                layout = contentView;
//            } else {
//                layout = inflater.inflate(R.layout.dialog_select, null);
//            }
//
//            // 设置内容
//            TextView messageView = (TextView) layout.findViewById(R.id.message);
//            if (null == message) {
//                messageView.setVisibility(View.GONE);
//            } else {
//                messageView.setText(message);
//            }
//            // 设置积极按钮
//            RelativeLayout sure_layout = (RelativeLayout) layout
//                    .findViewById(R.id.sure_layout);
//            TextView sure_text = (TextView) layout
//                    .findViewById(R.id.sure_text);
//            if (TextUtils.isEmpty(positiveButtonText)
//                    || null == positiveButtonClickListener) {
//                sure_layout.setVisibility(View.GONE);
//            } else {
//                sure_text.setText(positiveButtonText);
//                sure_layout.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
//                        dialog.dismiss();
//                        positiveButtonClickListener.onClick(dialog,
//                                DialogInterface.BUTTON_POSITIVE);
//                    }
//                });
//            }
//
//            // 设置消极按钮
//            RelativeLayout quit_layout = (RelativeLayout) layout
//                    .findViewById(R.id.quit_layout);
//            TextView quit_text = (TextView) layout
//                    .findViewById(R.id.quit_text);
//            if (TextUtils.isEmpty(negativeButtonText)
//                    || null == negativeButtonClickListener) {
//                quit_layout.setVisibility(View.GONE);
//            } else {
//                quit_text.setText(negativeButtonText);
//                quit_layout.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
//                        dialog.dismiss();
//                        negativeButtonClickListener.onClick(dialog,
//                                DialogInterface.BUTTON_NEGATIVE);
//                    }
//                });
//            }
//
//            if (null != positiveButtonText && null == negativeButtonText) {
//                if (null == positiveButtonClickListener) {
//                    sure_layout.setVisibility(View.VISIBLE);
//
//                    sure_layout.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            // TODO Auto-generated method stub
//                            dialog.dismiss();
//                        }
//                    });
//                }
//            }
//
//            // 设置对话框的视图
//            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
//                    LayoutParams.WRAP_CONTENT);
//            dialog.setContentView(layout, params);
//            return dialog;
//        }
//    }
//}
