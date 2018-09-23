package com.lbstudio.timer.app.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.DbTool.PlanTool;
import com.lbstudio.timer.app.app.adapter.ItemBean;
import com.lbstudio.timer.app.app.adapter.MyExpandableListViewAdapter;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.lbstudio.timer.app.app.calendar.CalendarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PlanFrament3 extends Fragment {


    @BindView(R.id.guideline_or)
    Guideline guidelineOr;
    @BindView(R.id.guideline_ver)
    Guideline guidelineVer;
    @BindView(R.id.guideline_left)
    Guideline guidelineLeft;
    @BindView(R.id.top_background)
    TextView topBackground;
    @BindView(R.id.tv_day)
    TextView tvday;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.bt_jilu)
    TextView btJilu;
    @BindView(R.id.ExpandableListView)
    ExpandableListView expandableListView;
    private Unbinder unbinder;

    String TAG = "debug";
    private List<ItemBean> runningList;
    private List<ItemBean> enStartList;
    private List<ItemBean> finishList;
    private List<List<ItemBean>> lists;
    private List<String> groupName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.planlist3, null);
        unbinder = ButterKnife.bind(this, view);
        getPlanData();
        setGroupName();
        showItem();
        //设置今天的日期
        setDate();
        return view;
    }

    private void setDate() {
        //获得日期
        String nowDateNum = CommonUilts.getNowDateNum();
        String year = nowDateNum.substring(0, 4);
        String month = nowDateNum.substring(4, 6);
        String day = nowDateNum.substring(6, 8);

        //展示日期
        tvDate.setText(year + getString(R.string.year) + month + getString(R.string.day));
        tvday.setText(day + getString(R.string.day_txt));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 获得页面展示需要的数据
     * <p>
     * private List<ItemBean> runningList;
     * private List<ItemBean> enStartList;
     * private List<ItemBean> finishList;
     */
    private void getPlanData() {
        Log.d("TAsdsssG", "onCreateView: ddddddddddddddddddd");
        PlanTool.PlanData planData = PlanTool.getPlanData();
        runningList = planData.getRunningList();
        enStartList = planData.getEnStartList();
        finishList = planData.getFinishList();

        getPlanDataList();
    }

    private void getPlanDataList() {
        lists = new ArrayList<>();
        lists.add(runningList);
        lists.add(enStartList);
        lists.add(finishList);
    }

    /**
     * 设置分组名数据
     */
    private void setGroupName() {
        groupName = new ArrayList<>();
        groupName.add("进行中");
        groupName.add("未开始");
        groupName.add("已完成");
    }

    /**
     * 设置数据适配器 展示数据
     */
    private void showItem() {
        expandableListView.setAdapter(new MyExpandableListViewAdapter(getActivity(), groupName, lists));
    }

    /**
     * 刷新整个页面
     */
    public void refreshPage() {

    }

    @OnClick(R.id.bt_jilu)
    public void onViewClicked() {
        CommonUilts.thisToActivity(CalendarActivity.class);
        CommonUilts.cacheFragmentNum(getActivity(),"1");
        getActivity().finish();
    }
}
