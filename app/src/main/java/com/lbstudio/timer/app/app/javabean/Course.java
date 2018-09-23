package com.lbstudio.timer.app.app.javabean;

import org.litepal.crud.LitePalSupport;

public class Course extends LitePalSupport{
    private int id;
    private String name; //课名
    private String site; //教室
    private int term;  //学期 2018是指“2018-2019学年”
    private String week0fClass;  //上课周  "1,2,4,7"
    private int week;  //周几
    private int  startLesson; //开始节
    private int  endLesson; //结束节
    private String teacher;  //老师
    private int date;  //第一周周一的日期

    //用于标识课程所属人
    private String userId; //用户id

    //用于同步的字段
    private String courseId;//服务器数据中的id
    private int status;  //用来标识记录的状态  0 表示本地新增     -1 表示标记删除   1 表示本地更新   9表示已同步
    private int anchor;  //记录服务端同步过来的时间戳

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getWeek0fClass() {
        return week0fClass;
    }

    public void setWeek0fClass(String week0fClass) {
        this.week0fClass = week0fClass;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getStartLesson() {
        return startLesson;
    }

    public void setStartLesson(int startLesson) {
        this.startLesson = startLesson;
    }

    public int getEndLesson() {
        return endLesson;
    }

    public void setEndLesson(int endLesson) {
        this.endLesson = endLesson;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
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
