package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.entity.Grade;
import cn.edu.jmu.dvs.mapper.GradeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {
    @Autowired
    GradeMapper gradeMapper;

    public List<Grade> getAll() {
        return gradeMapper.getAll();
    }

    public List<String> get班级(int gradeId){
        return gradeMapper.get班级(gradeId);
    }

    public int getGrade(String className) {
        return gradeMapper.getGrade(className);
    }
}
