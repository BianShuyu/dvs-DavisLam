package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.PageData;
import cn.edu.jmu.dvs.entity.Grade;
import cn.edu.jmu.dvs.service.GradeService;
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


}
