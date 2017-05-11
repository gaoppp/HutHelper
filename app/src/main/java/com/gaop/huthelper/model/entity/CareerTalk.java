package com.gaop.huthelper.model.entity;

/**
 * Created by 高沛 on 2017/4/9.
 * 宣讲会
 */

public class CareerTalk {
    /**
     * id : 456306
     * company : 屹丰集团湘潭屹丰模具制造有限公司
     * title : 屹丰集团湘潭屹丰模具制造有限公司
     * holdtime : 2017-04-10 10:00:00
     * univ_id : 218
     * universityShortName : 湖工大
     * address : 电气楼317
     * logoUrl : https://cdn6.haitou.cc/university/218.png
     * is_cancel : 0
     * is_official : 0
     * isExpired : false
     * totalClicks : 39
     * isSaved : false
     * zone : cs
     * company_id : 84813
     * clicks : 23
     */

    private int id;
    private String company;
    private String title;
    private String holdtime;
    private int univ_id;
    private String universityShortName;
    private String address;
    private String logoUrl;
    private int is_cancel;
    private int is_official;
    private boolean isExpired;
    private int totalClicks;
    private boolean isSaved;
    private String zone;
    private int company_id;
    private int clicks;

    public CareerTalk(String company,String universityShortName,String address,String holdtime,int is_official,boolean isSaved){
        this.company=company;
        this.universityShortName=universityShortName;
        this.address=address;
        this.holdtime=holdtime;
        this.is_official=is_official;
        this.isSaved=isSaved;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHoldtime() {
        return holdtime;
    }

    public void setHoldtime(String holdtime) {
        this.holdtime = holdtime;
    }

    public int getUniv_id() {
        return univ_id;
    }

    public void setUniv_id(int univ_id) {
        this.univ_id = univ_id;
    }

    public String getUniversityShortName() {
        return universityShortName;
    }

    public void setUniversityShortName(String universityShortName) {
        this.universityShortName = universityShortName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getIs_cancel() {
        return is_cancel;
    }

    public void setIs_cancel(int is_cancel) {
        this.is_cancel = is_cancel;
    }

    public int getIs_official() {
        return is_official;
    }

    public void setIs_official(int is_official) {
        this.is_official = is_official;
    }

    public boolean isIsExpired() {
        return isExpired;
    }

    public void setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public int getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(int totalClicks) {
        this.totalClicks = totalClicks;
    }

    public boolean isIsSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }
}
