package com.gaop.huthelper.model.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 说说点赞缓存
 * Created by 高沛 on 2017/3/12.
 */

public class SayLikeCache {
    private static List<String> likedList = new ArrayList<>();

    public static void setLikeList(List<String> list) {
        likedList = list;
    }

    public static boolean isHave(String id) {
        for (String s : likedList) {
            if (s.equals(id))
                return true;
        }
        return false;
    }

    public static void clear() {
        likedList.clear();
    }

    public static void addLike(String id) {
        likedList.add(id);
    }

    public static void removeLike(String id) {
        Iterator iterList = likedList.iterator();
        while (iterList.hasNext()) {
            if (id.equals(iterList.next())) {
                iterList.remove();
                break;
            }
        }
    }
}
