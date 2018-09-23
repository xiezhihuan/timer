package com.lbstudio.timer.app.app.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.ie1e.mdialog.view.ActionSheetDialog;
import com.lbstudio.timer.app.AppManager;
import com.lbstudio.timer.app.MyApplication;
import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.DbTool.CourseTool;
import com.lbstudio.timer.app.app.DbTool.PlanTool;
import com.lbstudio.timer.app.app.TimerActivity;
import com.lbstudio.timer.app.app.browser.activity.WebViewActivity;
import com.lbstudio.timer.app.app.javabean.Course;
import com.lbstudio.timer.app.app.javabean.Course_un_inited;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.net.AddCourse;
import com.lbstudio.timer.app.app.net.DelectCourse;
import com.lbstudio.timer.app.app.net.DelectPlan;
import com.lbstudio.timer.app.app.net.GetData;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.net.NetTool;
import com.lbstudio.timer.app.app.net.PullCourseData;
import com.lbstudio.timer.app.app.net.PullPlanData;
import com.lbstudio.timer.app.app.net.SyncCourse;
import com.lbstudio.timer.app.app.net.SyncPlan;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.LitePal;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUilts {
    static Context appContext = MyApplication.mContext;
    static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * (无参数)跳转到目标页
     *
     * @param clazz 目标页
     */
    public static void thisToActivity(Class clazz) {
        Intent intent = new Intent(MyApplication.mContext, clazz);
        MyApplication.mContext.startActivity(intent);
    }

    public static void thisToActivity(boolean shouldGetData, Class clazz) {
        Intent intent = new Intent(MyApplication.mContext, clazz);
        intent.putExtra("shouldGetData", shouldGetData);
        MyApplication.mContext.startActivity(intent);
    }

    /**
     * (int参数)跳转到目标页
     *
     * @param dataId 计划id
     * @param clazz  目标页
     */
    public static void thisToActivity(int dataId, Class clazz) {
        Intent intent = new Intent(MyApplication.mContext, clazz);
        intent.putExtra("dataId", dataId);
        MyApplication.mContext.startActivity(intent);
    }

    public static void thisToActivity(int dataId, String pageName, Class clazz) {
        Intent intent = new Intent(MyApplication.mContext, clazz);
        intent.putExtra("dataId", dataId);
        intent.putExtra("pageName", pageName);
        MyApplication.mContext.startActivity(intent);
    }

    public static void thisToActivity(String pageName, Class clazz) {
        Intent intent = new Intent(MyApplication.mContext, clazz);
        intent.putExtra("pageName", pageName);
        MyApplication.mContext.startActivity(intent);
    }

    /**
     * 从Intent中获取 dataId
     *
     * @return 返回 dataId
     */
    public static int getDataID(Activity activity) {
        int planID = activity.getIntent().getIntExtra("dataId", -1);
        return planID;
    }


    /**
     * post请求
     *
     * @param url                      请求路径
     * @param params                   请求参数
     * @param responseHandlerInterface 回调函数
     */
    public static void post(String url, RequestParams params, ResponseHandlerInterface responseHandlerInterface) {
        ProgressDialog dialog = getProcessDialog(MyApplication.mContext, "玩命加载中");
        dialog.show();
        client.post(url, params, responseHandlerInterface);
        dialog.dismiss();
    }

    /**
     * 弹出一个Toast
     *
     * @param msg    弹框内容
     * @param isLong 是否为长时间
     */
    public static void showToast(String msg, boolean isLong) {
        Toast.makeText(appContext, msg, isLong == true ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    /**
     * 将字节数组转成字符串
     *
     * @param bytes 字节数组
     * @return 得到的字符串
     */
    public static String byte2string(byte[] bytes) {
        return new String(bytes);
    }

    /**
     * 进度对话框
     * <p>
     * dialog.show(); 显示  开始时调用
     * dialog.dismiss（）消失  结束时调用
     *
     * @param context
     * @param tips
     * @return
     */
    public static ProgressDialog getProcessDialog(Context context, String tips) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage(tips);
        dialog.setCancelable(false);
        return dialog;
    }

    /**
     * 将字符串数字转为整型数字
     *
     * @param text 字符串数字
     * @return 处理后得到的整型数字
     */
    public static int string2int(String text) {
        return Integer.getInteger(text);
    }

    /**
     * 将输入的字符串true和false 转为布尔值true和false
     *
     * @param bboolean 输入的值 规定只能输出true或者false  输入其他值都是返回布尔值false
     * @return
     */
    public static boolean string2boolean(String bboolean) {
        return bboolean.equals("true");//bboolean为true时返回布尔值true，否则返回false
    }


    /**
     * 获得服务器返回的planID
     * 为不用每次都写这段代码，而把它抽取出来
     *
     * @param bytes 服务器返回的内容
     * @return 计划id
     */
//    public static int getRespondPlanID(byte[] bytes) {
//        String result = new String(bytes);
//        Respond_creatPlan respond = JSON.parseObject(result, Respond_creatPlan.class);
//        return respond.getPlanID();
//    }

    /**
     * 将布尔值转成字符串
     *
     * @param bboolean 要转换的布尔值
     * @return 转换得到了字符串
     */
    public static String boolean2string(boolean bboolean) {
        if (bboolean) {
            return "true";
        } else {
            return "false";
        }
    }

    public static void savePlanDataToDb(String planTitle, String planContent, int planDate, int planStartTime, int planEndTime, boolean isRegular) {
        Plan plan = new Plan();
        plan.setPlanTitle(planTitle);
        plan.setPlanContent(planContent);
        plan.setPlanDate(planDate);
        plan.setPlanStartTime(planStartTime);
        plan.setPlanEndTime(planEndTime);
        plan.setRegular(isRegular);
        plan.save();
    }

    /**
     * 在数据库中，删除一条计划数据
     *
     * @param planID 要删除的计划的id
     */
    public static void delectPlanDataInDb(int planID) {
        String planIDd = int2string(planID);
        LitePal.deleteAll(Plan.class, "planID = ?", planIDd);
    }

    public static void undatePlanDataInDb(int conditionID, int planID) {
        String conditonIDd = int2string(conditionID);

        Plan plan = new Plan();
        plan.setId(planID);

        plan.updateAll("planID = ?", conditonIDd);
    }

//***************************************************************************************************************************************************
    //数据库操作

//**********************************************************
    //课程的数据库操作

    /**
     * 在数据库中，新建一个课程
     */
    public static void addCourse(String name, String site, int term, String week0fClass, int week,
                                 int startLesson, int endLesson, String teacher, int date) {
        Course course = new Course();
        course.setName(name);
        course.setSite(site);
        course.setTerm(term);
        course.setWeek0fClass(week0fClass);
        course.setWeek(week);
        course.setStartLesson(startLesson);
        course.setEndLesson(endLesson);
        course.setTeacher(teacher);
        course.setDate(date);
        course.save();

        queryAllCourse();
        showToast("新建成功", false);
    }

    /**
     * 删除所有课程数据
     */
    public static void delectAllCourse() {
        LitePal.deleteAll(Course.class, "id>0");
    }

    /**
     * 查询数据库中所有的课程数据
     *
     * @return
     */
    public static void queryAllCourse() {
        List<Course> courses = LitePal.findAll(Course.class);
        for (Course course : courses) {
            Log.d("debug", "getId_course: " + course.getId());
            Log.d("debug", "getName: " + course.getName());
            Log.d("debug", "getSite: " + course.getSite());
            Log.d("debug", "getTerm: " + course.getTerm());
            Log.d("debug", "getWeek0fClass: " + course.getWeek0fClass());
            Log.d("debug", "getStartLesson: " + course.getStartLesson());
            Log.d("debug", "getEndLesson: " + course.getEndLesson());
            Log.d("debug", "getTeacher: " + course.getTeacher());
            Log.d("debug", "getWeek: " + course.getWeek());
            Log.d("debug", "getDate: " + course.getDate());
            Log.d("debug", "getDate: " + course.getUserId());
        }
    }

    /**
     * 根据学期term，查询课程
     *
     * @param term
     * @return
     */
    public static List<Course> queryCourse_byTerm(int term) {
        String termm = String.valueOf(term);
        List<Course> courseList = LitePal.where("term = ?", termm).find(Course.class);
        return courseList;
    }

    /**
     * 根据id获得一条课程数据
     *
     * @param courseId 查询的id
     * @return 返回课程信息
     */
    public static Course queryTheCourseById(int courseId) {
        String courseId1 = int2string(courseId);
        List<Course> courses = LitePal.where("id = ?", courseId1).find(Course.class);

        //避免空指针
        if (courses.isEmpty()) {
            return null;
        }

        Course course = courses.get(0);
        return course;
    }

//**********************************************************
    //计划的数据库操作


    /**
     * 删除所有计划数据
     */
    public static void delectAllPlan() {
        LitePal.deleteAll(Plan.class, "id>0");
    }

    /**
     * 根据id删除一个计划
     *
     * @param id
     */
    public static void delectPlan_byId(int id) {
        LitePal.delete(Plan.class, id);
        Log.d("debug", "删除id:" + id + "的计划");
    }

    /**
     * 根据id修改计划title、content、date、startTime、endTime、isRegular
     *
     * @param planID
     * @param planTitle
     * @param planContent
     * @param planDate
     * @param planStartTime
     * @param planEndTime
     * @param isRegular
     */
    public static void undatePlanDataInDb(int planID, String planTitle, String planContent,
                                          int planDate, int planStartTime, int planEndTime,
                                          boolean isRegular) {
        Plan plan = new Plan();

        plan.setId(planID);
        plan.setPlanTitle(planTitle);
        plan.setPlanContent(planContent);
        plan.setPlanDate(planDate);
        plan.setPlanStartTime(planStartTime);
        plan.setPlanEndTime(planEndTime);
        plan.setRegular(isRegular);
        plan.setStatus(1);//表示本地更新

        String id = String.valueOf(planID);
        plan.updateAll("id = ?", id);
    }


    /**
     * 根据id修改planDate
     *
     * @param id
     * @param undateDate
     */
    public static void undate_thePlanDate_ofThePlanInDb(int id, int undateDate) {
        Plan plan = new Plan();
        plan.setPlanDate(undateDate);
        plan.update(id);
        Log.d("debug", "根据id:" + id + "修改date为" + undateDate);
    }

    /**
     * 根据id修改spendTime
     *
     * @param id
     * @param spendTime
     */
    public static void undate_thePlanSpendTime_ofThePlanInDb(int id, int spendTime) {
        Plan plan = new Plan();
        plan.setSpendTime(spendTime);
        plan.update(id);
        Log.d("debug", "根据id:" + id + "修改spendTime为" + spendTime);
    }

    /**
     * 根据id修改realEndTime
     *
     * @param id
     * @param realEndTime
     */
    public static void undate_TheRealEndTIme_OfThePlanInDb(int id, int realEndTime) {
        Plan plan = new Plan();
        plan.setRealEndTime(realEndTime);
        plan.updateAll("id = ?", String.valueOf(id));
        Log.d("debug", "将id：" + id + "计划的realEndTime改为" + realEndTime);
    }


    /**
     * 查询数据库中所有的计划数据
     */
    public static void queryAllPlanIndDb() {
        List<Plan> plans = LitePal.findAll(Plan.class);
        for (Plan plan : plans) {
            Log.d("debug", "getPlanID: " + plan.getId());
            Log.d("debug", "getPlanTitle: " + plan.getPlanTitle());
            Log.d("debug", "getPlanContent: " + plan.getPlanContent());
            Log.d("debug", "getPlanDate: " + plan.getPlanDate());
            Log.d("debug", "getPlanStatus: " + plan.getPlanStatus());
            Log.d("debug", "getPlanStartTime: " + plan.getPlanStartTime());
            Log.d("debug", "getPlanEndTime: " + plan.getPlanEndTime());
            Log.d("debug", "isRegular: " + plan.isRegular());
        }
    }

    /**
     * 根据日期查找所有计划数据
     *
     * @param planDate
     * @return
     */
    public static List<Plan> queryPlanDataByDate(String planDate) {
        List<Plan> plans = LitePal.where("planDate = ?", planDate).find(Plan.class);
        return plans;
    }

    /**
     * 根据id获得一条计划数据
     *
     * @param planID 查询的id
     * @return 返回计划信息
     */
    public static Plan queryThePlanById(int planID) {
        String planIDd = int2string(planID);
        List<Plan> plans = LitePal.where("id = ?", planIDd).find(Plan.class);

        //避免空指针
        if (plans.isEmpty()) {
            return null;
        }

        Plan plan = plans.get(0);
        return plan;
    }

//**********************************************************************************************************************************************************
//数据类型转换

    /**
     * 整型转字符串
     *
     * @param number 整型数据
     * @return 转换后得到的字符串
     */
    public static String int2string(int number) {
        return String.valueOf(number);
    }


    /**
     * 1dp---1px;
     * 1dp---0.75px;
     * 1dp---0.5px;
     * ....
     *
     * @param dp
     * @return
     */
    public static int dp2px(int dp) {
        float density = getMyApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    public static int px2dp(int px) {
        float density = getMyApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = getMyApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获得MyApplication的mContext
     *
     * @return
     */
    public static Context getMyApplicationContext() {
        return MyApplication.mContext;
    }

    /**
     * 获得现在的时间  yyyy年MM月dd日 HH:mm:ss
     *
     * @return 返回现在的时间
     */
    public static String getNowTime() {
        //获得网路时间
        Date netTime = getNetTime();
        Log.d("debug", "Date: " + netTime.toString());
        //设置时间格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss

        return simpleDateFormat.format(netTime);
    }

    /**
     * 将日期转为数字
     * 计算2018-8-21 处于2018年的第几天
     *
     * @param month
     * @param day
     * @return
     */
    public static int getDateNumber(int month, int day) {
        int dateNumber = 0;
        switch (month) {
            case 1:
                dateNumber = 0;
                break;
            case 2:
                dateNumber = 31;
                break;
            case 3:
                dateNumber = 31 + 28;
                break;
            case 4:
                dateNumber = 31 + 28 + 31;
                break;
            case 5:
                dateNumber = 31 + 28 + 31 + 30;
                break;
            case 6:
                dateNumber = 31 + 28 + 31 + 30 + 31;
                break;
            case 7:
                dateNumber = 31 + 28 + 31 + 30 + 31 + 30;
                break;
            case 8:
                dateNumber = 31 + 28 + 31 + 30 + 31 + 30 + 31;
                break;
            case 9:
                dateNumber = 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31;
                break;
            case 10:
                dateNumber = 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30;
                break;
            case 11:
                dateNumber = 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31;
                break;
            case 12:
                dateNumber = 31 + 28 + 31 + 30 + 31 + 30 + 31 + 31 + 30 + 31 + 30;
                break;
        }
        dateNumber += day;
        return dateNumber;
    }

    /**
     * 将星期文字转为数字
     * 如：将周三转为数字3
     *
     * @param week
     * @return
     */
    public static int getWeekNumber(String week) {
        int weekNumber = 0;
        switch (week) {
            case "周一":
                weekNumber = 1;
                break;
            case "周二":
                weekNumber = 2;
                break;
            case "周三":
                weekNumber = 3;
                break;
            case "周四":
                weekNumber = 4;
                break;
            case "周五":
                weekNumber = 5;
                break;
            case "周六":
                weekNumber = 6;
                break;
            case "周日":
                weekNumber = 7;
                break;
        }
        return weekNumber;
    }

    /**
     * 将小时转为分钟
     * 如：  23：15 是今天的第几分钟
     *
     * @param hour
     * @param minute
     * @return
     */
    public static int getTimeNumber(int hour, int minute) {
        int timeNumber = 0;
        for (int i = 0; i < hour; i++) {
            timeNumber += 60;
        }
        timeNumber += minute;
        return timeNumber;
    }

    /**
     * 将英文缩写的月份转为数字   Jan --> 01
     *
     * @param month_Endlish
     * @return
     */
    public static String getMOnth_AbcTO123(String month_Endlish) {
        String month = null;
        switch (month_Endlish) {
            case "Jan":
                month = "01";
                break;
            case "Feb":
                month = "02";
                break;
            case "Mar":
                month = "03";
                break;
            case "Apr":
                month = "04";
                break;
            case "May":
                month = "05";
                break;
            case "Jun":
                month = "06";
                break;
            case "Jul":
                month = "07";
                break;
            case "Aug":
                month = "08";
                break;
            case "Sep":
                month = "09";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;
            default:
                month = "99";
                break;
        }
        return month;
    }

    public static void getDateRange() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Date nowTime = getNetTime(); // Fri Aug 24 18:16:46 GMT+08:00 2018
                String nowTime_string = nowTime.toString();
                String[] split = nowTime_string.split(" ");
                String year = split[5];
                String month_English = split[1];
                String month = getMOnth_AbcTO123(month_English);
                String day = split[2];
                String week = split[0];

                String startQueryDay = null;
                String endQueryDay = null;
                String endQueryMonth = null;
                String endQueryYear = null;
                switch (week) {
                    case "Mon":
                        startQueryDay = day;
                        endQueryDay = int2string(Integer.parseInt(day) + 6);
                        //处理一周属于两个月的情况
                        Map<String, String> date = isOverMax(year, month, endQueryDay);
                        boolean isOverMax = string2boolean(date.get("isOverMax"));
                        if (isOverMax) {
                            endQueryDay = date.get("endQueryDay");
                            endQueryMonth = date.get("endQueryMonth");
                            endQueryYear = date.get("endQueryYear");
                        }
                        break;
                    case "Tue":
                        startQueryDay = int2string(Integer.parseInt(day) - 1);
                        endQueryDay = int2string(Integer.parseInt(day) + 5);
                        //处理一周属于两个月的情况
                        Map<String, String> date2 = isOverMax(year, month, endQueryDay);
                        boolean isOverMax2 = string2boolean(date2.get("isOverMax"));
                        if (isOverMax2) {
                            endQueryDay = date2.get("endQueryDay");
                            endQueryMonth = date2.get("endQueryMonth");
                            endQueryYear = date2.get("endQueryYear");
                        }
                        break;
                    case "Wed":
                        startQueryDay = int2string(Integer.parseInt(day) - 2);
                        endQueryDay = int2string(Integer.parseInt(day) + 4);
                        //处理一周属于两个月的情况
                        Map<String, String> date3 = isOverMax(year, month, endQueryDay);
                        boolean isOverMax3 = string2boolean(date3.get("isOverMax"));
                        if (isOverMax3) {
                            endQueryDay = date3.get("endQueryDay");
                            endQueryMonth = date3.get("endQueryMonth");
                            endQueryYear = date3.get("endQueryYear");
                        }
                        break;
                    case "Thu":
                        startQueryDay = int2string(Integer.parseInt(day) - 3);
                        endQueryDay = int2string(Integer.parseInt(day) + 3);
                        //处理一周属于两个月的情况
                        Map<String, String> date4 = isOverMax(year, month, endQueryDay);
                        boolean isOverMax4 = string2boolean(date4.get("isOverMax"));
                        if (isOverMax4) {
                            endQueryDay = date4.get("endQueryDay");
                            endQueryMonth = date4.get("endQueryMonth");
                            endQueryYear = date4.get("endQueryYear");
                        }
                        break;
                    case "Fri":
                        startQueryDay = int2string(Integer.parseInt(day) - 4);
                        endQueryDay = int2string(Integer.parseInt(day) + 2);
                        //处理一周属于两个月的情况
                        Map<String, String> date5 = isOverMax(year, month, endQueryDay);
                        boolean isOverMax5 = string2boolean(date5.get("isOverMax"));
                        if (isOverMax5) {
                            endQueryDay = date5.get("endQueryDay");
                            endQueryMonth = date5.get("endQueryMonth");
                            endQueryYear = date5.get("endQueryYear");
                        }
                        break;
                    case "Sat":
                        startQueryDay = int2string(Integer.parseInt(day) - 5);
                        endQueryDay = int2string(Integer.parseInt(day) + 1);
                        //处理一周属于两个月的情况
                        Map<String, String> date6 = isOverMax(year, month, endQueryDay);
                        boolean isOverMax6 = string2boolean(date6.get("isOverMax"));
                        if (isOverMax6) {
                            endQueryDay = date6.get("endQueryDay");
                            endQueryMonth = date6.get("endQueryMonth");
                            endQueryYear = date6.get("endQueryYear");
                        }
                        break;
                    case "Sun":
                        startQueryDay = int2string(Integer.parseInt(day) - 6);
                        endQueryDay = day;
                        //处理一周属于两个月的情况
                        Map<String, String> date7 = isOverMax(year, month, endQueryDay);
                        boolean isOverMax7 = string2boolean(date7.get("isOverMax"));
                        if (isOverMax7) {
                            endQueryDay = date7.get("endQueryDay");
                            endQueryMonth = date7.get("endQueryMonth");
                            endQueryYear = date7.get("endQueryYear");
                        }
                        break;
                    default:
                        startQueryDay = "99";
                        endQueryDay = "99";
                        break;
                }
                Map<String, String> date = new HashMap<>();
                date.put("startYear", year);
                date.put("startMonth", month);
                date.put("startDay", day);
                date.put("endYear", endQueryDay);
                date.put("endMonth", endQueryMonth);
                date.put("endDay", endQueryDay);
            }
        }).start();

    }

    public static Map<String, String> isOverMax(String year, String month, String endQueryDay) {
        boolean isOverMax = false;
        switch (month) {
            case "1":
                if (Integer.parseInt(endQueryDay) > 31) {
                    int day2 = Integer.parseInt(endQueryDay) - 31;
                    endQueryDay = int2string(day2);
                    month = int2string(Integer.parseInt(month) + 1);
                    isOverMax = true;
                }
                break;
            case "2":
                if (Integer.parseInt(endQueryDay) > 28) {
                    int day2 = Integer.parseInt(endQueryDay) - 28;
                    endQueryDay = int2string(day2);
                    month = int2string(Integer.parseInt(month) + 1);
                    isOverMax = true;
                }
                break;
            case "3":
                if (Integer.parseInt(endQueryDay) > 31) {
                    int day2 = Integer.parseInt(endQueryDay) - 31;
                    endQueryDay = int2string(day2);
                    month = int2string(Integer.parseInt(month) + 1);
                    isOverMax = true;
                }
                break;
            case "4":
                if (Integer.parseInt(endQueryDay) > 30) {
                    int day2 = Integer.parseInt(endQueryDay) - 30;
                    endQueryDay = int2string(day2);
                    month = int2string(Integer.parseInt(month) + 1);
                    isOverMax = true;
                }
                break;
            case "5":
                if (Integer.parseInt(endQueryDay) > 31) {
                    int day2 = Integer.parseInt(endQueryDay) - 31;
                    endQueryDay = int2string(day2);
                    month = int2string(Integer.parseInt(month) + 1);
                    isOverMax = true;
                }
                break;
            case "6":
                if (Integer.parseInt(endQueryDay) > 30) {
                    int day2 = Integer.parseInt(endQueryDay) - 30;
                    endQueryDay = int2string(day2);
                    month = int2string(Integer.parseInt(month) + 1);
                    isOverMax = true;
                }
                break;
            case "7":
                if (Integer.parseInt(endQueryDay) > 31) {
                    int day2 = Integer.parseInt(endQueryDay) - 31;
                    endQueryDay = int2string(day2);
                    month = int2string(Integer.parseInt(month) + 1);
                    isOverMax = true;
                }
                break;
            case "8":
                if (Integer.parseInt(endQueryDay) > 31) {
                    int day2 = Integer.parseInt(endQueryDay) - 31;
                    endQueryDay = int2string(day2);
                    month = int2string(Integer.parseInt(month) + 1);
                    isOverMax = true;
                }
                break;
            case "9":
                if (Integer.parseInt(endQueryDay) > 30) {
                    int day2 = Integer.parseInt(endQueryDay) - 30;
                    endQueryDay = int2string(day2);
                    month = int2string(Integer.parseInt(month) + 1);
                    isOverMax = true;
                }
                break;
            case "10":
                if (Integer.parseInt(endQueryDay) > 31) {
                    int day2 = Integer.parseInt(endQueryDay) - 31;
                    endQueryDay = int2string(day2);
                    month = int2string(Integer.parseInt(month) + 1);
                    isOverMax = true;
                }
                break;
            case "11":
                if (Integer.parseInt(endQueryDay) > 30) {
                    int day2 = Integer.parseInt(endQueryDay) - 30;
                    endQueryDay = int2string(day2);
                    month = int2string(Integer.parseInt(month) + 1);
                    isOverMax = true;
                }
                break;
            case "12":
                if (Integer.parseInt(endQueryDay) > 31) {
                    int day2 = Integer.parseInt(endQueryDay) - 31;
                    endQueryDay = int2string(day2);
                    month = "1";
                    year += 1;
                    isOverMax = true;
                }
                break;
        }
        String isOverMaxx = boolean2string(isOverMax);
        Map<String, String> date = new HashMap<>();
        date.put("endQueryYear", year);
        date.put("endQueryMonth", month);
        date.put("endQueryDay", endQueryDay);
        date.put("isOverMax", isOverMaxx);
        return date;
    }

    /**
     * 输入百分比 获得该手机的百分比像素值
     *
     * @param percent
     * @return
     */
    public static int percent2px_height(float percent) {
        DisplayMetrics displayMetrics = getMyApplicationContext().getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int i = (int) (height * percent);
        Log.d("debug", "width: " + width);

        Log.d("debug", "percent2px_width: " + i);
        return i;
    }

    /**
     * * 输入百分比 获得该手机的百分比像素值
     *
     * @param percent
     * @return
     */
    public static int percent2px_width(float percent) {
        DisplayMetrics displayMetrics = getMyApplicationContext().getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int i = (int) (width * percent);
        Log.d("debug", "width: " + width);

        Log.d("debug", "percent2px_width: " + i);
        return i;
    }

    /**
     * 获得网络时间 请求失败时获得本地时间
     * 在子线程中调用
     *
     * @return
     */
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
            return new Date(System.currentTimeMillis());
        }
    }

    /**
     * 返回今日日期的数字格式 20180828
     * 在子线程中使用此方法
     *
     * @return
     */
    public static String getNowDateNum() {
        Date nowTime = CommonUilts.getNetTime(); // Fri Aug 24 18:16:46 GMT+08:00 2018
        String nowTime_string = nowTime.toString();
        String[] split = nowTime_string.split(" ");
        String year = split[5]; //2018
        String month_English = split[1]; //Jun
        String month = CommonUilts.getMOnth_AbcTO123(month_English); //8
        String day = split[2]; //28
        Log.d("debug", "nowTime: " + year + month + day);
        return year + month + day;
    }

    /**
     * 获得当前时间是第几分钟
     *
     * @return
     */
    public static int getNowMinuteNum() {
        Date date = getNetTime();
        String date_ToString = date.toString();
        String[] date_array = date_ToString.split(" ");
        String[] hour_containS = date_array[3].split(":");
        String hour = hour_containS[0];
        String minute = hour_containS[1];
        int number = getTimeNumber(Integer.parseInt(hour), Integer.parseInt(minute));
        return number;
    }

    public static String getWeek_abc2aoe(String week_English) {
        String week_Chinese = null;
        switch (week_English) {
            case "Mon":
                week_Chinese = "周一";
                break;
            case "Tue":
                week_Chinese = "周二";
                break;
            case "Wed":
                week_Chinese = "周三";
                break;
            case "Thu":
                week_Chinese = "周四";
                break;
            case "Fri":
                week_Chinese = "周五";
                break;
            case "Sat":
                week_Chinese = "周六";
                break;
            case "Sun":
                week_Chinese = "周日";
                break;
            default:
                week_Chinese = " ";
                break;
        }
        return week_Chinese;
    }

    /**
     * 缓存“正在展示的fragment标号”
     */
    public static void cacheFragmentNum(Context context, String fragmentNum) {
        SharedPreferences.Editor edit = context.getSharedPreferences(NetConfig.APP_ID, Context.MODE_PRIVATE).edit();
        edit.putString(NetConfig.KEY_FRAGMENTNUM, fragmentNum);
        edit.commit();
    }

    public static void cacheHtml(Context context, String html) {
        SharedPreferences.Editor edit = context.getSharedPreferences(NetConfig.APP_ID, Context.MODE_PRIVATE).edit();
        edit.putString(NetConfig.KEY_HTML, html);
        edit.commit();
    }

    public static String getHtml(Context context) {
        return context.getSharedPreferences(NetConfig.APP_ID, Context.MODE_PRIVATE).getString(NetConfig.KEY_HTML, null);
    }

    /**
     * 获得“正在展示的fragment标号”
     *
     * @param context
     * @return
     */
    public static String getFragmentNum(Context context) {
        return context.getSharedPreferences(NetConfig.APP_ID, Context.MODE_PRIVATE).getString(NetConfig.KEY_FRAGMENTNUM, "1");
    }

    /**
     * 获得本周的日期范围 周一的日期（09月08日）-周日的日期（09月15日）
     *
     * @return
     */
    public static Map<String, String> getWeekDateRange() {

        Date nowTime = CommonUilts.getNetTime(); // Fri Aug 24 18:16:46 GMT+08:00 2018
        String nowTime_string = nowTime.toString();
        String[] split = nowTime_string.split(" ");
        String year = split[5]; //2018
        String month_English = split[1]; //Jun
        String month = CommonUilts.getMOnth_AbcTO123(month_English); //8
        String day = split[2]; //28
        String week = split[0];

        String startQueryDay = null;
        String startQueryMonth = month;
        String startQueryYear = year;
        String endQueryDay = null;
        String endQueryMonth = month;
        String endQueryYear = year;
        switch (week) {
            case "Mon":
                startQueryDay = day;
                endQueryDay = CommonUilts.int2string(Integer.parseInt(day) + 6);
                //处理一周属于两个月的情况
                Map<String, String> date = CommonUilts.isOverMax(year, month, endQueryDay);
                boolean isOverMax = CommonUilts.string2boolean(date.get("isOverMax"));
                if (isOverMax) {
                    endQueryDay = date.get("endQueryDay");
                    endQueryMonth = date.get("endQueryMonth");
                    endQueryYear = date.get("endQueryYear");
                }
                break;
            case "Tue":
                startQueryDay = CommonUilts.int2string(Integer.parseInt(day) - 1);
                endQueryDay = CommonUilts.int2string(Integer.parseInt(day) + 5);
                //处理一周属于两个月的情况
                Map<String, String> date2 = CommonUilts.isOverMax(year, month, endQueryDay);
                boolean isOverMax2 = CommonUilts.string2boolean(date2.get("isOverMax"));
                if (isOverMax2) {
                    endQueryDay = date2.get("endQueryDay");
                    endQueryMonth = date2.get("endQueryMonth");
                    endQueryYear = date2.get("endQueryYear");
                }
                break;
            case "Wed":
                startQueryDay = CommonUilts.int2string(Integer.parseInt(day) - 2);
                endQueryDay = CommonUilts.int2string(Integer.parseInt(day) + 4);
                //处理一周属于两个月的情况
                Map<String, String> date3 = CommonUilts.isOverMax(year, month, endQueryDay);
                boolean isOverMax3 = CommonUilts.string2boolean(date3.get("isOverMax"));
                if (isOverMax3) {
                    endQueryDay = date3.get("endQueryDay");
                    endQueryMonth = date3.get("endQueryMonth");
                    endQueryYear = date3.get("endQueryYear");
                }
                break;
            case "Thu":
                startQueryDay = CommonUilts.int2string(Integer.parseInt(day) - 3);
                endQueryDay = CommonUilts.int2string(Integer.parseInt(day) + 3);
                //处理一周属于两个月的情况
                Map<String, String> date4 = CommonUilts.isOverMax(year, month, endQueryDay);
                boolean isOverMax4 = CommonUilts.string2boolean(date4.get("isOverMax"));
                if (isOverMax4) {
                    endQueryDay = date4.get("endQueryDay");
                    endQueryMonth = date4.get("endQueryMonth");
                    endQueryYear = date4.get("endQueryYear");
                }
                break;
            case "Fri":
                startQueryDay = CommonUilts.int2string(Integer.parseInt(day) - 4);
                endQueryDay = CommonUilts.int2string(Integer.parseInt(day) + 2);
                //处理一周属于两个月的情况
                Map<String, String> date5 = CommonUilts.isOverMax(year, month, endQueryDay);
                boolean isOverMax5 = CommonUilts.string2boolean(date5.get("isOverMax"));
                if (isOverMax5) {
                    endQueryDay = date5.get("endQueryDay");
                    endQueryMonth = date5.get("endQueryMonth");
                    endQueryYear = date5.get("endQueryYear");
                }
                break;
            case "Sat":
                startQueryDay = CommonUilts.int2string(Integer.parseInt(day) - 5);
                endQueryDay = CommonUilts.int2string(Integer.parseInt(day) + 1);
                //处理一周属于两个月的情况
                Map<String, String> date6 = CommonUilts.isOverMax(year, month, endQueryDay);
                boolean isOverMax6 = CommonUilts.string2boolean(date6.get("isOverMax"));
                if (isOverMax6) {
                    endQueryDay = date6.get("endQueryDay");
                    endQueryMonth = date6.get("endQueryMonth");
                    endQueryYear = date6.get("endQueryYear");
                }
                break;
            case "Sun":
                startQueryDay = CommonUilts.int2string(Integer.parseInt(day) - 6);
                endQueryDay = day;
                //处理一周属于两个月的情况
                Map<String, String> date7 = CommonUilts.isOverMax(year, month, endQueryDay);
                boolean isOverMax7 = CommonUilts.string2boolean(date7.get("isOverMax"));
                if (isOverMax7) {
                    endQueryDay = date7.get("endQueryDay");
                    endQueryMonth = date7.get("endQueryMonth");
                    endQueryYear = date7.get("endQueryYear");
                }
                break;
            default:
                startQueryDay = "99";
                endQueryDay = "99";
                break;
        }

        if (Integer.parseInt(startQueryDay) < 10) {
            startQueryDay = "0" + startQueryDay;
        }
        if (Integer.parseInt(endQueryDay) < 10) {
            endQueryDay = "0" + endQueryDay;
        }

        String startTime = startQueryYear + startQueryMonth + startQueryDay;
        String endTime = endQueryYear + endQueryMonth + endQueryDay;
        Log.d("svsavs", "startTime: " + startTime + "  endTime:" + endTime);

        Map<String, String> dateRange = new HashMap<>();
        dateRange.put("startTime", startTime);
        dateRange.put("endTime", endTime);

        return dateRange;
    }

    public static void showDialog(Context context, String txt) {
        new ActionSheetDialog(context)
                .builder()
                .setTitle(context.getString(R.string.tip))
                .setCanceledOnTouchOutside(true)
                .setDialogLocation(Gravity.CENTER)
                .addSheetItem(txt, ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {

                    }
                })
                .show();
    }

    public static void showDialog_finish(final Context context, String txt) {
        new ActionSheetDialog(context)
                .builder()
                .setTitle(context.getString(R.string.tip))
                .setCanceledOnTouchOutside(true)
                .setDialogLocation(Gravity.CENTER)
                .addSheetItem(txt, ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        CommonUilts.thisToActivity(TimerActivity.class);
                        AppManager.getInstance().removeCurrent();
                    }
                })
                .show();
    }


    public static List<Course_un_inited> initHtmlData(String html) {
        String span = null;
        String regex = "<span.*?>[\\s\\S]*<\\/span>";
        Matcher matcher = Pattern.compile(regex).matcher(html);
        while (matcher.find()) {
            span = matcher.group();
            Log.d(NetConfig.TAG, "getHtml: " + span);
        }
//        <option selected="selected" value="2018-2019">2018-2019</option>
        String term_option = null;
        regex = "<option selected=\"selected\" value=.*?>\\d{4}-\\d{4}(<\\/option>){1}";
        Matcher termOptionMatcher = Pattern.compile(regex).matcher(span);
        while (termOptionMatcher.find()) {
            term_option = termOptionMatcher.group();
            Log.d(NetConfig.TAG, "term: " + term_option);
        }
        String term = null;//todo 学年：2018-2019
        regex = "\\d{4}-\\d{4}";
        Matcher termMatcher = Pattern.compile(regex).matcher(term_option);
        while (termMatcher.find()) {
            term = termMatcher.group();
            Log.d(NetConfig.TAG, "term: " + term + "学年");
        }

//<table id="Table1" class="blacktab" bordercolor="Black" border="0" width="100%">
        String table; //todo 表格里的数据
        regex = "<table id=\"Table1\".*?>[\\s\\S]*<\\/table>";
        Matcher tableMatcher = Pattern.compile(regex).matcher(span);
        if (tableMatcher.find()) {
            table = tableMatcher.group();
            Log.d("tabletable", "table: " + table);
        }

        Document document = Jsoup.parse(html);
        Element table1 = document.getElementById("Table1");
        Log.d("FDSADFS", "table1: " + table1.text());
        Elements tr = table1.getElementsByTag("tr"); //所有课程的集合

        List<List<String>> lines = new ArrayList<>();
        for (int j = 0; j < tr.size(); j++) {
            Element element = tr.get(j); //某一行课程
            Log.d("fsadfdsfionbv", "element: " + element.text());
            List<String> lessons = new ArrayList<>();
            for (int k = 0; k < element.childNodeSize() - 2; k++) {
                Element child = element.child(k);//某一课程
                String text = child.text();
                Log.d("dfdsifsnverop932", "child.text(): " + text);
                if (!text.equals("")) {
                    lessons.add(text);
                }
            }
            lines.add(lessons);
        }

        List<List<String>> effectiveLines = new ArrayList<>();  //有效的行课程数据  即 有效的课程信息 的集合
        for (List<String> list : lines) {
            List<String> effectiveLessons = new ArrayList<>();  //有效的课程信息
            for (String a : list) {
                String firstByte = a.split("")[1];
                Log.d("cdg44443", "firstByte： " + firstByte);
                if (!firstByte.equals("时") && !firstByte.equals("星") && !firstByte.equals("早") &&
                        !firstByte.equals("上") && !firstByte.equals("下") && !firstByte.equals("晚") && !firstByte.equals("第")) {
                    effectiveLessons.add(a);
                    Log.d("ojmoilkn ", "a: " + a);
                }
//                Log.d("get", "child.text(): " + a);
            }
            effectiveLines.add(effectiveLessons);
        }

        Log.d("fsafer2322", "getHtml: " + effectiveLines.size());
        List<String> electives = new ArrayList<>();
        List<Course_un_inited> courseList = new ArrayList<>();
        for (List<String> list : effectiveLines) {
            for (String s : list) {
                Log.d("fsda", "ddddd: ");
                Log.d("543543fdffffb43", "course: " + s);

                //判断是否是选修课 选出选修课集合       旅游摄影 {第1-16周|2节/周} 刘啸/王建军 综实B202【 】
                String regexx = "\\{.*\\|(?!单)(?!双).*?\\}";
                Matcher electiveMatcher = Pattern.compile(regexx).matcher(s);
                if (electiveMatcher.find()) {
                    electives.add(s);
                    Log.d("sgdfgfdhdfhhhhhh", "elective: " + s);

                    //调试
                    String elective = electiveMatcher.group();
                    Log.d("elective", "elective: " + elective);
                } else {
                    //选出课名
                    String[] nameSplit = s.split(" ");
                    String name = nameSplit[0];
                    Log.d("namename", "name: " + name);

                    //选出周几
                    String[] weekSplit = s.split(" ");
                    String weekName = nameSplit[1].substring(0, 2);
                    Log.d("weekName", "weekName:" + weekName);
//

                    //选出第几周第几节
                    Pattern number = Pattern.compile("\\d+");
                    Matcher week = Pattern.compile("\\{第.*?周").matcher(s);
                    List<String> weekNum = new ArrayList<>();
                    if (week.find()) {
                        Log.d("fsdfsdbbb", "week.group(): " + week.group());
                        Matcher weekMatcher = number.matcher(week.group());
                        while (weekMatcher.find()) {
                            weekNum.add(weekMatcher.group());
                        }
                    }
                    Matcher clazz = Pattern.compile("第.*?节").matcher(s);
                    List<String> clazzNum = new ArrayList<>();
                    if (clazz.find()) {
                        Matcher clazzMatcher = number.matcher(clazz.group());
                        while (clazzMatcher.find()) {
                            clazzNum.add(clazzMatcher.group());
                        }
                    }

                    Log.d("get", "child.text(): " + s);
                    String[] split = s.split(" ");  //对某节课程信息经行切片 “人工智能概论 周四第8,9节{第1-16周} 马楠/李佳洪/李德毅 综实A206”

                    //选出教师
                    String teacher = nameSplit[2];
                    Log.d("iiiiiiiiinnjknj", "getHtml: " + teacher);

                    //选出教室地点
                    String site = nameSplit[3];
                    Log.d("fsdfsfrewwewewe", "getHtml: " + site);

                    int termYear = Integer.parseInt(term.split("-")[0]);
                    int weekNumber = CommonUilts.getWeekNumber(weekName);
                    Log.d("agafsafs", "termYear: " + termYear);
                    Log.d("agafsafs", "weekNumber: " + weekNumber);
                    String startWeek = weekNum.get(0);
                    String endWeek = weekNum.get(1);
                    Log.d("ggfgfgfgfgfgf", "startWeek:" + startWeek + " endWeek: " + endWeek);
                    String startLesson = clazzNum.get(0);
                    String endLesson = "";
                    if (clazzNum.size() > 1) {
                        endLesson = clazzNum.get(1);
                    } else {
                        endLesson = clazzNum.get(0);

                    }
                    String weekStr = "";
                    for (int i = Integer.parseInt(startWeek); i <= Integer.parseInt(endWeek); i++) {
                        weekStr += i + ",";
                    }
                    Log.d("kokokokok", "weekStr: " + weekStr);
                    Log.d("iuiuububj", "startLesson: " + startLesson + "  endLesson：" + endLesson);
                    Course_un_inited course = new Course_un_inited();
                    course.setName(name);
                    course.setSite(site);
                    course.setTerm(termYear);
                    course.setWeek(weekNumber);
                    course.setWeek0fClass(weekStr);
                    course.setTeacher(teacher);
                    course.setStartLesson(Integer.parseInt(startLesson));
                    course.setEndLesson(Integer.parseInt(endLesson));
                    courseList.add(course);
                    Log.d("43242rert", "name:" + name + " site:" + site + " termYear:" + termYear + " weekNumber:" + weekNumber + " weekStr:" + weekStr + " teacher:" + teacher + " startLesson:" + startLesson + " endLesson：" + endLesson);
                }
            }
        }

        return courseList;
    }

    public static void save(String html) {
        //处理html后得到的数据
        List<Course_un_inited> courseList = initHtmlData(html);

        Log.d("lpkppk", "courseList: " + courseList.size());
        //“人工智能概论 周四第8,9节{第1-16周} 马楠/李佳洪/李德毅 综实A206”

        //今天的日期
        String nowDateNum = CommonUilts.getNowDateNum();

        //遍历 添加到
        for (Course_un_inited course : courseList) {
            String name = course.getName();
            String site = course.getSite();
            int term1 = course.getTerm();
            String week0fClass = course.getWeek0fClass();
            int week = course.getWeek();
            int startLesson = course.getStartLesson();
            int endLesson = course.getEndLesson();
            String teacher = course.getTeacher();

            //本地添加
            CourseTool.addCourse(name, site, term1, week0fClass, week, startLesson, endLesson, teacher, Integer.parseInt(nowDateNum), Integer.parseInt(NetConfig.UPDATE));
            //云端添加
            String userId = NetConfig.getUserId(MyApplication.mContext);
            String token = NetConfig.getToken(MyApplication.mContext);
            String nowDateNum1 = CommonUilts.getNowDateNum();
            new AddCourse(MyApplication.mContext, Integer.parseInt(userId), token, name, site, term1, week0fClass, week, startLesson,
                    endLesson, teacher, Integer.parseInt(nowDateNum1), new AddCourse.SuccessCallBack() {
                @Override
                public void onSuccess(int courseId, int status) {

                }
            }, new AddCourse.FailCallBack() {
                @Override
                public void onFail(int status) {
                    if (status == NetConfig.TOKEN_INVALID) {
                        NetTool.reLogin(MyApplication.mContext);
                    }
                }
            });
        }
    }

    public static void cacheShouldGetData(Context context, boolean shouldGetData) {
        SharedPreferences.Editor edit = context.getSharedPreferences(NetConfig.APP_ID, Context.MODE_PRIVATE).edit();
        edit.putBoolean("shouldGetData", shouldGetData);
        edit.commit();
    }

    public static boolean getShouldGetData(Context context) {
        return context.getSharedPreferences(NetConfig.APP_ID, Context.MODE_PRIVATE).getBoolean("shouldGetData", false);

    }


    /**
     * 缓存获取的课表链接参数
     *
     * @param context
     * @param urlParams
     */
    public static void cacheUrlParams(Context context, Map<String, String> urlParams) {
        SharedPreferences.Editor edit = context.getSharedPreferences(NetConfig.APP_ID, Context.MODE_PRIVATE).edit();
        edit.putString(NetConfig.KEY_NUMBER, urlParams.get("number"));
        edit.putString(NetConfig.KEY_NAME, urlParams.get("name"));
        edit.putString(NetConfig.KEY_GNMKDM, urlParams.get("gnmkdm"));
        edit.commit();
    }

    /**
     * 从缓存中获取课表链接参数
     * @param context
     * @return
     */
    public static Map<String, String> getUrlParams(Context context) {
        String number = context.getSharedPreferences(NetConfig.APP_ID, Context.MODE_PRIVATE).getString(NetConfig.KEY_NUMBER, null);
        String name = context.getSharedPreferences(NetConfig.APP_ID, Context.MODE_PRIVATE).getString(NetConfig.KEY_NAME, null);
        String gnmkdm = context.getSharedPreferences(NetConfig.APP_ID, Context.MODE_PRIVATE).getString(NetConfig.KEY_GNMKDM, null);
        Map<String, String> urlParamsMap = new HashMap<>();
        urlParamsMap.put(NetConfig.KEY_NUMBER, number);
        urlParamsMap.put(NetConfig.KEY_NAME, name);
        urlParamsMap.put(NetConfig.KEY_GNMKDM, gnmkdm);

        return urlParamsMap;
    }

    public static void pullData(final Context context, boolean shouldGetDate, String userId, String token) {
        final ProgressDialog dialog = CommonUilts.getProcessDialog(context, "正在同步数据");
        dialog.show();
        if (shouldGetDate) {
           new GetData(context, shouldGetDate, userId, token, new GetData.SuccessCallback() {
               @Override
               public void onSuccess() {
                   dialog.dismiss();

               }
           }, new GetData.FailCallBack() {
               @Override
               public void onFail(int status) {
                   dialog.dismiss();
                   NetTool.chackFailStatus(context,status);

               }
           });
        }
        CommonUilts.cacheShouldGetData(context, false);
    }


    /**
     * //先同步逻辑删除计划、再同步其他状态下的状态
     */
    public static void syncDelctPlan(final Context context, final String userId, final String token) {
        List<Plan> plans = PlanTool.quaryPlan_havaDelected();
        try {
            if (plans.size() == 0) {
                //同步计划
                syncPlan(context,userId,token);
            } else {
                for (final Plan plan : plans) {
                    //在服务器物理删除
                    new DelectPlan(Integer.parseInt(userId), token, Integer.parseInt(plan.getPlanId()), new DelectPlan.SuccessCallback() {
                        @Override
                        public void onSuccess() {
                            //本地物理删除
                            PlanTool.delectPlan_planId(plan.getPlanId());
                            //物理删除成功了才能同步本地数据
                            syncPlan(context,userId,token);
                        }
                    }, new DelectPlan.FailCallback() {
                        @Override
                        public void onFail(int status) {

                        }
                    });
                }
            }
        } catch (Exception e) {
            Log.e(NetConfig.CATCH, "syncDelctPlan: " + e.getMessage());
        }
    }

    /**
     * //先同步逻辑删除课程、再同步其他状态下的课程
     */
    public static void syncDelctCourse(final Context context, final String userId, final String token) {
        List<Course> courses = CourseTool.quaryCourse_havaDelect();
        try {
            if (courses.size() == 0) {
                syncCourse(context,userId,token);
            } else {
                for (final Course course : courses) {
                    new DelectCourse(Integer.parseInt(userId), token, Integer.parseInt(course.getCourseId()),
                            new DelectCourse.SuccessCallBack() {
                                @Override
                                public void onSuccess(int courseId, int status) {
                                    //本地物理删除
                                    CourseTool.delectPlan_planId(course.getCourseId());
                                    //物理删除成功了才能同步本地数据
                                    syncCourse(context,userId,token);
                                }
                            }, new DelectCourse.FailCallBack() {
                        @Override
                        public void onFail(int statusCode) {

                        }
                    });
                }
            }
        } catch (Exception e) {
            Log.e(NetConfig.CATCH, "syncDelctPlan: " + e.getMessage());
        }

    }

    /**
     * 同步课程
     */
    public static void syncCourse(Context context, String userId, String token) {
        //获得所有需要同步的课程
        List<Course> courses = CourseTool.quaryCourse_enSync();
        Log.d("nknool", "syncCourse: " + courses.size());
        Log.d("obomfff", "syncCourse: " + userId);
        new SyncCourse(context, userId, token, courses, new SyncCourse.SuccessCallBack() {
            @Override
            public void onSuccess() {
                CourseTool.sync_staus_9();
            }
        }, new SyncCourse.FailCallBack() {
            @Override
            public void onFail(int statusCode) {

            }
        });
    }

    /**
     * 同步计划
     */
    public static void syncPlan(Context context, String userId, String token) {
        //获得所有需要同步的计划
        List<Plan> plans = PlanTool.quaryPlan_enSync();
        Log.d("dfdfdfgg", "syncPlan: " + plans.size());
        //发到后端
        new SyncPlan(context, userId, token, plans, new SyncPlan.SuccessCallBack() {
            @Override
            public void onSuccess() {
                //同步成功 该status为9
                PlanTool.sync_staus_9();

            }
        }, new SyncPlan.FailCallBack() {
            @Override
            public void onFail(int statusCode) {
                Log.d("dfdfdfgg", "888888888888888888888888888");

            }
        });
    }

}