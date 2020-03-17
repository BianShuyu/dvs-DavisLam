package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.mapper.CourseMapper;
import cn.edu.jmu.dvs.mapper.GradeMapper;
import cn.edu.jmu.dvs.service.LoginService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    LoginService loginService;

    @RequestMapping("")
    public String login() {
        return "login";
    }

    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("main")
    public String main() {
        return "main";
    }

}
