package com.lbstudio.timer.app.app.adapter;


public class ItemBean {
    int planId;
    String itemTitle;
    String itemContent;

    public ItemBean(int planId, String itemTitle, String itemContent) {
        this.itemTitle = itemTitle;
        this.itemContent = itemContent;
        this.planId = planId;
    }

    public int getPlanId() {
        return planId;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }
}