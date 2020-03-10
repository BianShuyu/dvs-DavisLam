package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/get")
    public String GetAdmin() {
        return adminService.Sel(1).toString();
    }

}
