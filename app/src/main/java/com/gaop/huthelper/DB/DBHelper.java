package com.gaop.huthelper.DB;

import com.gaop.huthelper.MApplication;
import com.gaop.huthelperdao.CourseGrade;
import com.gaop.huthelperdao.Explesson;
import com.gaop.huthelperdao.Grade;
import com.gaop.huthelperdao.Lesson;
import com.gaop.huthelperdao.LessonDao;
import com.gaop.huthelperdao.Notice;
import com.gaop.huthelperdao.Trem;
import com.gaop.huthelperdao.User;

import java.util.List;


/**
 * Created by gaop1 on 2016/8/30.
 */
public class DBHelper {
    /***********************
     * User
     **********************/
    public static void insertUserDao(User user) {
        MApplication.daoManager.insertUser(user);
    }

    public static List<User> getUserDao() {
        List<User> list = null;
        list = MApplication.daoManager.orderAscUser();
        return list;
    }

    public static void deleteAllUser() {
        MApplication.daoManager.deleteAllUser();
    }

    public static void UpdateUser(User user) {
        MApplication.daoManager.updateUser(user);
    }

    /***********************
     * Lesson
     ****************/
    public static List<Lesson> getLessonDao() {
        List<Lesson> list = null;
        list = MApplication.daoManager.orderAscLesson();
        return list;
    }

    public static void insertLesson(Lesson lesson) {

        if (lesson == null)
            return;
        MApplication.daoManager.insertLesson(lesson);
    }

    public static List<Lesson> getLessonByWeek(String num) {
        return MApplication.daoManager.queryLesson(LessonDao.Properties.Xqj.eq(num));
    }

    public static void insertListLessonDao(List<Lesson> list) {
        if (list == null)
            return;
        MApplication.daoManager.insertListLesson(list);
    }

    public static void deleteLessonById(List<Long> list) {
        MApplication.daoManager.deleteLessonByid(list);
    }

    public static void deleteAllLesson() {
        MApplication.daoManager.deleteAllLesson();
    }

    /*********************
     * Grade
     ************************/
    public static List<Grade> getGradeDao() {
        List<Grade> gradeList = null;
        gradeList = MApplication.daoManager.orderAscGrade();
        return gradeList;
    }

    public static void insertGradeDao(Grade grade) {
        MApplication.daoManager.insertGrade(grade);
    }

    public static void deleteAllGrade() {
        MApplication.daoManager.deleteAllGrade();
    }

    /***********************
     * Trem
     ***************/
    public static List<Trem> getTremDao() {
        return MApplication.daoManager.orderAscTrem();
    }

    public static void insertTremList(List<Trem> list) {
        MApplication.daoManager.insertListTrem(list);
    }

    public static void deleteAllTrem() {
        MApplication.daoManager.deleteAllTrem();
    }

    /********************
     * CourseGrade
     *********************/
    public static List<CourseGrade> getCourseGradeDao() {
        return MApplication.daoManager.orderAscCourseGrade();
    }

    public static void insertCourseGradeList(List<CourseGrade> list) {
        if (list == null)
            return;
        MApplication.daoManager.insertListCourseGrade(list);
    }

    public static void deleteAllCourseGrade() {
        MApplication.daoManager.deleteAllCourseGrade();
    }

    /***************
     * Notice
     */
    public static List<Notice> getNoticeDao() {
        return MApplication.daoManager.orderAscnotice();
    }

    public static void insertNotice(Notice list) {
        MApplication.daoManager.insertNotice(list);
    }

    public static void deleteAllNotice() {
        MApplication.daoManager.deleteAllNotice();
    }

    /***************
     * ExpLesson
     */
    public static List<Explesson> getExpLessons() {
        return MApplication.daoManager.orderAscExpLesson();
    }

    public static void insertListExpLesson(List<Explesson> list) {
        MApplication.daoManager.insertListExpLesson(list);
    }

    public static void deleteAllExpLesson() {
        MApplication.daoManager.deleteAllExpLesson();
    }

}
