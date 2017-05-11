package com.gaop.huthelper.model.entity;

import com.gaop.huthelperdao.Ranking;

import java.util.List;

/**
 * Created by 高沛 on 2017/4/27.
 */

public class RankingApiData {


    /**
     * msg : ok
     * XH : 14408300213
     * data : [[],[],[]]
     * ndata : []
     */

    private String msg;
    private String XH;
    private List<List<Ranking>> data;
    private List<List<Ranking>> ndata;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getXH() {
        return XH;
    }

    public void setXH(String XH) {
        this.XH = XH;
    }

    public List<List<Ranking>> getData() {
        return data;
    }

    public void setData(List<List<Ranking>> data) {
        this.data = data;
    }

    public List<List<Ranking>> getNdata() {
        return ndata;
    }

    public void setNdata(List<List<Ranking>> ndata) {
        this.ndata = ndata;
    }
}
