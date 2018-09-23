//package com.lbstudio.timer.app.app.fragment;
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.ie1e.mdialog.view.ActionSheetDialog;
//import com.lbstudio.timer.app.CommonUtils;
//import com.lbstudio.timer.app.R;
//import com.lbstudio.timer.app.app.TimerActivity;
//import com.lbstudio.timer.app.app.activity.PlanDetail_Activity;
//import com.lbstudio.timer.app.app.javabean.Plan;
//import com.lbstudio.timer.app.app.ui.ExpandView;
//import com.lbstudio.timer.app.app.util.CommonUilts;
//import com.lbstudio.timer.app.app.calendar.CalendarActivity;
//import com.scwang.smartrefresh.layout.SmartRefreshLayout;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//
//public class PlanFrament extends Fragment {
//
//    @BindView(R.id.running_jiantou)
//    LinearLayout runningJiantou;
//    @BindView(R.id.expandView_running)
//    ExpandView expandViewRunning;
//    @BindView(R.id.enStart_jiantou)
//    LinearLayout enStartJiantou;
//    @BindView(R.id.expandView_enStart)
//    ExpandView expandViewEnStart;
//    @BindView(R.id.finish_background)
//    TextView finishBackground;
//    @BindView(R.id.text_finish)
//    TextView textFinish;
//    @BindView(R.id.finish_jiantou)
//    LinearLayout finishJiantou;
//    @BindView(R.id.expandView_finish)
//    ExpandView expandViewFinish;
//    @BindView(R.id.running_jiantou2)
//    TextView runningJiantou2;
//    @BindView(R.id.enStart_jiantou2)
//    TextView enStartJiantou2;
//    @BindView(R.id.finish_jiantou2)
//    TextView finishJiantou2;
//    @BindView(R.id.running_number)
//    TextView runningNumber;
//    @BindView(R.id.enStart_number)
//    TextView enStartNumber;
//    @BindView(R.id.finish_number)
//    TextView finishNumber;
//    @BindView(R.id.refreshView)
//    SmartRefreshLayout refreshView;
//    private Unbinder unbinder;
//
//    String TAG = "debug";
//    private int runningNum;
//    private int enStartNum;
//    private int finishiNum;
//    private int unningPlans_size;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.planlist, null);
//        unbinder = ButterKnife.bind(this, view);
//        init();
//        refreshView.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshLayout) {
//                refreshLayout.finishRefresh();
//            }
//        });
//        return view;
//    }
//
//
//    private void init() {
//        String nowDateNum = CommonUilts.getNowDateNum(); //获得此刻的时间
//        List<Plan> plans = CommonUilts.queryPlanDataByDate(nowDateNum);//获得今日的计划数据
//
//        runningNum = 0;        //进行中的数量
//        enStartNum = 0;        //未开始的数量
//        finishiNum = 0;        //已完成的数量
//        List<Plan> runningPlans = new ArrayList<>(); //进行中的计划数据
//        List<Plan> enStartPlans = new ArrayList<>(); //未开始的计划数据
//        List<Plan> finishPlans = new ArrayList<>();//已完成的计划数据
//        for (Plan plan : plans) {
//            int planStatus = plan.getPlanStatus();
//            if (planStatus == 10) {
//                enStartNum += 1;
//                enStartPlans.add(plan);
//            } else if (planStatus == 11) {
//                runningNum += 1;
//                runningPlans.add(plan);
//            } else if (planStatus == 12) {
//                finishiNum += 1;
//                Log.d("debug", "finishiNum: " + finishiNum);
//                finishPlans.add(plan);
//            }
//        }
//
//        //显示计划三个状态的数量
//        runningNumber.setText("(" + String.valueOf(runningNum) + ")");
//        enStartNumber.setText("(" + String.valueOf(enStartNum) + ")");
//        finishNumber.setText("(" + String.valueOf(finishiNum) + ")");
//
//        //判断三个状态的计划集合中是否存在空的  防止空指针异常
//        boolean isNull_running = false;
//        boolean isNull_enStart = false;
//        boolean isNull_finish = false;
//        if (runningPlans.isEmpty()) {
//            isNull_running = true;
//        }
//        if (enStartPlans.isEmpty()) {
//            isNull_enStart = true;
//        }
//        if (finishPlans.isEmpty()) {
//            isNull_finish = true;
//        }
//
//        unningPlans_size = runningPlans.size();
//        //展示“进行中"的计划
//        if (!isNull_running) {
//            for (int i = 0; i < runningPlans.size(); i++) {
//                Plan plan = runningPlans.get(i);
//                final String title = plan.getPlanTitle();
//                final String content = plan.getPlanContent();
//                final int planId = plan.getId();
//                final int date = plan.getPlanDate();
//                final int startTime = plan.getPlanStartTime();
//                final int endTime = plan.getPlanEndTime();
//                final boolean isRegular = plan.isRegular();
//                final int realStartTime = plan.getRealStartTime();
//
//                expandViewRunning
//                        .addItem(title, content
//                                , new ExpandView.OnItemClickTextViewListener() {
//                                    @Override
//                                    public void onClickTextView(int which) {
//                                        CommonUilts.thisToActivity(planId, PlanDetail_Activity.class);
//                                        CommonUilts.showToast("标题一", false);
//
//                                    }
//
//
//                                }, new ExpandView.OnItemClickButtonListener() {
//                                    @Override
//                                    public void onClickButton(int which) {
//                                        new ActionSheetDialog(getContext())
//                                                .builder()
//                                                .setTitle(title)
//                                                .setCanceledOnTouchOutside(true)
//                                                .setDialogLocation(Gravity.CENTER)
//                                                .addSheetItem("完成计划", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
//                                                    @Override
//                                                    public void onClick(int which) {
//                                                        CommonUilts.undate_ThePlanStatus_OfThePlanInDb(planId, 12);//修改计划状态
//                                                        int realEndTime = CommonUilts.getNowMinuteNum();//记录realEndTime
//                                                        CommonUilts.undate_TheRealEndTIme_OfThePlanInDb(planId, realEndTime);//修改realEndTime
//                                                        //todo 计算spendTime 并修改DB
//                                                        int spendTime = realEndTime - realStartTime;
//                                                        CommonUilts.undate_thePlanSpendTime_ofThePlanInDb(planId, spendTime); //修改spendTime
//                                                        CommonUilts.showToast("完成计划", false);
////                                                        refresh();
//                                                    }
//                                                })
//                                                .addSheetItem("明天继续", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
//                                                    @Override
//                                                    public void onClick(int which) {
//                                                        CommonUilts.undate_ThePlanStatus_OfThePlanInDb(planId, 12);//修改planStatus
//                                                        CommonUilts.addPlanInTomorrow(title, content, date + 1, startTime, endTime, isRegular);//在明天新建一个计划
//                                                        CommonUilts.showToast("完成计划,明天继续", false);
//                                                        refresh();
//                                                    }
//                                                }).show();
//                                    }
//                                });
//
//            }
//            expandViewRunning.show();
//        }
//
//
//        //展示“未开始”的计划
//        if (!isNull_enStart) {
//            for (int i = 0; i < enStartPlans.size(); i++) {
//                Plan plan = enStartPlans.get(i);
//                final String title = plan.getPlanTitle();
//                String content = plan.getPlanContent();
//                final int id = plan.getId();
//                final int date = plan.getPlanDate();
//                expandViewEnStart
//                        .addItem(title, content
//                                , new ExpandView.OnItemClickTextViewListener() {
//                                    @Override
//                                    public void onClickTextView(int which) {
//                                        CommonUilts.thisToActivity(id, PlanDetail_Activity.class);
//                                        CommonUilts.showToast("标题一", false);
//
//                                    }
//
//
//                                }, new ExpandView.OnItemClickButtonListener() {
//                                    @Override
//                                    public void onClickButton(int which) {
//                                        new ActionSheetDialog(getContext())
//                                                .builder()
//                                                .setTitle(title)
//                                                .setCanceledOnTouchOutside(true)
//                                                .setDialogLocation(Gravity.CENTER)
//                                                .addSheetItem("开始计划", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
//                                                    @Override
//                                                    public void onClick(int which) {
//                                                        if (unningPlans_size < 1) {
//                                                            CommonUilts.undate_ThePlanStatus_OfThePlanInDb(id, 11);//一、修改planStatus为进行中
//                                                            int realStartTime = CommonUilts.getNowMinuteNum();//记录realStartTime
//                                                            CommonUilts.undate_TheRealStartTIme_OfThePlanInDb(id, realStartTime); //二、修改realStartTime
//                                                            CommonUilts.showToast("开始计划成功", false);
//                                                            //刷新页面
//                                                            refresh();
//                                                        } else {
//                                                            CommonUilts.showToast("只能同时开始一个计划", false);
//                                                        }
//
//                                                    }
//                                                })
//                                                .addSheetItem("推迟一天", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
//                                                    @Override
//                                                    public void onClick(int which) {
//                                                        CommonUilts.undate_thePlanDate_ofThePlanInDb(id, date + 1); //日期+1 修改日期
//                                                        CommonUilts.showToast("计划推迟一天", false);
//                                                        refresh();
//                                                    }
//                                                })
//                                                .addSheetItem("删除计划", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
//                                                    @Override
//                                                    public void onClick(int which) {
//                                                        CommonUilts.delectPlan_byId(id);
//                                                        CommonUilts.showToast("删除计划成功", false);
//                                                        refresh();
//                                                    }
//                                                })
//                                                .show();
//                                    }
//                                });
//
//            }
//            expandViewEnStart.show();
//        }
//
//        //展示“已完成”的计划
//        if (!isNull_finish) {
//            for (int i = 0; i < finishPlans.size(); i++) {
//                Plan plan = finishPlans.get(i);
//                final String title = plan.getPlanTitle();
//                String content = plan.getPlanContent();
//                final int id = plan.getId();
//                expandViewFinish
//                        .addItem(title, content
//                                , new ExpandView.OnItemClickTextViewListener() {
//                                    @Override
//                                    public void onClickTextView(int which) {
//                                        CommonUilts.thisToActivity(id, PlanDetail_Activity.class);
//                                        CommonUilts.showToast("标题一", false);
//
//                                    }
//
//
//                                }, new ExpandView.OnItemClickButtonListener() {
//                                    @Override
//                                    public void onClickButton(int which) {
//                                        new ActionSheetDialog(getContext())
//                                                .builder()
//                                                .setTitle(title)
//                                                .setCanceledOnTouchOutside(true)
//                                                .setDialogLocation(Gravity.CENTER)
//                                                .addSheetItem("再次开启计划", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
//                                                    @Override
//                                                    public void onClick(int which) {
//                                                        if (unningPlans_size < 1) {
//                                                            CommonUilts.undate_ThePlanStatus_OfThePlanInDb(id, 11);
//                                                            CommonUilts.showToast("再次开启计划成功", false);
//                                                            refresh();
//                                                        } else {
//                                                            CommonUilts.showToast("只能同时开始一个计划", false);
//                                                        }
//
//                                                    }
//                                                })
//                                                .show();
//                                    }
//                                });
//
//            }
//            expandViewFinish.show();
//        }
//    }
//
//    public void refresh() {
//        CommonUilts.thisToActivity(TimerActivity.class);
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//    @OnClick({R.id.running_jiantou, R.id.enStart_jiantou, R.id.finish_jiantou, R.id.bt_jilu})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.running_jiantou: //进行中的箭头
//                controlJianTou(expandViewRunning, runningJiantou2);
//                controlExpandView(expandViewRunning);
//                break;
//            case R.id.enStart_jiantou://未开始的箭头
//                controlJianTou(expandViewEnStart, enStartJiantou2);
//                controlExpandView(expandViewEnStart);
//                break;
//            case R.id.finish_jiantou://已完成的箭头
//                controlJianTou(expandViewFinish, finishJiantou2);
//                controlExpandView(expandViewFinish);
//                break;
//            case R.id.bt_jilu:
//                CommonUtils.intentToActivity(getActivity(), CalendarActivity.class);
//                break;
//        }
//    }
//
//    private void controlJianTou(ExpandView expandView, TextView textView) {
//        boolean isExpand = expandView.isExpand();
//        if (isExpand) {
//            isExpand = false;
//            textView.setBackgroundResource(R.drawable.down_pull_shou);
//        } else {
//            textView.setBackgroundResource(R.drawable.down_pull);
//            isExpand = true;
//        }
//    }
//
//    private void controlExpandView(ExpandView expandView) {
//        if (expandView.isExpand()) {
//            expandView.collapse();
//        } else {
//            expandView.expand();
//        }
//    }
//
//}
