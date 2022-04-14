package com.itheima.dao;

import com.itheima.domain.Student;
import com.itheima.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

/*
    OtherStudentDao: 升级为集合容器实现
    开闭原则: 对修改关闭, 对扩展开放!
 */
public class OtherStudentDao implements BaseStudentDao {
    //1. 创建一个数组容器,用来管理系统中的学生对象信息
    private static ArrayList<Student> stus = new ArrayList<Student>();

    private static final Logger LOGGER= LoggerFactory.getLogger(StudentService.class);

    static {

        LOGGER.info("...........系统初始化中loading............");
        reLoad5();
        LOGGER.info("...........系统初始化完成............");
    }

    //库管类中的添加学生功能
    public boolean addStudent(Student stu) {
        stus.add(stu);
        reSave5();
        return true;
    }

    //库管类中的查看学生功能
    public ArrayList<Student> findAllStudents() {

        return stus;
    }

    //库管类中的删除学生功能
    public boolean deleteStudent(String delId) {
        // 1. 根据学号找到对应学生对象在容器中的索引位置
        int index = getIndex(delId);
        // 根据返回的索引决定能否删除成功
        if (index == -1) {
            //学号不存在,则删除失败,返回false
            return false;
        } else {
            //存在,才做删除
            stus.remove(index);
            reSave5();
            return true;
        }
    }

    //库管类中的修改学生功能
    public void updateStudent(String stu) {
        //1. 根据要修改的学号,找对应学生在容器中的索引位置
        String[] split = stu.split(",");

        Student student = new Student(split[0], split[1], split[2], split[3]);

        int index = getIndex(split[0]);

        //2. 将index记录的索引位置使用newStu进行替换,达到修改效果
        stus.set(index, student);
        reSave5();
    }

    //封装一个方法: 根据学号查找对应学生对象在容器中的索引位置
    public int getIndex(String id) {
        //假设要查找的学号不存在!
        int index = -1;
        //遍历查找
        for (int i = 0; i < stus.size(); i++) {
            //拿到每一个索引位置的元素
            Student stu = stus.get(i);
            //判断当前索引位置的元素不为null,并且当前学生对象的学号和我们要查找的学号是否一致
            if (stu != null && stu.getId().equals(id)) {
                //找到了就修改标记
                index = i;
                break;
            }
        }

        //返回index
        return index;
    }

    //存档                -------基于序列化流
    private void reSave5(){
        try {

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("server/stu.txt"));

            oos.writeObject(stus);

            oos.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //读档                -------基于序列化流
    private static void reLoad5(){
        try {

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("server/stu.txt"));

            ArrayList<Student> students = (ArrayList<Student>) ois.readObject();

            stus=students;

            ois.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
