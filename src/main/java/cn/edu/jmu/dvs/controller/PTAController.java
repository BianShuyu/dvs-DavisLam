package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.service.LoginService;
import cn.edu.jmu.dvs.service.PTAService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PTAController {

    @Autowired
    PTAService ptaService;

    @Autowired
    LoginService loginService;

    //todo 命名
    @PostMapping("/ptaTest")
    @ResponseBody
    public String ptaTest(@RequestBody String raw, ModelMap map) {

        JSONObject returnMap = new JSONObject();
        returnMap.put("tokenValid", false);
        //JSONObject returnData = new JSONObject();
        JSONObject rawJsonObject = JSONObject.parseObject(raw);
        if (loginService.verify(rawJsonObject.get("token").toString())) {
            returnMap.put("tokenValid", true);

            //验证完毕 干正事
            String sheet=rawJsonObject.get("Sheet0").toString();
            String courseId=rawJsonObject.get("courseId").toString();
            JSONArray data = JSONArray.parseArray(sheet);
            ptaService.savePTAData(data,courseId);
            returnMap.put("success",true);
        }
        //returnMap.put("data", returnData);
        return returnMap.toJSONString();
    }

}
