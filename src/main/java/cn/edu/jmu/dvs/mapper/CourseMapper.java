package cn.edu.jmu.dvs.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper @Repository
public interface CourseMapper {

    @Insert("insert into tb_course values(null, #{name})")
    int addCourse(@Param("name") String courseName);


}
