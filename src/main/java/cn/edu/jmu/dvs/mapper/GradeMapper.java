package cn.edu.jmu.dvs.mapper;


import cn.edu.jmu.dvs.entity.Grade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface GradeMapper {

    @Select("select * from tb_grade")
    List<Grade> getAll();

    @Select("select name from tb_class where grade_id=#{grade_id}")
    List<String> get班级(@Param("grade_id") int gradeId);

    @Select("select grade_id from tb_class where name = #{className}")
    int getGrade(@Param("className") String className);
}
