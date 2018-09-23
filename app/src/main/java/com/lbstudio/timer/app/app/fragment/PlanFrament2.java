//package com.lbstudio.timer.app.app.fragment;
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.constraint.Guideline;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.lbstudio.timer.app.CommonUtils;
//import com.lbstudio.timer.app.app.DbTool.PlanTool;
//import com.lbstudio.timer.app.app.adapter.EnstartAdapter;
//import com.lbstudio.timer.app.app.adapter.FinishAdapter;
//import com.lbstudio.timer.app.app.adapter.ItemBean;
//import com.lbstudio.timer.app.R;
//import com.lbstudio.timer.app.app.adapter.RunningAdapter;
//import com.lbstudio.timer.app.app.calendar.CalendarActivity;
//import com.scwang.smartrefresh.layout.SmartRefreshLayout;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//
//public class PlanFrament2 extends Fragment {
//
//
//    @BindView(R.id.guideline_or)
//    Guideline guidelineOr;
//    @BindView(R.id.guideline_ver)
//    Guideline guidelineVer;
//    @BindView(R.id.guideline_left)
//    Guideline guidelineLeft;
//    @BindView(R.id.top_background)
//    TextView topBackground;
//    @BindView(R.id.bt_jilu)
//    TextView btJilu;
//    @BindView(R.id.text_running)
//    TextView textRunning;
//    @BindView(R.id.running_number)
//    TextView runningNumber;
//    @BindView(R.id.running_jiantou2)
//    TextView runningJiantou2;
//    @BindView(R.id.running_jiantou)
//    LinearLayout runningJiantou;
//    @BindView(R.id.view_running)
//    RecyclerView expandViewRunning;
//    @BindView(R.id.enStart_background)
//    TextView enStartBackground;
//    @BindView(R.id.text_enStart)
//    TextView textEnStart;
//    @BindView(R.id.enStart_number)
//    TextView enStartNumber;
//    @BindView(R.id.enStart_jiantou2)
//    TextView enStartJiantou2;
//    @BindView(R.id.enStart_jiantou)
//    LinearLayout enStartJiantou;
//    @BindView(R.id.view_enStart)
//    RecyclerView expandViewEnStart;
//    @BindView(R.id.finish_background)
//    TextView finishBackground;
//    @BindView(R.id.text_finish)
//    TextView textFinish;
//    @BindView(R.id.finish_number)
//    TextView finishNumber;
//    @BindView(R.id.finish_jiantou2)
//    TextView finishJiantou2;
//    @BindView(R.id.finish_jiantou)
//    LinearLayout finishJiantou;
//    @BindView(R.id.view_finish)
//    RecyclerView expandViewFinish;
//    @BindView(R.id.tv_month)
//    TextView tvMonth;
//    @BindView(R.id.tv_date)
//    TextView tvDate;
//    @BindView(R.id.refreshView)
//    SmartRefreshLayout refreshView;
//    private Unbinder unbinder;
//
//    String TAG = "debug";
//    private int runningNum;
//    private int enStartNum;
//    private int finishiNum;
//
//    private List<ItemBean> runningList;
//    private List<ItemBean> enStartList;
//    private List<ItemBean> finishList;
//    private RunningAdapter runningAdapter;
//    private EnstartAdapter enstartAdapter;
//    private FinishAdapter finishAdapter;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.planlist2, null);
//        unbinder = ButterKnife.bind(this, view);
//        getPlanData();
//        showPlanCount();
//        showItem();
//        //下拉刷新
//        refreshView.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshLayout) {
//                refreshPage();
//            }
//        });
//        return view;
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
//    private void showPlanCount() {
//        //显示计划三个状态的数量
//        runningNumber.setText("(" + String.valueOf(runningNum) + ")");
//        enStartNumber.setText("(" + String.valueOf(enStartNum) + ")");
//        finishNumber.setText("(" + String.valueOf(finishiNum) + ")");
//    }
//
//    /**
//     * 设置数据适配器 展示数据
//     */
//    private void showItem() {
//
//        runningAdapter = new RunningAdapter(getActivity(), runningList, new RunningAdapter.RefreshCallBack() {
//            @Override
//            public void refresh() {
//                refreshPage();
//            }
//        });
//
//        enstartAdapter = new EnstartAdapter(getActivity(), enStartList, new EnstartAdapter.RefreshCallBack() {
//            @Override
//            public void refresh() {
//                refreshPage();
//            }
//        }, new EnstartAdapter.SinglePlanCallBack() {
//            @Override
//            public boolean isSingleRunningPlan() {
//                return runningList.isEmpty();
//            }
//        });
//
//        finishAdapter = new FinishAdapter(getActivity(), finishList, new FinishAdapter.RefreshCallBack() {
//            @Override
//            public void refresh() {
//                refreshPage();
//            }
//        }, new FinishAdapter.SinglePlanCallBack() {
//            @Override
//            public boolean isSingleRunningPlan() {
//                return runningList.isEmpty();
//            }
//        });
//
//        expandViewRunning.setLayoutManager(new LinearLayoutManager(getActivity()));
//        expandViewEnStart.setLayoutManager(new LinearLayoutManager(getActivity()));
//        expandViewFinish.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        expandViewRunning.setAdapter(runningAdapter);
//        expandViewEnStart.setAdapter(enstartAdapter);
//        expandViewFinish.setAdapter(finishAdapter);
//
//    }
//
//    /**
//     * 刷新整个页面
//     */
//    public void refreshPage() {
//        runningAdapter.refresh();
//        enstartAdapter.refresh();
//        finishAdapter.refresh();
//
//        getPlanData();
//        showPlanCount();
//    }
//
//    /**
//     * 获得页面展示需要的数据
//     * private int runningNum;
//     * private int enStartNum;
//     * private int finishiNum;
//     * private List<ItemBean> runningList;
//     * private List<ItemBean> enStartList;
//     * private List<ItemBean> finishList;
//     */
//    private void getPlanData() {
//        PlanTool.PlanData planData = PlanTool.getPlanData();
//        runningNum = planData.getRunningNum();
//        enStartNum = planData.getEnStartNum();
//        finishiNum = planData.getFinishiNum();
//        runningList = planData.getRunningList();
//        enStartList = planData.getEnStartList();
//        finishList = planData.getFinishList();
//    }
//
//    private boolean isExpand = true;
//
//    private void controlJianTou(RecyclerView expandView, TextView textView) {
//        if (isExpand) {
//            isExpand = false;
//            textView.setBackgroundResource(R.drawable.down_pull_shou);
//        } else {
//            textView.setBackgroundResource(R.drawable.down_pull);
//            isExpand = true;
//        }
//    }
//
//    private void controlExpandView(RecyclerView expandView) {
//        if (isExpand) {
//            expandView.setVisibility(View.VISIBLE);
//        } else {
//            expandView.setVisibility(View.GONE);
//
//        }
//    }
//
//}
