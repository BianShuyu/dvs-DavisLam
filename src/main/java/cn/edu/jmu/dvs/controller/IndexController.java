package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class IndexController {

    @Autowired
    LoginService loginService;

    @RequestMapping("")
    public String login() {
        return "login";
    }

    @RequestMapping("index")
    public String index(@RequestParam("token") String token) {
        if (loginService.verify(token)) {
            return "index";
        }
        return "login";
    }

    @RequestMapping("main")
    public String main() {
        return "main";
    }

}