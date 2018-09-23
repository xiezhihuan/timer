package com.lbstudio.timer.app;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;

import com.lbstudio.timer.app.app.TimerActivity;
import com.lbstudio.timer.app.app.activity.LoginAcitvity;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.base.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivityy extends BaseActivity {

    public static String token;
    public static String userId;


    @Override
    protected void init() {

        token = NetConfig.getToken(this);
        userId = NetConfig.getUserId(this);
        //全屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        if (!token.equals(NetConfig.ISNULL) && !userId.equals(NetConfig.ISNULL)) {
            //将token传到需要使用token的类
            Intent intent = new Intent(this, TimerActivity.class);
            intent.putExtra(NetConfig.KEY_TOKEN, token);
            intent.putExtra(NetConfig.KEY_USERID, userId);
            startActivity(intent);
        } else {//若没有token则跳转到登录
            Intent intent = new Intent(this, LoginAcitvity.class);
            startActivity(intent);
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
