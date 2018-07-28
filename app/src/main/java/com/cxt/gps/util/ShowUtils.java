package com.cxt.gps.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.cxt.gps.R;
import com.cxt.gps.UI.view.HandyTextView;

public class ShowUtils {
    public static void showCustomToast(Context context, String text) {
        View toastRoot = LayoutInflater.from(context).inflate(
                R.layout.common_toast, null);
        ((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);
        toast.show();
    }

}
