package com.kaituo.comparison.back;

import com.kaituo.comparison.back.core.dto.app.ResultRegister;
import com.kaituo.comparison.back.core.service.hksdk.HkAuthService;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class InitResource {
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

    @org.junit.Test
    //@Scheduled(fixedDelay= 5*1000*60)
    public void schedulePeople() {
//        ResultRegister resultRegister = restTemplate.getForObject("http://192.168.110.132:8080/qzf/front/anon/doorAccessList.json?synctag=1", ResultRegister.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("token", token);
        System.out.println(11);
        //已下发人员
        ResultRegister resultRegister = restTemplate.exchange(doorAccessListAuth, HttpMethod.GET, new HttpEntity<String>(headers), ResultRegister.class).getBody();

     /*   try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        System.out.println(resultRegister);

        if (!CollectionUtils.isEmpty(resultRegister.getCommand())) {
            log.info("==================新增权限开始");
            //hkAuthService.addAuth(resultRegister, resultResource);
            hkAuthService.addAuth(resultRegister);
        }
        //4.人——权限遍历完，一起提交
        //hkAuthService.startTask();

    }
}
