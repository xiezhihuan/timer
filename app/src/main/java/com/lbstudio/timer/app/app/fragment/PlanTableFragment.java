package com.lbstudio.timer.app.app.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.activity.PlanDetail_Activity;
import com.lbstudio.timer.app.app.activity.Plan_creat_Activity;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.ui.DrawableUtil;
import com.lbstudio.timer.app.app.ui.FlowLayout;
import com.lbstudio.timer.app.app.util.CommonUilts;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PlanTableFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.topBar_title)
    TextView topBarTitle;
    @BindView(R.id.topBar_right)
    ImageView topBarRight;
    @BindView(R.id.topBar_skin)
    ImageView topBarSkin;
    @BindView(R.id.view_flow)
    FlowLayout viewFlow;
    @BindView(R.id.week01_date)
    TextView week01Date;
    @BindView(R.id.week02_date)
    TextView week02Date;
    @BindView(R.id.week03_date)
    TextView week03Date;
    @BindView(R.id.week04_date)
    TextView week04Date;
    @BindView(R.id.week05_date)
    TextView week05Date;
    @BindView(R.id.week06_date)
    TextView week06Date;
    @BindView(R.id.week07_date)
    TextView week07Date;

    private List<List<Plan>> allWeekData;//当前周的计划数据
    private Map<String, String> dateRange;

    private Random random;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_calendar2, null);
            unbinder = ButterKnife.bind(this, view);
            CommonUilts.cacheFragmentNum(getContext(),"3");

            //设置顶部栏样式
            initTopBar();
            //获得本周的日期范围
            dateRange = CommonUilts.getWeekDateRange();
            //设置本周七天的日期
            setWeekDate();

            allWeekData = getAllWeekData();  //获得本周的计划数据

//        initData(allWeekData);//将计划数据展示在界面上
            return view;

        }catch (Exception e){
            Log.d("debug", e.getMessage());
        }

       return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewFlow.removeAllViews();
        initData(allWeekData);//将计划数据展示在界面上
    }

    @OnClick(R.id.topBar_right)
    public void addPlan(View view) {
        CommonUilts.thisToActivity(Plan_creat_Activity.class);
        getActivity().finish();
    }

    /**
     * 获得本周的计划数据
     *
     * @return
     */
    private List<List<Plan>> getAllWeekData() {
        String startTime = dateRange.get("startTime");
        String endTime = dateRange.get("endTime");

        List<List<Plan>> weekPlan = new ArrayList<>();
        for (int i = Integer.parseInt(startTime); i <= Integer.parseInt(endTime); i++) {
            List<Plan> plans = LitePal.where("planDate > ? and planDate < ?",
                    CommonUilts.int2string(i - 1), CommonUilts.int2string(i + 1)).find(Plan.class);

            Log.d("svsavs", "size: " + plans.size());
            weekPlan.add(plans);
        }
        Log.d("svsavs", "weekPlan size: " + weekPlan.size());
        return weekPlan;
    }

    /**
     * 设置本周七天的日期
     */
    private void setWeekDate() {
        String startTime = dateRange.get("startTime");
        String endTime = dateRange.get("endTime");
        Log.d("svsavs", "endTime: " + endTime + "  startTime:" + startTime);
        String[] startTime_split = startTime.split("");
        String[] endTime_split = endTime.split("");

        String startDay = startTime_split[7] + startTime_split[8];
        String endDay = endTime_split[7] + endTime_split[8];
        Log.d("svsavs", "startDay: " + startDay + "  endDay:" + endDay);

        week01Date.setText(Integer.parseInt(startDay) + "");
        week02Date.setText(Integer.parseInt(startDay) + 1 + "");
        week03Date.setText(Integer.parseInt(startDay) + 2 + "");
        week04Date.setText(Integer.parseInt(startDay) + 3 + "");
        week05Date.setText(Integer.parseInt(endDay) - 2 + "");
        week06Date.setText(Integer.parseInt(endDay) - 1 + "");
        week07Date.setText(Integer.parseInt(endDay) + "");
        Log.d("svsavs", "fddddddddddddddddddddddddddddddddd");

    }


    /**
     * 将计划数据展示在界面上
     *
     * @param datas
     */
    private void initData(List<List<Plan>> datas) {

        random = new Random();
        int week = 1;
        for (List<Plan> data : datas) {
            for (Plan plan : data) {
                final int id = plan.getId();
                Log.d("svsavs", "plan.getId: " + id);
                String name = plan.getPlanTitle();
                Log.d("svsavs", "plan.getPlanTitle(): " + name);
                int singleHeight = CommonUilts.dp2px(1);

                int endTime = plan.getPlanEndTime();
                int startTime = plan.getPlanStartTime();
                Log.d("svsavs", "endTime: " + endTime + "  startTime:" + startTime);
                int spendTime = endTime - startTime;
                Log.d("svsavs", "spendTime:" + spendTime);
                int blockHeight = singleHeight * spendTime;
                int blockWidth = CommonUilts.dp2px(45);

                TextView tv = new TextView(getActivity());
                ViewGroup.MarginLayoutParams mp = new ViewGroup.MarginLayoutParams(blockWidth, blockHeight);
                mp.leftMargin = CommonUilts.dp2px(1);
                mp.rightMargin = CommonUilts.dp2px(1);
                mp.topMargin = CommonUilts.dp2px(1);
                mp.bottomMargin = CommonUilts.dp2px(1);
                tv.setLayoutParams(mp);
                tv.setText(name);
                tv.setTextColor(getResources().getColor(R.color.white));
                int r = random.nextInt(200);
                int g = random.nextInt(200);
                int b = random.nextInt(200);
                tv.setBackground(
                        DrawableUtil.getSelector(DrawableUtil.getDrawable(Color.rgb(r, g, b), CommonUilts.dp2px(5)),
                                DrawableUtil.getDrawable(Color.WHITE, CommonUilts.dp2px(5))));

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonUilts.thisToActivity(id, PlanDetail_Activity.class);
                        getActivity().finish();
                    }
                });

                int width = blockWidth + mp.leftMargin + mp.rightMargin;
                int height = blockHeight + mp.topMargin;

                int right = width * week - mp.rightMargin;
                int top = (startTime - 360) * singleHeight + mp.topMargin;
                int bottom = top + blockHeight - mp.bottomMargin;
                int left = right - width + mp.rightMargin;

                tv.layout(left, top, right, bottom);
                viewFlow.addView(tv);
            }
            week += 1;//周几
        }
    }

    /**
     * 初始化顶部导航栏
     */
    private void initTopBar() {
        topBarSkin.setVisibility(View.INVISIBLE);
        topBarTitle.setText("日程表");
        topBarRight.setVisibility(View.VISIBLE);
    }

}
