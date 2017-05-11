package com.gaop.huthelper.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 视频专栏 每个栏目实体类
 * Created by 高沛 on 2017/4/9.
 */

public  class LinksBean implements Serializable {

    private String name;
    private String img;
    private List<VedioListBean> vedioList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<VedioListBean> getVedioList() {
        return vedioList;
    }

    public void setVedioList(List<VedioListBean> vedioList) {
        this.vedioList = vedioList;
    }

    public static class VedioListBean implements Serializable{

        private String title;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
