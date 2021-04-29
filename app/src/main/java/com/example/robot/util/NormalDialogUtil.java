package com.example.robot.util;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.robot.R;

public class NormalDialogUtil {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    public void dismiss(){
        dialog.dismiss();
    }
    
     public boolean isDialogShow(){
        if (dialog != null) {
            if (dialog.isShowing()) {
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }
    public synchronized AlertDialog showDialog(Context context, String title, String content,
                                                      String btnCancleText, String btnSureText, DialogInterface.OnClickListener cancleListener,
                                                      DialogInterface.OnClickListener sureListener) {
        builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        // dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
        dialog.setCanceledOnTouchOutside(false);
        //dialog弹出后会点击屏幕或物理返回键，dialog不消失
        dialog.setCancelable(false);

        View view = View.inflate(context, R.layout.dialog_normal, null);
        //标题
        TextView tvTitle = view.findViewById(R.id.tv_alert_title);
        //内容
        TextView tvContent = view.findViewById(R.id.tv_alert_content);
        //取消按钮
        TextView buttonCancel = view.findViewById(R.id.tv_dialog_cancel);
        //确定按钮
        TextView buttonOk = view.findViewById(R.id.tv_dialog_ok);
        //view
        View viewLine = view.findViewById(R.id.view_line);

        if (TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

        tvContent.setText(TextUtils.isEmpty(content) ? "" : content);

        if (TextUtils.isEmpty(btnCancleText)) {
            buttonCancel.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        } else {
            buttonCancel.setText(btnCancleText);
        }
        if (TextUtils.isEmpty(btnSureText)) {
            buttonOk.setVisibility(View.GONE);
        } else {
            buttonOk.setText(btnSureText);
        }

        final AlertDialog dialogFinal = dialog;
        final DialogInterface.OnClickListener finalCancleListener = cancleListener;
        final DialogInterface.OnClickListener finalSureListener = sureListener;
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalCancleListener.onClick(dialogFinal, DialogInterface.BUTTON_NEGATIVE);
            }
        });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalSureListener.onClick(dialogFinal, DialogInterface.BUTTON_POSITIVE);
            }
        });

        //设置背景透明,去四个角
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        dialog.setContentView(view);
        return dialog;
    }
}
