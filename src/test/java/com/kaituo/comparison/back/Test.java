package com.kaituo.comparison.back;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kaituo.comparison.back.core.constant.CommonConstant;
import com.kaituo.comparison.back.core.dto.app.*;
import com.kaituo.comparison.back.core.dto.hksdk.*;
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

import java.util.*;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/12 9:57
 * @Modified by:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class Test {
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
    //@Scheduled(fixedDelay= 1*1000*60)
    public void addAuthZyd() {
        //342201198602288253

        ResultRegister resultRegister =new ResultRegister();
        List<PeoPleData> command=new ArrayList<PeoPleData>();
        PeoPleData peoPleData=new PeoPleData();
        peoPleData.setCardid("320283199006260979");
        PeoPleData peoPleData2=new PeoPleData();
        peoPleData2.setCardid("342201198602288253");
        command.add(peoPleData);
        command.add(peoPleData2);
        resultRegister.setCommand(command);
//        ResultRegister resultRegister = restTemplate.getForObject("http://192.168.110.132:8080/qzf/front/anon/doorAccessList.json?synctag=1", ResultRegister.class);

        HttpHeaders headers = new HttpHeaders();
        headers.add("token", token);

        //已下发人员
        //ResultRegister resultRegister = restTemplate.exchange(doorAccessListAuth, HttpMethod.GET, new HttpEntity<String>(headers), ResultRegister.class).getBody();


        if (!CollectionUtils.isEmpty(resultRegister.getCommand())) {
            log.info("==================新增权限开始");
            //hkAuthService.addAuth(resultRegister, resultResource);
            hkAuthService.addAuth(resultRegister);
        }
        hkAuthService.startTask();

    }
    @org.junit.Test
    //@Scheduled(fixedDelay= 5*1000)
    public void updateStatus() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", token);
        ResultRegister resultRegister = restTemplate.exchange(doorAccessListWait, HttpMethod.GET, new HttpEntity<String>(headers), ResultRegister.class).getBody();
        ResultRegister resultRegister2 = restTemplate.exchange(doorAccessListFail, HttpMethod.GET, new HttpEntity<String>(headers), ResultRegister.class).getBody();
        List<PeoPleData> command = resultRegister.getCommand();
        List<PeoPleData> command2 = resultRegister2.getCommand();
        System.out.println(command);
        System.out.println(command2);
        //command.addAll(command2);
      //  log.info("需要判断 "+command.size()+" 个状态");
      //  hkAuthService.qr(command);


    }
}
