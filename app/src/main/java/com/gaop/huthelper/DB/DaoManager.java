package com.gaop.huthelper.DB;

import android.content.Context;

import com.gaop.huthelper.MApplication;
import com.gaop.huthelperdao.CourseGrade;
import com.gaop.huthelperdao.CourseGradeDao;
import com.gaop.huthelperdao.DaoSession;
import com.gaop.huthelperdao.Exam;
import com.gaop.huthelperdao.ExamDao;
import com.gaop.huthelperdao.Explesson;
import com.gaop.huthelperdao.ExplessonDao;
import com.gaop.huthelperdao.Grade;
import com.gaop.huthelperdao.GradeDao;
import com.gaop.huthelperdao.Lesson;
import com.gaop.huthelperdao.LessonDao;
import com.gaop.huthelperdao.Notice;
import com.gaop.huthelperdao.NoticeDao;
import com.gaop.huthelperdao.Trem;
import com.gaop.huthelperdao.TremDao;
import com.gaop.huthelperdao.User;
import com.gaop.huthelperdao.UserDao;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

/**
 * Created by gaop1 on 2016/8/30.
 */
public class DaoManager {
    private static DaoManager instance;
    private static Context appContext;
    private DaoSession mDaoSession;
    private UserDao userDao;
    private ExplessonDao explessonDao;
    private TremDao tremDao;
    private CourseGradeDao courseGradeDao;
    private LessonDao lessonDao;
    private GradeDao gradeDao;
    private NoticeDao noticeDao;
    private ExamDao examDao;

    public DaoManager() {

    }

    public static DaoManager getInstance(Context context) {
        if (instance == null) {
            instance = new DaoManager();
            if (appContext == null) {
                appContext = context.getApplicationContext();
            }
            instance.mDaoSession = MApplication.getDaoSession(context);
            instance.userDao = instance.mDaoSession.getUserDao();
            instance.lessonDao = instance.mDaoSession.getLessonDao();
            instance.gradeDao = instance.mDaoSession.getGradeDao();
            instance.courseGradeDao = instance.mDaoSession.getCourseGradeDao();
            instance.tremDao = instance.mDaoSession.getTremDao();
            instance.noticeDao = instance.mDaoSession.getNoticeDao();
            instance.explessonDao = instance.mDaoSession.getExplessonDao();
            instance.examDao=instance.mDaoSession.getExamDao();
        }
        return instance;
    }

    /**
     * ================User====================*
     */


    public List<User> orderAscUser() {
        return userDao.queryBuilder().orderAsc(UserDao.Properties.Id).list();
    }

    /**
     * User插入功能
     *
     * @return
     * @param:album
     */
    public void insertUser(User person) {
        userDao.insert(person);
    }

    public void insertOrReplaceUser(User person) {
        userDao.insertOrReplaceInTx(person);
    }

    public void updateUser(User person) {
        userDao.update(person);
    }

    /**
     * User查找功能
     * //查找条件
     *
     * @param arg0
     * @param conditions
     * @return:albumList
     */
    public List<User> queryUser(WhereCondition arg0,
                                WhereCondition... conditions) {
        QueryBuilder<User> qb = userDao.queryBuilder();
        qb.where(arg0, conditions);
        List<User> personList = qb.list();

        return personList;
    }


    /**
     * User删除所有功能
     *
     * @param
     * @return
     */
    public void deleteAllUser() {
        userDao.deleteAll();
    }

    /**
     * User删除功能
     *
     * @return
     * @param:album
     */
    public void deletePerson(User person) {
        userDao.delete(person);
    }

    /**
     * ================Lesson====================*
     */

    public List<Lesson> orderAscLesson() {
        return lessonDao.queryBuilder().orderAsc(LessonDao.Properties.Id).list();
    }

    /**
     * Lesson插入功能
     *
     * @return
     * @param:album
     */
    public void insertLesson(Lesson person) {
        lessonDao.insert(person);
    }

