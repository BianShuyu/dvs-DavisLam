package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.service.FinalExamService;
import cn.edu.jmu.dvs.service.LoginService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/finalExam")
public class FinalExamController {

    @Autowired
    FinalExamService finalExamService;

    @Autowired
    LoginService loginService;

    @PostMapping("/upload")
    @ResponseBody
    public String finalExamTest(@RequestBody String raw) {

        JSONObject returnMap = new JSONObject();
        returnMap.put("tokenValid", false);
        JSONObject rawJsonObject = JSONObject.parseObject(raw);
        if (loginService.verify(rawJsonObject.get("token").toString())) {
            returnMap.put("tokenValid", true);

            //验证完毕 干正事
            String courseId = rawJsonObject.get("courseId").toString();
            JSONObject data = JSONObject.parseObject(rawJsonObject.get("data").toString());
            for (String s : data.keySet()) {
                String sheet = data.get(s).toString();
                JSONArray sheetArray = JSONArray.parseArray(sheet);
                System.out.println(sheetArray);
                finalExamService.saveFinalExamData(sheetArray, courseId);
                returnMap.put("success", true);
                break;
            }
        }
        //returnMap.put("data", returnData);
        System.out.println("\n\n\n返回：");
        System.out.println(returnMap.toJSONString());
        return returnMap.toJSONString();
    }


    @PostMapping("/summary")
    @ResponseBody
    public String summary(@RequestBody String raw) {

        JSONObject returnMap = new JSONObject();
        returnMap.put("tokenValid", false);
        JSONObject rawJsonObject = JSONObject.parseObject(raw);
        if (loginService.verify(rawJsonObject.get("token").toString())) {
            returnMap.put("tokenValid", true);

            int courseId = Integer.parseInt(rawJsonObject.get("courseId").toString());
            int gradeId = Integer.parseInt(rawJsonObject.get("gradeId").toString());
            returnMap.put("data", finalExamService.getSegmentByClass(courseId, gradeId));

        }
        return returnMap.toJSONString();
    }

}
