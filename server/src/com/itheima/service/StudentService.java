package com.itheima.service;

import com.itheima.dao.BaseStudentDao;
import com.itheima.domain.Option;
import com.itheima.domain.Student;
import com.itheima.factory.StudentDaoFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class StudentService implements Runnable{

    private BaseStudentDao dao = StudentDaoFactory.getStudentDao();

    private static final Logger LOGGER= LoggerFactory.getLogger(StudentService.class);

    private Socket socket ;
    public StudentService(Socket socket) {
        this.socket=socket;
    }


    //业务员类中的添加学生功能
    private void addStudent(String stu) throws IOException {
        LOGGER.info("接受到客户端添加学生请求,学生为:"+stu);
        String[] split = stu.split(",");

        Student student = new Student(split[0], split[1], split[2], split[3]);

        boolean res = dao.addStudent(student);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        LOGGER.info("添加结果为:"+res);

        bw.write(res+"");
        bw.newLine();
        bw.flush();

        bw.close();


    }

    //业务员类中的查看学生功能
    private void findAllStudents() throws IOException {

        LOGGER.info("接收到客户端查看请求");

        ArrayList<Student> allStudents = dao.findAllStudents();

        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

        LOGGER.info("集合中学生个数为:"+allStudents.size());

        oos.writeObject(allStudents);
        oos.flush();

        oos.close();

    }

    //业务员中的删除学生功能
    private void deleteStudent(String delId) throws IOException {
        LOGGER.info("接收到删除请求,id为:"+delId);
        //指挥库管根据学号删除学生
        boolean res = dao.deleteStudent(delId);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        LOGGER.info("删除结果为:"+res);
        bw.write(res+"");
        bw.newLine();
        bw.flush();

        bw.close();
    }

    //业务员中的修改学生功能
    private void updateStudent(String stu) {
        LOGGER.info("接收到修改学生请求,学生:"+stu);

        //指挥库管根据学号修改对应学生信息
        dao.updateStudent(stu);
    }

    //业务员中的校验学号是否存在功能
    private void isExists(String id) throws IOException {
        //指挥dao根据学号查询对应学生在容器中的索引

        LOGGER.info("接收到客户端IdisExist请求");

        boolean flag = false;
        int index = dao.getIndex(id);

        if (index != -1){
            flag =true;
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        LOGGER.info("接收到客户端IdisExist请求"+id+" "+flag);
        bw.write(flag+"");
        bw.newLine();
        bw.flush();

        bw.close();

    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String s = br.readLine();

            String[] split = s.split("&");
            String optionStr = split[0];
            String msg = split[1];

            Option option = Option.valueOf(optionStr);

            switch (option){
                case STUDENT_ADD:
                    addStudent(msg);
                    break;
                case STUDENT_FIND_ALL:
                    findAllStudents();
                    break;
                case STUDENT_DELETE:
                    deleteStudent(msg);
                    break;
                case STUDENT_UPDATE:
                    updateStudent(msg);
                    break;
                case STUDENT_CHECK_ID:
                    isExists(msg);
                    break;

            }

            br.close();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }
}
