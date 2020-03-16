package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private LoginService loginService;

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
