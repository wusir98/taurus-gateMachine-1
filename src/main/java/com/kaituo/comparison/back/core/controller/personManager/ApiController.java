package com.kaituo.comparison.back.core.controller.personManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kaituo.comparison.back.common.bean.ResponseResult;
import com.kaituo.comparison.back.core.dto.hksdk.Param;
import com.kaituo.comparison.back.core.dto.hksdk.RestToken;
import com.kaituo.comparison.back.core.entity.system.Log;
import com.kaituo.comparison.back.core.service.hksdk.HkAuthService;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import com.kaituo.comparison.back.core.service.system.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("hk")
@Slf4j
public class ApiController {


    @Autowired
    HkService hkService;

    @Autowired
    LogService logService;
    @Autowired
    HkAuthService hkAuthService;

    @PostMapping("/api")
    public String api(@RequestBody Param param, @RequestHeader(value = "token") String token) {
        Log log = logService.addLog(param, token);

        boolean allow = hkService.isAllow(param.getUri(), token);
        if (true) {
            String response = "";
            try {
                response = hkService.getResponse(param.getUri(), param.getParam());
                JSONObject jsonObject = JSON.parseObject(response);
                log.setMsg(jsonObject.getString("msg"));
                log.setResult(jsonObject.getString("code"));
                logService.update(log);


//                hkService.startHkAync();


                return response;
            } catch (Exception e) {
                ApiController.log.error(e.getMessage(), e);
                log.setMsg("other error");
                log.setResult("0x00000001");
                logService.update(log);
                return response;
            }
        } else {
            ResponseResult result = new ResponseResult();
            log.setMsg("no permission");
            log.setResult("0x00000000");
            result.setCode("0x00000000");
            result.setMsg("no permission");
            logService.update(log);
            return JSON.toJSONString(result);
        }
    }


    @PutMapping("/token")
    public ResponseResult api(RestToken restToken) {
        hkService.updateToken(restToken);
        return new ResponseResult();
    }

    @GetMapping("/previewAuth")
    public ResponseResult previewAuth(String personId,String areaid,String unitno) {
        log.info("previewAuth?personId="+personId+"&areaid="+areaid+"&unitno="+unitno);
        hkAuthService.tempAuth(personId,areaid,unitno);
        return new ResponseResult();
    }

}
