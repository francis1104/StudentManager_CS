package com.itheima.controller;

import com.itheima.domain.Option;
import com.itheima.domain.Student;
import com.itheima.util.AgeUtil;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/*
    客服类核心职责:
        1. 接收用户请求
        2. 指挥业务员干活
        3. 根据业务员返回的结果数据,告诉用户结果信息
 */
public class StudentController {
    private Scanner sc = new Scanner(System.in);

    //客服中的菜单功能入口
    public void start() {
        while (true) {
            //1. 展示学生管理系统二级功能菜单
            System.out.println("--------------欢迎来到学生管理系统------------");
            System.out.println("1. 添加学生");
            System.out.println("2. 删除学生");
            System.out.println("3. 修改学生");
            System.out.println("4. 查找学生");
            System.out.println("5. 退出");

            //2. 拿到用户的选项,进行系统功能路由
            String option = sc.next();

            switch (option) {
                case "1":
                    addStudent();
                    break;
                case "2":
                    deleteStudent();
                    break;
                case "3":
                    updateStudent();
                    break;
                case "4":
                    findAllStudents();
                    break;
                case "5":
                    System.out.println("感谢使用学生管理系统,再见!");
                    return;
                default:
                    System.out.println("输入有误,请重试!");
                    break;
            }
        }
    }

    //客服类中的修改学生功能
    private void updateStudent() {
        System.out.println("请输入要修改的Id");
        String updateId = sc.next();
        boolean res = idIsExist(updateId);

        if (res){
            //id存在,可以修改
            Student stu = inputStudentInfo(updateId);
            try {
                Socket socket = getSocket();
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                bw.write(Option.STUDENT_UPDATE+"&" + stu.toString());
                bw.newLine();
                bw.flush();

                socket.close();

            }catch (Exception e){
                e.printStackTrace();
            }

            System.out.println("修改成功");

        }else{
            //id不存在
            System.out.println("暂无数据或学生不存在~");
        }
    }

    //客服类中的删除学生功能
    private void deleteStudent() {

        System.out.println("请输入要删除的ID");

        String delId = sc.next();

        System.out.println("确认要删除ID为" + delId + "的学生信息吗?  Y(确认)/N(取消)");
        String option = sc.next();

        if ("y".equalsIgnoreCase(option)) {

            try {
                Socket socket = getSocket();

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                bw.write(Option.STUDENT_DELETE+"&" + delId);
                bw.newLine();
                bw.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String res = br.readLine();

                System.out.println("true".equals(res) ? "删除成功" : "删除失败");

                socket.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("已取消~");
        }

    }

    //客服类中的查看学生功能
    private void findAllStudents() {
        try {
            Socket socket = getSocket();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bw.write(Option.STUDENT_FIND_ALL+"&findAll");
            bw.newLine();
            bw.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            ArrayList<Student> students = (ArrayList<Student>) ois.readObject();

            if (students.size() == 0) {
                System.out.println("暂无数据,请添加后再试");
            } else {
                System.out.println("ID  姓名  年龄  生日");

                for (Student student : students) {
                    System.out.println(student.toString());
                }
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //客服类中的添加学生功能
    private void addStudent() {
        //1. 让用户录入要添加的学生信息
        System.out.println("请输入要添加的学生学号:");
        String id = sc.next();

        boolean res1 = idIsExist(id);

        if (res1){
            System.out.println("学号不可用...");
            return;
        }

        //调用inputStudentInfo方法,完成键盘录入学生信息,封装学生对象功能
        Student stu = inputStudentInfo(id);

        //3.请求服务器添加数据
        try {
            Socket socket = getSocket();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bw.write(Option.STUDENT_ADD+"&" + stu.toString());
            bw.newLine();
            bw.flush();

            //4. 根据服务器返回的结果给出相应的提示信息给用户

            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String res = br.readLine();

            System.out.println("true".equals(res) ? "添加成功" : "添加失败");

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //封装一个方法: 专门来做键盘录入学生信息,封装学生对象并返回这个事
    private Student inputStudentInfo(String id) {
        System.out.println("请输入学生姓名:");
        String name = sc.next();

        System.out.println("请输入学生出生日期【例如:1999-11-11】:");
        String birthday = sc.next();

        //调用年龄工具类, 根据生日自动计算年龄!
        String age = AgeUtil.getAge(birthday);

        //2. 封装新的学生对象
        Student stu = new Student(id, name, age, birthday);

        return stu;
    }


    private boolean idIsExist(String id) {
        boolean flag = false;
        try {
            //请求服务器查看id是否存在
            Socket socket = getSocket();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            bw.write(Option.STUDENT_CHECK_ID+"&" + id);
            bw.newLine();
            bw.flush();

            //接受服务器的相应
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String res = br.readLine();

            if ("true".equals(res)) {
                flag = true;
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    //封装一个方法用来获取socket对象
    private Socket getSocket() throws IOException {

        Socket socket = new Socket("127.0.0.1", 10000);
        return socket;

    }
}
