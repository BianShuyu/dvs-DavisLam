package cn.edu.jmu.dvs.mapper;

import cn.edu.jmu.dvs.entity.PTAData;
import cn.edu.jmu.dvs.entity.PTASubtotal;
import cn.edu.jmu.dvs.entity.Task;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface PTAMapper {

    @Insert("insert into tb_pta values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "(select id from tb_course where id=#{course_id})," +
            "#{question_type}," +
            "#{question_num}," +
            "#{score}" +
            ")")
    void addPTAScore(@Param("student_num") String studentNum,
                     @Param("course_id") String courseId,
                     @Param("question_type") String questionType,
                     @Param("question_num") String questionNum,
                     @Param("score") int score);

    @Select("select student_id as studentId, question_type as questionType, sum(score) as score "
            + "from tb_pta where course_id = #{courseId} "
            + "group by student_id, question_type")
    List<PTASubtotal> getSubtotal(@Param("courseId") int courseId);


    @Select("select student_id as studentId, tb_class.name as name, question_type " +
            "as questionType, question_num as questionNum, score from tb_class join " +
            "tb_student join tb_pta where tb_pta.course_id = #{courseId} and " +
            "tb_class.grade_id = #{gradeId} and tb_student.class_id = tb_class.id and " +
            "tb_pta.student_id = tb_student.id order by name, studentId, questionType, questionNum")
    List<PTAData> overviewByClass(@Param("courseId") int courseId, @Param("gradeId") int gradeId);


    @Select("select sum(score) from tb_pta where student_id = #{studentId} and course_id = #{courseId}")
    int getScoreByCourseAndStudent(@Param("courseId") int courseId, @Param("studentId") int studentId);

    @Select("select sum(score) from (select max(score) as score from tb_pta where course_id = #{courseId} " +
            "group by question_num) as t;")
    int getFullScore(@Param("courseId") int courseId);


    @Select("select sum(score) from (select max(score) as score from tb_pta where course_id = #{courseId} " +
            "and student_id " +
            "in (select id from tb_student where class_id in (select id from tb_class where grade_id = #{gradeId})) " +
            "group by question_num) as t")
    int getFullScoreByCourseAndGrade(@Param("courseId") int courseId, @Param("gradeId") int gradeId);


    @Select("select student_id as name, sum(score) as val from tb_pta where student_id " +
            "in (select id from tb_student where class_id in (select id from tb_class where grade_id = #{gradeId})) " +
            "and course_id = #{courseId} group by student_id;")
    List<Task> getScoresByCourseAndGrade(@Param("courseId") int courseId,
                                         @Param("gradeId") int gradeId);

}
