package cn.edu.jmu.dvs.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper @Repository
public interface PTAMapper {

    @Insert("insert into tb_pta values(" +
            "(select id from tb_student where school_num=#{school_num})," +
            "(select id from tb_course where name=#{course_id})," +
            "#{question_type}," +
            "#{question_num}," +
            "#{score}" +
            ")")
    void addPTAScore(@Param("school_num") String schoolNum,
                     @Param("course_id") String courseId,
                     @Param("question_type") String questionType,
                     @Param("question_num") String questionNum,
                     @Param("score") int score);
}
