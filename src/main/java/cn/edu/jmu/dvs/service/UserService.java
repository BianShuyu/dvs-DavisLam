package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public String login(String username, String password) {
        if (userMapper.selectUser(username, password) != null) {
            Random r = new Random();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < 128; i++) {
                stringBuilder.append(r.nextInt(10));
            }
            String token = stringBuilder.toString();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String expireTime = simpleDateFormat.format(new Date(System.currentTimeMillis() + 1000 * 60 * 60));
            int result = userMapper.updateUserLoginStatus(username, password, token, expireTime);
            if (result == 0) return null;
            return token;
        } else {
            return null;
        }
    }
}
