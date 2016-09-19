package com.gaop.huthelperdao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "LESSON".
 */
public class Lesson implements java.io.Serializable {

    private Long id;
    private String xh;
    private String dsz;
    private String name;
    private String teacher;
    private String room;
    private Integer xqj;
    private Integer djj;
    private Integer qsz;
    private Integer jsz;
    private String index;

    public Lesson() {
    }

    public Lesson(Long id) {
        this.id = id;
    }

    public Lesson(Long id, String xh, String dsz, String name, String teacher, String room, Integer xqj, Integer djj, Integer qsz, Integer jsz, String index) {
        this.id = id;
        this.xh = xh;
        this.dsz = dsz;
        this.name = name;
        this.teacher = teacher;
        this.room = room;
        this.xqj = xqj;
        this.djj = djj;
        this.qsz = qsz;
        this.jsz = jsz;
        this.index = index;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getDsz() {
        return dsz;
    }

    public void setDsz(String dsz) {
        this.dsz = dsz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getRoom() {
        if(room==null||room=="")
            return "无教室";
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getXqj() {
        return xqj;
    }

    public void setXqj(Integer xqj) {
        this.xqj = xqj;
    }

    public Integer getDjj() {
        return djj;
    }

    public void setDjj(Integer djj) {
        this.djj = djj;
    }

    public Integer getQsz() {
        return qsz==null?0:qsz;
    }

    public void setQsz(Integer qsz) {
        this.qsz = qsz;
    }

    public Integer getJsz() {
        return jsz==null?0:jsz;
    }

    public void setJsz(Integer jsz) {
        this.jsz = jsz;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

}
