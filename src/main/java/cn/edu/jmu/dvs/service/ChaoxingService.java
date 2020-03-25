package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.entity.ChaoxingAccess;
import cn.edu.jmu.dvs.entity.ChaoxingDetail;
import cn.edu.jmu.dvs.entity.ChaoxingSummary;
import cn.edu.jmu.dvs.entity.Task;
import cn.edu.jmu.dvs.mapper.ChaoxingMapper;
import cn.edu.jmu.dvs.mapper.CourseMapper;
import cn.edu.jmu.dvs.mapper.StudentInfoMapper;
import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;

@Service
public class ChaoxingService {

    @Autowired
    ChaoxingMapper chaoxingMapper;

    @Autowired
    StudentInfoMapper studentInfoMapper;

    @Autowired
    CourseMapper courseMapper;

    public String saveDiscussInfo(JSONArray jsonData, String courseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        String tcourseId = null;
        for (int i = 1; i < data.size(); i++) {
            String row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(1);
            String comments = rowData.get(11);
            String replies = rowData.get(12);
            String suggestScore = (int) (Double.parseDouble(rowData.get(13))) + "";
            if (tcourseId == null) tcourseId = courseMapper.getTcourseIdByCourseIdAndStudentNum(courseId, studentNum);
            //如果当前授课id不知道就看一下当前学生是否有授课id
            if (studentInfoMapper.getStudentName(studentNum) != null) {//如果是一个合法的学生(排除掉非法学生)
                if (tcourseId != null) {//如果有授课id了就跟着这个来
                    try {
                        chaoxingMapper.addDiscussInfo(studentNum, tcourseId, comments, replies, suggestScore);
                    } catch (Exception ignored) {
                    }
                } else {//如果没有授课id（即当前学生没有授课id，但合法 所以要加入授课id）
                    try {
                        courseMapper.setTeachCourseByCourseIdAndStudentNum(courseId, studentNum);
                    } catch (Exception ignored) {
                    }
                    tcourseId = courseMapper.getTcourseIdByCourseIdAndStudentNum(courseId, studentNum);
                }
            }//不是合法的学生不计入统计
        }
        return tcourseId;
    }

    public void saveAccessInfo(JSONArray jsonData, String tcourseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        for (int i = 1; i < data.size(); i++) {
            String row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String strDate = rowData.get(2);
            int t00t04 = Integer.parseInt(rowData.get(4));
            int t04t08 = Integer.parseInt(rowData.get(5));
            int t08t12 = Integer.parseInt(rowData.get(6));
            int t12t16 = Integer.parseInt(rowData.get(7));
            int t16t20 = Integer.parseInt(rowData.get(8));
            int t20t24 = Integer.parseInt(rowData.get(9));
            try {
                chaoxingMapper.addAccessInfo(tcourseId, strDate, t00t04, t04t08, t08t12, t12t16, t16t20, t20t24);
            } catch (Exception ignored) {
            }
        }
    }

