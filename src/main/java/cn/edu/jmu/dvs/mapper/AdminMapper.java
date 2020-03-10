package cn.edu.jmu.dvs.mapper;

import cn.edu.jmu.dvs.entity.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper {
    Admin Sel(int id);
}