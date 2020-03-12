package com.kaituo.comparison.back;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kaituo.comparison.back.core.constant.CommonConstant;
import com.kaituo.comparison.back.core.dto.app.PeoPleData;
import com.kaituo.comparison.back.core.dto.app.ResultRegister;
import com.kaituo.comparison.back.core.dto.app.ResultResource;
import com.kaituo.comparison.back.core.dto.hksdk.*;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
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
public class Test {

    @Autowired
    HkService hkService;


    @Autowired
    private RestTemplate restTemplate;

    @org.junit.Test
    public void list() {
        ResultRegister resultRegister = restTemplate.getForObject("http://sq.wxsmart.xyz/qzf/front/anon/doorAccessList.json?synctag=1", ResultRegister.class);

        resultRegister.getCommand().forEach(v -> System.out.println(v.toString()));

    }


    @org.junit.Test
    public void test1() {


        //已下发人员
        ResultRegister resultRegister = new ResultRegister();
        resultRegister.setMsg("");
        resultRegister.setStatus("0");
        List<PeoPleData> list = new ArrayList<>();
        PeoPleData peoPleData = new PeoPleData();
        peoPleData.setId("320281199208238515");
        peoPleData.setAreaid("1111");
        peoPleData.setUnitno("11");
        list.add(peoPleData);
        ResourceInfo resourceInfo = new ResourceInfo();
        List<ResourceInfo> resourceInfos = new ArrayList<>();
        resourceInfo.setResourceIndexCode("5abbeb15490f48b3b40d1bd2e1a854fa");
        resourceInfos.add(resourceInfo);
        ResourceInfo resourceInfo1 = new ResourceInfo();
        ;
        resourceInfo1.setResourceIndexCode("811bcd98f885433a964e739a9fd8dc49");
        resourceInfos.add(resourceInfo1);
        peoPleData.setResourceInfos(resourceInfos);
        resultRegister.setCommand(list);
        //楼栋和设备关系表
        ResultResource resultResource = new ResultResource();


        List<CardInfo> cardInfos = new ArrayList<>();


        if (!Objects.isNull(resultRegister)) {
            List<PeoPleData> PeoPleDatas = resultRegister.getCommand();
            PeoPleDatas.forEach(v -> {
                        //下发卡信息
                        CardInfo cardInfo = new CardInfo();
                        cardInfo.setPersonId(v.getId());
                        cardInfo.setCardNo(v.getId());
                        cardInfos.add(cardInfo);

                    }

            );
        }


        CardsBind cardsBind = new CardsBind();
        cardsBind.setStartDate("2018-10-30");
        cardsBind.setEndDate("2028-10-30");
        cardsBind.setCardList(cardInfos);
        //批量开卡
        String responseCardsBind = hkService.getResponse(CommonConstant.HK_CARD_BINDS, cardsBind);
        System.out.println(responseCardsBind);

        if (CommonConstant.SUCCESS_CODE.equals(JSONObject.parseObject(responseCardsBind).getString("code"))) {
            //权限下发do
            AuthAdd authAdd = new AuthAdd();
            authAdd.setStartTime("2018-09-03T17:30:08.000+08:00");
            authAdd.setEndTime("2028-09-03T17:30:08.000+08:00");

            resultRegister.getCommand().forEach(v -> {
                List<PersonDatas> personAuthList = new ArrayList<>();
                PersonDatas personDatas = new PersonDatas();
                List<String> personIndexs = new ArrayList<>();
                personIndexs.add(v.getId());
                personDatas.setIndexCodes(personIndexs);
                personAuthList.add(personDatas);

                authAdd.setPersonDatas(personAuthList);
                authAdd.setResourceInfos(v.getResourceInfos());
                System.out.println(JSON.toJSON(authAdd).toString());
                //添加权限配置
                String response = hkService.getResponse(CommonConstant.HK_AUTH_ADD, authAdd);
                System.out.println("权限" + response);
            });

        }


        //创建下载任务
        //1卡片 4人脸
        Map<String, Integer> taskAddMap = new HashMap();
        String responseTask = hkService.getResponse(CommonConstant.HK_TASK_ADD, taskAddMap);
        String taskId = JSONObject.parseObject(responseTask).getJSONObject("data").getString("taskId");

        //开始下载任务
        Map<String, String> taskStartMap = new HashMap();
        taskStartMap.put("taskId", taskId);
        System.out.println(taskId + "=============taskid");
        String response = hkService.getResponse(CommonConstant.HK_TASK_START, taskStartMap);
        System.out.println(response);
    }
}
