//package com.lbstudio.timer.app.app.adapter;
//
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.ie1e.mdialog.view.ActionSheetDialog;
//import com.lbstudio.timer.app.R;
//import com.lbstudio.timer.app.app.DbTool.PlanTool;
//import com.lbstudio.timer.app.app.activity.PlanDetail_Activity;
//import com.lbstudio.timer.app.app.javabean.Plan;
//import com.lbstudio.timer.app.app.util.CommonUilts;
//
//import java.util.List;
//
//public class FinishAdapter extends RecyclerView.Adapter<FinishAdapter.ViewHolder> {
//
//    private List<ItemBean> mList;
//    private Context mContext;
//    RefreshCallBack refreshCallBack;
//    SinglePlanCallBack singlePlanCallBack;
//
//
//    static class ViewHolder extends RecyclerView.ViewHolder {
//        View myView;
//        TextView title;
//        TextView content;
//        TextView bottom;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            myView = itemView;
//            title = (TextView) itemView.findViewById(R.id.tv_titlt);
//            content = (TextView) itemView.findViewById(R.id.tv_content);
//            bottom = (TextView) itemView.findViewById(R.id.bt_threePoint);
//        }
//    }
//
//    public FinishAdapter(Context mContext, List<ItemBean> list, RefreshCallBack refreshCallBack, SinglePlanCallBack singlePlanCallBack) {
//        this.mContext = mContext;
//        this.mList = list;
//        this.refreshCallBack = refreshCallBack;
//        this.singlePlanCallBack = singlePlanCallBack;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item3, null);
//        final ViewHolder holder = new ViewHolder(view);
//        return holder;
//    }
//
//    //将数据绑定到控件上
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position) {
//        final ItemBean bean = mList.get(position);
//        holder.title.setText(bean.itemTitle);
//        holder.content.setText(bean.itemContent);
//        holder.bottom.setBackgroundResource(R.drawable.icon_three_dot);
//        holder.title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int planId = bean.planId;
//                CommonUilts.thisToActivity(planId, PlanDetail_Activity.class);
//            }
//        });
//        holder.content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int planId = bean.planId;
//                CommonUilts.thisToActivity(planId, PlanDetail_Activity.class);
//            }
//        });
//        holder.bottom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new ActionSheetDialog(mContext)
//                        .builder()
//                        .setTitle(bean.itemTitle)
//                        .setCanceledOnTouchOutside(true)
//                        .setDialogLocation(Gravity.CENTER)
//                        .addSheetItem("再次开启计划", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
//                            @Override
//                            public void onClick(int which) {
//                                if (singlePlanCallBack.isSingleRunningPlan()) {
//                                    CommonUilts.undate_ThePlanStatus_OfThePlanInDb(bean.planId, 11);
//                                    CommonUilts.showToast("再次开启计划成功", false);
//                                    refreshCallBack.refresh();
//                                } else {
//                                    CommonUilts.showToast("只能同时开始一个计划", false);
//                                }
//
//                            }
//                        })
//                        .show();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mList.size();
//    }
//
//
//    //提供给页面刷新和加载时调用
//    public void add(List<ItemBean> addMessageList) {
//        //增加数据
//        int position = mList.size();
//        mList.addAll(position, addMessageList);
//        notifyItemInserted(position);
//    }
//
//    public void refresh() {
//        //刷新数据
//        List<ItemBean> newList = PlanTool.getPlanData().getFinishList();
//        mList.removeAll(mList);
//        mList.addAll(newList);
//        notifyDataSetChanged();
//    }
//
//    public static interface RefreshCallBack {
//        void refresh();
//    }
//
//    public static interface SinglePlanCallBack {
//        boolean isSingleRunningPlan();
//    }
//}
