package com.gaop.huthelper.model.entity;

import java.util.List;

/**
 * Created by 高沛 on 2017/4/21.
 */

public class CareerTalkItem {
    /**
     * id : 433432
     * company : 青岛阿香餐饮管理服务有限公司
     * title : 青岛阿香餐饮管理服务有限公司
     * holdtime : 2017-03-12 14:00:00
     * univ_id : 89
     * universityName : 湖南商学院
     * address : 实验楼一楼
     * logoUrl : https://cdn6.haitou.cc/university/89.png
     * apply_url :
     * applyUrl :
     * is_cancel : 0
     * is_official : 0
     * web : 湖南商学院就业信息网
     * totalClicks : 248
     * content : <pp>
     * isSaved : false
     * positions : [{"id":268798,"name":"见习助理","resume":"maoweina@axiang2000.com","info_id":433432,"company_id":8835,"create_time":"2017-03-03 20:46:55","update_time":"2017-03-03 20:46:55","sort":0,"is_deleted":0}]
     * xjhs : [{"id":448054,"title":"青岛阿香餐饮管理服务有限公司","company":"青岛阿香餐饮管理服务有限公司","univ_id":84,"holdtime":"2017-03-01 14:00:00","address":"服务大楼107","universityName":"湘潭大学","universityShortName":"湘大","logoUrl":"https://cdn6.haitou.cc/university/84.png"},{"id":433432,"title":"青岛阿香餐饮管理服务有限公司","company":"青岛阿香餐饮管理服务有限公司","univ_id":89,"holdtime":"2017-03-12 14:00:00","address":"实验楼一楼","universityName":"湖南商学院","universityShortName":"商学院","logoUrl":"https://cdn6.haitou.cc/university/89.png"},{"id":449144,"title":"青岛阿香餐饮管理服务有限公司","company":"青岛阿香餐饮管理服务有限公司","univ_id":213,"holdtime":"2017-03-16 10:00:00","address":"J-1402(信息技术中心楼)","universityName":"三峡大学","universityShortName":"三大","logoUrl":"https://cdn6.haitou.cc/university/213.png"},{"id":444012,"title":"青岛阿香餐饮管理服务有限公司校园招聘会","company":"青岛阿香餐饮管理服务有限公司","univ_id":167,"holdtime":"2017-03-27 14:00:00","address":"学生宿舍14栋招聘大厅3","universityName":"江西师范大学","universityShortName":"师大","logoUrl":"https://cdn6.haitou.cc/university/167.png"}]
     */

    private int id;
    private String company;
    private String title;
    private String holdtime;
    private int univ_id;
    private String universityName;
    private String address;
    private String logoUrl;
    private String apply_url;
    private String applyUrl;
    private int is_cancel;
    private int is_official;
    private String web;
    private int totalClicks;
    private String content;
    private boolean isSaved;
    private List<PositionsBean> positions;
    private List<XjhsBean> xjhs;

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

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
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

    public String getApply_url() {
        return apply_url;
    }

    public void setApply_url(String apply_url) {
        this.apply_url = apply_url;
    }

    public String getApplyUrl() {
        return applyUrl;
    }

    public void setApplyUrl(String applyUrl) {
        this.applyUrl = applyUrl;
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

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public int getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(int totalClicks) {
        this.totalClicks = totalClicks;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isIsSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public List<PositionsBean> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionsBean> positions) {
        this.positions = positions;
    }

    public List<XjhsBean> getXjhs() {
        return xjhs;
    }

    public void setXjhs(List<XjhsBean> xjhs) {
        this.xjhs = xjhs;
    }

    public static class PositionsBean {
        /**
         * id : 268798
         * name : 见习助理
         * resume : maoweina@axiang2000.com
         * info_id : 433432
         * company_id : 8835
         * create_time : 2017-03-03 20:46:55
         * update_time : 2017-03-03 20:46:55
         * sort : 0
         * is_deleted : 0
         */

        private int id;
        private String name;
        private String resume;
        private int info_id;
        private int company_id;
        private String create_time;
        private String update_time;
        private int sort;
        private int is_deleted;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResume() {
            return resume;
        }

        public void setResume(String resume) {
            this.resume = resume;
        }

        public int getInfo_id() {
            return info_id;
        }

        public void setInfo_id(int info_id) {
            this.info_id = info_id;
        }

        public int getCompany_id() {
            return company_id;
        }

        public void setCompany_id(int company_id) {
            this.company_id = company_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(int is_deleted) {
            this.is_deleted = is_deleted;
        }
    }

    public static class XjhsBean {
        /**
         * id : 448054
         * title : 青岛阿香餐饮管理服务有限公司
         * company : 青岛阿香餐饮管理服务有限公司
         * univ_id : 84
         * holdtime : 2017-03-01 14:00:00
         * address : 服务大楼107
         * universityName : 湘潭大学
         * universityShortName : 湘大
         * logoUrl : https://cdn6.haitou.cc/university/84.png
         */

        private int id;
        private String title;
        private String company;
        private int univ_id;
        private String holdtime;
        private String address;
        private String universityName;
        private String universityShortName;
        private String logoUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getUniv_id() {
            return univ_id;
        }

        public void setUniv_id(int univ_id) {
            this.univ_id = univ_id;
        }

        public String getHoldtime() {
            return holdtime;
        }

        public void setHoldtime(String holdtime) {
            this.holdtime = holdtime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUniversityName() {
            return universityName;
        }

        public void setUniversityName(String universityName) {
            this.universityName = universityName;
        }

        public String getUniversityShortName() {
            return universityShortName;
        }

        public void setUniversityShortName(String universityShortName) {
            this.universityShortName = universityShortName;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }
    }
}
