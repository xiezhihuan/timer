package com.lbstudio.timer.app.app.activity;

import android.content.Intent;
import android.support.constraint.Guideline;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.DbTool.CourseTool;
import com.lbstudio.timer.app.app.TimerActivity;
import com.lbstudio.timer.app.app.javabean.Course;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.net.UpdateCourse;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.lbstudio.timer.app.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class CourseDetail_Activity extends BaseActivity {
    @BindView(R.id.plan_detailPge_vertical_guideline)
    Guideline planDetailPgeVerticalGuideline;
    @BindView(R.id.plan_detailPge_vertical_guideline_center)
    Guideline planDetailPgeVerticalGuidelineCenter;
    @BindView(R.id.plan_detailPge_vertical_guideline_end)
    Guideline planDetailPgeVerticalGuidelineEnd;
    @BindView(R.id.topBar_left)
    ImageView topBarLeft;
    @BindView(R.id.topBar_close)
    ImageView topBarClose;
    @BindView(R.id.topBar_skin)
    ImageView topBarSkin;
    @BindView(R.id.topBar_title)
    TextView topBarTitle;
    @BindView(R.id.topBar_right)
    ImageView topBarRight;
    @BindView(R.id.topBar_edit)
    ImageView topBarEdit;
    @BindView(R.id.topBar_save)
    ImageView topBarSave;
    @BindView(R.id.divider1)
    View divider1;
    @BindView(R.id.course_detailPge_name)
    TextView courseDetailPgeName;
    @BindView(R.id.course_detailPge_name_txt)
    EditText courseDetailPgeNameTxt;
    @BindView(R.id.divider2)
    View divider2;
    @BindView(R.id.course_detailPge_site)
    TextView courseDetailPgeSite;
    @BindView(R.id.course_detailPge_site_txt)
    EditText courseDetailPgeSiteTxt;
    @BindView(R.id.divider3)
    View divider3;
    @BindView(R.id.course_detailPge_weekCount)
    TextView courseDetailPgeWeekCount;
    @BindView(R.id.course_detailPge_weekCount_txt_end)
    TextView courseDetailPgeWeekCountTxtEnd;
    @BindView(R.id.divider4)
    View divider4;
    @BindView(R.id.course_detailPge_lessonCount)
    TextView courseDetailPgeLessonCount;
    @BindView(R.id.course_detailPge_lessonCount_txt)
    TextView courseDetailPgeLessonCountTxt;
    @BindView(R.id.divider4_1)
    View divider41;
    @BindView(R.id.course_detailPge_teacher)
    TextView courseDetailPgeTeacher;
    @BindView(R.id.course_detailPge_teacher_txt)
    EditText courseDetailPgeTeacherTxt;
    @BindView(R.id.divider5)
    View divider5;
    @BindView(R.id.course_detailPge_beizhu)
    TextView courseDetailPgeBeizhu;
    @BindView(R.id.course_detailPge_beizhu_background)
    TextView courseDetailPgeBeizhuBackground;
    @BindView(R.id.course_detailPge_beizhu_txt)
    EditText courseDetailPgeBeizhuTxt;

    private int mCourseId;
    private Course course;
    private String userId;
    private String token;


    @Override
    protected void init() {
        userId = NetConfig.getUserId(this);
        token = NetConfig.getToken(this);


        initTopbar();
        mCourseId = getCourseId();

        //获得课程数据
        course = CommonUilts.queryTheCourseById(mCourseId);

        showCourseData();//展示课程数据

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_course_detail;
    }

    @OnClick({R.id.topBar_left, R.id.topBar_save, R.id.course_detailPge_weekCount_txt_end, R.id.course_detailPge_lessonCount_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.topBar_left:
                CommonUilts.thisToActivity(TimerActivity.class);
                finish();
                break;
            case R.id.topBar_save:
                String name = courseDetailPgeNameTxt.getText().toString().trim();
                String site = courseDetailPgeSiteTxt.getText().toString().trim();
                String weekOfclass = courseDetailPgeWeekCountTxtEnd.getText().toString().trim();
                String lesson = courseDetailPgeLessonCountTxt.getText().toString().trim();
                String teacher = courseDetailPgeTeacherTxt.getText().toString().trim();

                int startLesson = Integer.parseInt(lesson.substring(0, 1));
                int endLesson = Integer.parseInt(lesson.substring(2, 3));
                //修改本地数据库
                CourseTool.undateCourse_byId(mCourseId, name, site, weekOfclass, startLesson, endLesson, teacher, Integer.parseInt(NetConfig.UPDATE));
                //修改服务器数据库
                Course course = CourseTool.quaryCourse_byCourseId(String.valueOf(mCourseId));
                new UpdateCourse(Integer.parseInt(userId), token, name, site, course.getTerm(), weekOfclass,
                        course.getWeek(), startLesson, endLesson, teacher, course.getDate(), mCourseId,
                        new UpdateCourse.SuccessCallBack() {
                            @Override
                            public void onSuccess(int courseId, int status) {
                                //标记更新
                                CourseTool.undateCourse_status_byId(mCourseId, Integer.parseInt(NetConfig.HAVE_SYNC));
                            }
                        }, new UpdateCourse.FailCallBack() {
                    @Override
                    public void onFail(int statusCode) {

                    }
                });
                CommonUilts.showToast("课程修改成功", false);
                //关闭当前activity
                CommonUilts.thisToActivity(TimerActivity.class);
                finish();
                break;
            case R.id.course_detailPge_weekCount_txt_end:
                //选择上课周
                break;
            case R.id.course_detailPge_lessonCount_txt:
                //选择第几节上课
                break;
        }
    }

    /**
     * 设置顶部栏的样式
     */
    private void initTopbar() {
        topBarTitle.setText("编辑课程");
        topBarLeft.setVisibility(View.VISIBLE);
        topBarSave.setVisibility(View.VISIBLE);
    }

    /**
     * 获取上个页面传过来的参数（课程id）
     *
     * @return
     */
    public int getCourseId() {
        int courseId = CommonUilts.getDataID(this);
        return courseId;
    }

    /**
     * 展示课程数据
     */
    private void showCourseData() {
        //需要展示的字段
        String name = course.getName();
        String site = course.getSite();
        String week0fClass = course.getWeek0fClass();//上课周
        int startLesson = course.getStartLesson();
        int endLesson = course.getEndLesson();
        String teacher = course.getTeacher();

        courseDetailPgeNameTxt.setText(name);
        courseDetailPgeSiteTxt.setText(site);
        courseDetailPgeWeekCountTxtEnd.setText(week0fClass);
        courseDetailPgeLessonCountTxt.setText(startLesson + "-" + endLesson + "节");
        courseDetailPgeTeacherTxt.setText(teacher);
    }
}
