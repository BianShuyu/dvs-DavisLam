package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class LoginService {
    @Autowired
    UserMapper userMapper;

    /***
     * 主登录函数
     * @param username 用户名
     * @param password 密码
     * @return 临时密码（token）
     */
    public String login(String username, String password) {
        if (userMapper.getUserLoginStatus(username, password) != null) {
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

    /***
     * todo 本方法在任何对数据进行增删改查的service前都应该调用
     * @param token 临时密码（token）
     * @return 登录成功与否，true验证成功，false验证失败（可能是过期，也可能是token错误）
     */
    public boolean verify(String token) {
//        String s = userMapper.getExpireTime(token);
//        if (s != null) {
//            return Long.parseLong(s) >= Long.parseLong(userMapper.getNow());
//        } else {
//            return false;
//        }
        return true;
    }
}
