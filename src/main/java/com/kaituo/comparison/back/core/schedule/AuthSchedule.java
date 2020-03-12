package com.kaituo.comparison.back.core.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kaituo.comparison.back.core.constant.CommonConstant;
import com.kaituo.comparison.back.core.dto.app.*;
import com.kaituo.comparison.back.core.dto.hksdk.*;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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


    //    @Scheduled(cron = "0/3 * * * * ?")
    public void schedulePeople() {

        //已下发人员
        ResultRegister resultRegister = restTemplate.getForObject("http://sq.wxsmart.xyz/qzf/front/anon/doorAccessList.json?synctag=1", ResultRegister.class);
        //楼栋和设备关系表
        ResultResource resultResource = restTemplate.postForObject("http://sq.wxsmart.xyz/qzf//front/anon/doorDeviceList.json", null, ResultResource.class);


        List<CardInfo> cardInfos = new ArrayList<>();


        if (!Objects.isNull(resultRegister)) {
            List<PeoPleData> PeoPleDatas = resultRegister.getCommand();
            PeoPleDatas.forEach(v -> {
                        //下发卡信息
                        CardInfo cardInfo = new CardInfo();
                        cardInfo.setPersonId(v.getId());
                        cardInfo.setCardNo(v.getId());
                        cardInfos.add(cardInfo);
                        //权限下发信息
                        resultResource.getCommand().forEach(v1 -> {

                            if (v.getAreaid().equals(v1.getAreaid()) && v.getUnitno().equals(v1.getUnitno())) {
                                List<ResourceInfo> list = new ArrayList<>();
                                ResourceInfo resourceInfo = new ResourceInfo();
                                resourceInfo.setResourceIndexCode(v1.getDeviceid());
                                list.add(resourceInfo);
                                v.setResourceInfos(list);
                            }

                        });

                    }
            );
        }


        CardsBind cardsBind = new CardsBind();
        cardsBind.setStartDate("2018-10-30");
        cardsBind.setEndDate("2028-10-30");
        cardsBind.setCardList(cardInfos);
        //批量开卡
        String responseCardsBind;
        if (!CollectionUtils.isEmpty(cardsBind.getCardList())) {
            responseCardsBind = hkService.getResponse(CommonConstant.HK_CARD_BINDS, cardsBind);
            log.info("批量开卡" + responseCardsBind);


            if (CommonConstant.SUCCESS_CODE.equals(JSONObject.parseObject(responseCardsBind).getString("code"))) {
                //权限下发do
                AuthAdd authAdd = new AuthAdd();
                authAdd.setStartTime("2018-10-30");
                authAdd.setEndTime("2028-10-30");

                resultRegister.getCommand().forEach(v -> {
                    List<PersonDatas> personAuthList = new ArrayList<>();
                    PersonDatas personDatas = new PersonDatas();
                    List<String> personIndexs = new ArrayList<>();
                    personIndexs.add(v.getId());
                    personDatas.setIndexCodes(personIndexs);
                    personAuthList.add(personDatas);

                    authAdd.setPersonDatas(personAuthList);
                    authAdd.setResourceInfos(v.getResourceInfos());
                    log.info("权限下发入参：" + JSON.toJSONString(authAdd).toString());
                    //添加权限配置
                    hkService.getResponse(CommonConstant.HK_AUTH_ADD, authAdd);
                });

            }


            //创建下载任务
            //1卡片 4人脸
            Map<String, Integer> taskAddMap = new HashMap();
            String responseTask = hkService.getResponse(CommonConstant.HK_TASK_ADD, taskAddMap);

            log.info("创建下载任务" + responseTask);
            String taskId = JSONObject.parseObject(responseTask).getJSONObject("data").getString("taskId");

            //开始下载任务
            Map<String, String> taskStartMap = new HashMap();
            taskStartMap.put("taskId", taskId);
            String response = hkService.getResponse(CommonConstant.HK_TASK_START, taskStartMap);

            log.info("开始下载任务返回：" + response);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            //查看下载任务进度
            String taskProgress = hkService.getResponse(CommonConstant.HK_TASK_PROGRESS, taskId);

            log.info("查看下载任务进度返回：" + taskProgress);

            TaskProgress progress = JSONObject.parseObject(taskProgress, TaskProgress.class);


            List<String> resourceIndexCodeList = new ArrayList<>();

            //获取下发成功的设备号
            if (!Objects.isNull(progress)) {
                List<ResourceDownloadProgress> resourceDownloadProgress = progress.getResourceDownloadProgress();
                if (!CollectionUtils.isEmpty(resourceDownloadProgress)) {
                    resourceDownloadProgress.forEach(v -> {
                                if (CommonConstant.SUCCESS_CODE.equals(v.getErrorCode())) {
                                    resourceIndexCodeList.add(v.getResourceInfo().getResourceIndexCode());
                                }
                            }
                    );
                }
            }

            log.info("下发成功的设备号:");


            //更改最终状态码
            if (!Objects.isNull(resultRegister)) {
                resultRegister.getCommand().forEach(v -> {
                    ResultCommit resultCommit = new ResultCommit();
                    resultCommit.setId(v.getId());
                    resultCommit.setSynctag("3");
                    resultCommit.setSyncmsg("success");
                    ResultBase resultBase = restTemplate.postForObject("http://sq.wxsmart.xyz/qzf/front/anon/doorAccessSync.json", resultCommit, ResultBase.class);
                    log.info("门禁校验结果提交" + resultBase.toString());
                });
            }


        }


    }
}
