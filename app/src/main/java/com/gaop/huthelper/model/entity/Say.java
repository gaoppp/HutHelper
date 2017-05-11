package com.gaop.huthelper.model.entity;

import java.util.List;

/**
 * 说说实体类
 * Created by 高沛 on 16-9-9.
 */
public class Say {


    /**
     * id : 156
     * user_id : 32200
     * content : 每天都来看看
     * pics : ["/uploads/moments/201609/1474612085.56558907_thumb.jpg","/uploads/moments/201609/1474612096.65983857_thumb.jpg","/uploads/moments/201609/1474612102.924590804_thumb.jpg"]
     * created_on : 2016-09-23
     * ipaddr : 218.75.197.121
     * is_top : 0
     * likes : 0
     * view_cnt : 33
     * username : 一粒含笑半步颠
     * dep_name : 电气与信息工程学院
     * comments : [{"id":"36","moment_id":"156","comment":"啊嘞，有评论？","user_id":"32200","created_on":"2016-09-23 14:29:03","username":"一粒含笑半步颠"},{"id":"37","moment_id":"156","comment":"又是你, 小子","user_id":"13147","created_on":"2016-09-23 14:40:12","username":"Tony"}]
     */

    private String id;
    private String user_id;
    private String content;
    private String created_on;
    private String ipaddr;
    private String is_top;
    private int likes;
    private String view_cnt;
    private String username;
    private String dep_name;
    private String head_pic;
    private String head_pic_thumb;
    private List<String> pics;
    private boolean islike;

    public void setHead_pic(String head_pic) {
        this.head_pic = head_pic;
    }

    public void setHead_pic_thumb(String head_pic_thumb) {
        this.head_pic_thumb = head_pic_thumb;
    }

    public String getHead_pic() {
        return head_pic;
    }

    public String getHead_pic_thumb() {
        return head_pic_thumb;
    }

    public boolean islike() {
        return islike;
    }

    public void setIslike(boolean islike) {
        this.islike = islike;
    }

    /**
     * id : 36
     * moment_id : 156
     * comment : 啊嘞，有评论？
     * user_id : 32200
     * created_on : 2016-09-23 14:29:03
     * username : 一粒含笑半步颠
     */

    private List<CommentsBean> comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getIs_top() {
        return is_top;
    }

    public void setIs_top(String is_top) {
        this.is_top = is_top;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getView_cnt() {
        return view_cnt;
    }

    public void setView_cnt(String view_cnt) {
        this.view_cnt = view_cnt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        private String id;
        private String moment_id;
        private String comment;
        private String user_id;
        private String created_on;
        private String username;
        public CommentsBean(String user_id,String username,String comment){
            this.user_id=user_id;
            this.username=username;
            this.comment=comment;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMoment_id() {
            return moment_id;
        }

        public void setMoment_id(String moment_id) {
            this.moment_id = moment_id;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getCreated_on() {
            return created_on;
        }

        public void setCreated_on(String created_on) {
            this.created_on = created_on;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
