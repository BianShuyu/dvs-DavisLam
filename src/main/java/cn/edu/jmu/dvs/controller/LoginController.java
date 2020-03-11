package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.entity.FinalExamData;
import cn.edu.jmu.dvs.service.UserService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/verify")
    @ResponseBody
    public HashMap<String, Object> login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HashMap<String, Object> map = new HashMap<>();
        if (userService.login(username, password)) {
            map.put("success", true);
            map.put("url", "index");
        } else {
            map.put("success", false);
            map.put("message", "failure");
        }
        return map;
    }

    @PostMapping("/test")
    @ResponseBody
    public String test(@RequestBody String m){
        JSONObject jsonObject=JSONObject.parseObject(m);
        System.out.println(jsonObject);
        JSONArray jsonArray=JSONArray.parseArray(jsonObject.getString("网络18"));
        String a=jsonArray.get(0).toString();
        System.out.println(a);
        return "index";
    }
}
