package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.service.LoginService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/verify")
    @ResponseBody
    public HashMap<String, Object> login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HashMap<String, Object> map = new HashMap<>();
        String token = loginService.login(username,password);
        if (token!=null) {
            map.put("success", true);
            map.put("token", token);
            map.put("url", "index");
        } else {
            map.put("success", false);
            map.put("message", "failure");
        }
        return map;
    }

    @PostMapping("/test")
    @ResponseBody
    public String test(@RequestBody String m) throws FileNotFoundException {
        PrintWriter pw=new PrintWriter(new FileOutputStream(new File("testjson.txt")));
        pw.write(m);
        pw.close();
        JSONObject jsonObject=JSONObject.parseObject(m);
        System.out.println(jsonObject);
        JSONArray jsonArray=JSONArray.parseArray(jsonObject.getString("网络18"));
        String a=jsonArray.get(0).toString();
        System.out.println(a);
        return "index";
    }
}
