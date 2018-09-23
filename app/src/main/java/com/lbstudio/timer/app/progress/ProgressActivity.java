package com.lbstudio.timer.app.progress;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressActivity extends BaseActivity {
//    @BindView(R.id.topBar_left)
//    ImageView topBarLeft;
//    @BindView(R.id.topBar_title)
//    TextView topBarTitle;
//    @BindView(R.id.topBar_right)
//    ImageView topBarRight;   //todo 为什么找不到id？

    @Override
    protected void init() {

//        //全屏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_progress;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
