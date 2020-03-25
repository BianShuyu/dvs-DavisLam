package cn.edu.jmu.dvs;

import java.util.ArrayList;
import java.util.List;

public class PageData<T> {

    private int code = 0;
    private int count;
    private List<T> data;
    private String msg = "";

    public PageData(List<T> source, int page, int limit) {
        ArrayList<T> data = new ArrayList<>();
        int start = (page - 1) * limit;
        int end = Math.min(page * limit, source.size());
        for (int i = start; i < end; i++) {
            data.add(source.get(i));
        }
        this.data = data;
        this.count = source.size();
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
