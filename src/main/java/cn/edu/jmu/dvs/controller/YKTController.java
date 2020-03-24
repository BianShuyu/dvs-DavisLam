package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.service.LoginService;
import cn.edu.jmu.dvs.service.YKTService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/ykt")
public class YKTController {
    @Autowired
    LoginService loginService;

    @Autowired
    YKTService yktService;

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestBody String raw) {

        JSONObject returnMap = new JSONObject();
        returnMap.put("tokenValid", false);
        JSONObject rawJsonObject = JSONObject.parseObject(raw);
        if (loginService.verify(rawJsonObject.get("token").toString())) {
            returnMap.put("tokenValid", true);

            //验证完毕 干正事
            String courseId=rawJsonObject.get("courseId").toString();

            JSONObject data=JSONObject.parseObject(rawJsonObject.get("data").toString());
            String tcourseId=null;

            for(Map.Entry<String, Object> sheetEntry:data.entrySet()){
                String sheetName=sheetEntry.getKey();
                String sheet=sheetEntry.getValue().toString();
                JSONArray sheetArray=JSONArray.parseArray(sheet);

                System.out.println("解析：" + sheetName);
                if(tcourseId==null){
                    tcourseId=yktService.getTcourseId(sheetArray,courseId);
                }
                if(sheetName.contains("公告")){
                    System.out.println("公告");
                    yktService.saveAnnouncementInfo(sheetArray,tcourseId);
                }
                else if(sheetName.contains("课件推送")){
                    System.out.println("课件推送");
                    yktService.savePushInfo(sheetArray,tcourseId);
                }
                else if(sheetName.contains("课堂情况")){
                    System.out.println("课堂情况");
                    yktService.saveCondition(sheetArray,tcourseId);
                }
            }

            returnMap.put("success",true);
        }
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
            String[] types = {"readingRatio"};
            for (String type : types) {
                returnMap.put(type, yktService.get(courseId, gradeId, type));
            }

        }
        return returnMap.toJSONString();
    }

}
