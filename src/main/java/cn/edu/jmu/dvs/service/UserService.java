package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    public boolean login(String username, String password) {
        return userMapper.selectUser(username, password) != null;
    }
}
