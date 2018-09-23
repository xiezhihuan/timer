package com.lbstudio.timer.app.app.DbTool;

import android.util.Log;

import com.lbstudio.timer.app.app.adapter.ItemBean;
import com.lbstudio.timer.app.app.adapter.RunningAdapter;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.net.NetConfig;
import com.lbstudio.timer.app.app.util.CommonUilts;

import org.litepal.LitePal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PlanTool {
    public static final int ENSTART = 10;
    public final static int RUNNING = 11;
    public final static int FINISH = 12;

    /**
     * 新建一条计划数据
     *
     * @param title
     * @param content
     * @param date
     * @param startTime
     * @param endTime
     * @param isRegular
     */
    public static void addPlan(String userId, String title, String content, int date, int startTime, int endTime, boolean isRegular) {
        Plan plan = new Plan();
        plan.setUserId(userId);
        plan.setPlanTitle(title);
        plan.setPlanContent(content);
        plan.setPlanDate(date);
        plan.setPlanStartTime(startTime);
        plan.setPlanEndTime(endTime);
        plan.setRegular(isRegular);
        plan.setPlanStatus(10);
        plan.setStatus(0); //本地新增
        plan.save();
    }


    /**
     * 根据id修改planStatus
     *
     * @param id
     * @param undateStatus
     */
    public static void undate_ThePlanStatus_OfThePlanInDb(int id, int undateStatus) {
        Plan plan = new Plan();
        plan.setPlanStatus(undateStatus);
        plan.updateAll("id = ?", String.valueOf(id));
    }

    /**
     * 根据id修改realStartTime
     *
     * @param id
     * @param realStartTime
     */
    public static void undate_TheRealStartTIme_OfThePlanInDb(int id, int realStartTime) {
        Plan plan = new Plan();
        plan.setRealStartTime(realStartTime);
        plan.updateAll("id = ?", String.valueOf(id));
    }

    /**
     * 通过planId修改计划的更新状态
     *
     * @param planId
     * @param status
     */
    public static void updatePlan_status(int planId, int status) {
        Plan plan = new Plan();
        plan.setStatus(status);
        plan.updateAll("planId=?", String.valueOf(planId));
    }

    public static void sync_staus_9() {
        Plan plan = new Plan();
        plan.setStatus(Integer.parseInt(NetConfig.HAVE_SYNC));
        plan.updateAll("status<? and status > ?", NetConfig.HAVE_SYNC,"0");
    }

    /**
     * 通过日期查询计划数据
     *
     * @param date
     * @return
     */
    public static List<Plan> quaryPlan_byDate(String date) {
        Plan plan = new Plan();
        List<Plan> plans = LitePal.where("planDate=?", date).find(Plan.class);
        return plans;
    }

    public static Plan quaryPlan_byPlanId(String planId) {
        Plan plan = new Plan();
        List<Plan> plans = LitePal.where("planId=?", planId).find(Plan.class);
        return plans.get(0);
    }

    public static List<Plan> quaryPlan_enSync() {
        List<Plan> plans = LitePal.where("status<?", "9").find(Plan.class);
        List<Plan> plans_ensync=new ArrayList<>();
        for (Plan plan:plans){
            int status = plan.getStatus();
            if (status>0){
                plans_ensync.add(plan);
            }
        }
        return plans_ensync;
    }

    public static List<Plan> quaryPlan_havaDelected() {
        List<Plan> plans = LitePal.where("status<?", "0").find(Plan.class);
        return plans;
    }

    /**
     * 获得首页需要的数据
     *
     * @return
     */
    public static PlanData getPlanData() {
        int runningNum;
        int enStartNum;
        int finishiNum;
        List<ItemBean> runningList;
        List<ItemBean> enStartList;
        List<ItemBean> finishList;

        String nowDateNum = CommonUilts.getNowDateNum(); //获得此刻的时间
        List<Plan> plans = CommonUilts.queryPlanDataByDate(nowDateNum);//获得今日的计划数据
        Log.d("dddddd", "getPlanData: " + plans.size());
        runningNum = 0;        //进行中的数量
        enStartNum = 0;        //未开始的数量
        finishiNum = 0;        //已完成的数量
        List<Plan> runningPlans = new ArrayList<>(); //进行中的计划数据
        List<Plan> enStartPlans = new ArrayList<>(); //未开始的计划数据
        List<Plan> finishPlans = new ArrayList<>();//已完成的计划数据
        for (Plan plan : plans) {
            int planStatus = plan.getPlanStatus();
            if (planStatus == 10) {
                enStartNum += 1;
                enStartPlans.add(plan);
            } else if (planStatus == 11) {
                runningNum += 1;
                runningPlans.add(plan);
            } else if (planStatus == 12) {
                finishiNum += 1;
                Log.d("debug", "finishiNum: " + finishiNum);
                finishPlans.add(plan);
            }
        }

        Log.d("dffdfdfdfbh", "runningPlans: " + runningPlans.size() + " enStartPlans:" + enStartPlans.size() + "  finishPlans:" + finishPlans.size()
        );
        //判断三个状态的计划集合中是否存在空的  防止空指针异常
        boolean isNull_running = false;
        boolean isNull_enStart = false;
        boolean isNull_finish = false;
        if (runningPlans.isEmpty()) {
            isNull_running = true;
        }
        if (enStartPlans.isEmpty()) {
            isNull_enStart = true;
        }
        if (finishPlans.isEmpty()) {
            isNull_finish = true;
        }

        //为“进行中"的计划配置item数据
        runningList = new ArrayList<>();
        if (!isNull_running) {
            for (int i = 0; i < runningPlans.size(); i++) {
                Plan plan = runningPlans.get(i);
                final String title = plan.getPlanTitle();
                final String content = plan.getPlanContent();
                final int planId = plan.getId();

                runningList.add(new ItemBean(planId, title, content));
            }
        }

        //为“未开始”的计划配置item数据
        enStartList = new ArrayList<>();
        if (!isNull_enStart) {
            for (int i = 0; i < enStartPlans.size(); i++) {
                Plan plan = enStartPlans.get(i);
                final String title = plan.getPlanTitle();
                final String content = plan.getPlanContent();
                final int id = plan.getId();

                enStartList.add(new ItemBean(id, title, content));
            }
        }

        //为“已完成”的计划配置item数据
        finishList = new ArrayList<>();
        if (!isNull_finish) {
            for (int i = 0; i < finishPlans.size(); i++) {
                Plan plan = finishPlans.get(i);
                final String title = plan.getPlanTitle();
                String content = plan.getPlanContent();
                final int id = plan.getId();

                finishList.add(new ItemBean(id, title, content));
            }
        }

        return new PlanData(runningNum, enStartNum, finishiNum, runningList, enStartList, finishList);
    }

    public static class PlanData {
        int runningNum;
        int enStartNum;
        int finishiNum;
        List<ItemBean> runningList;
        List<ItemBean> enStartList;
        List<ItemBean> finishList;

        public PlanData(int runningNum, int enStartNum, int finishiNum,
                        List<ItemBean> runningList,
                        List<ItemBean> enStartList,
                        List<ItemBean> finishList) {
            this.runningNum = runningNum;
            this.enStartNum = enStartNum;
            this.finishiNum = finishiNum;
            this.runningList = runningList;
            this.enStartList = enStartList;
            this.finishList = finishList;
        }

        public int getRunningNum() {
            return runningNum;
        }

        public void setRunningNum(int runningNum) {
            this.runningNum = runningNum;
        }

        public int getEnStartNum() {
            return enStartNum;
        }

        public void setEnStartNum(int enStartNum) {
            this.enStartNum = enStartNum;
        }

        public int getFinishiNum() {
            return finishiNum;
        }

        public void setFinishiNum(int finishiNum) {
            this.finishiNum = finishiNum;
        }

        public List<ItemBean> getRunningList() {
            return runningList;
        }

        public void setRunningList(List<ItemBean> runningList) {
            this.runningList = runningList;
        }

        public List<ItemBean> getEnStartList() {
            return enStartList;
        }

        public void setEnStartList(List<ItemBean> enStartList) {
            this.enStartList = enStartList;
        }

        public List<ItemBean> getFinishList() {
            return finishList;
        }

        public void setFinishList(List<ItemBean> finishList) {
            this.finishList = finishList;
        }

    }

    /**
     * 更新首页
     */
    public static void refresh(List<ItemBean> runningList,
                               List<ItemBean> enStartList,
                               List<ItemBean> finishList) {
//        RunningAdapter
    }

    /**
     * 删除所有计划数据
     */
    public static void delectAllPlan() {
        LitePal.deleteAll("Plan");
    }

    public static void delectPlan_planId(String planId) {
        LitePal.deleteAll(Plan.class, "planId=?", planId);
    }
}
