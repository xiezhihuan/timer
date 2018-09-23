package com.lbstudio.timer.app.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ie1e.mdialog.view.ActionSheetDialog;
import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.DbTool.PlanTool;
import com.lbstudio.timer.app.app.activity.PlanDetail_Activity;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.net.AddPlan;
import com.lbstudio.timer.app.app.net.DelectPlan;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.net.UpdatePlan;
import com.lbstudio.timer.app.app.util.CommonUilts;

import java.util.ArrayList;
import java.util.List;

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

    final int RUNNING = 0;
    final int ENSTART = 1;
    final int FINISH = 2;
    String userId;
    String token;
    Context mContext;
    List<List<ItemBean>> lists;
    List<String> groupNames;

    public MyExpandableListViewAdapter(Context context, List<String> groupNames, List<List<ItemBean>> lists) {
        userId = NetConfig.getUserId(context);
        token = NetConfig.getToken(context);
        this.lists = lists;
        this.groupNames = groupNames;
        this.mContext = context;
    }

    @Override
    public int getGroupCount() {
        return lists.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return lists.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupNames.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return lists.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int parentPos, boolean isExpand, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_expand_group, null);
        }
        view.setTag(R.layout.item_expand_group, parentPos);
        TextView text = (TextView) view.findViewById(R.id.enStart_number);
        TextView itemName = (TextView) view.findViewById(R.id.text_enStart);
        itemName.setText(groupNames.get(parentPos));
        TextView jiantou = (TextView) view.findViewById(R.id.jiantou);
        text.setText("(" + String.valueOf(lists.get(parentPos).size()) + ")");
        if (isExpand) {
            jiantou.setBackgroundResource(R.drawable.down_pull);
        } else {
            jiantou.setBackgroundResource(R.drawable.down_pull_shou);
        }
        return view;
    }

    @Override
    public View getChildView(final int parentPos, final int childPos, boolean b, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
        }
        final ItemBean itemBean = lists.get(parentPos).get(childPos);
        view.setTag(R.layout.item, childPos);
        TextView title = (TextView) view.findViewById(R.id.tv_titlt);
        TextView content = (TextView) view.findViewById(R.id.tv_content);
        TextView bottom = (TextView) view.findViewById(R.id.bt_threePoint);
        title.setText(itemBean.itemTitle);
        content.setText(itemBean.itemContent);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem(parentPos, itemBean);
            }
        });
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickItem(parentPos, itemBean);
            }
        });
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBottom(parentPos, itemBean);
            }


        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private void clickBottom(int parentPos, ItemBean itemBean) {
        switch (parentPos) {
            case RUNNING:
                runningSelectView(itemBean);
                break;
            case ENSTART:
                enStartSelectView(itemBean);
                break;
            case FINISH:
                finishSelectView(itemBean);
                break;

        }
    }

    private void finishSelectView(final ItemBean bean) {
        new ActionSheetDialog(mContext)
                .builder()
                .setTitle(bean.itemTitle)
                .setCanceledOnTouchOutside(true)
                .setDialogLocation(Gravity.CENTER)
                .addSheetItem("再次开启计划", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        if (lists.get(RUNNING).size() == 0) {
                            PlanTool.undate_ThePlanStatus_OfThePlanInDb(bean.planId, PlanTool.RUNNING);
                            CommonUilts.showToast("再次开启计划成功", false);
                            refresh();
                        } else {
                            CommonUilts.showToast("只能同时开始一个计划", false);
                        }

                    }
                })
                .show();
    }

    private void enStartSelectView(final ItemBean bean) {

        new ActionSheetDialog(mContext)
                .builder()
                .setTitle(bean.itemContent)
                .setCanceledOnTouchOutside(true)
                .setDialogLocation(Gravity.CENTER)
                .addSheetItem("开始计划", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        if (lists.get(RUNNING).size() == 0) {
                            PlanTool.undate_ThePlanStatus_OfThePlanInDb(bean.planId, PlanTool.RUNNING);//一、修改planStatus为进行中
                            int realStartTime = CommonUilts.getNowMinuteNum();//记录realStartTime
                            PlanTool.undate_TheRealStartTIme_OfThePlanInDb(bean.planId, realStartTime); //二、修改realStartTime
                            //刷新页面
                            refresh();
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
                        //本地修改
                        CommonUilts.undate_thePlanDate_ofThePlanInDb(bean.planId, date + 1); //日期+1 修改日期
                        //服务器修改
                        Plan plan = PlanTool.quaryPlan_byPlanId(String.valueOf(bean.planId));
                        new UpdatePlan(mContext, Integer.parseInt(userId), token, plan.getPlanTitle(), plan.getPlanContent(), plan.getPlanDate(),
                                plan.getPlanStartTime(), plan.getPlanEndTime(), 0, 0, plan.isRegular(), PlanTool.ENSTART,
                                new UpdatePlan.SuccessCallback() {
                                    @Override
                                    public void onSuccess() {
                                        //标记本地计划为同步
                                        PlanTool.updatePlan_status(bean.planId, Integer.parseInt(NetConfig.HAVE_SYNC));
                                    }
                                }, new UpdatePlan.FailCallback() {
                            @Override
                            public void onFail(int status) {

                            }
                        });
                        CommonUilts.showToast("计划推迟一天", false);
                        refresh();
                    }
                })
                .addSheetItem("删除计划", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        //逻辑删除计划 status -1
                        PlanTool.updatePlan_status(bean.planId, Integer.parseInt(NetConfig.DELECT));
                        //在服务器物理删除
                        new DelectPlan(Integer.parseInt(userId), token, bean.planId, new DelectPlan.SuccessCallback() {
                            @Override
                            public void onSuccess() {
                                //本地物理删除
                                PlanTool.delectPlan_planId(String.valueOf(bean.planId));
                            }
                        }, new DelectPlan.FailCallback() {
                            @Override
                            public void onFail(int status) {

                            }
                        });
                        CommonUilts.showToast("删除计划成功", false);
                        refresh();

                    }
                })
                .show();
    }

    private void runningSelectView(final ItemBean bean) {
        new ActionSheetDialog(mContext)
                .builder()
                .setTitle(bean.itemTitle)
                .setCanceledOnTouchOutside(true)
                .setDialogLocation(Gravity.CENTER)
                .addSheetItem("完成计划", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        PlanTool.undate_ThePlanStatus_OfThePlanInDb(bean.planId, PlanTool.FINISH);//修改计划状态
                        int realEndTime = CommonUilts.getNowMinuteNum();//记录realEndTime
                        CommonUilts.undate_TheRealEndTIme_OfThePlanInDb(bean.planId, realEndTime);//修改realEndTime
                        // 计算spendTime 并修改DB
                        int realStartTime = CommonUilts.queryThePlanById(bean.planId).getRealStartTime();
                        int spendTime = realEndTime - realStartTime;
                        CommonUilts.undate_thePlanSpendTime_ofThePlanInDb(bean.planId, spendTime); //修改spendTime
                        CommonUilts.showToast("完成计划", false);
                        refresh();

                    }
                })
                .addSheetItem("明天继续", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        PlanTool.undate_ThePlanStatus_OfThePlanInDb(bean.planId, PlanTool.FINISH);//修改planStatus
                        //在明天新建一个计划
                        Plan plan = CommonUilts.queryThePlanById(bean.planId);
                        //日期加1，本地新建一条计划
                        String userId = NetConfig.getUserId(mContext);
                        String token = NetConfig.getToken(mContext);
                        PlanTool.addPlan(userId, plan.getPlanTitle(), plan.getPlanContent(), plan.getPlanDate() + 1,
                                plan.getPlanStartTime(), plan.getPlanEndTime(), plan.isRegular());
                        //在服务器新建一条
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
                        CommonUilts.showToast("完成计划,明天继续", false);
                        refresh();
                    }
                }).show();
    }

    private void clickItem(int parentPos, ItemBean itemBean) {
        switch (parentPos) {
            case RUNNING:
                CommonUilts.thisToActivity(itemBean.planId, PlanDetail_Activity.class);
                break;
            case ENSTART:
                CommonUilts.thisToActivity(itemBean.planId, PlanDetail_Activity.class);
                break;
            case FINISH:
                CommonUilts.thisToActivity(itemBean.planId, PlanDetail_Activity.class);
                break;

        }
    }

    public void refresh() {
        //获得新数据
        PlanTool.PlanData planData = PlanTool.getPlanData();
        List<ItemBean> runningList = planData.getRunningList();
        List<ItemBean> enStartList = planData.getEnStartList();
        List<ItemBean> finishList = planData.getFinishList();
        List<List<ItemBean>> newList = new ArrayList<>();
        newList.add(runningList);
        newList.add(enStartList);
        newList.add(finishList);

        //更新数据
        lists.removeAll(lists);
        lists.addAll(newList);
        notifyDataSetChanged();
    }
}
