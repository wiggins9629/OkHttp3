package com.wiggins.okhttp3.http;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import com.wiggins.okhttp3.utils.Constant;
import com.wiggins.okhttp3.utils.LogUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Platform {
    private static final Platform PLATFORM = findPlatform();

    public static Platform get() {
        LogUtil.e(Constant.LOG_TAG, PLATFORM.getClass().toString());
        return PLATFORM;
    }

    private static Platform findPlatform() {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0) {
                return new Android();
            }
        } catch (ClassNotFoundException ignored) {
            ignored.printStackTrace();
        }
        return new Platform();
    }

    public Executor defaultCallbackExecutor() {
        return Executors.newCachedThreadPool();
    }

    public void execute(Runnable runnable) {
        defaultCallbackExecutor().execute(runnable);
    }

    static class Android extends Platform {

        @Override
        public Executor defaultCallbackExecutor() {
            return new MainThreadExecutor();
        }

        static class MainThreadExecutor implements Executor {
            private final Handler handler = new Handler(Looper.getMainLooper());

            @Override
            public void execute(Runnable r) {
                handler.post(r);
            }
        }
    }
}
