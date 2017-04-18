package com.wiggins.okhttp3.utils;


import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wiggins.okhttp3.R;
import com.wiggins.okhttp3.app.MyApplication;

/**
 * @Description 通用Toast提示
 * @Author 一花一世界
 */

public class ToastUtil {

    private volatile static ToastUtil globalBoast = null;
    private final static int posY = 300;
    private Toast internalToast;

    private ToastUtil(Toast toast) {
        if (toast == null) {
            throw new NullPointerException("Toast requires a non-null parameter.");
        }
        internalToast = toast;
    }

    public static Context getContext() {
        return MyApplication.getContext();
    }

    public static void showText(CharSequence text) {
        ToastUtil.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showText(CharSequence text, int duration) {
        ToastUtil.makeText(getContext(), text, duration).show();
    }

    public static void showText(int resId) throws Resources.NotFoundException {
        ToastUtil.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showText(int resId, int duration) throws Resources.NotFoundException {
        ToastUtil.makeText(getContext(), resId, duration).show();
    }

    public static void show(View view, int duration) {
        if (getContext() != null && view != null)
            ToastUtil.make(getContext(), view, duration).show();
    }

    public static ToastUtil makeText(Context context, CharSequence text, int duration) {
        return new ToastUtil(InnerCreater(context, text, duration));
    }

    public static ToastUtil makeText(Context context, int resId, int duration) throws Resources.NotFoundException {
        return new ToastUtil(InnerCreater(context, resId, duration));
    }

    public static ToastUtil make(Context context, View vContent, int duration) {
        return new ToastUtil(InnerCreater(context, vContent, duration));
    }

    private static Toast InnerCreater(Context context, int resId, int duration) {
        Toast returnValue = null;
        if (context != null)
            returnValue = InnerCreater(context, context.getString(resId), duration);
        return returnValue;
    }

    private static Toast InnerCreater(Context context, CharSequence info, int duration) {
        Toast returnValue = null;
        if (context != null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View vContent = inflater.inflate(R.layout.activity_toast, null);
            TextView mTvContent = (TextView) vContent.findViewById(R.id.tv_content);
            mTvContent.setText(info);
            mTvContent.bringToFront();
            returnValue = new Toast(context.getApplicationContext());
            returnValue.setGravity(Gravity.BOTTOM, 0, posY);
            returnValue.setDuration(duration);
            returnValue.setView(vContent);
        }
        return returnValue;
    }

    private static Toast InnerCreater(Context context, View vContent, int duration) {
        Toast returnValue = null;
        if (context != null && vContent != null) {
            returnValue = new Toast(context.getApplicationContext());
            returnValue.setGravity(Gravity.CENTER, 0, posY);
            returnValue.setDuration(duration);
            returnValue.setView(vContent);
        }
        return returnValue;
    }

    public void cancel() {
        internalToast.cancel();
    }

    public void show() {
        show(true);
    }

    public void show(boolean cancelCurrent) {
        if (cancelCurrent && (globalBoast != null)) {
            globalBoast.cancel();
        }

        globalBoast = this;
        internalToast.show();
    }
}
