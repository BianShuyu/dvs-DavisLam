package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.PageData;
import cn.edu.jmu.dvs.service.LoginService;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private LoginService loginService;

    @GetMapping("/list")
    public String getStudentList() {
        return "/student/list";
    }


    @PostMapping("list")
    @ResponseBody
    public PageData<Map<String, Object>> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        Map<String, Object> mp1 = new HashMap<>(), mp2 = new HashMap<>();
        List<Map<String, Object>> l = new ArrayList<>();
        mp1.put("id", 17);
        mp1.put("studentNum", 1);
        mp1.put("name", "ZWH");
        mp1.put("class", "网络1611");
        mp2.put("id", 31);
        mp2.put("studentNum", 12);
        mp2.put("name", "zwh");
        mp2.put("class", "网络1612");
        l.add(mp1);
        l.add(mp2);
        return new PageData<>(l, page, limit);

    }

    // TODO: 2020/3/17  根据id取出学生信息
    @RequestMapping("info")
    public String info(@RequestParam(value = "id") int id, ModelMap map) {
        Map<String, Object> student = new HashMap<>();
        student.put("name", "ZWH");
        student.put("num", "201621123008");
        student.put("class", "网络1611");
        map.put("student", student);
        return "/student/info";
    }
}
