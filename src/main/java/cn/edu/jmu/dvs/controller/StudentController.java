package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.PageData;
import cn.edu.jmu.dvs.entity.User;
import cn.edu.jmu.dvs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String getStudentList() {
        return "/student/list";
    }


//    @PostMapping("list")
//    @ResponseBody
//    public PageData<User> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
//                               @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
//        List<User> all = new ArrayList<>();
//        ArrayList<User> cur = new ArrayList<>();
//        int start = (page - 1) * limit;
//        int end  = Math.min(page * limit, all.size());
//        for (int i = start; i < end; i++) {
//            cur.add(all.get(i));
//        }
//        return new PageData<>(cur, new Long(all.size()));
//
//    }
}
