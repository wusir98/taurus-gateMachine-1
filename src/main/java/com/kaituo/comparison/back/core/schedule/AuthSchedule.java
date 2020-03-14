package com.kaituo.comparison.back.core.schedule;

import com.kaituo.comparison.back.core.dto.app.ResultRegister;
import com.kaituo.comparison.back.core.dto.app.ResultResource;
import com.kaituo.comparison.back.core.service.hksdk.HkAuthService;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

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


    @Scheduled(cron = "0/3 * * * * ?")
    public void schedulePeople() {
//        ResultRegister resultRegister = restTemplate.getForObject("http://sq.wxsmart.xyz/qzf/front/anon/doorAccessList.json?synctag=1", ResultRegister.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("token", "c26aaf2a5bd4a3c495cbf1a1290a0b57");

        //已下发人员
        ResultRegister resultRegister = restTemplate.exchange("http://sq.wxsmart.xyz/qzf/front/anon/doorAccessList.json?synctag=1", HttpMethod.GET, new HttpEntity<String>(headers), ResultRegister.class).getBody();
        //修改人员
        ResultRegister resultModify = restTemplate.exchange("http://sq.wxsmart.xyz/qzf/front/anon/doorAccessList.json?synctag=4", HttpMethod.GET, new HttpEntity<String>(headers), ResultRegister.class).getBody();
        //删除人员
        ResultRegister resultDelete = restTemplate.exchange("http://sq.wxsmart.xyz/qzf/front/anon/doorAccessList.json?synctag=5", HttpMethod.GET, new HttpEntity<String>(headers), ResultRegister.class).getBody();
        //楼栋和设备关系表
        ResultResource resultResource = restTemplate.exchange("http://sq.wxsmart.xyz/qzf//front/anon/doorDeviceList.json", HttpMethod.POST, new HttpEntity<String>(headers), ResultResource.class).getBody();


        if (!CollectionUtils.isEmpty(resultRegister.getCommand())) {
            log.info("==================新增权限开始");
            hkAuthService.addAuth(resultRegister, resultResource);
        }

        if (!CollectionUtils.isEmpty(resultModify.getCommand())) {
            log.info("==================修改权限开始");
            hkAuthService.modifyAuth(resultRegister, resultResource);
        }

        if (!CollectionUtils.isEmpty(resultDelete.getCommand())) {
            log.info("==================删除权限开始");
            hkAuthService.deleteAuth(resultRegister, resultResource);
        }


    }
}
