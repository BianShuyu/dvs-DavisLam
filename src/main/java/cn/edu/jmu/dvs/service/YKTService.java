package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.entity.Task;
import cn.edu.jmu.dvs.mapper.CourseMapper;
import cn.edu.jmu.dvs.mapper.StudentInfoMapper;
import cn.edu.jmu.dvs.mapper.YKTMapper;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class YKTService {

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    StudentInfoMapper studentInfoMapper;

    @Autowired
    YKTMapper yktMapper;

    public String getTcourseId(JSONArray jsonData, String courseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        String tcourseId = null;
        for (int i = 1; i < data.size(); i++) {
            String row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(0);
            if (studentInfoMapper.getStudentName(studentNum) != null) {
                tcourseId = courseMapper.getTcourseIdByCourseIdAndStudentNum(courseId, studentNum);
                if (tcourseId == null) {
                    courseMapper.setTeachCourseByCourseIdAndStudentNum(courseId, studentNum);
                    tcourseId = courseMapper.getTcourseIdByCourseIdAndStudentNum(courseId, studentNum);
                    break;
                } else break;
            }
        }
        return tcourseId;
    }


    public void saveAnnouncementInfo(JSONArray jsonData, String tcourseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        String row = data.get(0);
        List<String> rowData = JSONArray.parseArray(row, String.class);
        String announcementName = rowData.get(0);
        try {
            yktMapper.addAnnouncement(tcourseId, announcementName);
        }catch (Exception ignored){}

        String announcementId = yktMapper.getAnnouncementId(announcementName);

        for (int i = 2; i < data.size(); i++) {
            row = data.get(i);
            rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(0);
            String status = rowData.get(2);
            if (studentInfoMapper.getStudentName(studentNum) != null) {
                try {
                    yktMapper.addAnnouncementStatus(studentNum, announcementId, status);
                } catch (Exception ignored) { }
            }
        }
    }


    public void saveCondition(JSONArray jsonData, String tcourseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        String row = data.get(0);
        List<String> rowData = JSONArray.parseArray(row, String.class);
        String conditionName = rowData.get(0);
        try {
            yktMapper.addCondition(tcourseId, conditionName);
        } catch (Exception ignored) { }
        String conditionId = yktMapper.getConditionId(conditionName);

        for (int i = 4; i < data.size(); i++) {
            row = data.get(i);
            rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(0);
            int isPresent = 1;
            if (rowData.get(2).equals("未上课")) isPresent = 0;
            if (studentInfoMapper.getStudentName(studentNum) != null) {
                try {
                    yktMapper.addConditionStatus(studentNum, conditionId, isPresent);
                }catch (Exception ignored) { }
            }
        }
    }


    public void savePushInfo(JSONArray jsonData, String tcourseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        String row = data.get(0);
        List<String> rowData = JSONArray.parseArray(row, String.class);
        String pushName = rowData.get(0);
        try {
            yktMapper.addPush(tcourseId, pushName);
        }catch (Exception ignored) { }
        String pushId = yktMapper.getPushId(pushName);

        //观看信息部分
        for (int i = 3; i < data.size(); i++) {
            row = data.get(i);
            rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(0);
            int readPages = Integer.parseInt(rowData.get(2));
            String strTotalDuration = rowData.get(4);
            long totalDuration=0;
            if(readPages!=0){
                //h时m分s秒
                String pattern = "(.*)(时)(.*)(分)(.*)(秒)";
                Matcher m = Pattern.compile(pattern).matcher(strTotalDuration);
                m.find();
                int hour=Integer.parseInt(m.group(1));
                int min=Integer.parseInt(m.group(3));
                int sec=Integer.parseInt(m.group(5));
                totalDuration=hour*60*60+min*60+sec;
            }


            if (studentInfoMapper.getStudentName(studentNum) != null) {
                try {
                    yktMapper.addPushReadingInfo(studentNum, pushId, readPages, totalDuration);
                }catch (Exception ignored) { }
            }
        }

        //题目信息部分
        row = data.get(2);
        rowData = JSONArray.parseArray(row, String.class);

        List<String> questionNameList = new ArrayList<>();
        List<String> questionAnswerList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            questionNameList.add(null);
            questionAnswerList.add(null);
        }
        //先提交题目
        for (int i = 7; i < rowData.size(); i++) {
            String question = rowData.get(i);
            String questionName=null;
            String questionAnswer=null;
            String questionScore=null;
            if (!question.contains("主观题") && !question.contains("投票题")) {//仅考虑选择题
                //题号1 空格 答案3 空格 分值5 分  第1题 AD 3.0分
                String pattern = "(.*)( )(.*)( )(.*)分";
                Matcher m = Pattern.compile(pattern).matcher(question);
                m.find();
                questionName=m.group(1);
                questionAnswer=m.group(3);
                questionScore=m.group(5);
                try {
                    yktMapper.addPushQuestion(pushId,questionName,questionScore);
                }catch (Exception ignored) { }
            }
            questionNameList.add(questionName);
            questionAnswerList.add(questionAnswer);
        }
        for (int i = 3; i < data.size(); i++) {
            row = data.get(i);
            rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(0);
            if (studentInfoMapper.getStudentName(studentNum) != null) {
                for (int j = 7; j < questionNameList.size(); j++) {
                    if (questionNameList.get(j) != null) {
                        String questionId = yktMapper.getPushQuestionId(pushId,questionNameList.get(j));
                        String strAnswerCondition = rowData.get(j);
                        int answerCondition=0;
                        if(strAnswerCondition.equals(questionAnswerList.get(j))){
                            answerCondition=1;
                        }
                        try {
                            yktMapper.addPushAnswer(studentNum,questionId,answerCondition);
                        }catch (Exception ignored) { }
                    }
                }
            }
        }
    }


    public Map<String, Double> get(int courseId, int gradeId, String type) {

        Map<String, BiFunction<Integer, Integer, List<Task>>> functionMap = new HashMap<>();


        functionMap.put("readingRatio", yktMapper::getReadingRatio);
        functionMap.put("presentRatio", yktMapper::getPresentRatio);
        functionMap.put("studentPresentRatio", yktMapper::getStudentPresentRatio);
        functionMap.put("score", yktMapper::getScore);
        functionMap.put("pushStudentCorrectRatio", yktMapper::getPushStudentCorrectRatio);
        functionMap.put("pushCorrectRatio", yktMapper::getPushCorrectRatio);
        functionMap.put("pushReadingRatio", yktMapper::getPushReadingRatio);
        functionMap.put("pushDuration", yktMapper::getPushDuration);
        //todo
        List<Task> tasks = functionMap.get(type).apply(courseId, gradeId);
        Map<String, Double> res = new HashMap<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            res.put(task.getName(), task.getVal());
        }
        return res;
    }

    public Map<String, Double> getRightRatioByStudent(int courseId, int studentId) {
        List<Task> tasks = yktMapper.getRightRatioByStudent(courseId, studentId);
        Map<String, Double> res = new HashMap<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            res.put(task.getName(), task.getVal());
        }
        return res;
    }

    public List<String> getUnreadAnnouncementByStudent(int courseId, int studentId) {
        return yktMapper.getUnreadAnnouncementByStudent(courseId, studentId);
    }


    public List<String> getMissingClassByStudent(int courseId, int studentId) {
        return yktMapper.getMissingClassByStudent(courseId, studentId);
    }

    public double getScoreByCourseAndStudent(int courseId, int studentId) {
        return yktMapper.getScoreByCourseAndStudent(courseId, studentId) * 100.0 / yktMapper.getFullScoreByStudent(courseId, studentId);
    }

    public List<Task> getScoresByCourseAndGrade(int courseId, int gradeId) {
        List<Task> res = yktMapper.getScoresByCourseAndGrade(courseId, gradeId);
        int fullScore = yktMapper.getFullScoreByCourseAndGrade(courseId, gradeId);

        for (int i = 0; i < res.size(); i++) {
            Task task = res.get(i);
            task.setVal(task.getVal() * 100.0 / fullScore);
        }
        return res;
    }


}
