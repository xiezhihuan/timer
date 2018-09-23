package com.lbstudio.timer.app.app.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbstudio.timer.app.AppManager;
import com.lbstudio.timer.app.MainActivityy;
import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.DbTool.CourseTool;
import com.lbstudio.timer.app.app.TimerActivity;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.lbstudio.timer.app.base.BaseActivity;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Course_creat_Activity extends BaseActivity {


    @BindView(R.id.topBar_close)
    ImageView topBarClose;
    @BindView(R.id.topBar_save)
    ImageView topBarSave;
    @BindView(R.id.course_detailPge_name_txt)
    EditText courseDetailPgeNameTxt;
    @BindView(R.id.course_detailPge_site_txt)
    EditText courseDetailPgeSiteTxt;
    @BindView(R.id.course_detailPge_weekCount_txt_end)
    TextView courseDetailPgeWeekCountTxtEnd;
    @BindView(R.id.course_detailPge_lessonCount_txt)
    TextView courseDetailPgeLessonCountTxt;
    @BindView(R.id.course_detailPge_teacher_txt)
    EditText courseDetailPgeTeacherTxt;
    @BindView(R.id.course_detailPge_beizhu_txt)
    EditText courseDetailPgeBeizhuTxt;
    @BindView(R.id.topBar_left)
    ImageView topBarLeft;
    @BindView(R.id.topBar_title)
    TextView topBarTitle;

    private int term=2018;
    private int week;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CommonUilts.thisToActivity(TimerActivity.class);
            finish();
            return true;
        }
        return false;

    }

    @Override
    protected void init() {
        topBarLeft.setVisibility(View.VISIBLE);
        topBarSave.setVisibility(View.VISIBLE);
        topBarTitle.setText(R.string.newCourse2);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUilts.thisToActivity(TimerActivity.class);
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addcourse;
    }

    @OnClick({R.id.topBar_left, R.id.topBar_save, R.id.course_detailPge_weekCount_txt_end, R.id.course_detailPge_lessonCount_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.topBar_save:
                //本地保存
                save();
                //保存到服务器
                save_server();
                //
                CommonUilts.thisToActivity(TimerActivity.class);
                AppManager.getInstance().removeCurrent();
                break;
            case R.id.topBar_left:
                CommonUilts.thisToActivity(TimerActivity.class);
                break;
            case R.id.course_detailPge_weekCount_txt_end:
                break;
            case R.id.course_detailPge_lessonCount_txt:
                break;
        }
    }

    private void save_server() {

    }


    private void save() {
        String name = courseDetailPgeNameTxt.getText().toString().trim();
        String site = courseDetailPgeSiteTxt.getText().toString().trim();
        String weekOfclass = courseDetailPgeWeekCountTxtEnd.getText().toString().trim();
        String lesson = courseDetailPgeLessonCountTxt.getText().toString().trim();
        String teacher = courseDetailPgeTeacherTxt.getText().toString().trim();
//        CourseTool.addCourse(name, site,term,weekOfclass,week, );

    }

    public void nativeTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
//        etCourseName.setText("Date获取当前日期时间" + simpleDateFormat.format(date));
    }

    public void netTime() {

        //获得网路时间
        Date netTime = getNetTime();
        //设置时间格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //将格式化后的时间展示到
//        etCourseName.setText("Date获取当前日期时间" + simpleDateFormat.format(netTime));

    }

    public static Date getNetTime() {
        String webUrl = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
        try {
            URL url = new URL(webUrl);
            URLConnection uc = url.openConnection();
            uc.setReadTimeout(5000);
            uc.setConnectTimeout(5000);
            uc.connect();
            long correctTime = uc.getDate();
            Date date = new Date(correctTime);
            return date;
        } catch (Exception e) {
            return new Date();
        }
    }

    public static String getNativeTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        return time;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
