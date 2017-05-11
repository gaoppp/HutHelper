package com.gaop.huthelper.db;

import com.gaop.huthelper.app.MApplication;
import com.gaop.huthelperdao.CourseGrade;
import com.gaop.huthelperdao.Exam;
import com.gaop.huthelperdao.Explesson;
import com.gaop.huthelperdao.ExplessonDao;
import com.gaop.huthelperdao.Grade;
import com.gaop.huthelperdao.Lesson;
import com.gaop.huthelperdao.LessonDao;
import com.gaop.huthelperdao.Menu;
import com.gaop.huthelperdao.MenuDao;
import com.gaop.huthelperdao.Notice;
import com.gaop.huthelperdao.Ranking;
import com.gaop.huthelperdao.RankingDao;
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

    public static void deleteLessonbyImport() {
        MApplication.daoManager.deleteLessonbyImport(LessonDao.Properties.Addbyuser.eq(false));
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

    public static void deleteNoticeByid(Long id) {
        MApplication.daoManager.deleteNoteiceById(id);
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


    public static List<Explesson> getExpLessonByWeek(String week, String week_no) {
        return MApplication.daoManager.queryExpLesson(ExplessonDao.Properties.Week.eq(week), ExplessonDao.Properties.Weeks_no.eq(week_no));
    }

    /***************
     * Exam
     */
    public static List<Exam> getExam() {
        return MApplication.daoManager.orderAscExam();
    }

    public static void insertListExam(List<Exam> list) {
        MApplication.daoManager.insertListExam(list);
    }

    public static void deleteAllExam() {
        MApplication.daoManager.deleteAllExam();
    }

    /***************
     * Menu
     */

    public static List<Menu> getMenuInMain() {
        return MApplication.daoManager.queryMenu((MenuDao.Properties.IsMain.eq(true)));
    }

    public static List<Menu> getMenuInMainSortByIndex() {
        return MApplication.daoManager.queryMenuSortByIndex((MenuDao.Properties.IsMain.eq(true)));
    }

    public static List<Menu> getMenuNotInMain() {
        return MApplication.daoManager.queryMenu((MenuDao.Properties.IsMain.eq(false)));
    }

    public static void insertMenu(Menu list) {
        MApplication.daoManager.insertMenu(list);
    }

    public static List<Menu> getMenu() {
        return MApplication.daoManager.orderAscMenu();
    }

    public static void insertListMenu(List<Menu> list) {
        MApplication.daoManager.insertListMenu(list);
    }

    public static void deleteAllMenu() {
        MApplication.daoManager.deleteAllMenu();
    }

    /***************
     * RankingApiData
     */


    public static void insertRanking(Ranking list) {
        MApplication.daoManager.insertRanking(list);
    }

    public static List<Ranking> getRanking() {
        return MApplication.daoManager.orderAscRanking();
    }

    public static void insertListRanking(List<Ranking> list) {
        MApplication.daoManager.insertListRanking(list);
    }

    public static void deleteAllRanking() {
        MApplication.daoManager.deleteAllRanking();
    }

    public static List<Ranking> getNjAndXnRanking() {
        return MApplication.daoManager.queryRanking((RankingDao.Properties.ISXN.eq(true)), RankingDao.Properties.ISBJ.eq(false));
    }

    public static List<Ranking> getNjAndXqRanking() {
        return MApplication.daoManager.queryRanking((RankingDao.Properties.ISXN.eq(false)), RankingDao.Properties.ISBJ.eq(false));
    }

    public static List<Ranking> getBjAndXnRanking() {
        return MApplication.daoManager.queryRanking((RankingDao.Properties.ISXN.eq(true)), RankingDao.Properties.ISBJ.eq(true));
    }

    public static List<Ranking> getBjAndXqRanking() {
        return MApplication.daoManager.queryRanking((RankingDao.Properties.ISXN.eq(false)), RankingDao.Properties.ISBJ.eq(true));
    }
}
