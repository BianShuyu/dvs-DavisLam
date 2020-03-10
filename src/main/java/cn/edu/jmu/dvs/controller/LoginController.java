package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/verify")
    @ResponseBody
    public HashMap<String, Object> login(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HashMap<String, Object> map = new HashMap<>();
        if (userService.login(username, password)) {
            map.put("success", true);
            map.put("url", "index");
        } else {
            map.put("success", false);
            map.put("message", "failure");
        }
        return map;
    }
}
