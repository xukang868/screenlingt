package com.xiaohan.screenlight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;

/**
 * Created by xiaohan on 2019/6/22
 * Describe:
 */
public class ScreenOffUtils {
    private static PowerManager.WakeLock wakeLock;

    /**
     * 获取系统休眠时间
     */
    public static int getDormant() {
        int result = 0;
        try {
            result = Settings.System.getInt(MyApplication.mContext.getContentResolver(),
                    Settings.System.SCREEN_OFF_TIMEOUT);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置系统的休眠时间
     */
    public static void setDormant(int time) {
        Settings.System.putInt(MyApplication.mContext.getContentResolver(),
                Settings.System.SCREEN_OFF_TIMEOUT, time);
        Uri uri = Settings.System
                .getUriFor(Settings.System.SCREEN_OFF_TIMEOUT);
        MyApplication.mContext.getContentResolver().notifyChange(uri, null);
    }

    @SuppressLint("InvalidWakeLockTag")
    public static void keepLight() {
        PowerManager pm = (PowerManager) MyApplication.mContext.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "TAG");
        wakeLock.acquire();
        // wakeLock.acquire(30*1000);//保持屏幕常亮30s

       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wakeLock.release();
            }
        },1000);*/
    }

    public static void exitScreen() {
        wakeLock.release();
    }
}
