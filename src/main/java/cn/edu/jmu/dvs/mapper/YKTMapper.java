package cn.edu.jmu.dvs.mapper;

import cn.edu.jmu.dvs.entity.Task;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface YKTMapper {

    @Insert("insert into tb_ykt_announcement values(null,#{tcourse_id},#{name})")
    void addAnnouncement(@Param("tcourse_id") String tcourseId, @Param("name") String name) throws DataAccessException;

    @Select("select id from tb_ykt_announcement where announcement_name=#{announcement_name}")
    String getAnnouncementId(@Param("announcement_name") String announcementName);

    @Insert("insert into tb_ykt_student_annoucement values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "#{announcement_id},#{status})")
    void addAnnouncementStatus(@Param("student_num") String studentNum,
                               @Param("announcement_id") String announcementId,
                               @Param("status") String status) throws DataAccessException;


    @Insert("insert into tb_ykt_ykt_class_condition values(null,#{tcourse_id},#{name})")
    void addCondition(@Param("tcourse_id") String tcourseId, @Param("name") String name) throws DataAccessException;

    @Select("select id from tb_ykt_ykt_class_condition where class_condition=#{condition_name}")
    String getConditionId(@Param("condition_name") String conditionName);

    @Insert("insert into tb_ykt_student_ykt_class values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "#{ykt_class_id},#{is_present})")
    void addConditionStatus(@Param("student_num") String studentNum,
                            @Param("ykt_class_id") String yktClassId,
                            @Param("is_present") int isPresent) throws DataAccessException;


    @Insert("insert into tb_ykt_push values(null,#{tcourse_id},#{name})")
    void addPush(@Param("tcourse_id") String tcourseId, @Param("name") String name) throws DataAccessException;

    @Select("select id from tb_ykt_push where push_name=#{push_name}")
    String getPushId(@Param("push_name") String pushName);

    @Insert("insert into tb_ykt_student_push values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "#{push_id},#{read_pages}," +
            "#{total_duration})")
    void addPushReadingInfo(@Param("student_num") String studentNum,
                            @Param("push_id") String pushId,
                            @Param("read_pages") int readPages,
                            @Param("total_duration") long totalDuration) throws DataAccessException;

    @Insert("insert into tb_ykt_push_question values(null," +
            "#{push_id},#{question_num},#{score}" +
            ")")
    void addPushQuestion(@Param("push_id") String pushId,
                         @Param("question_num") String questionNum,
                         @Param("score") String score) throws DataAccessException;

    @Select("select id from tb_ykt_push_question where push_id=#{push_id} and question_num=#{question_name}")
    String getPushQuestionId(@Param("push_id") String pushId, @Param("question_name") String questionName);

    @Insert("insert into tb_ykt_push_answer values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "#{question_id},#{answer_condition})")
    void addPushAnswer(@Param("student_num") String studentNum,
                       @Param("question_id") String questionId,
                       @Param("answer_condition") int answerCondition) throws DataAccessException;


    @Select("select announcement_name as name, " +
            "round(sum(case status when '已读' then 1 else 0 end) / count(announcement_id), 2) * 100 as val " +
            "from tb_ykt_student_annoucement join tb_ykt_announcement " +
            "where tb_ykt_student_annoucement.announcement_id = tb_ykt_announcement.id " +
            "and tcourse_id = (select id from tb_teach_course where course_id = #{courseId} and grade_id = #{gradeId}) " +
            "group by announcement_id;")
    List<Task> getReadingRatio(@Param("courseId") int courseId,
                               @Param("gradeId") int gradeId);


    @Select("select class_condition as name, " +
            "round(sum(is_present) / count(is_present) ,2) * 100 as val " +
            "from tb_ykt_student_ykt_class join tb_ykt_ykt_class_condition " +
            "where tb_ykt_student_ykt_class.ykt_class_id = tb_ykt_ykt_class_condition.id " +
            "and tcourse_id = (select id from tb_teach_course where course_id = #{courseId} and grade_id = #{gradeId}) " +
            "group by ykt_class_id;")
    List<Task> getPresentRatio(@Param("courseId") int courseId,
                               @Param("gradeId") int gradeId);

    //学生到课率
    @Select("select tb_student.name name, " +
            "round(sum(is_present) / count(is_present) ,2) * 100 as val" +
            "from tb_student join tb_ykt_student_ykt_class join tb_ykt_ykt_class_condition" +
            "where tb_student.id=tb_ykt_student_ykt_class.student_id and " +
            "tb_ykt_ykt_class_condition.id=tb_ykt_student_ykt_class.ykt_class_id and " +//内连接完毕
            "tcourse_id=(select id from tb_teach_course where course_id = #{courseId} and grade_id = #{gradeId}) " +
            "group by tb_student.id")
    List<Task> getStudentPresentRatio(@Param("courseId") int courseId,
                                      @Param("gradeId") int gradeId);

    @Select("select tb_class.name, tb_student.name, sum(answer_condition * score) " +
            "from tb_ykt_push_answer join tb_ykt_push_question join tb_student join tb_class " +
            "where push_question_id = tb_ykt_push_question.id and " +
            "tb_ykt_push_answer.student_id = tb_student.id and " +
            "tb_student.class_id = tb_class.id and " +
            "push_id in (select id from tb_ykt_push where tcourse_id = " +
            "   (select id from tb_teach_course where course_id = 2 and grade_id = 5))" +
            "group by tb_student.id")
    List<Task> getScore(@Param("courseId") int courseId,
                        @Param("gradeId") int gradeId);

    //每个学生的得分率
    @Select("select tb_student.name name, " +
            "round(sum(tb_ykt_push_question.score * tb_ykt_push_answer.answer_condition) / sum(tb_ykt_push_question.score) ,2) * 100 as val " +
            "from tb_student join tb_ykt_push join tb_ykt_push_question join tb_ykt_push_answer " +
            "where tb_student.id=tb_ykt_push_answer.student_id and " +
            "tb_ykt_push.id=tb_ykt_push_question.push_id and " +
            "tb_ykt_push_answer.push_question_id=tb_ykt_push_question.id and " +//内连接完毕
            "tcourse_id=(select id from tb_teach_course where course_id = #{courseId} and grade_id = #{gradeId}) " +
            "group by tb_student.id")
    List<Task> getPushStudentCorrectRatio(@Param("courseId") int courseId,
                               @Param("gradeId") int gradeId);
    //每个push的得分率
    @Select("select push_name name, " +
            "round(sum(tb_ykt_push_answer.answer_condition) / count(tb_ykt_push_answer.answer_condition) ,2) * 100 as val " +
            "from tb_ykt_push join tb_ykt_push_question join tb_ykt_push_answer " +
            "where tb_ykt_push.id=tb_ykt_push_question.push_id and " +
            "tb_ykt_push_answer.push_question_id=tb_ykt_push_question.id and " +//内连接完毕
            "tcourse_id = (select id from tb_teach_course where course_id = #{courseId} and grade_id = #{gradeId}) " +
            "group by tb_ykt_push.id")
    List<Task> getPushCorrectRatio(@Param("courseId") int courseId,
                               @Param("gradeId") int gradeId);

    //观看率
    @Select("select push_name name, " +
            "round(sum(read_pages) / count(read_pages)*max(read_pages) ,2) * 100 as val " +
            "from tb_ykt_push join tb_ykt_student_push " +
            "where tb_ykt_push.id=tb_ykt_student_push.push_id and " +//内连接完毕
            "tcourse_id = (select id from tb_teach_course where course_id = #{courseId} and grade_id = #{gradeId}) " +
            "group by tb_ykt_push.id")
    List<Task> getPushReadingRatio(@Param("courseId") int courseId,
                                   @Param("gradeId") int gradeId);

    //观看平均时长
    @Select("select push_name name, " +
            "round(sum(total_duration_sec) / count(total_duration_sec)/60,2) as val " +
            "from tb_ykt_push join tb_ykt_student_push " +
            "where tb_ykt_push.id=tb_ykt_student_push.push_id and " +//内连接完毕
            "tcourse_id = (select id from tb_teach_course where course_id = #{courseId} and grade_id = #{gradeId}) " +
            "group by tb_ykt_push.id")
    List<Task> getPushDurationRatio(@Param("courseId") int courseId,
                                   @Param("gradeId") int gradeId);


    List<Task> getR
}
