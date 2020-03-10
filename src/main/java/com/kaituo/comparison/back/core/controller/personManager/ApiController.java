package com.kaituo.comparison.back.core.controller.personManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kaituo.comparison.back.common.bean.ResponseResult2;
import com.kaituo.comparison.back.core.dto.hksdk.Param;
import com.kaituo.comparison.back.core.dto.hksdk.RestToken;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import com.kaituo.comparison.back.core.service.system.LogService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@Slf4j
public class ApiController {


    @Autowired
    HkService hkService;
    @Autowired
    LogService logService;
    @PostMapping("/api")
    public String api(@RequestBody Param param,@RequestHeader(value = "token",required = true) String token){
        String id = logService.addLog(param, token);
        System.out.println(param.getParam());
        System.out.println(token);

        boolean allow = hkService.isAllow(param.getUri(), token);
        if(allow){
            String response ="";
            try {
                response = hkService.getResponse(param.getUri(), param.getParam());
                JSONObject jsonObject = JSON.parseObject(response);
                String code = jsonObject.getString("code");
                String msg=jsonObject.getString("msg");
                logService.update(id,code,msg);
                return response;
            }catch (Exception e){
                log.error(e.getMessage(),e);
                logService.update(id,"0x00000001","other error");
                return response;
            }
        }else {
            ResponseResult2 result = new ResponseResult2();
            result.setCode("0x00000000");
            result.setMsg("no permission");
            logService.update(id,"0x00000000","no permission");
            return JSON.toJSONString(result);
        }
    }

    @PutMapping("/token")
    public ResponseResult2 api(RestToken restToken){
        hkService.updateToken(restToken);
        return new ResponseResult2();
    }
}
