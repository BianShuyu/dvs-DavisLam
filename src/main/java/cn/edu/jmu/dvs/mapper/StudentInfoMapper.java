package cn.edu.jmu.dvs.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper @Repository
public interface StudentInfoMapper {

    @Insert("insert into tb_grade values(null,#{grade})")
    void saveGrade(@Param("grade") String grade);

//    @Insert("insert into tb_class values( null,#{class}, id2) " +
//            "(select id id2 from tb_grade where #{grade}=name)" +
//            "where #{grade} in (select name from tb_grade)")
    @Insert("insert into tb_class values(null, #{class}, " +
            "(select id from tb_grade where name=#{grade}) )")
    void saveClass(@Param("grade") String grade, @Param("class") String 班级);

    @Insert("insert into tb_student values(null, #{id}, #{name}, " +
            "(select id from tb_class where name=#{class}) )")
    void saveStudentInfo(@Param("grade") String grade, @Param("class") String clazz, @Param("id") String id, @Param("name") String name);
}