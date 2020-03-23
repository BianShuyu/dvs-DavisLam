package cn.edu.jmu.dvs.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

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
                             @Param("status") String status)throws DataAccessException;


    @Insert("insert into tb_ykt_ykt_class_condition values(null,#{tcourse_id},#{name})")
    void addCondition(@Param("tcourse_id") String tcourseId, @Param("name") String name)throws DataAccessException;

    @Select("select id from tb_ykt_ykt_class_condition where class_condition=#{condition_name}")
    String getConditionId(@Param("condition_name") String conditionName);

    @Insert("insert into tb_ykt_student_ykt_class values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "#{ykt_class_id},#{is_present})")
    void addConditionStatus(@Param("student_num") String studentNum,
                               @Param("ykt_class_id") String yktClassId,
                               @Param("is_present") int isPresent)throws DataAccessException;


    @Insert("insert into tb_ykt_push values(null,#{tcourse_id},#{name})")
    void addPush(@Param("tcourse_id") String tcourseId, @Param("name") String name)throws DataAccessException;

    @Select("select id from tb_ykt_push where push_name=#{push_name}")
    String getPushId(@Param("push_name") String pushName);

    @Insert("insert into tb_ykt_student_push values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "#{push_id},#{read_pages}," +
            "#{total_duration})")
    void addPushReadingInfo(@Param("student_num") String studentNum,
                            @Param("push_id") String pushId,
                            @Param("read_pages") int readPages,
                            @Param("total_duration") long totalDuration)throws DataAccessException;

    @Insert("insert into tb_ykt_push_question values(null," +
            "#{push_id},#{question_num},#{score}" +
            ")")
    void addPushQuestion(@Param("push_id") String pushId,
                         @Param("question_num") String questionNum,
                         @Param("score") String score)throws DataAccessException;

    @Select("select id from tb_ykt_push_question where push_id=#{push_id} and question_num=#{question_name}")
    String getPushQuestionId(@Param("push_id") String pushId,@Param("question_name") String questionName);

    @Insert("insert into tb_ykt_push_answer values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "#{question_id},#{answer_condition})")
    void addPushAnswer(@Param("student_num") String studentNum,
                            @Param("question_id") String questionId,
                            @Param("answer_condition") int answerCondition)throws DataAccessException;
}
