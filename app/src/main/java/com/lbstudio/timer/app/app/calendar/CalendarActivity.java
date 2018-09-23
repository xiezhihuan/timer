package com.lbstudio.timer.app.app.calendar;

import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.DbTool.PlanTool;
import com.lbstudio.timer.app.app.TimerActivity;
import com.lbstudio.timer.app.app.activity.Plan_creat_Activity;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.lbstudio.timer.app.base.BaseActivity;
import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.necer.ncalendar.utils.Attrs;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CalendarActivity extends BaseActivity implements OnCalendarChangedListener {

    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.calendar_head)
    RelativeLayout calendarHead;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ncalendarrrr)
    NCalendar ncalendarrrr;
    private List<Plan> planList;

    private int[] color = {R.color.lightBlue, R.color.lightRed, R.color.purple, R.color.wineRed};
    private Adapter adapter;//每天的计划

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUilts.thisToActivity(TimerActivity.class);
        finish();

    }

    @OnClick(R.id.calendar_add)
    public void onViewClicked() {
        CommonUilts.thisToActivity("CalendarActivity",Plan_creat_Activity.class);
        finish();
    }

    @Override
    protected void init() {

        initPlanList();

        //全屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }


        Attrs.selectCircleColor = getColor(R.color.themeCorol);
        //设置日程的主题色
        calendarHead = (RelativeLayout) findViewById(R.id.calendar_head);
        calendarHead.setBackgroundColor(Attrs.selectCircleColor);

        //给recycleView适配内容
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//必须有一个Manager 不然内容无法显示


        ncalendarrrr.setOnCalendarChangedListener(this);

        ncalendarrrr.post(new Runnable() {
            @Override
            public void run() {

                List<String> list = new ArrayList<>();
                list.add("2017-09-21");
                list.add("2017-10-21");
                list.add("2017-10-1");
                list.add("2017-10-15");
                list.add("2017-10-18");
                list.add("2017-10-26");
                list.add("2017-11-21");

                ncalendarrrr.setPoint(list);
            }
        });

    }

    private void initPlanList() {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_calendar;
    }

    /**
     * 通过实现接口OnCalendarChangedListener重写此方法 从而监听日历
     *
     * @param date
     */
    @Override
    public void onCalendarChanged(LocalDate date) {

        tvMonth.setText(date.getMonthOfYear() + "月");
        tvDate.setText(date.getYear() + "年" + date.getMonthOfYear() + "月" + date.getDayOfMonth() + "日");

        String year = date.getYear() + "";
        String month = date.getMonthOfYear() + "";
        String day = date.getDayOfMonth() + "";
        if (Integer.parseInt(month) < 10) {
            month = "0" + month;
        }
        if (Integer.parseInt(day) < 10) {
            day = "0" + day;
        }
        String planDate = year + month + day;
        Log.d(NetConfig.TAG, "date:" + planDate);
        List<com.lbstudio.timer.app.app.javabean.Plan> plans = PlanTool.quaryPlan_byDate(planDate);


        adapter = new Adapter(this, plans);
        //适配数据
        recyclerView.setAdapter(adapter);


        //先实例化Callback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(recyclerView);


    }

    public void setDate(View view) {
        ncalendarrrr.setDate("2017-12-31");
    }

    public void toMonth(View view) {
        ncalendarrrr.toMonth();
    }

    public void toWeek(View view) {
        ncalendarrrr.toWeek();
    }

    public void toToday(View view) {
        ncalendarrrr.toToday();
    }

    public void toNextPager(View view) {
        ncalendarrrr.toNextPager();
//        Attrs.selectCircleColor = color[CommonUtils.getRandom(color.length - 1)];//随机获取背景色

    }

    public void toLastPager(View view) {
        ncalendarrrr.toLastPager();
    }


    public void setPoint(View view) {
        List<String> list = new ArrayList<>();
        list.add("2017-09-21");
        list.add("2017-10-21");
        list.add("2017-10-1");
        list.add("2017-10-15");
        list.add("2017-10-18");
        list.add("2017-10-26");
        list.add("2017-11-21");

        ncalendarrrr.setPoint(list);
    }



}
