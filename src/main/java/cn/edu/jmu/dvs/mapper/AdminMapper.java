package cn.edu.jmu.dvs.mapper;

import cn.edu.jmu.dvs.entity.Admin;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper {
    Admin selectUser(@Param("username") String username, @Param("password") String password);
}