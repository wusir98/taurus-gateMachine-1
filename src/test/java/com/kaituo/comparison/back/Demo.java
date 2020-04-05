package com.kaituo.comparison.back;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaituo.comparison.back.core.dto.hksdk.ResourceInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

public class Demo {
    public static void main(String[] args) {
        JSONObject jo=new JSONObject();
        jo.put("a",null);
        jo.put("b","fds");
        String a = jo.getString("a");
        Integer a1 = jo.getInteger("a");
       // Integer b = jo.getInteger("b");
        System.out.println(a);
        System.out.println(a1==null);
       // System.out.println(b);

    }
}
