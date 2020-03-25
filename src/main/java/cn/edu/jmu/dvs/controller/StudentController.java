package cn.edu.jmu.dvs.controller;

import cn.edu.jmu.dvs.PageData;
import cn.edu.jmu.dvs.entity.FinalExamData;
import cn.edu.jmu.dvs.entity.Student;
import cn.edu.jmu.dvs.service.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private LoginService loginService;

    @Autowired
    StudentService studentService;

    @Autowired
    CourseService courseService;

    @Autowired
    GradeService gradeService;

    @Autowired
    FinalExamService finalExamService;

    @Autowired
    PTAService ptaService;

    @Autowired
    ChaoxingService chaoxingService;

    @Autowired
    YKTService yktService;

    @GetMapping("/list")
    public String getStudentList() {
        return "/student/list";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestBody String raw) {

        JSONObject returnMap = new JSONObject();
        returnMap.put("tokenValid", false);
        JSONObject rawJsonObject = JSONObject.parseObject(raw);
        if (loginService.verify(rawJsonObject.get("token").toString())) {
            returnMap.put("tokenValid", true);
            //验证完毕 干正事
            JSONObject data = JSONObject.parseObject(rawJsonObject.get("data").toString());
            for (String s : data.keySet()) {
                String sheet = data.get(s).toString();
                JSONArray sheetArray = JSONArray.parseArray(sheet);
                System.out.println(sheetArray);
                studentService.saveStudentData(sheetArray);
                returnMap.put("success", true);
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
    public PageData<Student> list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                  @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                  @RequestParam(value = "token") String token,
                                  ServletRequest request) {
        if (!loginService.verify(token)) return null;
        Map extra = WebUtils.getParametersStartingWith(request, "s_");
        List l = new ArrayList<Student>();
        if (!extra.isEmpty()) {
            String param = (String) extra.get("key");
            l.addAll(studentService.getByNameLike(param));
            List tmp = studentService.getByNumberLike(param);
            l.removeAll(tmp);
            l.addAll(tmp);
        }
        return new PageData<>(l, page, limit);

    }


    @PostMapping("overview")
    @ResponseBody
    public PageData<FinalExamData> overview(@RequestParam(value = "page", defaultValue = "1") int page,
                                            @RequestParam(value = "limit", defaultValue = "10") int limit,
                                            @RequestParam(value = "token") String token,
                                            ServletRequest request) {
        if (!loginService.verify(token)) return null;
        return new PageData<>(finalExamService.overviewByStudent(Integer.parseInt(request.getParameter("studentId"))), page, limit);
    }

    // TODO: 2020/3/17  根据id取出学生信息
    @RequestMapping("info")
    public String info(@RequestParam(value = "id") int id,
                       @RequestParam(value = "token") String token,
                       ModelMap map) {
        if (!loginService.verify(token)) return null;
        Student student = studentService.getByID(id);

        map.put("student", student);
        int gradeID = gradeService.getGrade(student.getClazz());
        map.put("courses", courseService.getCourseList(gradeID));
        return "/student/info";
    }

    @PostMapping("/lidar")
    @ResponseBody
    public String lidar(@RequestBody String raw) {
        JSONObject returnMap = new JSONObject();
        returnMap.put("tokenValid", false);
        JSONObject rawJsonObject = JSONObject.parseObject(raw);
        if (loginService.verify(rawJsonObject.get("token").toString())) {
            returnMap.put("tokenValid", true);
            int courseId = Integer.parseInt(rawJsonObject.get("courseId").toString());
            int studentId = Integer.parseInt(rawJsonObject.get("studentId").toString());
            returnMap.put("final", finalExamService.getScoreByCourseAndStudent(courseId, studentId));
            returnMap.put("pta", ptaService.getScoreByCourseAndStudent(courseId, studentId));
            returnMap.put("chaoxing", chaoxingService.getScoreByCourseAndStudent(courseId, studentId));
            returnMap.put("yuketang", yktService.getScoreByCourseAndStudent(courseId, studentId));
        }
        return returnMap.toJSONString();
    }
}
