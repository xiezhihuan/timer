package com.lbstudio.timer.app.app.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ie1e.mdialog.view.ActionSheetDialog;
import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.DbTool.PlanTool;
import com.lbstudio.timer.app.app.activity.PlanDetail_Activity;
import com.lbstudio.timer.app.app.util.CommonUilts;

import java.util.List;

public class EnstartAdapter extends RecyclerView.Adapter<EnstartAdapter.ViewHolder> {

    private List<ItemBean> mList; //需要展示的数据
    private Context mContext;
    RefreshCallBack refreshCallBack; //刷新页面回调
    SinglePlanCallBack singlePlanCallBack;//判断是否有计划在进行的回调
    private final int PLANSTATUS = 11; //点击开始计划变为该状态


    static class ViewHolder extends RecyclerView.ViewHolder {
        View myView;
        TextView title;
        TextView content;
        TextView bottom;

        public ViewHolder(View itemView) {
            super(itemView);
            myView = itemView;
            title = (TextView) itemView.findViewById(R.id.tv_titlt);
            content = (TextView) itemView.findViewById(R.id.tv_content);
            bottom = (TextView) itemView.findViewById(R.id.bt_threePoint);
        }
    }

    public EnstartAdapter(Context mContext, List<ItemBean> list, RefreshCallBack refreshCallBack, SinglePlanCallBack singlePlanCallBack) {
        this.mContext = mContext;
        this.mList = list;
        this.refreshCallBack = refreshCallBack;
        this.singlePlanCallBack = singlePlanCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item3, null);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //将数据绑定到控件上
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ItemBean bean = mList.get(position);
        holder.title.setText(bean.itemTitle);
        holder.content.setText(bean.itemContent);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int planId = bean.planId;
                CommonUilts.thisToActivity(planId, PlanDetail_Activity.class);
            }
        });
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int planId = bean.planId;
                CommonUilts.thisToActivity(planId, PlanDetail_Activity.class);
            }
        });
        holder.bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ActionSheetDialog(mContext)
                        .builder()
                        .setTitle(bean.itemContent)
                        .setCanceledOnTouchOutside(true)
                        .setDialogLocation(Gravity.CENTER)
                        .addSheetItem("开始计划", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                if (singlePlanCallBack.isSingleRunningPlan()) {
                                    PlanTool.undate_ThePlanStatus_OfThePlanInDb(bean.planId, PLANSTATUS);//一、修改planStatus为进行中
                                    int realStartTime = CommonUilts.getNowMinuteNum();//记录realStartTime
                                    PlanTool.undate_TheRealStartTIme_OfThePlanInDb(bean.planId, realStartTime); //二、修改realStartTime
                                    CommonUilts.showToast("开始计划成功", false);
                                    //刷新页面
                                    refreshCallBack.refresh();
                                    //设置isSingleRunningPlan 为true
                                } else {
                                    CommonUilts.showToast("只能同时开始一个计划", false);
                                    new ActionSheetDialog(mContext)
                                            .builder()
                                            .setTitle(mContext.getString(R.string.tip))
                                            .setCanceledOnTouchOutside(true)
                                            .setDialogLocation(Gravity.CENTER)
                                            .addSheetItem("只能同时开始一个计划", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                                                @Override
                                                public void onClick(int which) {

                                                }
                                            })
                                            .show();
                                }
                            }
                        })
                        .addSheetItem("推迟一天", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                int date = CommonUilts.queryThePlanById(bean.planId).getPlanDate();
                                CommonUilts.undate_thePlanDate_ofThePlanInDb(bean.planId, date + 1); //日期+1 修改日期
                                CommonUilts.showToast("计划推迟一天", false);
                                refreshCallBack.refresh();
                            }
                        })
                        .addSheetItem("删除计划", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                CommonUilts.delectPlan_byId(bean.planId);
                                CommonUilts.showToast("删除计划成功", false);
                                refreshCallBack.refresh();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //提供给页面刷新和加载时调用
    public void add(List<ItemBean> addMessageList) {
        //增加数据
        int position = mList.size();
        mList.addAll(position, addMessageList);
        notifyItemInserted(position);
    }

    public void refresh() {
        //刷新数据
        List<ItemBean> newList = PlanTool.getPlanData().getEnStartList();
        mList.removeAll(mList);
        mList.addAll(newList);
        notifyDataSetChanged();
    }

    public static interface RefreshCallBack {
        void refresh();
    }

    public static interface SinglePlanCallBack {
        boolean isSingleRunningPlan();
    }
}
