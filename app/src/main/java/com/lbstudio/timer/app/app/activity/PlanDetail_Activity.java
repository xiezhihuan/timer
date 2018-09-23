package com.lbstudio.timer.app.app.activity;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.Guideline;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.lbstudio.timer.app.MainActivityy;
import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.TimerActivity;
import com.lbstudio.timer.app.app.calendar.CalendarActivity;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.net.UpdatePlan;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.lbstudio.timer.app.app.util.ToastMassage;
import com.lbstudio.timer.app.base.BaseActivity;
import com.ie1e.mdialog.view.ActionSheetDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PlanDetail_Activity extends BaseActivity {

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
    @BindView(R.id.plan_detailPge_title)
    TextView planDetailPgeTitle;
    @BindView(R.id.plan_detailPge_title_txt)
    EditText planDetailPgeTitleTxt;
    @BindView(R.id.divider2)
    View divider2;
    @BindView(R.id.plan_detailPge_date)
    TextView planDetailPgeDate;
    @BindView(R.id.plan_detailPge_date_txt)
    TextView planDetailPgeDateTxt;
    @BindView(R.id.divider3)
    View divider3;
    @BindView(R.id.plan_detailPge_timeRange)
    TextView planDetailPgeTimeRange;
    @BindView(R.id.plan_detailPge_timeRange_txt_start)
    TextView planDetailPgeTimeRangeTxtStart;
    @BindView(R.id.plan_detailPge_timeRange_txt_end)
    TextView planDetailPgeTimeRangeTxtEnd;
    @BindView(R.id.divider4)
    View divider4;
    @BindView(R.id.plan_detailPge_startTime)
    TextView planDetailPgeStartTime;
    @BindView(R.id.plan_detailPge_startTime_txt)
    TextView planDetailPgeStartTimeTxt;
    @BindView(R.id.divider5)
    View divider5;
    @BindView(R.id.plan_detailPge_spendTime)
    TextView planDetailPgeSpendTime;
    @BindView(R.id.plan_detailPge_spendTime_txt)
    TextView planDetailPgeSpendTimeTxt;
    @BindView(R.id.divider6)
    View divider6;
    @BindView(R.id.plan_detailPge_content)
    TextView planDetailPgeContent;
    @BindView(R.id.plan_detailPge_content_background)
    TextView planDetailPgeContentBackground;
    @BindView(R.id.plan_detailPge_content_txt)
    EditText planDetailPgeContentTxt;
    @BindView(R.id.plan_detailPge_regular_txt)
    TextView planDetailPgeRegularTxt;
    @BindView(R.id.plan_detailPge_regular)
    TextView planDetailPgeRegular;
    @BindView(R.id.divider4_1)
    View divider41;

    int mPlanID;//该页需要显示详细的计划id
    private Plan plan;//展示的数据
    private String title;
    private String content;
    private int dateNumber;
    private int startTimeNum;
    private int endTimeNum;
    private String regualar;
    private boolean isRegular;

    String userId;
    String token;
    private int planStatus;
    private String lastPageName;


    @Override
    protected void init() {
        mPlanID = getIntent().getIntExtra("dataId", -1);
        lastPageName = getIntent().getStringExtra("pageName");
        userId = NetConfig.getUserId(this);
        token = NetConfig.getToken(this);
        //设置顶部栏的样式
        initTopBar();
        //获得数据id
        mPlanID = CommonUilts.getDataID(this);
        //获得数据
        plan = CommonUilts.queryThePlanById(mPlanID);
        //展示数据
        showData(plan);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_plan_detail;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommonUilts.thisToActivity(TimerActivity.class);

//       backLastPage();
    }

    private void backLastPage() {
        if (lastPageName.equals("CalendarActivity")){
            CommonUilts.thisToActivity(CalendarActivity.class);

        }else {
            CommonUilts.thisToActivity(TimerActivity.class);
        }
        finish();
    }

    //各个按钮的点击事件
    @OnClick({R.id.topBar_left, R.id.topBar_save, R.id.plan_detailPge_date_txt, R.id.plan_detailPge_regular_txt, R.id.plan_detailPge_timeRange_txt_start, R.id.plan_detailPge_timeRange_txt_end})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.topBar_left:
//                backLastPage();
                CommonUilts.thisToActivity(TimerActivity.class);

                break;
            case R.id.topBar_save:
                //将修改后的数据保存到本地数据库
                save();
                //将修改后的数据保存到服务器数据库
                uploadServer();
//                backLastPage();
                CommonUilts.thisToActivity(TimerActivity.class);

                break;
            case R.id.plan_detailPge_date_txt:
                getTodayDate();
                break;
            case R.id.plan_detailPge_regular_txt:
                getRegular();
                break;
            case R.id.plan_detailPge_timeRange_txt_start:
                getStartTime();
                break;
            case R.id.plan_detailPge_timeRange_txt_end:
                getEndTime();
                break;
        }
    }

    /**
     * 将数据上传到服务器
     */
    private void uploadServer() {
        new UpdatePlan(this, Integer.parseInt(userId), token, title, content, dateNumber, startTimeNum, endTimeNum,0,0, isRegular, planStatus,
                new UpdatePlan.SuccessCallback() {
                    @Override
                    public void onSuccess() {
                    }
                }, new UpdatePlan.FailCallback() {
            @Override
            public void onFail(int status) {
            }
        });
    }

    /**
     * 展示计划的详细数据
     *
     * @param plan
     */
    private void showData(Plan plan) {
        //需要展示的字段
        String title = plan.getPlanTitle();
        String content = plan.getPlanContent();
        int date = plan.getPlanDate();
        String date_txt = getDate_txt(date);// "2018 - 09 - 08"
        int startTime = plan.getPlanStartTime(); //483
        String startTime_txt = getStartTime_txt(startTime);// "08:03 - "
        int endTime = plan.getPlanEndTime(); //542
        String endTime_txt = getEndTime_txt(endTime);// "09:12"
        int realStartTime = plan.getRealStartTime();//570
        String realStartTime_txt = getEndTime_txt(realStartTime); // "09:30"
        boolean regular = plan.isRegular();
        String isRegular = "否";
        if (regular) {
            isRegular = "是";
        }
        int spendTime = plan.getSpendTime();
        String spendTime_txt = spendTime + "分钟";

        if (regular) {  //如果是固定计划
            //隐藏“开始时间”；
            planDetailPgeStartTimeTxt.setVisibility(View.GONE);
            planDetailPgeStartTime.setVisibility(View.GONE);
        }

        //展示
        planDetailPgeTitleTxt.setText(title);
        planDetailPgeDateTxt.setText(date_txt);
        planDetailPgeTimeRangeTxtStart.setText(startTime_txt);
        planDetailPgeTimeRangeTxtEnd.setText(endTime_txt);
        planDetailPgeRegularTxt.setText(isRegular);
        planDetailPgeStartTimeTxt.setText(realStartTime_txt);
        planDetailPgeSpendTimeTxt.setText(spendTime_txt);
        planDetailPgeContentTxt.setText(content);
    }

    /**
     * 542 转为 "09:02"
     *
     * @param endTime
     * @return
     */
    private String getEndTime_txt(int endTime) {
        //endTime 542
        int hour = endTime / 60;// 9
        int minute = endTime - hour * 60;// 2
        String hour2 = String.valueOf(hour); // "9"
        if (hour < 10) {
            hour2 = "0" + hour;
        }
        String minute2 = String.valueOf(minute); //"2"
        if (minute < 10) {
            minute2 = "0" + minute;
        }
        String endTime_txt = hour2 + ":" + minute2;
        return endTime_txt;
    }

    /**
     * 483 转为 "08:03 - "
     *
     * @param startTime
     * @return
     */
    private String getStartTime_txt(int startTime) {
        // startTime 483
        int hour = startTime / 60; // 8
        int minute = startTime - hour * 60; // 3

        String hour2 = String.valueOf(hour); // "9"
        if (hour < 10) {
            hour2 = "0" + hour;
        }
        String minute2 = String.valueOf(minute); //"2"
        if (minute < 10) {
            minute2 = "0" + minute;
        }

        String startTime_txt = hour2 + ":" + minute2 + " - "; // "08:03 - "
        return startTime_txt;
    }

    /**
     * "20180908" 转为  "2018 - 09 - 08"
     *
     * @param date
     * @return
     */
    private String getDate_txt(int date) {
        //date  20180908
        String date2 = String.valueOf(date);// "20180908"
        String[] date3 = date2.split("");
        String date_year2 = date3[0] + date3[1] + date3[2] + date3[3];//"2018"
        String date_year = date3[0] + date3[1] + date3[2] + date3[3] + date3[4];//"2018" //todo 不知道为什么数组第一位为“ ”
        Log.d("debug", "date_year2: " + date_year2 + "   " + date_year);
        for (String s : date3) {
            Log.d("debug", "date3: " + s);
        }
        String date_month = date3[5] + date3[6];// "09"
        String date_day = date3[7] + date3[8];//"08"
        String date_txt = date_year + " - " + date_month + " - " + date_day;// "2018 - 09 - 08"
        return date_txt;
    }

    /**
     * 设置顶部栏样式
     */
    private void initTopBar() {
        topBarLeft.setVisibility(View.VISIBLE);
        topBarTitle.setText("编辑");
        topBarSave.setVisibility(View.VISIBLE);
    }

    /**
     * 保存数据到本地数据库
     */
    private void save() {
        title = planDetailPgeTitleTxt.getText().toString().trim();
        content = planDetailPgeContentTxt.getText().toString().trim();
        //2018 - 09 - 12
        String date = planDetailPgeDateTxt.getText().toString().trim();
        //20180912
        dateNumber = getDateNumber2(date);
        //8:00
        String startTime = planDetailPgeTimeRangeTxtStart.getText().toString().trim();
        //480
        startTimeNum = getStartTimeNum(startTime);
        //9:00
        String endTime = planDetailPgeTimeRangeTxtEnd.getText().toString().trim();
        //540
        endTimeNum = getEndTimeNum(endTime);
        planStatus = plan.getPlanStatus();
        regualar = planDetailPgeRegularTxt.getText().toString().trim();
        isRegular = false;
        if (regualar.equals("是")) {
            isRegular = true;
        }
        if (title.isEmpty()) {
            CommonUilts.showToast(ToastMassage.ERRO_PLAN_TITLE_NULL, false);//计划标题不能为空
        }
        //修改计划数据
        CommonUilts.undatePlanDataInDb(mPlanID, title, content, dateNumber, startTimeNum, endTimeNum, isRegular);
        CommonUilts.showToast(ToastMassage.SAVE_SUCCESS, false);// 弹出"保存成功"
//        finish();
    }


    private int getEndTimeNum(String endTime) {
        //endTime 09:00
        String[] end2 = endTime.split(":");
        String end_hour = end2[0];//9
        String end_minute = end2[1];//00
        int end_num = Integer.parseInt(end_hour) * 60 + Integer.parseInt(end_minute);//540
        return end_num;
    }

    private int getStartTimeNum(String startTime) {
        //startTime 08:30 - ;
        String[] start2 = startTime.split(" ");
        String start3 = start2[0];//8:30;
        String[] start4 = start3.split(":");
        String start_hour = start4[0];//08
        String start_minute = start4[1];//30
        int start_num = Integer.parseInt(start_hour) * 60 + Integer.parseInt(start_minute);
        return start_num;
    }

    private void getRegular() {
        new ActionSheetDialog(this)
                .builder()
                .setTitle("是否固定该计划")
                .setCanceledOnTouchOutside(true)
                .setDialogLocation(Gravity.CENTER)
                .addSheetItem("是", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        planDetailPgeRegularTxt.setText("是");
                        //隐藏“开始时间”；
                        planDetailPgeStartTimeTxt.setVisibility(View.GONE);
                        planDetailPgeStartTime.setVisibility(View.GONE);
                        //计算出“计划用时”，并展示
                        //结束时间
                        String end = planDetailPgeTimeRangeTxtEnd.getText().toString().trim();//09:00
                        int endTimeNum = getEndTimeNum(end);//540
                        //开始时间
                        String start = planDetailPgeTimeRangeTxtStart.getText().toString().trim();//08:30 - ;
                        int startTimeNum = getStartTimeNum(start);//510

                        int spendTime = endTimeNum - startTimeNum;

                        planDetailPgeSpendTimeTxt.setText(String.valueOf(spendTime));
                    }
                })
                .addSheetItem("否", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        planDetailPgeRegularTxt.setText("否");
                        planDetailPgeStartTimeTxt.setVisibility(View.VISIBLE);
                        planDetailPgeStartTime.setVisibility(View.VISIBLE);
                    }
                })
                .show();
    }

    private void getTodayDate() {
        DatePickDialog dialog = initDatePcik_date();
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                Log.d("debug", "date: " + date.toString());
                String date_ToString = date.toString();
                String[] date_array = date_ToString.split(" "); //对日期切片
                String year = date_array[5]; //年
                String day = date_array[2]; //28
                String month_Endlish = date_array[1];//Jun
                String month = CommonUilts.getMOnth_AbcTO123(month_Endlish); //月
                String date_txt = year + " - " + month + " - " + day;//2018 - 11 - 23

                planDetailPgeDateTxt.setText(date_txt);
            }
        });
        dialog.show();
    }

    private void getStartTime() {
        DatePickDialog dialog = initDatePcik();
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                Log.d("debug", "date: " + date.toString());
                String date_ToString = date.toString();
                String[] date_array = date_ToString.split(" ");//切片
                String[] hour_containS = date_array[3].split(":"); //12：21：59
                String hour = hour_containS[0] + ":" + hour_containS[1];//小时 12：21
                String hour2 = hour + " - "; //12：21 -

                planDetailPgeTimeRangeTxtStart.setText(hour2);
            }
        });
        dialog.show();
    }

    private void getEndTime() {
        DatePickDialog dialog = initDatePcik();
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                Log.d("debug", "date: " + date.toString());
                String date_ToString = date.toString();
                String[] date_array = date_ToString.split(" ");
                String[] hour_containS = date_array[3].split(":");
                String hour = hour_containS[0] + ":" + hour_containS[1];//小时  xx:xx

                planDetailPgeTimeRangeTxtEnd.setText(hour);
            }
        });
        dialog.show();
    }

    private DatePickDialog initDatePcik() {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(DateType.TYPE_HM);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd HH:mm");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调6

        return dialog;
    }

    private DatePickDialog initDatePcik_date() {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(5);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(DateType.TYPE_YMD);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd HH:mm");
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调6

        return dialog;
    }

    /**
     * 日期格式转为数字
     * Fri Aug 24 12:44:25 GMT+08:00 2018 转为 20180824
     *
     * @param date
     */
    private int getDateNumber(Date date) {
        String date_ToString = date.toString();
        String[] date_array = date_ToString.split(" ");
        String year = date_array[5]; //年
        String day = date_array[2];
        String month_Endlish = date_array[1];
        String month = null; //月
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
        }
        String date_result = year + month + day;
        return Integer.parseInt(date_result);
    }

    /**
     * 2018 - 09 - 23 转为 20180923
     *
     * @param date
     * @return
     */
    private int getDateNumber2(String date) {
        //  date 2018 - 09 - 12
        String[] date2 = date.split(" - ");
        String date_year = date2[0];//"2018"
        String date_month = date2[1];//"09"
        String date_day = date2[2];//"12"
        String date3 = date_year + date_month + date_day; //"20180912"
        int date4 = Integer.parseInt(date3);
        return date4;
    }

}
