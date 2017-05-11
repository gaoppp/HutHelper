package com.gaop.huthelper.view.ui;

import com.gaop.huthelperdao.Menu;
import com.gaop.huthelperdao.Notice;
import com.gaop.huthelperdao.User;

import java.util.List;

/**
 * Created by gaop1 on 2017/4/17.
 */

public interface MainView {

    void showMenu(List<Menu> menuList);

    void showUserData(User user);

    void showNotice(Notice notice);

    void showNextClassAndTime(String nextClass,String time);

}
