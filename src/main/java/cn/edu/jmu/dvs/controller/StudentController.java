package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.PageData;
import cn.edu.jmu.dvs.service.LoginService;
import cn.edu.jmu.dvs.service.StudentService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

    @Autowired
    StudentService studentService;

    @GetMapping("/list")
    public String getStudentList() {
        return "/student/list";
    }

    //todo 命名
    @PostMapping("/putStudentTest")
    @ResponseBody
    public String ptaTest(@RequestBody String raw) {

        JSONObject returnMap = new JSONObject();
        returnMap.put("tokenValid", false);
        JSONObject rawJsonObject = JSONObject.parseObject(raw);
        if (loginService.verify(rawJsonObject.get("token").toString())) {
            returnMap.put("tokenValid", true);

            //验证完毕 干正事
            JSONObject data=JSONObject.parseObject(rawJsonObject.get("data").toString());
            for(String s:data.keySet()){
                String sheet=data.get(s).toString();
                JSONArray sheetArray = JSONArray.parseArray(sheet);
                System.out.println(sheetArray);
                studentService.saveStudentData(sheetArray);
                returnMap.put("success",true);
                break;
            }
        }
        //returnMap.put("data", returnData);
        System.out.println("\n\n\n返回：");
        System.out.println(returnMap.toJSONString());
        return returnMap.toJSONString();
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
