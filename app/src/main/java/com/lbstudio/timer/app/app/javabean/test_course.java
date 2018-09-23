package com.lbstudio.timer.app.app.javabean;

public class test_course {
    //    单片机原理及应用 实0218 7-14周 周一1-2节 潘峰
//    体育篮球 体育馆地下篮球场 1-16周 周一3-4节 陈思宇
//    机械工程制图 综实513 1-16周 周一6-7节 王慧
//    电路分析 教6012 1-16周 周二4-5节 张军
//    物理实验 综0907 1-10周 周二7-9节 王雪梅
//    Python语言程序设计 综实501 1-16周 周二10-11节 廖文江
    private String courseName; //课名
    private String courseSite; //教室
    private String weekCount; //周数
    private String lessonCount;  //节数
    private String teacher;  //老师

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseSite() {
        return courseSite;
    }

    public void setCourseSite(String courseSite) {
        this.courseSite = courseSite;
    }

    public String getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(String weekCount) {
        this.weekCount = weekCount;
    }

    public String getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(String lessonCount) {
        this.lessonCount = lessonCount;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
