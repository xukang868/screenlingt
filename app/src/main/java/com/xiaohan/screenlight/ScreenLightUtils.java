package com.xiaohan.screenlight;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by xiaohan on 2019/6/22
 * Describe:
 */
public class ScreenLightUtils {
    /**
     * 获得当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    public static int getScreenMode() {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(MyApplication.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {

        }
        return screenMode;
    }

    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    public static void setScreenMode(int paramInt) {
        try {
            Settings.System.putInt(MyApplication.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }


    /**
     * 获得当前屏幕亮度值 0--255
     */
    public static int getScreenBrightness() {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(MyApplication.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {

        }
        return screenBrightness;
    }

    /**
     * 修改当期Acitivity
     * 保存当前的屏幕亮度值，并使之生效
     */
    public static void setScreenBrightness(Activity activity, int paramInt) {
        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = paramInt / 255.0F;
        localLayoutParams.screenBrightness = f;//修改
        localWindow.setAttributes(localLayoutParams);
    }

    /**
     * 修改系统的屏幕亮度值
     *
     * @param brightness
     */
    public static void changSystemBrightness(int brightness) {
        ContentResolver resolver = MyApplication.mContext.getContentResolver();
        try {
            if (Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL != (Settings.System.getInt(MyApplication.mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE))) {
                //设置为手动调节模式
                Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        //保存到系统中
        Uri uri = android.provider.Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        android.provider.Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
        resolver.notifyChange(uri, null);
    }
}
