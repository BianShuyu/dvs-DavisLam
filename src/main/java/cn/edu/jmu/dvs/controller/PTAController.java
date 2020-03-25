package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.service.LoginService;
import cn.edu.jmu.dvs.service.PTAService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pta")
public class PTAController {

    @Autowired
    PTAService ptaService;

    @Autowired
    LoginService loginService;

    //todo 命名
    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestBody String raw, ModelMap map) {

        JSONObject returnMap = new JSONObject();
        returnMap.put("tokenValid", false);
        //JSONObject returnData = new JSONObject();
        JSONObject rawJsonObject = JSONObject.parseObject(raw);
        if (loginService.verify(rawJsonObject.get("token").toString())) {
            returnMap.put("tokenValid", true);

            //验证完毕 干正事
            String courseId = rawJsonObject.get("courseId").toString();

            JSONObject data = JSONObject.parseObject(rawJsonObject.get("data").toString());
            String sheet = data.get("Sheet0").toString();
            JSONArray sheetArray = JSONArray.parseArray(sheet);
            //System.out.println(sheetArray);
            ptaService.savePTAData(sheetArray, courseId);
            returnMap.put("success", true);
        }
        //returnMap.put("data", returnData);
        System.out.println("\n\n\n返回：");
        System.out.println(returnMap.toJSONString());
        return returnMap.toJSONString();
    }

    @PostMapping("/studentInfo")
    @ResponseBody
    public String subtotal(@RequestBody String raw) {
        JSONObject returnMap = new JSONObject();
        returnMap.put("tokenValid", false);
        JSONObject rawJsonObject = JSONObject.parseObject(raw);
        if (loginService.verify(rawJsonObject.get("token").toString())) {
            returnMap.put("tokenValid", true);

            int studentId = Integer.parseInt(rawJsonObject.get("studentId").toString());
            int courseId = Integer.parseInt(rawJsonObject.get("courseId").toString());
            returnMap.put("data", ptaService.getSubtotal(studentId, courseId));
        }
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
            returnMap.put("data", ptaService.overviewByClass(courseId, gradeId));

        }
        return returnMap.toJSONString();
    }

}
