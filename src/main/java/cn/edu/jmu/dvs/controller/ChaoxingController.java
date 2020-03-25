package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.service.ChaoxingService;
import cn.edu.jmu.dvs.service.LoginService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chaoxing")
public class ChaoxingController {

    @Autowired
    LoginService loginService;

    @Autowired
    ChaoxingService chaoxingService;

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

            //讨论详情部分（在此处获取tcourseId）
            String sheet=data.get("讨论详情").toString();
            JSONArray sheetArray = JSONArray.parseArray(sheet);
            System.out.println("讨论详情");
            String tcourseId=chaoxingService.saveDiscussInfo(sheetArray,courseId);
            //视频观看详情部分
            sheet=data.get("视频观看详情").toString();
            sheetArray = JSONArray.parseArray(sheet);
            System.out.println("视频观看详情");
            chaoxingService.saveVideoInfo(sheetArray,tcourseId);
            //访问详情部分
            sheet=data.get("访问详情").toString();
            sheetArray = JSONArray.parseArray(sheet);
            System.out.println("访问详情");
            chaoxingService.saveAccessInfo(sheetArray,tcourseId);
            //成绩详情部分
            sheet=data.get("成绩详情").toString();
            sheetArray = JSONArray.parseArray(sheet);
            System.out.println("成绩详情");
            chaoxingService.saveScoreInfo(sheetArray,tcourseId);
            //作业统计部分
            sheet=data.get("作业统计").toString();
            sheetArray = JSONArray.parseArray(sheet);
            System.out.println("作业统计");
            chaoxingService.saveWorkInfo(sheetArray,tcourseId);
            //考试统计部分
            sheet=data.get("考试统计").toString();
            sheetArray = JSONArray.parseArray(sheet);
            System.out.println("考试统计");
            chaoxingService.saveExamInfo(sheetArray,tcourseId);
            //章节测验统计部分
            sheet=data.get("章节测验统计").toString();
            sheetArray = JSONArray.parseArray(sheet);
            System.out.println("章节测验统计");
            chaoxingService.saveChapterQuizScore(sheetArray,tcourseId);


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
            returnMap.put("video", chaoxingService.videoOverview(courseId, gradeId));
            returnMap.put("work", chaoxingService.workOverview(courseId, gradeId));
            returnMap.put("chapter", chaoxingService.chapterOverview(courseId, gradeId));
            returnMap.put("summary", chaoxingService.summary(courseId, gradeId));
            returnMap.put("access", chaoxingService.getAccess(courseId, gradeId));
        }
        return returnMap.toJSONString();
    }


    @PostMapping("/studentInfo")
    @ResponseBody
    public String student(@RequestBody String raw) {
        JSONObject returnMap = new JSONObject();
        returnMap.put("tokenValid", false);
        JSONObject rawJsonObject = JSONObject.parseObject(raw);
        if (loginService.verify(rawJsonObject.get("token").toString())) {
            returnMap.put("tokenValid", true);
            int courseId = Integer.parseInt(rawJsonObject.get("courseId").toString());
            int studentId = Integer.parseInt(rawJsonObject.get("studentId").toString());

            String[] types = {"work", "video", "chapter"};
            for (String type : types) {
                returnMap.put(type, chaoxingService.get(courseId, studentId, type));
            }
            returnMap.put("detail", chaoxingService.getDetailByCourseAndStudent(courseId, studentId));
        }
        return returnMap.toJSONString();
    }


}
