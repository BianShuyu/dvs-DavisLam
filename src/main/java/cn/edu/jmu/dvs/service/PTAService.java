package cn.edu.jmu.dvs.service;

import cn.edu.jmu.dvs.mapper.PTAMapper;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PTAService {

    @Autowired
    PTAMapper ptaMapper;

    /***
     * 必须提供二维数组形式的，不带元数据的json字符串！！！
     * @param jsonData 二维数组形式的json字符串
     * @param courseId 课程id
     */
    public void savePTAData(String jsonData, String courseId){
        savePTAData(JSONArray.parseArray(jsonData),courseId);
    }

    public void savePTAData(JSONArray jsonData, String courseId){
        List<String> data=JSONArray.parseArray(jsonData.toJSONString(),String.class);
        int colSchoolNum=0;
        List<String> questionType=new ArrayList<>();
        List<String> questionNum=new ArrayList<>();
        for (int i=0;i<data.size();i++){
            String row=data.get(i);
            List<String> rowData=JSONArray.parseArray(row,String.class);

            if(i==0){  //获取题目类型列表
                String currentType=null;
                for(int j=0;j<rowData.size();j++){
                    String col=rowData.get(j);
                    if(j==0){ //第一列不是题目类型名 不算
                        questionType.add(null);
                    }else {
                        if(col!=null && !col.equals("")){
                            //如果出现了题目类型名，还要把上一列题目类型设为空（每类题目最后一列是总分）
                            if(currentType != null && currentType.equals(col)){
                                questionType.set(j-1,null);
                            }
                            currentType=col;
                        }
                        questionType.add(currentType);
                    }
                }
            }else if(i==1){  //获取题目标号
                boolean isQuestionNum=false;
                for(int j=0;j<rowData.size();j++){
                    if(j<questionType.size() && questionType.get(j)!=null && !questionType.get(j).equals("")){
                        // 如果对应列的题目类型不是空串 说明该列存在题目编号
                        questionNum.add(rowData.get(j));
                    }else if (j>=questionType.size() && j<rowData.size()-1){
                        questionNum.add(rowData.get(j));
                    }else {
                        questionNum.add(null);
                    }
                }
            }else if(i!=2){
                String schoolNum=rowData.get(1);
                String currentQuestionType=null;
                String currentQuestionNum=null;
                int score=0;
                for (int j=2;j<rowData.size();j++){
                    if(j<questionType.size()){
                        currentQuestionType=questionType.get(j);
                        currentQuestionNum=questionNum.get(j);
                    }else {
                        currentQuestionNum=questionNum.get(j);
                    }

                    if(!rowData.get(j).equals("-")){
                        String s=rowData.get(j);
                        String pattern="(\\d+\\.\\d+)(\\(.*)";
                        Matcher m= Pattern.compile(pattern).matcher(s);
                        score=(int)(Double.parseDouble(m.group(1)));
                    }else {
                        score=0;
                    }

                    ptaMapper.addPTAScore(schoolNum,courseId,currentQuestionType,currentQuestionNum,score);
                }
            }
        }
    }
}
