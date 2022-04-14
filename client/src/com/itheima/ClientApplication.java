package com.itheima;

import com.itheima.controller.StudentController;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

//系统启动类
public class ClientApplication {
    private static Scanner sc = new Scanner(System.in);

    private static Properties prop = new Properties();

    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.println("1.登录");
            System.out.println("2.注册");
            System.out.println("3.退出");

            String option = sc.next();

            switch (option) {
                case "1":
                    login();
                    start();
                    break;
                case "2":
                    signup();
                    break;
                case "3":
                    System.exit(0);
                    break;
                default:
                    System.out.println("您的输入有误");
                    break;
            }
        }





    }

    private static void signup() throws IOException {
        FileInputStream fis = new FileInputStream("client/user.properties");
        prop.load(fis);
        fis.close();

        String username;
        while (true) {
            System.out.println("请输入用户名:");

            username = sc.next();

            if (prop.containsKey(username)) {
                //用户名重复
                System.out.println("用户名已存在");
            }else {
                break;
            }
        }

        System.out.println("请输入密码:");
        String password = sc.next();

        prop.setProperty(username,password);
        FileOutputStream fos = new FileOutputStream("client/user.properties");
        prop.store(fos,username+"="+password);
        fos.close();

        System.out.println("注册成功");




    }

    private static void start() {
        while (true) {
            //1. 展示系统一级功能菜单
            System.out.println("--------------欢迎来到黑马信息管理系统------------");
            System.out.println("1. 学生管理");
            System.out.println("2. 老师管理");
            System.out.println("3. 退出");

            //2. 拿到用户的选项,进行系统功能路由

            String option = sc.next();

            //小括号中本质上就是要一个值,值的类型支持: byte,short,int,char. jdk1.5开始支持枚举!jdk1.7开始支持String
            switch (option) {
                case "1":
                    StudentController controller = new StudentController();
                    controller.start();
                    break;
                case "2":
                    System.out.println("进行老师管理系统....");
                    break;
                case "3":
                    System.out.println("感谢您的使用,再见!");
                    //结束当前虚拟机: 传入一个整数即可! 传0 表示告诉JVM安心的走.
                    return;
                default:
                    System.out.println("输入有误,请重试!");
                    break;
            }
        }
    }

    private static void login() throws IOException {
        Scanner sc = new Scanner(System.in);
        for (int i = 1; i <= 3; i++) {
            System.out.println("请输入用户名");
            String inputUsername = sc.next();
            System.out.println("请输入密码");
            String inputPassword = sc.next();

            FileInputStream fis = new FileInputStream("client/user.properties");

            prop.load(fis);

            fis.close();

            Set<String> keys = prop.stringPropertyNames();

            for (String key : keys) {
                if (key.equals(inputUsername) && prop.getProperty(key).equals(inputPassword)) {
                    System.out.println("登录成功,当前时间为:" + new SimpleDateFormat().format(new Date()));
                    return;
                }
            }
            if (i == 3) {
                System.out.println("今日已达上限,请明日再试!");
                System.exit(0);
            }else {
                System.out.println("登录失败还剩" + (3 - i) + "次机会!");
            }

        }


    }
}
