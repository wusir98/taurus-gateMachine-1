package com.kaituo.comparison.back.core.schedule;

import com.kaituo.comparison.back.core.dto.app.ResultRegister;
import com.kaituo.comparison.back.core.dto.app.ResultResource;
import com.kaituo.comparison.back.core.service.hksdk.HkAuthService;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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


    @Value("${xh.token}")
    String token;
    @Value("${xh.commitResult}")
    String commitResult;
    @Value("${xh.doorDeviceList}")
    String doorDeviceList;
    @Value("${xh.doorAccessListAdd}")
    String doorAccessListAdd;
    @Value("${xh.doorAccessListModify}")
    String doorAccessListModify;
    @Value("${xh.doorAccessListDelete}")
    String doorAccessListDelete;


    //@Scheduled(cron = "0/3 * * * * ?")
    public void schedulePeople() {
//        ResultRegister resultRegister = restTemplate.getForObject("http://192.168.110.132:8080/qzf/front/anon/doorAccessList.json?synctag=1", ResultRegister.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("token", token);

        //已下发人员
        ResultRegister resultRegister = restTemplate.exchange(doorAccessListAdd, HttpMethod.GET, new HttpEntity<String>(headers), ResultRegister.class).getBody();
        //修改人员
        ResultRegister resultModify = restTemplate.exchange(doorAccessListModify, HttpMethod.GET, new HttpEntity<String>(headers), ResultRegister.class).getBody();
        //删除人员
        ResultRegister resultDelete = restTemplate.exchange(doorAccessListDelete, HttpMethod.GET, new HttpEntity<String>(headers), ResultRegister.class).getBody();
        //楼栋和设备关系表
        ResultResource resultResource = restTemplate.exchange(doorDeviceList, HttpMethod.POST, new HttpEntity<String>(headers), ResultResource.class).getBody();


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
