package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.entity.PTAData;
import cn.edu.jmu.dvs.entity.PTASubtotal;
import cn.edu.jmu.dvs.entity.Task;
import cn.edu.jmu.dvs.mapper.PTAMapper;
import cn.edu.jmu.dvs.mapper.StudentInfoMapper;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PTAService {

    @Autowired
    PTAMapper ptaMapper;

    @Autowired
    StudentInfoMapper studentInfoMapper;

    /***
     * 必须提供二维数组形式的，不带元数据的json字符串！！！
     * @param jsonData 二维数组形式的json字符串
     * @param courseId 课程id
     */
    public void savePTAData(String jsonData, String courseId) {
        savePTAData(JSONArray.parseArray(jsonData), courseId);
    }

    public void savePTAData(JSONArray jsonData, String courseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        int colSchoolNum = 0;
        List<String> questionType = new ArrayList<>();
        List<String> questionNum = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            String row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            if (i == 0) {  //获取题目类型列表
                String currentType = null;
                for (int j = 0; j < rowData.size(); j++) {
                    String col = rowData.get(j);
                    if (j == 0) { //第一列不是题目类型名 不算
                        questionType.add(null);
                    } else {
                        if (col != null && !col.equals("")) {
                            //如果出现了题目类型名，还要把上一列题目类型设为空（每类题目最后一列是总分）
                            if (currentType != null) {
                                questionType.set(j - 1, null);
                            }
                            currentType = col;
                        }
                        questionType.add(currentType);
                    }
                }
                System.out.println("题目列表获取完成");
            } else if (i == 1) {  //获取题目标号
                boolean isQuestionNum = false;
                for (int j = 0; j < rowData.size(); j++) {
                    if (j < questionType.size() && questionType.get(j) != null && !questionType.get(j).equals("")) {
                        // 如果对应列的题目类型不是空串 说明该列存在题目编号
                        questionNum.add(rowData.get(j));
                    } else if (j >= questionType.size() && j < rowData.size() - 1) {
                        questionNum.add(rowData.get(j));
                    } else {
                        questionNum.add(null);
                    }
                }
            } else if (i != 2) {
                if (i % 20 == 0) System.out.println("导入" + i + "/" + data.size());
                String studentNum = rowData.get(1);
                String currentQuestionType = null;
                String currentQuestionNum = null;
                int score = 0;
                for (int j = 2; j < rowData.size(); j++) {
                    if (j < questionType.size()) {
                        currentQuestionType = questionType.get(j);
                        currentQuestionNum = questionNum.get(j);
                    } else {
                        currentQuestionNum = questionNum.get(j);
                    }

                    if (currentQuestionNum != null && !rowData.get(j).equals("-")) {
                        String s = rowData.get(j);
                        //System.out.println("解析：" + s);
                        //若干数字.若干数字 任意文字 10.0(10;3ms)
                        String pattern = "(\\d+\\.\\d+)(.*)";
                        Matcher m = Pattern.compile(pattern).matcher(s);
                        m.find();
                        //System.out.println(m.group(1));
                        score = (int) (Double.parseDouble(m.group(1)));
                    } else {
                        score = 0;
                    }
                    //if (studentNum != null && studentInfoMapper.getStudentName(studentNum) != null && currentQuestionNum != null) {
                    try {
                        ptaMapper.addPTAScore(studentNum, courseId, currentQuestionType, currentQuestionNum, score);
                    } catch (DataAccessException e) {
                        //System.out.println("PTA ERROR");
                    }

                    //}

                }
            }
        }
    }

    public Map<String, Object> getSubtotal(int studentId, int courseId) {
        List<PTASubtotal> source = ptaMapper.getSubtotal(courseId);
        int size = source.size();
        List<String> types = new ArrayList<>();
        Map<String, Integer> cur = new HashMap<>(), max = new HashMap<>(), aver = new HashMap<>();

        for (int i = 0; i < source.size(); i++) {
            String questionType = source.get(i).getQuestionType();
            int score = source.get(i).getScore();
            if (source.get(i).getStudentId() == studentId) {
                cur.put(questionType, score);
            }
            if (!aver.containsKey(questionType)) {
                aver.put(questionType, 0);
            }
            aver.put(questionType, aver.get(questionType) + score);
            if (!max.containsKey(questionType)) {
                max.put(questionType, 0);
            }
            max.put(questionType, Math.max(max.get(questionType), score));
        }
        for (Map.Entry<String, Integer> entry : aver.entrySet()) {
            aver.put(entry.getKey(), entry.getValue() / size);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("cur", cur);
        res.put("max", max);
        res.put("aver", aver);
        return res;
    }

    public Map<String, Object> overviewByClass(int courseId, int gradeId) {
        Map<String, Object> res = new HashMap<>();
        List<PTAData> data = ptaMapper.overviewByClass(courseId, gradeId);
        Map<String, Object> classData = new HashMap<>();
        Map<String, Map<String, ArrayList<Integer>>> typeData = new HashMap<>();
        ArrayList<Integer> scores = new ArrayList<>();
        Map<String, Integer> fullScore = new HashMap<>();
        int preId = -1;
        String preName = "";
        int sum = 0;
        for (int i = 0; i < data.size(); i++) {
            PTAData item = data.get(i);
            int studentId = item.getStudentId();
            String className = item.getName();
            String questionNum = item.getQuestionNum();
            int score = item.getScore();

            if (!classData.containsKey(className)) {
                if (!preName.isEmpty()) classData.put(preName, scores);
                preName = className;
                scores = new ArrayList<>();
            }
            if (preId != studentId) {
                if (preId != -1) scores.add(sum);
                preId = studentId;
                sum = 0;
            }
            sum += score;

            if (!fullScore.containsKey(questionNum)) fullScore.put(questionNum, 0);
            fullScore.put(questionNum, Math.max(fullScore.get(questionNum), score));
        }
        scores.add(sum);
        classData.put(preName, scores);


        for (int i = 0; i < data.size(); i++) {
            PTAData item = data.get(i);
            String questionType = item.getQuestionType();
            String questionNum = item.getQuestionNum();
            int score = item.getScore();

            if (!typeData.containsKey(questionType)) typeData.put(questionType, new HashMap<>());
            if (!typeData.get(questionType).containsKey(questionNum))
                typeData.get(questionType).put(questionNum, new ArrayList<>(Arrays.asList(0, 0)));
            int index = score == fullScore.get(questionNum) ? 1 : 0;
            ArrayList<Integer> tmp = typeData.get(questionType).get(questionNum);
            tmp.set(index, tmp.get(index) + 1);
        }
        res.put("classData", classData);
        res.put("typeData", typeData);
        return res;
    }

    public double getScoreByCourseAndStudent(int courseId, int studentId) {
        return ptaMapper.getScoreByCourseAndStudent(courseId, studentId) * 100.0 / ptaMapper.getFullScore(courseId);
    }

    public List<Task> getScoresByCourseAndGrade(int courseId, int gradeId) {
        List<Task> res = ptaMapper.getScoresByCourseAndGrade(courseId, gradeId);
        int fullScore = ptaMapper.getFullScoreByCourseAndGrade(courseId, gradeId);

        for (int i = 0; i < res.size(); i++) {
            Task task = res.get(i);
            task.setVal(task.getVal() * 100.0 / fullScore);
        }
        return res;
    }
}
