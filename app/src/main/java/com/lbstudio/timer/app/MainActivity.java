package com.lbstudio.timer.app;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lbstudio.timer.app.app.TimerActivity;
import com.lbstudio.timer.app.app.activity.LoginAcitvity;
import com.lbstudio.timer.app.app.javabean.Course;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.net.LoginOut;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.lbstudio.timer.app.base.BaseActivity;
import com.lbstudio.timer.app.app.browser.activity.WebViewActivity;
import com.lbstudio.timer.app.app.calendar.CalendarActivity;
import com.lbstudio.timer.app.progress.ProgressActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends BaseActivity {

    @BindView(R.id.addKB)
    Button addKB;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.bt_KB)
    Button btKB;
    @BindView(R.id.bt_calendar)
    Button btCalendar;
    @BindView(R.id.bt_progress)
    Button btProgress;
    @BindView(R.id.bt_app)
    Button btApp;
    @BindView(R.id.bt_fastjson)
    Button btFastjson;
    @BindView(R.id.bt_LitePal)
    Button btLitePal;

    @Override
    protected void init() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.addKB)
    public void setAddKB(View view) {
        String baiDuUrl = "http://jwxt.buu.edu.cn/";
        WebViewActivity.loadUrl(this, baiDuUrl, false);
    }

    @OnClick(R.id.bt_login)
    public void toLogin(View view) {
        CommonUtils.intentToActivity(this, LoginAcitvity.class);
    }

    @OnClick(R.id.bt_KB)
    public void kebiao() {

    }


    @OnClick(R.id.bt_calendar)
    public void rili() {
        CommonUtils.intentToActivity(this, CalendarActivity.class);
    }


    @OnClick(R.id.bt_progress)
    public void intentToActivity() {
        CommonUtils.intentToActivity(this, ProgressActivity.class);

    }

    @OnClick(R.id.bt_app)
    public void app() {
        CommonUtils.intentToActivity(this, TimerActivity.class);
    }


    @OnClick(R.id.bt_fastjson)
    public void fastjson() {
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.get("http://10.12.92.160:8080/P2PInvest/index", new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                String s = new String(bytes);
//                Log.d("debug", "String: " + s);
//                Data object = JSON.parseObject(s, Data.class);
//                Log.d("debug", "onSuccess: " + object.getStatus());
//                Log.d("debug", "onSuccess: " + object.getData()[0]);
//                List<Plan> planList = new ArrayList<>();
//                for (int j = 0; j < 3; j++) {
//                    String as = object.getData()[j];
//                    Plan plan = JSON.parseObject(as, Plan.class);
//                    planList.add(plan);
//                }
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                Log.d("debug", "onFailure: ");
//            }
//        });
    }

    @OnClick(R.id.bt_LitePal_find)
    public void litepal_find() {
        CommonUilts.delectAllPlan();
    }

    @OnClick(R.id.bt_test)
    public void test() {  //选择图片
        CommonUilts.thisToActivity(Test.class);
    }

    @OnClick(R.id.bt_query)
    public void query() {
        CommonUilts.queryAllPlanIndDb();
    }

    @OnClick(R.id.bt_LitePal)
    public void simulatedData() {
        for (int i = 0; i < 40; i++) {
            if (i < 20) {
                Plan plan = new Plan();
                plan.setPlanTitle("计划标题 " + i);
                plan.setPlanContent("计划详细 " + i);
                plan.setPlanStartTime(360 + 60 * i);
                plan.setPlanEndTime(420 + 60 * i);
                plan.setPlanDate(20180912);
                plan.setPlanStatus(10);
                plan.save();
            } else if (i < 35) {
                Plan plan = new Plan();
                plan.setPlanTitle("计划标题 " + i);
                plan.setPlanContent("计划详细 " + i);
                plan.setPlanDate(20180913);
                plan.setPlanStatus(10);
                plan.save();
            } else {
                Plan plan = new Plan();
                plan.setPlanTitle("计划标题 " + i);
                plan.setPlanContent("计划详细 " + i);
                plan.setPlanDate(20180914);
                plan.setPlanStatus(10);
                plan.save();
            }
        }
        CommonUilts.queryAllPlanIndDb();
    }

    @OnClick(R.id.bt_test2)
    public void test2() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Log.d("debug", " height: " + displayMetrics.heightPixels + " width: " + displayMetrics.widthPixels);

    }

    @OnClick({R.id.bt_test4, R.id.bt_test5, R.id.bt_test6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_test4: //模拟课表数据
                for (int i = 0; i < 19; i++) {
                    Log.d("debug", "==============================" + i);
                    if (i < 1) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setTerm(2018);
                        course1.setWeek(1);
                        course1.setStartLesson(1);
                        course1.setEndLesson(2);
                        course1.save();
                    } else if (i < 2) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(1);
                        course1.setTerm(2018);
                        course1.setStartLesson(3);
                        course1.setEndLesson(4);
                        course1.save();
                    } else if (i < 3) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(1);
                        course1.setTerm(2018);
                        course1.setStartLesson(6);
                        course1.setEndLesson(7);
                        course1.save();
                    } else if (i < 4) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(2);
                        course1.setTerm(2018);
                        course1.setStartLesson(4);
                        course1.setEndLesson(5);
                        course1.save();
                    } else if (i < 5) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(2);
                        course1.setTerm(2018);
                        course1.setStartLesson(7);
                        course1.setEndLesson(9);
                        course1.save();
                    } else if (i < 6) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(2);
                        course1.setTerm(2018);
                        course1.setStartLesson(10);
                        course1.setEndLesson(11);
                        course1.save();
                    } else if (i < 7) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(3);
                        course1.setTerm(2018);
                        course1.setStartLesson(1);
                        course1.setEndLesson(2);
                        course1.save();
                    } else if (i < 8) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setTerm(2018);
                        course1.setWeek(3);
                        course1.setStartLesson(4);
                        course1.setEndLesson(5);
                        course1.save();
                    } else if (i < 9) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(3);
                        course1.setTerm(2018);
                        course1.setStartLesson(6);
                        course1.setEndLesson(7);
                        course1.save();
                    } else if (i < 10) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(3);
                        course1.setTerm(2018);
                        course1.setStartLesson(8);
                        course1.setEndLesson(9);
                        course1.save();
                    } else if (i < 11) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(3);
                        course1.setTerm(2018);
                        course1.setStartLesson(10);
                        course1.setEndLesson(11);
                        course1.save();
                    } else if (i < 12) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(4);
                        course1.setTerm(2018);
                        course1.setStartLesson(1);
                        course1.setEndLesson(2);
                        course1.save();
                    } else if (i < 13) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(4);
                        course1.setTerm(2018);
                        course1.setStartLesson(3);
                        course1.setEndLesson(5);
                        course1.save();
                    } else if (i < 14) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setTerm(2018);
                        course1.setWeek(4);
                        course1.setStartLesson(8);
                        course1.setEndLesson(9);
                        course1.save();
                    } else if (i < 15) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(5);
                        course1.setTerm(2018);
                        course1.setStartLesson(1);
                        course1.setEndLesson(2);
                        course1.save();
                    } else if (i < 16) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(5);
                        course1.setTerm(2018);
                        course1.setStartLesson(4);
                        course1.setEndLesson(5);
                        course1.save();
                    } else if (i < 17) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(5);
                        course1.setTerm(2018);
                        course1.setStartLesson(7);
                        course1.setEndLesson(9);
                        course1.save();
                    } else if (i < 18) {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(7);
                        course1.setTerm(2018);
                        course1.setStartLesson(1);
                        course1.setEndLesson(2);
                        course1.save();
                    } else {
                        Course course1 = new Course();
                        course1.setName("课程 " + i);
                        course1.setSite("教室 " + i);
                        course1.setWeek(7);
                        course1.setTerm(2019);
                        course1.setStartLesson(1);
                        course1.setEndLesson(2);
                        course1.save();
                    }
                    Log.d("debug", "+++++++++++++++++++++++++++++++" + i);

                }
                Log.d("debug", "aaaaaaaaaaaaaaaaaaa");
                CommonUilts.showToast("模拟成功", false);
                CommonUilts.queryAllCourse();
                Log.d("debug", "ccccccccccccccccccc");
                break;
            case R.id.bt_test5://查询课表数据
                CommonUilts.queryAllCourse();
                CommonUilts.showToast("查询成功", false);
                break;
            case R.id.bt_test6://清空课表数据
                CommonUilts.delectAllCourse();
                CommonUilts.showToast("清空成功", false);
                break;
        }
    }

    @OnClick(R.id.bt_test9)
    public void loginout(View view) {
        String token = "cb935bc446c5477596d89940552703a6";
        new LoginOut(5, token, new LoginOut.SuccessCallBack() {
            @Override
            public void onSuccess() {

            }
        }, new LoginOut.FailCallBack() {
            @Override
            public void onFail(int status) {

            }
        });
    }
    @OnClick(R.id.bt_test9)
    public void getCOde(View view) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.199.119:9090/timer/user/register/getVerificationCode/1649148826@qq.com", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.d(NetConfig.TAG, "onSuccess: "+new String(bytes));
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.d(NetConfig.TAG, "onFailure: "+new String(bytes));

            }
        });
    }

    @OnClick(R.id.bt_test10)
    public void getOde(View view) {
        CommonUilts.thisToActivity(Test2.class);
    }

    @OnClick(R.id.bt_test11)
    public void getOdse(View view) {
        CommonUilts.thisToActivity(Test.class);
    }
}
