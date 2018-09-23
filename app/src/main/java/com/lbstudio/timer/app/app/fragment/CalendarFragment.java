package com.lbstudio.timer.app.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.activity.Plan_creat_Activity;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.necer.ncalendar.calendar.NCalendar;
import com.necer.ncalendar.listener.OnCalendarChangedListener;
import com.necer.ncalendar.utils.Attrs;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CalendarFragment extends Fragment implements OnCalendarChangedListener {
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

    Unbinder unbinder;
    @BindView(R.id.calendar_add)
    ImageView calendarAdd;

    private List<Plan> planList;
    private List<List<List<Plan>>> plan5month;  //模拟的五个月数据


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, null);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        //设置日程的主题色
        calendarHead.setBackgroundColor(Attrs.selectCircleColor);

        //给recycleView适配内容
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//必须有一个Manager 不然内容无法显示

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



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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


//        //做三个月的假数据
//        List<Plan> allPlanOfDay; //每天的计划
//        /**天*/
//        allPlanOfDay = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            Plan plan = new Plan("练习听力" + i + " " + date.getMonthOfYear() + "月" + date.getDayOfMonth() + "日", "6：00-6：30 " + i);
//            allPlanOfDay.add(plan);
//        }
//
//        //适配数据
//        recyclerView.setAdapter(new Adapter(getContext(), allPlanOfDay));

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

    @OnClick(R.id.calendar_add)
    public void onViewClicked() {
        CommonUilts.thisToActivity(Plan_creat_Activity.class);
    }
}
