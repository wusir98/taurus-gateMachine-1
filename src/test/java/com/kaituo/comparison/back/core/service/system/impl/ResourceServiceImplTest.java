package com.kaituo.comparison.back.core.service.system.impl;

import com.kaituo.comparison.back.core.constant.CommonConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;


/**
 *
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourceServiceImplTest {



    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void list() {

        SqPeople sqPeople = new SqPeople();
        sqPeople.setSickcity("湖北");
//        BaseResponse post = (BaseResponse) restClient.post(CommonConstant.KEY_SICKCITYINFODATA, sqPeople);
//        System.out.println(post.toString());


        ResponseResult responseResult = restTemplate.postForObject(CommonConstant.KEY_SICKCITYINFODATA, sqPeople, ResponseResult.class);
        System.out.println(responseResult.toString());
    }
}