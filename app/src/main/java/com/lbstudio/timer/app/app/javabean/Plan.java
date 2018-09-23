package com.lbstudio.timer.app.app.javabean;

import org.litepal.crud.LitePalSupport;

public class Plan extends LitePalSupport {
    private int id; //序号 唯一标识符
    private String planTitle; //标题
    private String planContent; //内容
    private int planDate;  //日期  20180824
    private int planStartTime;  //开始时间  六十进制转为10进制  如： 08：20 --> 500  即一天中的第500分钟
    private int planEndTime;      //结束时间    六十进制转为10进制  如： 08：20 --> 500  即一天中的第500分钟
    private int realStartTime;  //开始时间  六十进制转为10进制  如： 08：20 --> 500  即一天中的第500分钟
    private int realEndTime;      //结束时间    六十进制转为10进制  如： 08：20 --> 500  即一天中的第500分钟
    private int spendTime;
    private boolean isRegular;  //是否为固定耗时事件  true 表示固定耗时，不需要用户手动点击开始计划或结束计划
    private int planStatus; //状态   10 表示未开始     11 进行中       12 表示已完成

    //用于标识计划所属人
    private String userId; //用户的唯一标识

    //用于同步的字段
    private String planId;//服务器数据中的id
    private int status;  //用来标识记录的状态  0 表示本地新增     -1 表示标记删除   1 表示本地更新   9表示已同步
    private int anchor;  //记录服务端同步过来的时间戳

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanContent() {
        return planContent;
    }

    public void setPlanContent(String planContent) {
        this.planContent = planContent;
    }

    public int getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(int planStatus) {
        this.planStatus = planStatus;
    }

    public int getPlanDate() {
        return planDate;
    }

    public void setPlanDate(int planDate) {
        this.planDate = planDate;
    }

    public int getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(int planStartTime) {
        this.planStartTime = planStartTime;
    }

    public int getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(int planEndTime) {
        this.planEndTime = planEndTime;
    }

    public int getRealStartTime() {
        return realStartTime;
    }

    public void setRealStartTime(int realStartTime) {
        this.realStartTime = realStartTime;
    }

    public int getRealEndTime() {
        return realEndTime;
    }

    public void setRealEndTime(int realEndTime) {
        this.realEndTime = realEndTime;
    }

    public int getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(int spendTime) {
        this.spendTime = spendTime;
    }

    public boolean isRegular() {
        return isRegular;
    }

    public void setRegular(boolean regular) {
        isRegular = regular;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAnchor() {
        return anchor;
    }

    public void setAnchor(int anchor) {
        this.anchor = anchor;
    }
}