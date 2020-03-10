package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.entity.Admin;
import cn.edu.jmu.dvs.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    AdminMapper adminMapper;
    public boolean login(String username, String password) {
        return adminMapper.selectUser(username, password) != null;
    }
}
