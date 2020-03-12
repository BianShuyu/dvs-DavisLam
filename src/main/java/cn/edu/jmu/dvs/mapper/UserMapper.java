package cn.edu.jmu.dvs.mapper;

import cn.edu.jmu.dvs.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper @Repository
public interface UserMapper {
    @Select("select * from tb_admin where username = #{username} and password = #{password}")
    User selectUser(@Param("username") String username, @Param("password") String password);

    @Update("update tb_admin set token=#{token}, expire_time=str_to_date(\'#{expire_time}\',\'%Y-%m-%d %H:%I:%S\') " +
            "where username=#{username} and password=#{password}")
    int updateUserLoginStatus(@Param("username") String username,
                               @Param("password") String password,
                               @Param("token") String token,
                               @Param("expire_time") String expireTime);
    //update返回where匹配的数目，不是成功update的数目
    //delete返回删除的数目
    //insert返回null
}