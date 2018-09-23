package com.lbstudio.timer.app;

import android.os.Bundle;
import android.support.constraint.Guideline;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.lbstudio.timer.app.base.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Test3 extends BaseActivity {
    @BindView(R.id.guideline_or)
    Guideline guidelineOr;
    @BindView(R.id.guideline_ver)
    Guideline guidelineVer;
    @BindView(R.id.guideline_left)
    Guideline guidelineLeft;
    @BindView(R.id.refreshView)
    SmartRefreshLayout refreshView;
    @BindView(R.id.top_background)
    TextView topBackground;
    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.bt_jilu)
    TextView btJilu;
    @BindView(R.id.ExpandableListView)
    android.widget.ExpandableListView ExpandableListView;

    @Override
    protected void init() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.test3;
    }


}
