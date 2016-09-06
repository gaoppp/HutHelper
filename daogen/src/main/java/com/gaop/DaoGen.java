package com.gaop;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DaoGen {
    public static void main(String arg[])throws Exception{
        //创建一个用于添加实体的模式对象
        Schema schema = new Schema(1,"com.gaop.hut");
        new DaoGenerator().generateAll(schema,"app/src/main/java-gen");
    }



}