    public void saveVideoInfo(JSONArray jsonData, String tcourseId) {
        int repeat = 2;
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        String row = data.get(0);
        List<String> videoNameList = JSONArray.parseArray(row, String.class);
        for (int i = 6; i < videoNameList.size(); i++) {
            if (chaoxingMapper.getVideoId(videoNameList.get(i)) != null) {
                videoNameList.set(i, videoNameList.get(i) + repeat);
                try {
                    chaoxingMapper.addVideo(tcourseId, videoNameList.get(i));
                } catch (Exception ignored) {
                }
                repeat++;
            } else {
                try {
                    chaoxingMapper.addVideo(tcourseId, videoNameList.get(i));
                } catch (Exception ignored) {
                }
            }
        }
        for (int i = 2; i < data.size(); i++) {
            row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(1);
            if (studentInfoMapper.getStudentName(studentNum) != null) {
                for (int j = 6; j < videoNameList.size(); j++) {
                    String videoId = chaoxingMapper.getVideoId(videoNameList.get(j));
                    String percentage = rowData.get(j).substring(0, rowData.get(j).length() - 1);
                    try {
                        chaoxingMapper.addVideoWatchingInfo(studentNum, videoId, percentage);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    public void saveScoreInfo(JSONArray jsonData, String tcourseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        for (int i = 1; i < data.size(); i++) {
            String row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(4);
            String videoScore = rowData.get(11);
            String videoProgress = rowData.get(12).substring(0, rowData.get(12).length() - 4);
            String quizScore = rowData.get(13);
            String discussScore = rowData.get(15);
            String workScore = rowData.get(16);
            String examScore = rowData.get(17);
            String taskPercentage = rowData.get(18).substring(0, rowData.get(18).length() - 1);
            String score = rowData.get(19);
            String level = rowData.get(20);

            if (studentInfoMapper.getStudentName(studentNum) != null) {
                try {
                    chaoxingMapper.addScoreInfo(studentNum, tcourseId, videoScore, videoProgress, quizScore, discussScore,
                            workScore, examScore, taskPercentage, score, level);
                } catch (Exception ignored) {
                }
            }
        }
    }

    public void saveWorkInfo(JSONArray jsonData, String tcourseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        String row = data.get(0);
        List<String> workNameList = JSONArray.parseArray(row, String.class);
        for (int i = 6; i < workNameList.size(); i++) {
            if (workNameList.get(i) != null) {
                try {
                    chaoxingMapper.addWork(tcourseId, workNameList.get(i));
                } catch (Exception ignored) {
                }
            }
        }
        for (int i = 2; i < data.size(); i++) {
            row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(0);
            if (studentInfoMapper.getStudentName(studentNum) != null) {
                for (int j = 6; j < workNameList.size(); j++) {
                    if (workNameList.get(j) != null) {
                        String workId = chaoxingMapper.getWorkId(workNameList.get(j));
                        String score = rowData.get(j);
                        try {
                            chaoxingMapper.addWorkInfo(studentNum, workId, score);
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        }
    }


    public void saveExamInfo(JSONArray jsonData, String tcourseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        for (int i = 2; i < data.size(); i++) {
            String row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(3);
            String score = (int) Double.parseDouble(rowData.get(11)) + "";

            if (studentInfoMapper.getStudentName(studentNum) != null) {
                try {
                    chaoxingMapper.addExamInfo(studentNum, tcourseId, score);
                } catch (Exception ignored) {
                }
            }
        }
    }


    public void saveChapterQuizScore(JSONArray jsonData, String tcourseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        String row = data.get(0);
        List<String> chapterNameList = JSONArray.parseArray(row, String.class);
        for (int i = 6; i < chapterNameList.size(); i++) {
            if (chapterNameList.get(i) != null) {
                try {
                    chaoxingMapper.addChapterQuiz(tcourseId, chapterNameList.get(i));
                } catch (Exception ignored) {
                }
            }
        }
        for (int i = 2; i < data.size(); i++) {
            row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(0);
            if (studentInfoMapper.getStudentName(studentNum) != null) {
                for (int j = 6; j < chapterNameList.size(); j++) {
                    if (chapterNameList.get(j) != null) {
                        String workId = chaoxingMapper.getChapterQuizId(chapterNameList.get(j));
                        String score = rowData.get(j);
                        try {
                            chaoxingMapper.addChapterQuizScore(studentNum, workId, score);
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        }
    }

    public Map<String, ArrayList<Integer>> videoOverview(int courseId, int gradeId) {
        Map<String, ArrayList<Integer>> res = new HashMap<>();
        List<Task> videos = chaoxingMapper.getVideoListByCourseAndGrade(courseId, gradeId);
        for (int i = 0; i < videos.size(); i++) {
            Task v = videos.get(i);
            String name = v.getName();
            double percentage = v.getVal();
            if (!res.containsKey(name)) res.put(name, new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)));
            ArrayList<Integer> cur = res.get(name);
            int index = Math.min(9, (int) (percentage / 10));
            cur.set(index, cur.get(index) + 1);
        }
        return res;
    }

    private Map<String, ArrayList<Integer>> prepareData(List<Task> list) {
        Map<String, ArrayList<Integer>> res = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            Task item = list.get(i);
            String name = item.getName();
            int score = (int) item.getVal();
            //sum zeros non_zeros
            if (!res.containsKey(name)) res.put(name, new ArrayList<>(Arrays.asList(0, 0, 0)));
            ArrayList<Integer> cur = res.get(name);
            cur.set(0, cur.get(0) + score);
            int index = score == 0 ? 1 : 2;
            cur.set(index, cur.get(index) + 1);
        }
        return res;
    }

    public Map<String, ArrayList<Integer>> workOverview(int courseId, int gradeId) {
        return prepareData(chaoxingMapper.getWorkListByCourseAndGrade(courseId, gradeId));
    }


    public Map<String, ArrayList<Integer>> chapterOverview(int courseId, int gradeId) {
        return prepareData(chaoxingMapper.getChapterListByCourseAndGrade(courseId, gradeId));
    }

    public Map<String, Map<String, Object>> summary(int courseId, int gradeId) {
        Map<String, Map<String, Object>> res = new HashMap<>();
        List<ChaoxingSummary> data = chaoxingMapper.getSummaryByCourseAndGrade(courseId, gradeId);
        for (int i = 0; i < data.size(); i++) {
            ChaoxingSummary item = data.get(i);
            String className = item.getName();
            int examScore = item.getExamScore();
            String level = item.getLevel();

            if (!res.containsKey(className)) {
                res.put(className, new HashMap<>());
                Map<String, Object> classData = res.get(className);
                classData.put("examScore", new ArrayList<Integer>());
                classData.put("level", new ArrayList<String>());
            }
            Map<String, Object> classData = res.get(className);
            ((ArrayList<Integer>) classData.get("examScore")).add(examScore);
            ((ArrayList<String>) classData.get("level")).add(level);
        }
        return res;
    }

    public Map<String, Object> getAccess(int courseId, int gradeId) {
        Map<String, Object> res = new HashMap<>();
        List<ChaoxingAccess> accesses = chaoxingMapper.getAccess(courseId, gradeId);
        ArrayList<String> dates = new ArrayList<>();
        ArrayList<Integer> t0 = new ArrayList<>();
        ArrayList<Integer> t4 = new ArrayList<>();
        ArrayList<Integer> t8 = new ArrayList<>();
        ArrayList<Integer> t12 = new ArrayList<>();
        ArrayList<Integer> t16 = new ArrayList<>();
        ArrayList<Integer> t20 = new ArrayList<>();

        for (int i = 0; i < accesses.size(); i++) {
            ChaoxingAccess access = accesses.get(i);
            dates.add(access.getDate());
            t0.add(access.getT0());
            t4.add(access.getT4());
            t8.add(access.getT8());
            t12.add(access.getT12());
            t16.add(access.getT16());
            t20.add(access.getT20());
        }

        res.put("dates", dates);
        res.put("t0", t0);
        res.put("t4", t4);
        res.put("t8", t8);
        res.put("t12", t12);
        res.put("t16", t16);
        res.put("t20", t20);

        return res;
    }


    public Map<String, Double> get(int courseId, int studentId, String type) {

        Map<String, BiFunction<Integer, Integer, List<Task>>> functionMap = new HashMap<>();


        functionMap.put("work", chaoxingMapper::getWorkListByCourseAndStudent);
        functionMap.put("video", chaoxingMapper::getVideoListByCourseAndStudent);
        functionMap.put("chapter", chaoxingMapper::getChapterListByCourseAndStudent);

        List<Task> tasks = functionMap.get(type).apply(courseId, studentId);
        Map<String, Double> res = new HashMap<>();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            res.put(task.getName(), task.getVal());
        }
        return res;
    }

    public ChaoxingDetail getDetailByCourseAndStudent(int courseId, int studentId) {
        return chaoxingMapper.getDetailByCourseAndStudent(courseId, studentId);
    }

    public double getScoreByCourseAndStudent(int courseId, int studentId) {
        return chaoxingMapper.getScoreByCourseAndStudent(courseId, studentId);
    }

    public List<Task> getScoresByCourseAndGrade(int courseId, int gradeId) {
        return chaoxingMapper.getScoresByCourseAndGrade(courseId, gradeId);
    }


}
