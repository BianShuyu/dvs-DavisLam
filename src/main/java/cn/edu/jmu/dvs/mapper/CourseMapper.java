package cn.edu.jmu.dvs.mapper;

<<<<<<< HEAD
import cn.edu.jmu.dvs.entity.Course;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface CourseMapper {

    @Select("select * from tb_course")
    List<Course> getCourseList();

    @Select("select count(*) from tb_course where name=#{name}")
    int countByName(String name);

    @Insert("insert into tb_course values(null, #{name})")
    void save(String name);

    @Select("select count(*) from tb_course where id=#{id}")
    int countByID(int id);

    @Delete("delete from tb_course where id=#{id}")
    void delete(int id);
=======
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper @Repository
public interface CourseMapper {

    @Insert("insert into tb_course values(null, #{name})")
    int addCourse(@Param("name") String courseName);


>>>>>>> 40fd4eeb1af641eb04e379f0336aaed175a6bdaf
}
