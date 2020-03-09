package cn.edu.jmu.dvs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class LoginController {

    @PostMapping("/login.do")
    public String login(String username, String password){
        if(username.equals("aaa")&&password.equals("1111")){
            return "index";
        }else return "1111";
    }
}
