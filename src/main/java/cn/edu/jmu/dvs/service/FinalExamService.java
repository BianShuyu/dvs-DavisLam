package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.entity.FinalExamData;
import cn.edu.jmu.dvs.entity.Task;
import cn.edu.jmu.dvs.mapper.CourseMapper;
import cn.edu.jmu.dvs.mapper.FinalExamMapper;
import cn.edu.jmu.dvs.mapper.StudentInfoMapper;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FinalExamService {

    @Autowired
    FinalExamMapper finalExamMapper;

    @Autowired
    StudentInfoMapper studentInfoMapper;

    @Autowired
    CourseMapper courseMapper;

    /***
     * 必须提供二维数组形式的，不带元数据的json字符串！！！
     * @param jsonData 二维数组形式的json字符串
     * @param courseId 课程id
     */
    public void saveFinalExamData(String jsonData, String courseId) {
        saveFinalExamData(JSONArray.parseArray(jsonData), courseId);
    }

    public void saveFinalExamData(JSONArray jsonData, String courseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        for (int i = 1; i < data.size(); i++) {
            String row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(0);
            String strScore = rowData.get(2);
            int score;
            if (strScore.equals("缺考")) score = -1;
            else score = Integer.parseInt(strScore);
            if (studentInfoMapper.getStudentName(studentNum) != null && courseMapper.getCourse(courseId) != null
                    && finalExamMapper.getFinalExamScore(studentNum, courseId) == null) {
                finalExamMapper.addFinalExamScoreByCourseId(studentNum, courseId, score);
            }
        }
    }

    public List<FinalExamData> overviewByStudent(int studentId) {
        return finalExamMapper.overviewByStudent(studentId);
    }

    public Map<String, Object> getSegmentByClass(int courseId, int gradeId) {
        List<FinalExamData> l = finalExamMapper.overviewByClass(courseId, gradeId);
        String[] segments = {"[0, 60)", "[60, 70)", "[70, 80)", "[80, 90)", "[90, 100]"};
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> segmentData = new HashMap<>();
        ArrayList<ArrayList<Integer>> boxData = new ArrayList<>();
        String preName = "";
        Map<String, Integer> cur = new HashMap<>();
        ArrayList<Integer> cur2 = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            String name = l.get(i).getName();
            int score = l.get(i).getScore();

            if (!name.equals(preName)) {
                if (preName != "") {
                    segmentData.put(preName, cur);
                    boxData.add(cur2);
                }
                preName = name;
                cur = new HashMap<>();
                cur2 = new ArrayList<>();
                for (int j = 0; j < segments.length; j++) {
                    cur.put(segments[j], 0);
                }
            }
            cur2.add(Math.max(0, score));
            if (score < 60) {
                cur.put(segments[0], cur.get(segments[0]) + 1);
            } else {
                int index = Math.min((score - 60) / 10 + 1, 4);
                cur.put(segments[index], cur.get(segments[index]) + 1);
            }
        }
        segmentData.put(preName, cur);
        boxData.add(cur2);
        res.put("segmentData", segmentData);
        res.put("boxData", boxData);
        return res;
    }


    public int getScoreByCourseAndStudent(int courseId, int studentId) {
        return finalExamMapper.getScoreByCourseAndStudent(courseId, studentId);
    }

    public List<Task> getScoresByCourseAndGrade(int courseId, int gradeId) {
        return finalExamMapper.getScoresByCourseAndGrade(courseId, gradeId);
    }


}
