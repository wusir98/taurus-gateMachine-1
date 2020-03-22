package com.kaituo.comparison.back.core.schedule;

import com.kaituo.comparison.back.core.constant.CommonConstant;
import com.kaituo.comparison.back.core.dto.app.PeoPleData;
import com.kaituo.comparison.back.core.dto.app.ResultRegister;
import com.kaituo.comparison.back.core.dto.app.ResultResource;
import com.kaituo.comparison.back.core.dto.hksdk.AuthSingleQueryDTO;
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

import java.util.List;

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
    @Value("${xh.doorAccessListAuth}")
    String doorAccessListAuth;
    @Value("${xh.doorAccessListWait}")
    String doorAccessListWait;
    @Value("${xh.doorAccessListFail}")
    String doorAccessListFail;


    @Scheduled(fixedDelay= 1*1000*60)
    public void schedulePeople() {
//        ResultRegister resultRegister = restTemplate.getForObject("http://192.168.110.132:8080/qzf/front/anon/doorAccessList.json?synctag=1", ResultRegister.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("token", token);

        //已下发人员
        ResultRegister resultRegister = restTemplate.exchange(doorAccessListAuth, HttpMethod.GET, new HttpEntity<String>(headers), ResultRegister.class).getBody();


        if (!CollectionUtils.isEmpty(resultRegister.getCommand())) {
            log.info("==================新增权限开始");
            //hkAuthService.addAuth(resultRegister, resultResource);
            hkAuthService.addAuth(resultRegister);
        }
        //4.人——权限遍历完，一起提交
        hkAuthService.startTask();

    }

    @Scheduled(fixedDelay= 10*1000)
    public void updateStatus() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", token);
        ResultRegister resultRegister = restTemplate.exchange(doorAccessListWait, HttpMethod.GET, new HttpEntity<String>(headers), ResultRegister.class).getBody();
        ResultRegister resultRegister2 = restTemplate.exchange(doorAccessListFail, HttpMethod.GET, new HttpEntity<String>(headers), ResultRegister.class).getBody();
        List<PeoPleData> command = resultRegister.getCommand();
        List<PeoPleData> command2 = resultRegister2.getCommand();
        command.addAll(command2);
        hkAuthService.qr(command);


    }
}
