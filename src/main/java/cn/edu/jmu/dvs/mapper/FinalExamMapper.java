package cn.edu.jmu.dvs.mapper;

import cn.edu.jmu.dvs.entity.FinalExamData;
import cn.edu.jmu.dvs.entity.Task;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface FinalExamMapper {
    /*
    @Insert("insert into tb_final_exam values(" +
            "(select id from tb_student where student_num=#{id})," +
            "(select id from tb_course where name=#{course})," +
            "#{score}" +
            ")")
    void addFinalExamScore(@Param("student_num") String studentNum, @Param("course") String course, @Param("score") int score);
    */
    @Insert("insert into tb_final_exam values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "(select id from tb_course where id=#{course_id})," +
            "#{score}" +
            ")")
    void addFinalExamScoreByCourseId(@Param("student_num") String studentNum, @Param("course_id") String courseId, @Param("score") int score);

    @Select("select score from tb_final_exam where " +
            "student_id=(select id from tb_student where student_num=#{student_num}) and " +
            "course_id=#{course_id}")
    String getFinalExamScore(@Param("student_num") String studentNum, @Param("course_id") String courseId);

    @Select("select (select name from tb_course where id = course_id) as name," +
            "score from tb_final_exam where student_id = #{studentId};")
    List<FinalExamData> overviewByStudent(@Param("studentId") int studentId);


    @Select("select tb_class.name, score from tb_class join tb_student " +
            "join tb_final_exam where tb_final_exam.course_id = #{courseId} and " +
            "tb_class.grade_id = #{gradeId} and tb_student.class_id = tb_class.id " +
            "and tb_final_exam.student_id = tb_student.id order by tb_class.name")
    List<FinalExamData> overviewByClass(@Param("courseId") int courseId, @Param("gradeId") int gradeId);

    @Select("select score from tb_final_exam where student_id = #{studentId} and course_id  = #{courseId};")
    int getScoreByCourseAndStudent(@Param("courseId") int courseId, @Param("studentId") int studentId);


    @Select("select student_id as name, score as val from tb_final_exam where student_id " +
            "in (select id from tb_student where class_id in " +
            "(select id from tb_class where grade_id = #{gradeId})) " +
            " and course_id = #{courseId};")
    List<Task> getScoresByCourseAndGrade(@Param("courseId") int courseId, @Param("gradeId") int gradeId);
}
