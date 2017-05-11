package com.gaop;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class Generator {


    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(13, "com.gaop.huthelperdao");
        addUser(schema);
        addLesson(schema);
        addGrade(schema);
        addNotice(schema);
        addExpLesson(schema);
        addExam(schema);
        addMenu(schema);
        addClassRanking(schema);
        new DaoGenerator().generateAll(schema, "C:/Users/gaop1/Android/HutHelper1/app/src/main/java-gen");
//


    }


    private static void addExam(Schema schema) {
        Entity exam = schema.addEntity("Exam");
        exam.addIdProperty();
        exam.addStringProperty("CourseName");
        exam.addStringProperty("Starttime");
        exam.addStringProperty("EndTime");
        exam.addStringProperty("Week_Num");
        exam.addStringProperty("isset");
        exam.addStringProperty("RoomName");
        exam.addBooleanProperty("isCx");
    }

    private static void addClassRanking(Schema schema) {
        Entity ranking = schema.addEntity("Ranking");
        ranking.addIdProperty();
        ranking.addStringProperty("XN");
        ranking.addStringProperty("XQ");
        ranking.addStringProperty("ZJD");
        ranking.addIntProperty("MC");
        ranking.addBooleanProperty("ISXN");
        ranking.addBooleanProperty("ISBJ");
    }

    private static void addMenu(Schema schema) {
        Entity entity = schema.addEntity("Menu");
        entity.implementsSerializable();
        entity.addIdProperty();
        entity.addIntProperty("index");
        entity.addStringProperty("path");
        entity.addStringProperty("title");
        entity.addIntProperty("msg");
        entity.addIntProperty("picId");
        entity.addBooleanProperty("isMain");
    }

    private static void addNotice(Schema schema) {

        Entity notice = schema.addEntity("Notice");
        notice.implementsSerializable();
        notice.addIdProperty();
        notice.addStringProperty("type");
        notice.addStringProperty("time");
        notice.addStringProperty("content");
        notice.addStringProperty("title");
    }

    private static void addExpLesson(Schema schema) {
        Entity explesson = schema.addEntity("Explesson");
        explesson.addIdProperty();
        explesson.addStringProperty("teacher");
        explesson.addStringProperty("lesson");
        explesson.addStringProperty("obj");
        explesson.addStringProperty("locate");
        explesson.addStringProperty("weeks_no");
        explesson.addStringProperty("week");
        explesson.addStringProperty("lesson_no");
        explesson.addStringProperty("period");
        explesson.addStringProperty("real_time");
    }

    private static void addUser(Schema schema) {
        Entity user = schema.addEntity("User");
        user.implementsSerializable();
        user.addIdProperty();
        user.addStringProperty("user_id");
        user.addStringProperty("rember_code");
        user.addStringProperty("studentKH");
        user.addStringProperty("TrueName");
        user.addStringProperty("username");
        user.addStringProperty("dep_name");
        user.addStringProperty("class_name");
        user.addStringProperty("address");
        user.addStringProperty("active");
        user.addStringProperty("last_login");
        user.addStringProperty("login_cnt");
        user.addStringProperty("sex");
        user.addStringProperty("head_pic");
        user.addStringProperty("head_pic_thumb");
    }

    private static void addLesson(Schema schema) {
        Entity lesson = schema.addEntity("Lesson");
        lesson.implementsSerializable();
        lesson.addIdProperty();
        lesson.addStringProperty("xh");
        lesson.addStringProperty("dsz");
        lesson.addStringProperty("name");
        lesson.addStringProperty("teacher");
        lesson.addStringProperty("room");
        lesson.addIntProperty("xqj");
        lesson.addIntProperty("djj");
        lesson.addIntProperty("qsz");
        lesson.addIntProperty("jsz");
        lesson.addStringProperty("index");
        lesson.addBooleanProperty("addbyuser");
    }

    private static void addGrade(Schema schema) {

        Entity trim = schema.addEntity("Trem");
        trim.addIdProperty();
        trim.addStringProperty("XN");
        trim.addStringProperty("XQ");
        trim.addStringProperty("content");

        Entity coursegrade = schema.addEntity("CourseGrade");
        coursegrade.addIdProperty();
        coursegrade.addStringProperty("XN");
        coursegrade.addStringProperty("XQ");
        coursegrade.addStringProperty("XKKH");
        coursegrade.addStringProperty("KCMC");
        coursegrade.addStringProperty("XF");
        coursegrade.addStringProperty("CJ");
        coursegrade.addStringProperty("ZSCJ");
        coursegrade.addStringProperty("BKCJ");
        coursegrade.addStringProperty("JD");
        coursegrade.addStringProperty("CXBJ");
        coursegrade.addStringProperty("KCXZ");

        Entity grade = schema.addEntity("Grade");
        grade.addIdProperty();
        grade.addFloatProperty("allsf");
        grade.addFloatProperty("getsf");
        grade.addFloatProperty("cxsf");
        grade.addFloatProperty("nopassxf");
        grade.addFloatProperty("avgScore");
        grade.addFloatProperty("avgJd");
        grade.addIntProperty("noPassNum");
        grade.addIntProperty("allNum");


    }
}
