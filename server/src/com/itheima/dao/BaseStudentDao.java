package com.itheima.dao;

import com.itheima.domain.Student;

import java.util.ArrayList;

//dao层： 数据访问层。 操作规范！
public interface BaseStudentDao {

    //库管类中的添加学生功能
    boolean addStudent(Student stu);

    //库管类中的查看学生功能
    ArrayList<Student> findAllStudents();

    //库管类中的删除学生功能
    boolean deleteStudent(String delId);

    //库管类中的修改学生功能
    void updateStudent(String stu);

    //封装一个方法: 根据学号查找对应学生对象在容器中的索引位置
    int getIndex(String id);

}
