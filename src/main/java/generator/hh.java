package generator;

/**
 * @Author: 赵景南
 * @Date: 2025/1/11 22:23
 * @Version: v1.0.0
 * @Description: 略
 **/
import cn.hutool.json.JSONUtil;

import java.util.ArrayList;

import java.util.ArrayList;



public class hh {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("apple");
        list.add("banana");
        list.add("orange");

        String jsonString = JSONUtil.toJsonStr(list);
        System.out.println(jsonString);
    }
}