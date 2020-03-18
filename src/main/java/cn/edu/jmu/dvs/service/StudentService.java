package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.entity.Student;
import cn.edu.jmu.dvs.mapper.StudentInfoMapper;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentInfoMapper studentInfoMapper;

    public void saveStudentData(JSONArray jsonData) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        for (int i = 1; i < data.size(); i++) {
            String row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String grade = rowData.get(0);
            String 班级 = rowData.get(1);
            String studentNum = rowData.get(2);
            String name = rowData.get(3);
            if (studentInfoMapper.getGradeName(grade) == null) {
                studentInfoMapper.addGrade(grade);
            }
            if (studentInfoMapper.get班级Name(grade, 班级) == null) {
                studentInfoMapper.add班级(grade, 班级);
            }
            if (studentInfoMapper.getStudentName(studentNum) == null) {
                studentInfoMapper.addStudentInfo(班级, studentNum, name);
            }
        }
    }

    public Student getByID(int id) {
        return studentInfoMapper.getByID(id);
    }

    public List<Student> getByNameLike(String name) {
        return studentInfoMapper.getByNameLike("%" + name + "%");
    }

    public List<Student> getByNumberLike(String number) {
        return studentInfoMapper.getByNumberLike("%" + number + "%");
    }

}
