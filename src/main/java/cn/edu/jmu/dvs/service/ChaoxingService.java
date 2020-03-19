package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.mapper.ChaoxingMapper;
import cn.edu.jmu.dvs.mapper.CourseMapper;
import cn.edu.jmu.dvs.mapper.StudentInfoMapper;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
                    chaoxingMapper.addDiscussInfo(studentNum, tcourseId, comments, replies, suggestScore);
                } else {//如果没有授课id（即当前学生没有授课id，但合法 所以要加入授课id）
                    courseMapper.setTeachCourseByCourseIdAndStudentNum(courseId, studentNum);
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
            chaoxingMapper.addAccessInfo(tcourseId, strDate, t00t04, t04t08, t08t12, t12t16, t16t20, t20t24);
        }
    }

    public void saveVideoInfo(JSONArray jsonData, String tcourseId) {
        int repeat=2;
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        String row = data.get(0);
        List<String> videoNameList = JSONArray.parseArray(row, String.class);
        for (int i = 6; i < videoNameList.size(); i++) {
            if(chaoxingMapper.getVideoId(videoNameList.get(i))!=null){
                videoNameList.set(i,videoNameList.get(i)+repeat);
                chaoxingMapper.addVideo(tcourseId,videoNameList.get(i));
                repeat++;
            }else {
                chaoxingMapper.addVideo(tcourseId, videoNameList.get(i));
            }
        }
        for (int i = 2; i < data.size(); i++) {
            row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(1);
            if(studentInfoMapper.getStudentName(studentNum)!=null) {
                for (int j = 6; j < videoNameList.size(); j++) {
                    String videoId = chaoxingMapper.getVideoId(videoNameList.get(j));
                    String percentage = rowData.get(j).substring(0, rowData.get(j).length() - 1);
                    chaoxingMapper.addVideoWatchingInfo(studentNum, videoId, percentage);
                }
            }
        }
    }

    public void saveScoreInfo(JSONArray jsonData, String tcourseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        for (int i = 1; i < data.size(); i++) {
            String row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String studentNum=rowData.get(4);
            String videoScore = rowData.get(11);
            String videoProgress = rowData.get(12).substring(0,rowData.get(12).length()-4);
            String quizScore = rowData.get(13);
            String discussScore = rowData.get(15);
            String workScore = rowData.get(16);
            String examScore = rowData.get(17);
            String taskPercentage = rowData.get(18).substring(0,rowData.get(18).length()-1);
            String score = rowData.get(19);
            String level = rowData.get(20);

            if(studentInfoMapper.getStudentName(studentNum)!=null) {
                chaoxingMapper.addScoreInfo(studentNum,tcourseId,videoScore,videoProgress,quizScore,discussScore,
                        workScore,examScore,taskPercentage,score,level);
            }
        }
    }

    public void saveWorkInfo(JSONArray jsonData, String tcourseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        String row = data.get(0);
        List<String> workNameList = JSONArray.parseArray(row, String.class);
        for (int i = 6; i < workNameList.size(); i++) {
            if(workNameList.get(i)!=null){
                chaoxingMapper.addWork(tcourseId,workNameList.get(i));
            }
        }
        for (int i = 2; i < data.size(); i++) {
            row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(0);
            if(studentInfoMapper.getStudentName(studentNum)!=null) {
                for (int j = 6; j < workNameList.size(); j++) {
                    if(workNameList.get(j)!=null){
                        String workId=chaoxingMapper.getWorkId(workNameList.get(j));
                        String score=rowData.get(j);
                        chaoxingMapper.addWorkInfo(studentNum,workId,score);
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
            String studentNum=rowData.get(3);
            String score = (int)Double.parseDouble(rowData.get(11)) +"";

            if(studentInfoMapper.getStudentName(studentNum)!=null) {
                chaoxingMapper.addExamInfo(studentNum,tcourseId,score);
            }
        }
    }


    public void saveChapterQuizScore(JSONArray jsonData, String tcourseId) {
        List<String> data = JSONArray.parseArray(jsonData.toJSONString(), String.class);
        String row = data.get(0);
        List<String> chapterNameList = JSONArray.parseArray(row, String.class);
        for (int i = 6; i < chapterNameList.size(); i++) {
            if(chapterNameList.get(i)!=null){
                chaoxingMapper.addChapterQuiz(tcourseId,chapterNameList.get(i));
            }
        }
        for (int i = 2; i < data.size(); i++) {
            row = data.get(i);
            List<String> rowData = JSONArray.parseArray(row, String.class);
            String studentNum = rowData.get(0);
            if(studentInfoMapper.getStudentName(studentNum)!=null) {
                for (int j = 6; j < chapterNameList.size(); j++) {
                    if(chapterNameList.get(j)!=null){
                        String workId=chaoxingMapper.getChapterQuizId(chapterNameList.get(j));
                        String score=rowData.get(j);
                        chaoxingMapper.addChapterQuizScore(studentNum,workId,score);
                    }
                }
            }
        }
    }
}