    public void insertListLesson(List<Lesson> list) {
        if (null == list || list.isEmpty()) {
            return;
        }
        for (Lesson object : list) {
            lessonDao.insert(object);
        }
    }

    public void deleteLessonByid(List<Long> d) {
        lessonDao.deleteByKeyInTx(d);
    }

    public void insertOrReplaceLesson(Lesson person) {
        lessonDao.insertOrReplaceInTx(person);
    }

    public void updateLesson(Lesson person) {
        lessonDao.update(person);
    }

    /**
     * Lesson查找功能
     * //查找条件
     *
     * @param arg0
     * @param conditions
     * @return:albumList
     */
    public List<Lesson> queryLesson(WhereCondition arg0,
                                    WhereCondition... conditions) {
        QueryBuilder<Lesson> qb = lessonDao.queryBuilder();
        qb.where(arg0, conditions);
        List<Lesson> personList = qb.list();
        return personList;
    }

    /**
     * Lesson删除所有功能
     *
     * @param
     * @return
     */
    public void deleteAllLesson() {
        lessonDao.deleteAll();
    }

    /**
     * Lesson删除功能
     *
     * @return
     * @param:album
     */
    public void deleteLesson(Lesson person) {
        lessonDao.delete(person);
    }

    /**
     * ================Trem====================*
     */


    public List<Trem> orderAscTrem() {
        return tremDao.queryBuilder().orderAsc(TremDao.Properties.Id).list();
    }

    /**
     * Trem插入功能
     *
     * @return
     * @param:album
     */
    public void insertTrem(Trem person) {
        tremDao.insert(person);
    }

    public void insertOrReplaceTrem(Trem person) {
        tremDao.insertOrReplaceInTx(person);
    }

    public void updateTrem(Trem person) {
        tremDao.update(person);
    }

    public void insertListTrem(List<Trem> list) {
        if (null == list || list.isEmpty()) {
            return;
        }
        for (Trem object : list) {
            tremDao.insert(object);
        }
    }

    /**
     * Trem查找功能
     * //查找条件
     *
     * @param arg0
     * @param conditions
     * @return:albumList
     */
    public List<Trem> queryTrem(WhereCondition arg0,
                                WhereCondition... conditions) {
        QueryBuilder<Trem> qb = tremDao.queryBuilder();
        qb.where(arg0, conditions);
        List<Trem> personList = qb.list();

        return personList;
    }

    /**
     * Trem删除所有功能
     *
     * @param
     * @return
     */
    public void deleteAllTrem() {
        tremDao.deleteAll();
    }

    /**
     * Trem删除功能
     *
     * @return
     * @param:album
     */
    public void deleteTrem(Trem person) {
        tremDao.delete(person);
    }


    /**
     * ================CourseGrade===================*
     */

    public List<CourseGrade> orderAscCourseGrade() {
        return courseGradeDao.queryBuilder().orderAsc(CourseGradeDao.Properties.Id).list();
    }

    /**
     * CourseGrade插入功能
     *
     * @return
     * @param:album
     */
    public void insertCourseGrade(CourseGrade person) {
        courseGradeDao.insert(person);
    }

    public void insertListCourseGrade(List<CourseGrade> list) {
        if (null == list || list.isEmpty()) {
            return;
        }
        for (CourseGrade object : list) {
            courseGradeDao.insert(object);
        }
    }

    public void insertOrReplaceCourseGrade(CourseGrade person) {
        courseGradeDao.insertOrReplaceInTx(person);
    }

    public void updateCourseGrade(CourseGrade person) {
        courseGradeDao.update(person);
    }

    /**
     * CourseGrade查找功能
     * //查找条件
     *
     * @param arg0
     * @param conditions
     * @return:albumList
     */
    public List<CourseGrade> queryCourseGrade(WhereCondition arg0,
                                              WhereCondition... conditions) {
        QueryBuilder<CourseGrade> qb = courseGradeDao.queryBuilder();
        qb.where(arg0, conditions);
        List<CourseGrade> personList = qb.list();

        return personList;
    }

