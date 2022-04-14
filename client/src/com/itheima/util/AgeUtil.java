package com.itheima.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
    年龄工具类: 根据生日自动计算年龄

    工具类的设计规范:
        1. 构造方法私有化
        2. 成员静态化  -- static使用的重灾区!
 */
public class AgeUtil {
    /**
     * Don't let anyone instantiate this class.
     */
    private AgeUtil() { }

    //根据生日计算年龄
    public static String getAge(String birthdayStr){
        try{
            //2. 将生日字符串转为Date对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthday = sdf.parse(birthdayStr);

            //3. 拿当前系统时间和生日时间做一个差值运算
            long year = (new Date().getTime() - birthday.getTime()) / 1000 / 60 / 60 / 24 / 365;
            return year + "";

        }catch (Exception e){
            //如果根据生日算年龄出现了问题,采用系统默认年龄设计: 18岁!
            return "18";
        }
    }


}
