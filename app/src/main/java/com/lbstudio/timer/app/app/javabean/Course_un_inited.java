package com.lbstudio.timer.app.app.javabean;

public class Course_un_inited {

    private String name; //课名
    private String site; //教室
    private int term;  //学期 2018是指“2018-2019学年”
    private String week0fClass;  //上课周  "1,2,4,7"
    private int week;  //周几  //todo 选修课的未拿到
    private int  startLesson; //开始节
    private int  endLesson; //结束节
    private String teacher;  //老师

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
}
