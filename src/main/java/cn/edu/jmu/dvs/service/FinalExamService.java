package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.mapper.CourseMapper;
import cn.edu.jmu.dvs.mapper.FinalExamMapper;
import cn.edu.jmu.dvs.mapper.StudentInfoMapper;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public void saveFinalExamData(String jsonData, String courseId){
        saveFinalExamData(JSONArray.parseArray(jsonData),courseId);
    }

    public void saveFinalExamData(JSONArray jsonData, String courseId){
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        for (int i = 1; i < data.size(); i++) {
            String row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(0);
            String strScore = rowData.get(2);
            int score;
            if(strScore.equals("缺考")) score=-1;
            else score=Integer.parseInt(strScore);
            if (studentInfoMapper.getStudentName(studentNum)!=null && courseMapper.getCourse(courseId)!=null
                    && finalExamMapper.getFinalExamScore(studentNum,courseId)==null) {
                finalExamMapper.addFinalExamScoreByCourseId(studentNum,courseId,score);
            }
        }
    }
}
