package cn.edu.jmu.dvs.service;

import com.alibaba.fastjson.JSONArray;
import org.springframework.stereotype.Service;

@Service
public class PTAService {

    public void savePTAData(String jsonData){
        savePTAData(JSONArray.parseArray(jsonData));
    }

    public void savePTAData(JSONArray jsonData){

    }
}
