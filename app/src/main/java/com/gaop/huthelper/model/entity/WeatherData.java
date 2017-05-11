package com.gaop.huthelper.model.entity;

/**
 * 天气数据
 * Created by 高沛 on 16-11-21.
 */

public class WeatherData {


    /**
     * reason : 查询成功
     * result : {"data":{"realtime":{"city_code":"101210701","city_name":"温州","date":"2014-10-15","time":"09:00:00","week":3,"moon":"九月廿二","dataUptime":1413337811,"weather":{"temperature":"19","info":"雾"}}}}
     * error_code : 0
     */

    private String reason;
    private ResultBean result;
    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public static class ResultBean {
        /**
         * data : {"realtime":{"city_code":"101210701","city_name":"温州","date":"2014-10-15","time":"09:00:00","week":3,"moon":"九月廿二","dataUptime":1413337811,"weather":{"temperature":"19","info":"雾"}}}
         */

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            private RealtimeBean realtime;

            public RealtimeBean getRealtime() {
                return realtime;
            }

            public void setRealtime(RealtimeBean realtime) {
                this.realtime = realtime;
            }

            public static class RealtimeBean {
                /**
                 * city_code : 101210701
                 * city_name : 温州
                 * date : 2014-10-15
                 * time : 09:00:00
                 * week : 3
                 * moon : 九月廿二
                 * dataUptime : 1413337811
                 * weather : {"temperature":"19","info":"雾"}
                 */

                private String city_code;
                private String city_name;
                private WeatherBean weather;

                public String getCity_code() {
                    return city_code;
                }

                public void setCity_code(String city_code) {
                    this.city_code = city_code;
                }

                public String getCity_name() {
                    return city_name;
                }

                public void setCity_name(String city_name) {
                    this.city_name = city_name;
                }

                public WeatherBean getWeather() {
                    return weather;
                }

                public void setWeather(WeatherBean weather) {
                    this.weather = weather;
                }

                public static class WeatherBean {
                    /**
                     * temperature : 19
                     * info : 雾
                     */

                    private String temperature;
                    private String info;

                    public String getTemperature() {
                        return temperature;
                    }

                    public void setTemperature(String temperature) {
                        this.temperature = temperature;
                    }

                    public String getInfo() {
                        return info;
                    }

                    public void setInfo(String info) {
                        this.info = info;
                    }
                }
            }
        }
    }
}
