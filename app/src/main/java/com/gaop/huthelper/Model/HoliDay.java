package com.gaop.huthelper.Model;

import java.util.Date;

/**
 * Created by gaop on 16-9-12.
 */
public class HoliDay {
    String name;
    Date time;

    public HoliDay(String name, Date date) {
        this.name = name;
        this.time = date;
    }

    public Date getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
