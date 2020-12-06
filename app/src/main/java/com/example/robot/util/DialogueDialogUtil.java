package com.example.robot.util;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.robot.R;

public class DialogueDialogUtil {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private String answer;
    public void dismiss(){
        dialog.dismiss();
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public synchronized AlertDialog showDialog(Context context, String title,
                                               String btnCancelText, String btnSureText, DialogInterface.OnClickListener cancleListener,
                                               DialogInterface.OnClickListener sureListener) {
        builder = new AlertDialog.Builder(context);
        dialog = builder.create();
        // dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
        dialog.setCanceledOnTouchOutside(false);
        //dialog弹出后会点击屏幕或物理返回键，dialog不消失
        dialog.setCancelable(true);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        View view = View.inflate(context, R.layout.dialog_dialogue, null);
        //标题
        TextView tvTitle = view.findViewById(R.id.tv_alert_question);
        //内容
        EditText tvContent = view.findViewById(R.id.tv_alert_answer);
        answer = tvContent.getText().toString();
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

        if (TextUtils.isEmpty(btnCancelText)) {
            buttonCancel.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
        } else {
            buttonCancel.setText(btnCancelText);
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
        dialog.setView(new EditText(context));
        dialog.show();
        dialog.setContentView(view);
        return dialog;
    }
}