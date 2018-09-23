package com.lbstudio.timer.app.app.DbTool;

import com.lbstudio.timer.app.app.javabean.Course;
import com.lbstudio.timer.app.app.javabean.Plan;
import com.lbstudio.timer.app.app.net.NetConfig;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class CourseTool {

    /**
     * 通过id找到并修改课程信息
     * 用在编辑页
     *
     * @param courseId
     * @param name
     * @param site
     * @param weekOfClass
     * @param startLesson
     * @param endLesson
     * @param teacher
     */
    public static void undateCourse_byId(int courseId, String name, String site, String weekOfClass,
                                         int startLesson, int endLesson, String teacher,int status) {
        Course course = new Course();

        course.setName(name);
        course.setSite(site);
        course.setWeek0fClass(weekOfClass);
        course.setStartLesson(startLesson);
        course.setEndLesson(endLesson);
        course.setTeacher(teacher);
        course.setStatus(status);

        course.updateAll("courseId = ?", String.valueOf(courseId));
    }

    public static void undateCourse_status_byId(int courseId,int status) {
        Course course = new Course();
        course.setStatus(status);
        course.updateAll("courseId = ?", String.valueOf(courseId));
    }

    /**
     * 在数据库中，新建一个课程
     */
    public static void addCourse(String name, String site, int term, String week0fClass, int week,
                                 int startLesson, int endLesson, String teacher, int date, int status) {
        Course course = new Course();
        course.setName(name);
        course.setSite(site);
        course.setTerm(term);
        course.setWeek0fClass(week0fClass);
        course.setWeek(week);
        course.setStartLesson(startLesson);
        course.setEndLesson(endLesson);
        course.setTeacher(teacher);
        course.setDate(date);
        course.setStatus(status);
        course.save();
    }

    public static List<Course> quaryCourse_enSync() {
        List<Course> courses = LitePal.where("status<?", "9").find(Course.class);
        List<Course> courses_ensync=new ArrayList<>();
        for (Course plan:courses){
            int status = plan.getStatus();
            if (status>0){
                courses_ensync.add(plan);
            }
        }
        return courses_ensync;
    }



    public static List<Course> quaryCourse_havaDelect() {
        List<Course> courses = LitePal.where("status<?", "0").find(Course.class);
        return courses;
    }

    public static Course quaryCourse_byCourseId(String courseId) {
        List<Course> courses = LitePal.where("courseId=?", courseId).find(Course.class);
        return courses.get(0);
    }


    public static void sync_staus_9() {

        Course course = new Course();
        course.setStatus(Integer.parseInt(NetConfig.HAVE_SYNC));
        course.updateAll("status < ? and status > ?", NetConfig.HAVE_SYNC,"0");
    }

    /**
     * 删除所有课程数据
     */
    public static void delectAllCourse() {
//        LitePal.deleteAll(Course.class, "id>0");
        LitePal.deleteAll("Course");
    }


    public static void delectPlan_planId(String courseId) {
        LitePal.deleteAll(Course.class, "courseId=?", courseId);
    }

}
