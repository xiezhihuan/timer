package com.lbstudio.timer.app.base;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.lbstudio.timer.app.AppManager;

import butterknife.ButterKnife;

public abstract class BaseActivity extends Activity {

    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mContext=getApplicationContext();
        ButterKnife.bind(this);
        AppManager.getInstance().addActivity(this);
        init();
    }

    protected abstract void init();

    protected abstract int getLayoutId();

}
