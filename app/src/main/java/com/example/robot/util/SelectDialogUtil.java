package com.example.robot.util;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SelectDialogUtil extends Dialog {

    public SelectDialogUtil(@NonNull Context context) {
        super(context);
    }

    public SelectDialogUtil(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected SelectDialogUtil(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
