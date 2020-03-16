package cn.edu.jmu.dvs.mapper;


import cn.edu.jmu.dvs.entity.Grade;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface GradeMapper {

    @Select("select * from tb_grade")
    List<Grade> getAll();
}
