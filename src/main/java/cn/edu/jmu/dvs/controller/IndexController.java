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

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    GradeMapper gradeMapper;

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

    //todo 命名
    @PostMapping("")
    @ResponseBody
    public String a(@RequestBody String raw){
        JSONObject returnMap=new JSONObject();
        returnMap.put("tokenValid",false);
        JSONObject returnData=new JSONObject();
        JSONObject rawJsonObject=JSONObject.parseObject(raw);
        if(loginService.verify(rawJsonObject.get("token").toString())){
            returnMap.put("tokenValid",true);
            JSONObject data=JSONObject.parseObject(rawJsonObject.get("data").toString());
            int gradeId=Integer.parseInt(data.get("gradeId").toString());
            JSONArray courses=JSONArray.parseArray(JSON.toJSONString(courseMapper.getCourseList(gradeId)));
            returnData.put("courses",courses);
            JSONArray classes=JSONArray.parseArray(JSON.toJSONString(gradeMapper.get班级(gradeId)));
            returnData.put("classes",classes);
        }

        returnMap.put("data",returnData);

        return returnMap.toJSONString();

    }

}
