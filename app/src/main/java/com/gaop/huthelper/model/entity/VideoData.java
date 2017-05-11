package com.gaop.huthelper.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 视频专栏数据
 * Created by 高沛 on 2017/4/9.
 */

public class VideoData{

    /**
     * msg : ok
     * 480P : http://vedio.wxz.name/
     * 1080P : http://172.16.51.116/videos/
     */

    private String msg;
    @SerializedName("480P")
    private String _$480P;
    @SerializedName("1080P")
    private String _$1080P;
    private List<LinksBean> links;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String get_$480P() {
        return _$480P;
    }

    public void set_$480P(String _$480P) {
        this._$480P = _$480P;
    }

    public String get_$1080P() {
        return _$1080P;
    }

    public void set_$1080P(String _$1080P) {
        this._$1080P = _$1080P;
    }

    public List<LinksBean> getLinks() {
        return links;
    }

    public void setLinks(List<LinksBean> links) {
        this.links = links;
    }


}
