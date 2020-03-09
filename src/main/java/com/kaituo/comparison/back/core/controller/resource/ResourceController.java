package com.kaituo.comparison.back.core.controller.resource;

import com.kaituo.comparison.back.common.bean.ResponseResult;
import com.kaituo.comparison.back.common.bean.ResponseResult2;
import com.kaituo.comparison.back.core.constant.CommonConstant;
import com.kaituo.comparison.back.core.dto.system.log.FindLogDTO;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.mozilla.javascript.tools.idswitch.FileBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class ResourceController {
    @Autowired
    RestTemplate restTemplate;
    @GetMapping("{uri}")
    //{"pageSize":1,"pageNo":1}
    //"http://192.168.110.202:9017/api/resource/v1/person/personList"
    public String get(@PathVariable("uri") String uri) throws IOException {
       return "";
    }

    @PostMapping("{uri}")
    public String post(@PathVariable("uri") String uri, Map result){
        System.out.println(uri);
        System.out.println(result);
        return "";
    }
}