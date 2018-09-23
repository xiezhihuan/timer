package com.lbstudio.timer.app.app.calendar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lbstudio.timer.app.AppManager;
import com.lbstudio.timer.app.R;
import com.lbstudio.timer.app.app.activity.PlanDetail_Activity;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.util.CommonUilts;
import com.necer.ncalendar.utils.Attrs;
import com.necer.ncalendar.utils.MyLog;

import java.util.Collections;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.calendarViewHolder>  implements ItemTouchHelperAdapter{
    private Context context;
    private List<com.lbstudio.timer.app.app.javabean.Plan> allPlanOfDay;
    static int mPosition;
    public Adapter(Context context, List<Plan> allPlanOfDay) {
        this.context = context;
        this.allPlanOfDay = allPlanOfDay;
    }

    @Override
    public calendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new calendarViewHolder(LayoutInflater.from(context).inflate(R.layout.calendar_item, parent, false));

    }

    @Override
    public void onBindViewHolder(calendarViewHolder holder, final int position) {
        mPosition=position;

        holder.planContent.setText(allPlanOfDay.get(position).getPlanTitle());
        holder.planTime.setText(allPlanOfDay.get(position).getPlanContent());
        holder.xuhao.setTextColor(Attrs.selectCircleColor);

        final int planId = allPlanOfDay.get(position).getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUilts.thisToActivity(planId,"CalendarActivity",PlanDetail_Activity.class);
                AppManager.getInstance().removeCurrent();
            }
        });
    }


    @Override
    public int getItemCount() {
        return allPlanOfDay.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //交换位置
        Collections.swap(allPlanOfDay,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemDissmiss(int position) {
        //移除数据
        allPlanOfDay.remove(position);
        notifyItemRemoved(position);
    }

    static class calendarViewHolder extends RecyclerView.ViewHolder {
        TextView planContent;
        TextView planTime;
        TextView xuhao;

        public calendarViewHolder(View itemView) {
            super(itemView);
            planContent = (TextView) itemView.findViewById(R.id.tv_planContent);
            planTime = (TextView) itemView.findViewById(R.id.tv_planTime);
            xuhao= (TextView) itemView.findViewById(R.id.xuhao);
        }
        public static int getPositon(){
            return mPosition;
        }
    }
}
