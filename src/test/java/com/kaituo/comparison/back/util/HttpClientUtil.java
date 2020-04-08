package com.kaituo.comparison.back.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author:wuyuxiang
 * @creater:2019-08-08 16:56:59
 * @description:基于httpclient的二次封装
 */
public class HttpClientUtil {
    final static int SUCCESS_CODE=200;
    static CloseableHttpClient client=null;
    static {
        client= HttpClients.createDefault();
    }

    public static   JSONObject doGet(String uri) throws Exception {
        JSONObject jo=new JSONObject();
        HttpGet httpGet=new HttpGet(uri);
        CloseableHttpResponse response=client.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        jo.put("statusCode",statusCode);
        if(statusCode==SUCCESS_CODE){
            HttpEntity entity=response.getEntity();
            String s = EntityUtils.toString(entity);
            JSONObject content = JSONObject.parseObject(s);
            jo.put("content",content);
        }
        response.close();
        return jo;
    }
    public static   JSONObject doDelete(String uri) throws Exception {
        JSONObject jo=new JSONObject();
        HttpDelete httpDelete=new HttpDelete(uri);
        CloseableHttpResponse response=client.execute(httpDelete);
        int statusCode = response.getStatusLine().getStatusCode();
        jo.put("statusCode",statusCode);
        if(statusCode==SUCCESS_CODE){
            HttpEntity entity=response.getEntity();
            String s = EntityUtils.toString(entity);
            JSONObject content = JSONObject.parseObject(s);
            jo.put("content",content);
        }
        response.close();
        return jo;
    }

    public static JSONObject doPost(String uri,JSON param) throws Exception {
        JSONObject jo=new JSONObject();
        HttpPost post=new HttpPost(uri);
        StringEntity entity=new StringEntity(param.toString(),"UTF-8");
        entity.setContentType("application/json");
        post.setEntity(entity);
        CloseableHttpResponse response = client.execute(post);
        int statusCode = response.getStatusLine().getStatusCode();
        jo.put("statusCode",statusCode);
        if(statusCode==SUCCESS_CODE){
            String s = EntityUtils.toString(response.getEntity(),"UTF-8");
            JSONObject content = JSONObject.parseObject(s);
            jo.put("content",content);
        }
        response.close();
        return jo;
    }

    public static JSONObject doPost(String uri,String paramStr) throws Exception {
        JSON param=JSON.parseObject(paramStr);
        return doPost(uri,param);
    }

    public static JSONObject doPut(String uri,JSON param) throws Exception {
        JSONObject jo=new JSONObject();
        HttpPut put=new HttpPut(uri);
        StringEntity entity=new StringEntity(param.toString(),"UTF-8");
        entity.setContentType("application/json");
        put.setEntity(entity);
        CloseableHttpResponse response = client.execute(put);
        int statusCode = response.getStatusLine().getStatusCode();
        jo.put("statusCode",statusCode);
        if(statusCode==SUCCESS_CODE){
            String s = EntityUtils.toString(response.getEntity(),"UTF-8");
            JSONObject content = JSONObject.parseObject(s);
            jo.put("content",content);
        }
        response.close();
        return jo;
    }

    public static JSONObject doPut(String uri,String paramStr) throws Exception {
        JSON param=JSON.parseObject(paramStr);
        return doPut(uri,param);
    }

    public static void close() throws IOException {
        client.close();
    }

}
