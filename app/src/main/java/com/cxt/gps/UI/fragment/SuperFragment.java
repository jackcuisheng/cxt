package com.cxt.gps.UI.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cxt.gps.R;
import com.cxt.gps.UI.view.FlippingLoadingDialog;
import com.cxt.gps.UI.view.HandyTextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class SuperFragment extends Fragment {
    protected View mView;
    protected FlippingLoadingDialog mLoadingDialog ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initViews();
        mLoadingDialog = new FlippingLoadingDialog(getContext(), "请求提交中");
        init();
        initEvents();
        return mView;
    }
    protected abstract void initViews();
    protected abstract void initEvents();
    protected abstract void init();


    protected void showLoadingDialog(String text) {
        if (text != null) {
            mLoadingDialog.setText(text);
        }
        mLoadingDialog.show();
    }

    protected void dismissLoadingDialog() {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    /** 短暂显示Toast提示(来自res) **/
    protected void showShortToast(int resId) {
        Toast.makeText(getActivity(), getString(resId), Toast.LENGTH_SHORT).show();
    }

    /** 短暂显示Toast提示(来自String) **/
    protected void showShortToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    /** 长时间显示Toast提示(来自res) **/
    protected void showLongToast(int resId) {
        Toast.makeText(getActivity(), getString(resId), Toast.LENGTH_LONG).show();
    }

    /** 长时间显示Toast提示(来自String) **/
    protected void showLongToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    /** 显示自定义Toast提示(来自res) **/
    protected void showCustomToast(Context context, int resId) {
        View toastRoot = LayoutInflater.from(context).inflate(
                R.layout.common_toast, null);
        ((HandyTextView) toastRoot.findViewById(R.id.toast_text))
                .setText(getString(resId));
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);
        toast.show();
    }

    /** 显示自定义Toast提示(来自String) **/
    protected void showCustomToast(Context context,String text) {
        View toastRoot = LayoutInflater.from(context).inflate(
                R.layout.common_toast, null);
        ((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastRoot);
        toast.show();
    }

    /** Debug输出Log日志 **/
    protected void showLogDebug(String tag, String msg) {
        Log.d(tag, msg);
    }

    /** Error输出Log日志 **/
    protected void showLogError(String tag, String msg) {
        Log.e(tag, msg);
    }

    /** 通过Class跳转界面 **/
    protected void startActivity(home_fragment home_fragment, Class<?> cls) {
        startActivity(cls, null);
    }

    /** 含有Bundle通过Class跳转界面 **/
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getContext(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /** 通过Action跳转界面 **/
    protected void startActivity(String action) {
        startActivity(action, null);
    }

    /** 含有Bundle通过Action跳转界面 **/
    protected void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    protected void startActivity(Context context,Class clazz){
        Intent intent = new Intent(context,clazz);
        startActivity(intent);
    }
    protected void startActivity(Context context,Class clazz,Bundle bundle){
        Intent intent = new Intent(context,clazz);
        intent.putExtras(bundle);
        startActivity(intent);

    }
    /** 含有标题和内容的对话框 **/
    protected AlertDialog showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle(title)
                .setMessage(message).show();
        return alertDialog;
    }

    /** 含有标题、内容、两个按钮的对话框 **/
    protected AlertDialog showAlertDialog(String title, String message,
                                          String positiveText,
                                          DialogInterface.OnClickListener onPositiveClickListener,
                                          String negativeText,
                                          DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener)
                .show();
        return alertDialog;
    }

    /** 含有标题、内容、图标、两个按钮的对话框 **/
    protected AlertDialog showAlertDialog(String title, String message,
                                          int icon, String positiveText,
                                          DialogInterface.OnClickListener onPositiveClickListener,
                                          String negativeText,
                                          DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle(title)
                .setMessage(message).setIcon(icon)
                .setPositiveButton(positiveText, onPositiveClickListener)
                .setNegativeButton(negativeText, onNegativeClickListener)
                .show();
        return alertDialog;
    }

}