    /**
     * CourseGrade删除所有功能
     *
     * @param
     * @return
     */
    public void deleteAllCourseGrade() {
        courseGradeDao.deleteAll();
    }

    /**
     * CourseGrade删除功能
     *
     * @return
     * @param:album
     */
    public void deleteCourseGrade(CourseGrade person) {
        courseGradeDao.delete(person);
    }


    /**
     * ================Grade===================*
     */

    public List<Grade> orderAscGrade() {
        return gradeDao.queryBuilder().orderAsc(GradeDao.Properties.Id).list();
    }

    /**
     * Grade插入功能
     *
     * @return
     * @param:album
     */
    public void insertGrade(Grade person) {
        gradeDao.insert(person);
    }

    public void insertOrReplaceGrade(Grade person) {
        gradeDao.insertOrReplaceInTx(person);
    }

    public void updateGrade(Grade person) {
        gradeDao.update(person);
    }

    /**
     * Grade查找功能
     * //查找条件
     *
     * @param arg0
     * @param conditions
     * @return:albumList
     */
    public List<Grade> queryGrade(WhereCondition arg0,
                                  WhereCondition... conditions) {
        QueryBuilder<Grade> qb = gradeDao.queryBuilder();
        qb.where(arg0, conditions);
        List<Grade> personList = qb.list();

        return personList;
    }

    /**
     * Grade删除所有功能
     *
     * @param
     * @return
     */
    public void deleteAllGrade() {
        gradeDao.deleteAll();
    }

    /**
     * Grade删除功能
     *
     * @return
     * @param:album
     */
    public void deleteGrade(Grade person) {
        gradeDao.delete(person);
    }

    /****************************
     * 通知×××××××××××××××××××/
     */

    public List<Notice> orderAscnotice() {
        List<Notice> list = noticeDao.queryBuilder().orderAsc(NoticeDao.Properties.Id).list();
        if (list == null || list.size() == 0)
            return new ArrayList<>();
        else
            return list;
    }

    /**
     * 插入功能
     *
     * @return
     * @param:album
     */
    public void insertNotice(Notice person) {
        noticeDao.insert(person);
    }

    public void deleteAllNotice() {
        noticeDao.deleteAll();
    }


    /**************************
     * ExpLesson×××××××××××××××××××/
     */

    public List<Explesson> orderAscExpLesson() {
        List<Explesson> list = explessonDao.queryBuilder().orderAsc(ExplessonDao.Properties.Id).list();
        if (list == null || list.size() == 0)
            return new ArrayList<>();
        else
            return list;
    }

    /**
     * 插入功能
     *
     * @return
     * @param:album
     */
    public void insertExpLesson(Explesson person) {
        explessonDao.insert(person);
    }

    public void insertListExpLesson(List<Explesson> list) {
        if (null == list || list.isEmpty()) {
            return;
        }
        for (Explesson object : list) {
            explessonDao.insert(object);
        }
    }

    public List<Explesson> queryExpLesson(WhereCondition arg0,
                                    WhereCondition... conditions) {
        QueryBuilder<Explesson> qb = explessonDao.queryBuilder();
        qb.where(arg0, conditions);
        List<Explesson> personList = qb.list();
        return personList;
    }

    public void deleteAllExpLesson() {
        explessonDao.deleteAll();
    }
    /*******************Exam************************/

    public List<Exam> orderAscExam() {
        List<Exam> list = examDao.queryBuilder().orderAsc(ExamDao.Properties.Id).list();
        if (list == null || list.size() == 0)
            return new ArrayList<>();
        else
            return list;
    }
    public void insertExam(Exam person) {
        examDao.insert(person);
    }

    public void insertListExam(List<Exam> list) {
        if (null == list || list.isEmpty()) {
            return;
        }
        for (Exam object : list) {
            examDao.insert(object);
        }
    }

    public void deleteAllExam() {
        examDao.deleteAll();
    }

}

