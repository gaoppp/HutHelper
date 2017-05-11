package com.gaop.huthelper.model.entity;

import com.gaop.huthelperdao.Exam;

import java.util.List;

/**
 *  考试数据
 * Created by 高沛 on 16-10-16.
 */

public class ExamData {

    /**
     * code : 100
     * status : success
     * message : Excited
     * res : {"exam":[{"CourseName":"电工学2","Starttime":"2016-12-13 14:00:00","EndTime":"2016-12-13 15:40:00","Week_Num":"16","isset":"0","RoomName":"公共212"},{"CourseName":"经济学原理","Starttime":"2016-12-20 14:00:00","EndTime":"2016-12-20 15:40:00","Week_Num":"17","isset":"0","RoomName":"公共212"}],"cxexam":[]}
     * stuclass : 软件工程1402
     * stuname : 高沛
     * count : 0
     */

    private int code;
    private String status;
    private String message;
    private ResBean res;
    private String stuclass;
    private String stuname;
    private int count;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResBean getRes() {
        return res;
    }

    public void setRes(ResBean res) {
        this.res = res;
    }

    public String getStuclass() {
        return stuclass;
    }

    public void setStuclass(String stuclass) {
        this.stuclass = stuclass;
    }

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static class ResBean {
        /**
         * CourseName : 电工学2
         * Starttime : 2016-12-13 14:00:00
         * EndTime : 2016-12-13 15:40:00
         * Week_Num : 16
         * isset : 0
         * RoomName : 公共212
         */

        private List<Exam> exam;
        private List<Exam> cxexam;

        public List<Exam> getExam() {
            return exam;
        }

        public void setExam(List<Exam> exam) {
            this.exam = exam;
        }

        public List<Exam> getCxexam() {
            return cxexam;
        }

        public void setCxexam(List<Exam> cxexam) {
            this.cxexam = cxexam;
        }

    }
}
