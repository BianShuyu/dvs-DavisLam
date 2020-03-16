package cn.edu.jmu.dvs.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper @Repository
public interface FinalExamMapper {

    @Insert("insert into tb_final_exam values(" +
            "(select id from tb_student where school_num=#{id})," +
            "(select id from tb_course where name=#{course})," +
            "#{score}" +
            ")")
    void saveFinalExamScore(@Param("id") String id, @Param("course") String course, @Param("score") int score);
}
