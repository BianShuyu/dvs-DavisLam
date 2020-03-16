package cn.edu.jmu.dvs.service;


import cn.edu.jmu.dvs.entity.Course;
import cn.edu.jmu.dvs.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    CourseMapper courseMapper;

    public List<Course> getCourseList() {
        return courseMapper.getCourseList();
    }

    public int count(String name) {
        return courseMapper.countByName(name);
    }

    public int count(int id) {
        return courseMapper.countByID(id);
    }
    public boolean save(String name) {
        courseMapper.save(name);
        return count(name) > 0;
    }

    public boolean delete(int id) {
        courseMapper.delete(id);
        return count(id) == 0;
    }

    public List<String> getCourseList(int gradeId){
        return courseMapper.getCourseList(gradeId);
    }
}
