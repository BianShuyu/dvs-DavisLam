package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.PageData;
import cn.edu.jmu.dvs.entity.Grade;
import cn.edu.jmu.dvs.entity.Task;
import cn.edu.jmu.dvs.mapper.CourseMapper;
import cn.edu.jmu.dvs.mapper.GradeMapper;
import cn.edu.jmu.dvs.service.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("grade")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Autowired
    CourseService courseService;

    @Autowired
    LoginService loginService;

    @Autowired
    FinalExamService finalExamService;

    @Autowired
    PTAService ptaService;

    @Autowired
    ChaoxingService chaoxingService;

    @Autowired
    YKTService yktService;

    @GetMapping("list")
    public String list() {
        return "/grade/list";
    }

    @GetMapping("add")
    public String add() {
        return "/grade/add";
    }


    @PostMapping("list")
    @ResponseBody
    public PageData<Grade> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return new PageData<>(gradeService.getAll(), page, limit);
    }

    @GetMapping("info")
    public String info(@RequestParam(value = "id") int id, ModelMap map) {
        map.put("courses", courseService.getCourseList(id));
        map.put("gradeId", id);
        return "/grade/info";
    }

    @PostMapping("getCourseAndClass")
    @ResponseBody
    public String getCourseAndClass(@RequestBody String raw) {
        JSONObject returnMap = new JSONObject();
        returnMap.put("tokenValid", false);
        JSONObject returnData = new JSONObject();
        JSONObject rawJsonObject = JSONObject.parseObject(raw);
        if (loginService.verify(rawJsonObject.get("token").toString())) {
            returnMap.put("tokenValid", true);
            JSONObject data = JSONObject.parseObject(rawJsonObject.get("data").toString());
            int gradeId = Integer.parseInt(data.get("gradeId").toString());
            JSONArray courses = JSONArray.parseArray(JSON.toJSONString(courseService.getCourseList(gradeId)));
            returnData.put("courses", courses);
            JSONArray classes = JSONArray.parseArray(JSON.toJSONString(gradeService.get班级(gradeId)));
            returnData.put("classes", classes);
        }

        returnMap.put("data", returnData);

        return returnMap.toJSONString();

    }

    private List<String> getNameList(List<Task> in) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i < in.size(); i++) {
            out.add(in.get(i).getName());
        }
        return out;
    }

    private double findValByName(List<Task> in, String name) {
        double val = -1;
        for (int i = 0; i < in.size(); i++) {
            Task cur = in.get(i);
            if (cur.getName().equals(name)) {
                val = cur.getVal();
                break;
            }
        }
        return val;
    }

    @PostMapping("/match")
    @ResponseBody
    public String match(@RequestBody String raw) {
        JSONObject returnMap = new JSONObject();
        returnMap.put("tokenValid", false);
        JSONObject rawJsonObject = JSONObject.parseObject(raw);
        if (loginService.verify(rawJsonObject.get("token").toString())) {
            returnMap.put("tokenValid", true);
            int courseId = Integer.parseInt(rawJsonObject.get("courseId").toString());
            int gradeId = Integer.parseInt(rawJsonObject.get("gradeId").toString());


            List<Task> finals = finalExamService.getScoresByCourseAndGrade(courseId, gradeId),
                    ptas = ptaService.getScoresByCourseAndGrade(courseId, gradeId),
                    chaoxings = chaoxingService.getScoresByCourseAndGrade(courseId, gradeId),
                    ykts = yktService.getScoresByCourseAndGrade(courseId, gradeId);

            List<String> studentIds = getNameList(finals);
            studentIds.retainAll(getNameList(ptas));
            studentIds.retainAll(getNameList(chaoxings));
            studentIds.retainAll(getNameList(ykts));

            ArrayList<ArrayList<Double>> data = new ArrayList<>();
            for (String studentId : studentIds) {
                ArrayList<Double> cur = new ArrayList<>();
                cur.add(findValByName(finals, studentId));
                cur.add(findValByName(ptas, studentId));
                cur.add(findValByName(chaoxings, studentId));
                cur.add(findValByName(ykts, studentId));
                data.add(cur);
            }
            returnMap.put("data", data);

        }
        return returnMap.toJSONString();
    }
}
