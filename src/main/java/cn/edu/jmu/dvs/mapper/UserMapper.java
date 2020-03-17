package cn.edu.jmu.dvs.mapper;

import cn.edu.jmu.dvs.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper @Repository
public interface UserMapper {
    @Select("select * from tb_admin where username = #{username} and password = #{password}")
    User getUserLoginStatus(@Param("username") String username, @Param("password") String password);


    @Select("select * from tb_admin")
    List<User> getAllUsers();

    @Update("update tb_admin set token=#{token}, expire_time=str_to_date(#{expire_time},\'%Y-%m-%d %H:%i:%S\') " +
            "where username=#{username} and password=#{password}")
    int updateUserLoginStatus(@Param("username") String username,
                              @Param("password") String password,
                              @Param("token") String token,
                              @Param("expire_time") String expireTime);
    //update返回where匹配的数目，不是成功update的数目
    //delete返回删除的数目
    //insert返回null

    @Select("select unix_timestamp(expire_time) from tb_admin where token=#{token}")
    String getExpireTime(@Param("token") String token);

    @Select("select timestamp()")
    String getNow();
}