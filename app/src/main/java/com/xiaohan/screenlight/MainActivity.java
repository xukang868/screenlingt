package com.xiaohan.screenlight;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.currentScreenSeekBar)
    SeekBar mCurrentScreenSeekBar;
    @BindView(R.id.currentNum)
    TextView mCurrentNum;
    @BindView(R.id.systemScreenSeekBar)
    SeekBar mSystemScreenSeekBar;
    @BindView(R.id.systemNum)
    TextView mSystemNum;
    @BindView(R.id.screenOffNum)
    EditText mScreenOffNum;
    @BindView(R.id.Sure)
    Button mSure;
    @BindView(R.id.keepScreen)
    Button mKeepScreen;
    @BindView(R.id.relaseScreen)
    Button mRelaseScreen;
    private int currentScreenNum = 0;
    private int screenOffTime = 0;
    private final int REQUEST_CODE_WRITE_SETTINGS = 100;
    private boolean isSetKeepLight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestWriteSettings();
        initData();
    }

    private void initData() {
        currentScreenNum = ScreenLightUtils.getScreenBrightness();

        mCurrentScreenSeekBar.setProgress(currentScreenNum);
        mSystemScreenSeekBar.setProgress(currentScreenNum);
        mCurrentNum.setText(currentScreenNum + "");
        mSystemNum.setText(currentScreenNum + "");
        mCurrentScreenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentScreenNum = progress;
                mCurrentNum.setText(currentScreenNum + "");
                ScreenLightUtils.setScreenBrightness(MainActivity.this, currentScreenNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //对于系统的屏幕亮度调节，必须是手动调节才有效果
//        if(ScreenLightUtils.getScreenMode()==1){
//            ScreenLightUtils.setScreenMode(0);
//        }
        mSystemScreenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentScreenNum = progress;
                mSystemNum.setText(currentScreenNum + "");
                ScreenLightUtils.changSystemBrightness(currentScreenNum);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //获取当前息屏时间
        screenOffTime = ScreenOffUtils.getDormant();
        mScreenOffNum.setText(screenOffTime + "");

    }

    @OnClick({R.id.Sure, R.id.keepScreen, R.id.relaseScreen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Sure:
                String s = mScreenOffNum.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    screenOffTime = Integer.parseInt(s);
                    ScreenOffUtils.setDormant(screenOffTime);
                }
                break;
            case R.id.keepScreen:
//                ScreenOffUtils.keepLight();
                isSetKeepLight = true;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                break;
            case R.id.relaseScreen:
//                ScreenOffUtils.exitScreen();
                isSetKeepLight = false;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                break;
        }
    }

    /**
     * 申请权限
     */
    private void requestWriteSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //大于等于23 请求权限
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
            }
        } else {
            //小于23直接设置
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Settings.System.canWrite方法检测授权结果
                if (Settings.System.canWrite(getApplicationContext())) {
                    Toast.makeText(MainActivity.this, "获取了权限", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "您拒绝了权限", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isSetKeepLight) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}
