package com.gaop.huthelper.model.entity;

import java.util.List;

/**
 * 更新信息
 * Created by 高沛 on 2016/9/1.
 */
public class UpdateMsg {
    /**
     * versionNum : 1
     * url :
     * data : test
     */

    private int versionNum;
    private String url;
    private String data;
    /**
     * api_base_address : {"library":"218.75.197.121:8889","test_plan":"218.75.197.122:84"}
     * home_image_address : http://218.75.197.121:8888/res/app/images/phone.jpg
     * banner_image_addresses : []
     */

    private ApiBaseAddressBean api_base_address;
    private String home_image_address;
    private List<?> banner_image_addresses;

    public int getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(int versionNum) {
        this.versionNum = versionNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ApiBaseAddressBean getApi_base_address() {
        return api_base_address;
    }

    public void setApi_base_address(ApiBaseAddressBean api_base_address) {
        this.api_base_address = api_base_address;
    }

    public String getHome_image_address() {
        return home_image_address;
    }

    public void setHome_image_address(String home_image_address) {
        this.home_image_address = home_image_address;
    }

    public List<?> getBanner_image_addresses() {
        return banner_image_addresses;
    }

    public void setBanner_image_addresses(List<?> banner_image_addresses) {
        this.banner_image_addresses = banner_image_addresses;
    }


    public static class ApiBaseAddressBean {
        /**
         * library : 218.75.197.121:8889
         * test_plan : 218.75.197.122:84
         */

        private String library;
        private String test_plan;

        public String getLibrary() {
            return library;
        }

        public void setLibrary(String library) {
            this.library = library;
        }

        public String getTest_plan() {
            return test_plan;
        }

        public void setTest_plan(String test_plan) {
            this.test_plan = test_plan;
        }
    }
}
