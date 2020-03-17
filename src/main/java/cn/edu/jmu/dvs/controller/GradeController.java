package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.PageData;
import cn.edu.jmu.dvs.entity.Grade;
import cn.edu.jmu.dvs.mapper.CourseMapper;
import cn.edu.jmu.dvs.mapper.GradeMapper;
import cn.edu.jmu.dvs.service.GradeService;
import cn.edu.jmu.dvs.service.LoginService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("grade")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    GradeMapper gradeMapper;

    @Autowired
    LoginService loginService;

    @GetMapping("list")
    public String list() {
        return "/grade/list";
    }

    @GetMapping("add")
    public String add() {
        return "/grade/add";
    }


    @PostMapping("list")
    @ResponseBody
    public PageData<Grade> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return new PageData<>(gradeService.getAll(), page, limit);
    }

    @GetMapping("info")
    public String info() {
        return "/grade/info";
    }

    //todo 命名
    @PostMapping("getCourseAndClass")
    @ResponseBody
    public String getCourseAndClass(@RequestBody String raw) {
        JSONObject returnMap = new JSONObject();
        returnMap.put("tokenValid", false);
        JSONObject returnData = new JSONObject();
        JSONObject rawJsonObject = JSONObject.parseObject(raw);
        if (loginService.verify(rawJsonObject.get("token").toString())) {
            returnMap.put("tokenValid", true);
            JSONObject data = JSONObject.parseObject(rawJsonObject.get("data").toString());
            int gradeId = Integer.parseInt(data.get("gradeId").toString());
            JSONArray courses = JSONArray.parseArray(JSON.toJSONString(courseMapper.getCourseList(gradeId)));
            returnData.put("courses", courses);
            JSONArray classes = JSONArray.parseArray(JSON.toJSONString(gradeMapper.get班级(gradeId)));
            returnData.put("classes", classes);
        }

        returnMap.put("data", returnData);

        return returnMap.toJSONString();

    }
}
