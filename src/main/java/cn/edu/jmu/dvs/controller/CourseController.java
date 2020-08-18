package cn.edu.jmu.dvs.controller;


import cn.edu.jmu.dvs.PageData;
import cn.edu.jmu.dvs.entity.Course;
import cn.edu.jmu.dvs.service.CourseService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/list")
    public String getStudentList() {
        return "/course/list";
    }


    @PostMapping("list")
    @ResponseBody
    public PageData<Course> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return new PageData<>(courseService.getAll(), page, limit);
    }

    @GetMapping("add")
    public String add() {
        return "/course/add";
    }

    @PostMapping("add")
    @ResponseBody
    public Map<String, Object> add(HttpServletRequest request) {
        //String username = request.getParameter("username");
        String name = request.getParameter("courseName");
        Map<String, Object> res = new HashMap<>();
        if (StringUtils.isEmpty(name)) {
            res.put("success", false);
            res.put("message", "课程名不能为空");
        } else if (courseService.count(name) > 0) {
            res.put("success", false);
            res.put("message", "课程名已存在");
        } else {
            if (courseService.save(name)) {
                res.put("success", true);
            } else {
                res.put("success", false);
                res.put("message", "保存出错");
            }
        }
        return res;
    }


    @PostMapping("delete")
    @ResponseBody
    public Map<String, Object> delete(@RequestParam(value = "id") int id) {
        Map<String, Object> res = new HashMap<>();

        if (courseService.count(id) == 0) {
            res.put("success", false);
            res.put("message", "课程不存在");
        } else {
            if (courseService.delete(id)) {
                res.put("success", true);
            } else {
                res.put("success", false);
                res.put("message", "保存出错");
            }
        }
        return res;
    }

    @PostMapping("deleteSome")
    @ResponseBody
    public Map<String, Object> deleteSome(@RequestBody List<Course> courses) {
        Map<String, Object> res = new HashMap<>();
        int count = 0;
        for (Course course : courses) {
            if ((boolean) delete(course.getId()).get("success")) {
                count++;
            }
        }
        res.put("count", count);
        return res;
    }
}
