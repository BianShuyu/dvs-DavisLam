package cn.edu.jmu.dvs.mapper;

import cn.edu.jmu.dvs.entity.Course;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface CourseMapper {

    @Select("select * from tb_course")
    List<Course> getAll();

    @Select("select count(*) from tb_course where name=#{name}")
    int countByName(String name);

    @Insert("insert into tb_course values(null, #{name})")
    void save(String name);

    @Select("select count(*) from tb_course where id=#{id}")
    int countByID(int id);

    @Delete("delete from tb_course where id=#{id}")
    void delete(int id);

    @Select("select tb_course.name from tb_course " +
            "where tb_course.course_id in (select course_id from tb_teach_course where grade_id=#{grade_id})")
    List<String> getCourseList(@Param("grade_id") int gradeId);


}