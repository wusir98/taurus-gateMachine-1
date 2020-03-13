package com.kaituo.comparison.back.core.schedule;

import com.kaituo.comparison.back.core.dto.app.ResultRegister;
import com.kaituo.comparison.back.core.dto.app.ResultResource;
import com.kaituo.comparison.back.core.service.hksdk.HkAuthService;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/2/22 15:22
 * @Modified by:
 */
@Component
@Configuration
@EnableScheduling
@Slf4j
public class AuthSchedule {

    @Autowired
    HkService hkService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HkAuthService hkAuthService;


    //    @Scheduled(cron = "0/3 * * * * ?")
    public void schedulePeople() {

        //已下发人员
        ResultRegister resultRegister = restTemplate.getForObject("http://sq.wxsmart.xyz/qzf/front/anon/doorAccessList.json?synctag=1", ResultRegister.class);
        //修改人员
        ResultRegister resultModify = restTemplate.getForObject("http://sq.wxsmart.xyz/qzf/front/anon/doorAccessList.json?synctag=4", ResultRegister.class);
        //删除人员
        ResultRegister resultDelete = restTemplate.getForObject("http://sq.wxsmart.xyz/qzf/front/anon/doorAccessList.json?synctag=5", ResultRegister.class);
        //楼栋和设备关系表
        ResultResource resultResource = restTemplate.postForObject("http://sq.wxsmart.xyz/qzf//front/anon/doorDeviceList.json", null, ResultResource.class);


        if (!Objects.isNull(resultRegister.getCommand())) {

            hkAuthService.addAuth(resultRegister, resultResource);
        }

        if (!Objects.isNull(resultModify.getCommand())) {

            hkAuthService.modifyAuth(resultRegister, resultResource);
        }

        if (!Objects.isNull(resultDelete.getCommand())) {

            hkAuthService.deleteAuth(resultRegister, resultResource);
        }


    }
}
