package com.lbstudio.timer.app.app.adapter;


import android.app.Fragment;
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
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.net.AddPlan;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.util.CommonUilts;

import java.util.List;

public class RunningAdapter extends RecyclerView.Adapter<RunningAdapter.ViewHolder> {

    private List<ItemBean> mList;
    private Context mContext;
    private ItemBean bean;
    RefreshCallBack refreshCallBack;


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

    public RunningAdapter(Context mContext, List<ItemBean> list, RefreshCallBack refreshCallBack) {
        this.mContext = mContext;
        this.mList = list;
        this.refreshCallBack = refreshCallBack;
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
        bean = mList.get(position);
        holder.title.setText(bean.itemTitle);
        holder.content.setText(bean.itemContent);
        holder.bottom.setBackgroundResource(R.drawable.icon_three_dot);
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
                        .setTitle(bean.itemTitle)
                        .setCanceledOnTouchOutside(true)
                        .setDialogLocation(Gravity.CENTER)
                        .addSheetItem("完成计划", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                PlanTool.undate_ThePlanStatus_OfThePlanInDb(bean.planId, 12);//修改计划状态
                                int realEndTime = CommonUilts.getNowMinuteNum();//记录realEndTime
                                CommonUilts.undate_TheRealEndTIme_OfThePlanInDb(bean.planId, realEndTime);//修改realEndTime
                                // 计算spendTime 并修改DB
                                int realStartTime = CommonUilts.queryThePlanById(bean.planId).getRealStartTime();
                                int spendTime = realEndTime - realStartTime;
                                CommonUilts.undate_thePlanSpendTime_ofThePlanInDb(bean.planId, spendTime); //修改spendTime
                                CommonUilts.showToast("完成计划", false);
                                refreshCallBack.refresh();
                            }
                        })
                        .addSheetItem("明天继续", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                PlanTool.undate_ThePlanStatus_OfThePlanInDb(bean.planId, 12);//修改planStatus
                                //在明天新建一个计划
                                Plan plan = CommonUilts.queryThePlanById(bean.planId);

                                String userId= NetConfig.getUserId(mContext);
                                String token= NetConfig.getToken(mContext);

                                PlanTool.addPlan(userId,plan.getPlanTitle(), plan.getPlanContent(), plan.getPlanDate() + 1,
                                        plan.getPlanStartTime(), plan.getPlanEndTime(), plan.isRegular());
                                //上传服务器
                                new AddPlan(mContext, userId, token, plan.getPlanTitle(), plan.getPlanContent(), plan.getPlanDate() + 1,
                                        plan.getPlanStartTime(), plan.getPlanEndTime(), PlanTool.ENSTART, plan.isRegular(), new AddPlan.SuccessCallBack() {
                                    @Override
                                    public void onSuccess(int planId) {

                                    }
                                }, new AddPlan.FailCallBack() {
                                    @Override
                                    public void onFail(int status) {

                                    }
                                });
                                refreshCallBack.refresh();
                            }
                        }).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    //提供给页面刷新和加载时调用
    public void refresh() {
        List<ItemBean> newList = PlanTool.getPlanData().getRunningList();
        mList.removeAll(mList);
        mList.addAll(newList);
        notifyDataSetChanged();
    }

    public static interface RefreshCallBack {
        void refresh();
    }
}
