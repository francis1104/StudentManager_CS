package com.itheima.factory;

import com.itheima.dao.BaseStudentDao;
import com.itheima.dao.OtherStudentDao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/*
    学生库管工厂:
        核心职责: 专门对外生产学生库管对象!
 */
public class StudentDaoFactory {

    //当方法的返回值类型是共有的接口/父类型时,方法中要返回的是接口的任意实现类对象/任意子类对象!
    public static BaseStudentDao getStudentDao()  {
        BaseStudentDao dao = null;
        try {
            Properties prop = new Properties();

            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("daoprop.properties");

            prop.load(is);
            is.close();

            Class clazz = Class.forName(prop.getProperty("daoName"));

            dao = (BaseStudentDao) clazz.newInstance();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dao;
    }
}
