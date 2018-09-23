package com.lbstudio.timer.app;

import android.content.Intent;
import android.os.Build;
import android.view.WindowManager;

import com.lbstudio.timer.app.app.TimerActivity;
import com.lbstudio.timer.app.app.activity.LoginAcitvity;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity {

    public static String token;
    public static String userId;


    @Override
    protected void init() {
        //全屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        },2000);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activityy_main;
    }
}
