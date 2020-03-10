package cn.edu.jmu.dvs.mapper;

import cn.edu.jmu.dvs.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserMapper {
    @Select("select * from tb_admin where username = #{username} and password = #{password}")
    User selectUser(@Param("username") String username, @Param("password") String password);
}