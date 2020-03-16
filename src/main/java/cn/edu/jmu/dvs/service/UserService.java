package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.PageData;
import cn.edu.jmu.dvs.entity.User;
import cn.edu.jmu.dvs.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
=======
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
>>>>>>> 40fd4eeb1af641eb04e379f0336aaed175a6bdaf

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

<<<<<<< HEAD
    public boolean login(String username, String password) {
        return userMapper.selectUser(username, password) != null;
=======
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

    //todo 验证登录状态，true验证成功，false可能是过期可能是token错误，本方法在任何对数据进行增删改查的service前都应该调用
    public boolean verify(String token){
        String s=userMapper.getUserLoginStatus(token);
        if(s!=null){
            return Long.parseLong(s) >= System.currentTimeMillis();
        }else {
            return false;
        }
>>>>>>> 40fd4eeb1af641eb04e379f0336aaed175a6bdaf
    }

    public List<User> getUserList() {
        return userMapper.getAllUsers();
    }
}
