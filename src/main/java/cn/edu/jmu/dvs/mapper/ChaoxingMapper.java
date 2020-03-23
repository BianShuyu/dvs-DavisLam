package cn.edu.jmu.dvs.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Mapper @Repository
public interface ChaoxingMapper {



    @Insert("insert into tb_cx_access values(" +
            "#{tcourse_id}," +
            "str_to_date(#{access_date},\'%Y-%m-%d\')," +
            "#{t0},#{t4},#{t8},#{t12},#{t16},#{t20})")
    void addAccessInfo(@Param("tcourse_id") String tcourse_id, @Param("access_date") String access_date,
                       @Param("t0") int t0, @Param("t4") int t4, @Param("t8") int t8,
                       @Param("t12") int t12, @Param("t16") int t16, @Param("t20") int t20
                       )throws DataAccessException;


    @Insert("insert into tb_cx_discuss values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "#{tcourse_id},#{comments},#{replies},#{suggest_score}" +
            ")")
    void addDiscussInfo(@Param("student_num") String studentNum, @Param("tcourse_id") String tcourseId,
                        @Param("comments") String comments, @Param("replies") String replies,
                        @Param("suggest_score") String suggestScore)throws DataAccessException;


    @Insert("insert into tb_cx_video values(null,#{tcourse_id},#{name})")
    void addVideo(@Param("tcourse_id") String tcourseId,@Param("name") String name)throws DataAccessException;

    @Select("select id from tb_cx_video where name=#{video_name}")
    String getVideoId(@Param("video_name") String videoName);

    @Insert("insert into tb_cx_video_watching values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "#{video_id},#{percentage})")
    void addVideoWatchingInfo(@Param("student_num") String studentNum,
                              @Param("video_id") String videoId,
                              @Param("percentage") String percentage)throws DataAccessException;


    @Insert("insert into tb_cx_score_info values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "#{tcourse_id},#{video_score},#{video_progress},#{quiz_score},#{discuss_score}," +
            "#{work_score},#{exam_score},#{task_percentage},#{score},#{level}" +
            ")")
    void addScoreInfo(@Param("student_num") String studentNum,@Param("tcourse_id") String tcourseId,
                      @Param("video_score") String videoScore,@Param("video_progress") String videoProgress,
                      @Param("quiz_score") String quizScore, @Param("discuss_score") String discussScore,
                      @Param("work_score") String workScore, @Param("exam_score") String examScore,
                      @Param("task_percentage") String taskPercentage, @Param("score") String score,
                      @Param("level") String level)throws DataAccessException;


    @Insert("insert into tb_cx_work values(null,#{tcourse_id},#{name})")
    void addWork(@Param("tcourse_id") String tcourseId,@Param("name") String name)throws DataAccessException;

    @Select("select id from tb_cx_work where name=#{work_name}")
    String getWorkId(@Param("work_name") String workName);

    @Insert("insert into tb_cx_work_finishing values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "#{work_id},#{score})")
    void addWorkInfo(@Param("student_num") String studentNum,
                     @Param("work_id") String workId,
                     @Param("score") String score)throws DataAccessException;


    @Insert("insert into tb_cx_exam values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "#{tcourse_id},#{score})")
    void addExamInfo(@Param("student_num") String studentNum,
                     @Param("tcourse_id") String tcourseId,
                     @Param("score") String score)throws DataAccessException;


    @Insert("insert into tb_cx_chapter_quiz values(null,#{tcourse_id},#{name})")
    void addChapterQuiz(@Param("tcourse_id") String tcourseId,@Param("name") String name)throws DataAccessException;

    @Select("select id from tb_cx_chapter_quiz where name=#{chapter_name}")
    String getChapterQuizId(@Param("chapter_name") String chapterName);

    @Insert("insert into tb_cx_student_chapter values(" +
            "(select id from tb_student where student_num=#{student_num})," +
            "#{chapter_id},#{score})")
    void addChapterQuizScore(@Param("student_num") String studentNum,
                     @Param("chapter_id") String chapterId,
                     @Param("score") String score)throws DataAccessException;
}
