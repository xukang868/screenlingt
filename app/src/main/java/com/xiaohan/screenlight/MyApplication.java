package com.xiaohan.screenlight;

import android.app.Application;
import android.content.Context;

/**
 * Created by xiaohan on 2019/6/22
 * Describe:
 */
public class MyApplication extends Application {
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
