package cn.edu.jmu.dvs.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper @Repository
public interface StudentInfoMapper {

    @Insert("insert into tb_grade values(null,#{grade})")
    void addGrade(@Param("grade") String grade);

    @Select("select name from tb_grade where name=#{grade_name}")
    String getGradeName(@Param("grade_name") String grade);

//    @Insert("insert into tb_class values( null,#{class}, id2) " +
//            "(select id id2 from tb_grade where #{grade}=name)" +
//            "where #{grade} in (select name from tb_grade)")
    @Insert("insert into tb_class values(null, #{class}, " +
            "(select id from tb_grade where name=#{grade}) )")
    void add班级(@Param("grade") String grade, @Param("class") String 班级);

    @Select("select name from tb_class " +
            "where name=#{class} " +
            "and grade_id=(select id from tb_grade where name=#{grade})")
    String get班级Name(@Param("grade") String grade, @Param("class") String 班级);

    @Insert("insert into tb_student values(null, #{id}, #{name}, " +
            "(select id from tb_class where name=#{class}) )")
    void addStudentInfo(@Param("class") String 班级, @Param("id") String id, @Param("name") String name);

    @Select("select name from tb_student where student_num=#{student_num}")
    String getStudentName(@Param("student_num") String studentNum);
}
