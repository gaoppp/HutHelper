package com.gaop.huthelper.Model;

import java.util.List;

/**
 * Created by gaop1 on 2016/8/2.
 */
public class DepInfo {
    /**
     * depNo : 95
     * depName : 冶金工程学院
     * classes
     * */

    private String depNo;
    private String depName;

    /**
     * classNo : 123959101
     * className : 金属1201
     */

    private List<ClassesBean> classes;

    public String getDepNo() {
        return depNo;
    }

    public void setDepNo(String depNo) {
        this.depNo = depNo;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public List<ClassesBean> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassesBean> classes) {
        this.classes = classes;
    }

    public static class ClassesBean {
        private String classNo;
        private String className;

        public String getClassNo() {
            return classNo;
        }

        public void setClassNo(String classNo) {
            this.classNo = classNo;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }
}
