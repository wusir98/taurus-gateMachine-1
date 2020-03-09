package com.kaituo.comparison.back.core.controller.personManager;

import com.kaituo.comparison.back.core.dto.hksdk.Param;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    HkService hkService;

    @PostMapping("api")
    public String api(@RequestBody Param param){
        System.out.println(param.getParam());
        String response = hkService.getResponse(param.getUri(), param.getParam());
        return response;
    }
}
