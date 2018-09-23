package com.lbstudio.timer.app.app.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ie1e.mdialog.view.ActionSheetDialog;
import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.activity.CourseDetail_Activity;
import com.lbstudio.timer.app.app.activity.Course_creat_Activity;
import com.lbstudio.timer.app.app.javabean.Course;
import com.lbstudio.timer.app.app.javabean.Course_un_inited;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.ui.DrawableUtil;
import com.lbstudio.timer.app.app.ui.FlowLayout;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.lbstudio.timer.app.app.browser.activity.WebViewActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CourseFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.topBar_title)
    TextView topBarTitle;
    @BindView(R.id.topBar_right)
    ImageView topBarRight;
    @BindView(R.id.topBar_skin)
    ImageView topBarSkin;
    @BindView(R.id.view_flow)
    FlowLayout viewFlow;
    @BindView(R.id.week01Date)
    TextView week01Date;
    @BindView(R.id.week02Date)
    TextView week02Date;
    @BindView(R.id.week03Date)
    TextView week03Date;
    @BindView(R.id.week04Date)
    TextView week04Date;
    @BindView(R.id.week05Date)
    TextView week05Date;
    @BindView(R.id.week06Date)
    TextView week06Date;
    @BindView(R.id.week07Date)
    TextView week07Date;

    private Random random;
    private List<List<Course>> courseData;
    private Map<String, String> dateRange;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course2, null);
        unbinder = ButterKnife.bind(this, view);

        CommonUilts.cacheFragmentNum(getActivity(), NetConfig.SECOND_PAGE);

        //设置顶部栏样式
        initTopBar();
        //获得本周的日期范围
        dateRange = CommonUilts.getWeekDateRange();
        //设置本周七天的日期
        setWeekDate();
        //获得课表数据
        courseData = getCourseData();

        Log.d("fgggthhy", "onCreateView: " + courseData.size());
        boolean isNull=true;
        for (List<Course> courses : courseData) {
            if (courses.size() != 0) {
               isNull=false;
            }
        }
        if (isNull) {
            String html = CommonUilts.getHtml(getContext());
            Log.d("fffgtgt", "onCreateView: " + html);
            if (html!=null) {
                try{
                    List<Course_un_inited> course_un_initeds = CommonUilts.initHtmlData(html);
                    CommonUilts.save(html);
                }catch (NullPointerException ex){
                    Log.d("llolllllllllll", "NullPointerException: "+ex.getMessage());
                }

                //获得课表数据
                courseData = getCourseData();
            }
        }
        //绘制页面
        layout(courseData);

        return view;
    }

    /**
     * 设置本周七天的日期
     */
    private void setWeekDate() {
        String startTime = dateRange.get("startTime");
        String endTime = dateRange.get("endTime");
        String[] startTime_split = startTime.split("");
        String[] endTime_split = endTime.split("");

        String startDay = startTime_split[7] + startTime_split[8];
        String endDay = endTime_split[7] + endTime_split[8];

        week01Date.setText(Integer.parseInt(startDay) + "");
        week02Date.setText(Integer.parseInt(startDay) + 1 + "");
        week03Date.setText(Integer.parseInt(startDay) + 2 + "");
        week04Date.setText(Integer.parseInt(startDay) + 3 + "");
        week05Date.setText(Integer.parseInt(endDay) - 2 + "");
        week06Date.setText(Integer.parseInt(endDay) - 1 + "");
        week07Date.setText(Integer.parseInt(endDay) + "");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        viewFlow.removeAllViews();
        unbinder.unbind();
    }

    @OnClick(R.id.topBar_right)
    public void addPlan(View view) {
        new ActionSheetDialog(getActivity())
                .builder()
                .setTitle(getString(R.string.xuanze))
                .setCanceledOnTouchOutside(true)
                .setDialogLocation(Gravity.CENTER)
                .addSheetItem("一键添加课程", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        WebViewActivity.loadUrl(getActivity(), NetConfig.JWXT, false);

                        CommonUilts.cacheFragmentNum(getActivity(), NetConfig.SECOND_PAGE);
                        getActivity().finish();
                    }
                })
                .addSheetItem("手动添加课程", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        CommonUilts.thisToActivity(Course_creat_Activity.class);
                        getActivity().finish();
                    }
                })
                .show();
    }

    /**
     * 课程条块的点击事件
     *
     * @param id
     */
    private void onClickCourse(int id) {
        CommonUilts.thisToActivity(id, CourseDetail_Activity.class);
        getActivity().finish();
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        viewFlow.removeAllViews();
        layout(getCourseData());
    }

    /**
     * 获得一个大小为7的课程集合
     *
     * @return
     */
    public List<List<Course>> getCourseData() {
        int term = 2018;
        //从数据库中查询所有课程数据
        List<Course> courseList = CommonUilts.queryCourse_byTerm(term); //获得一个学期的课程

        //每天的课程集合  将课程分为七天
        List<Course> week1 = new ArrayList<>();
        List<Course> week2 = new ArrayList<>();
        List<Course> week3 = new ArrayList<>();
        List<Course> week4 = new ArrayList<>();
        List<Course> week5 = new ArrayList<>();
        List<Course> week6 = new ArrayList<>();
        List<Course> week7 = new ArrayList<>();
        for (Course course : courseList) {
            int week = course.getWeek();
            switch (week) {
                case 1:
                    week1.add(course);
                    break;
                case 2:
                    week2.add(course);
                    break;
                case 3:
                    week3.add(course);
                    break;
                case 4:
                    week4.add(course);
                    break;
                case 5:
                    week5.add(course);
                    break;
                case 6:
                    week6.add(course);
                    break;
                case 7:
                    week7.add(course);
                    break;
            }
        }

        List<List<Course>> allWeek = new ArrayList<>();//将每天的课程封装为一个集合
        allWeek.add(week1);
        allWeek.add(week2);
        allWeek.add(week3);
        allWeek.add(week4);
        allWeek.add(week5);
        allWeek.add(week6);
        allWeek.add(week7);

        return allWeek;
    }


    /**
     * 绘制布局页面
     */
    private void layout(List<List<Course>> courseData) {
        random = new Random();
        for (List<Course> courseList : courseData) {
            for (Course course : courseList) {
                final int id = course.getId();

                String name = course.getName();
                String site = course.getSite();
                String txt = name + "@" + site;

                int singleHeight = CommonUilts.dp2px(60);

                int endLesson = course.getEndLesson();
                int startLesson = course.getStartLesson();
                int lessonNum = endLesson - startLesson + 1;
                int blockHeight = singleHeight * lessonNum;
                int blockWidth = CommonUilts.dp2px(45);

                TextView tv = new TextView(getActivity());
                ViewGroup.MarginLayoutParams mp = new ViewGroup.MarginLayoutParams(blockWidth, blockHeight);
                mp.leftMargin = CommonUilts.dp2px(1);
                mp.rightMargin = CommonUilts.dp2px(1);
                mp.topMargin = CommonUilts.dp2px(1);
                mp.bottomMargin = CommonUilts.dp2px(1);
                tv.setLayoutParams(mp);
                tv.setText(txt);
                tv.setTextColor(getResources().getColor(R.color.white));
                int r = random.nextInt(200);
                int g = random.nextInt(200);
                int b = random.nextInt(200);
                tv.setBackground(
                        DrawableUtil.getSelector(DrawableUtil.getDrawable(Color.rgb(r, g, b), CommonUilts.dp2px(5)),
                                DrawableUtil.getDrawable(Color.WHITE, CommonUilts.dp2px(5))));
                int padding = CommonUilts.dp2px(5);
                tv.setPadding(padding, padding, padding, padding);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickCourse(id);
                    }
                });

                int week = course.getWeek();

                int width = blockWidth + mp.leftMargin + mp.rightMargin;
                int height = singleHeight + mp.topMargin;

                int right = width * week - mp.rightMargin;
                int top = (startLesson - 1) * height;
                int bottom = top + blockHeight;
                int left = right - width + mp.rightMargin;

                tv.layout(left, top, right, bottom);
                viewFlow.addView(tv);
            }
        }
    }


    /**
     * 设置顶部栏样式
     */
    private void initTopBar() {
        topBarSkin.setVisibility(View.INVISIBLE);
        topBarTitle.setText("课表");
        topBarRight.setVisibility(View.VISIBLE);
    }

}
