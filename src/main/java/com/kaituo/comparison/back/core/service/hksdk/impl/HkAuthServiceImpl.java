package com.kaituo.comparison.back.core.service.hksdk.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kaituo.comparison.back.core.constant.CommonConstant;
import com.kaituo.comparison.back.core.dto.app.*;
import com.kaituo.comparison.back.core.dto.hksdk.*;
import com.kaituo.comparison.back.core.service.hksdk.HkAuthService;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/13 12:15
 * @Modified by:
 */
@Service
@Slf4j
public class HkAuthServiceImpl implements HkAuthService {


    @Autowired
    HkService hkService;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void addAuth(ResultRegister resultRegister, ResultResource resultResource) {

        List<CardInfo> cardInfos = new ArrayList<>();

        if (!Objects.isNull(resultRegister.getCommand())) {
            List<PeoPleData> PeoPleDatas = resultRegister.getCommand();
            PeoPleDatas.forEach(v -> {
                        //下发卡信息
                        CardInfo cardInfo = new CardInfo();
                        cardInfo.setPersonId(v.getId());
                        cardInfo.setCardNo(v.getId());
                        cardInfos.add(cardInfo);
                        //权限下发信息
                        List<ResourceInfo> list = new ArrayList<>();
                        //用于判断最后是否下发成功list
                        List<String> list1 = new ArrayList<>();
                        resultResource.getCommand().forEach(v1 -> {
                            if (v.getAreaid().equals(v1.getAreaid()) && v.getUnitno().equals(v1.getUnitno())) {
                                ResourceInfo resourceInfo = new ResourceInfo();
                                resourceInfo.setResourceIndexCode(v1.getDeviceid());
                                list.add(resourceInfo);
                                list1.add(v1.getDeviceid());
                            }

                        });
                        v.setResourceInfos(list);
                        v.setResourceIndexCodes(list1);

                    }
            );
        }


        log.info(resultRegister.toString() + "=================入参");

        CardsBind cardsBind = new CardsBind();
        cardsBind.setStartDate("2018-10-30");
        cardsBind.setEndDate("2028-10-30");
        cardsBind.setCardList(cardInfos);


        //批量开卡
        String responseCardsBind;
        if (!CollectionUtils.isEmpty(cardsBind.getCardList())) {

            //批量开卡
            responseCardsBind = hkService.getResponse(CommonConstant.HK_CARD_BINDS, cardsBind);
            log.info("批量开卡" + responseCardsBind);

            authAddCommit(resultRegister);

            List<String> resourceIndexCodeList = doTask();

            resourceIndexCodeList.forEach(v -> log.info(v + "======="));

            commitResult(resultRegister, resourceIndexCodeList);
        }
    }


    @Override
    public void modifyAuth(ResultRegister resultRegister, ResultResource resultResource) {
        if (!Objects.isNull(resultRegister.getCommand())) {
            List<PeoPleData> PeoPleDatas = resultRegister.getCommand();
            PeoPleDatas.forEach(v -> {
                        //权限下发信息
                        List<ResourceInfo> list = new ArrayList<>();
                        //用于判断最后是否下发成功list
                        List<String> list1 = new ArrayList<>();
                        resultResource.getCommand().forEach(v1 -> {
                            if (v.getAreaid().equals(v1.getAreaid()) && v.getUnitno().equals(v1.getUnitno())) {
                                ResourceInfo resourceInfo = new ResourceInfo();
                                resourceInfo.setResourceIndexCode(v1.getDeviceid());
                                list.add(resourceInfo);
                                list1.add(v1.getDeviceid());
                            }

                        });
                        v.setResourceInfos(list);
                        v.setResourceIndexCodes(list1);
                    }
            );
        }
        authDelCommit(resultRegister);
        doTask();
        authAddCommit(resultRegister);
        List<String> resourceIndexCodeList = doTask();
        commitResult(resultRegister, resourceIndexCodeList);
    }


    @Override
    public void deleteAuth(ResultRegister resultRegister, ResultResource resultResource) {
        Map<String, List<String>> deletePeoples = new HashMap();
        List<String> personIds = new ArrayList<>();

        if (!Objects.isNull(resultRegister.getCommand())) {
            List<PeoPleData> PeoPleDatas = resultRegister.getCommand();
            PeoPleDatas.forEach(v -> {
                        //用于判断最后是否下发成功list
                        List<String> list1 = new ArrayList<>();
                        resultResource.getCommand().forEach(v1 -> {
                            if (v.getAreaid().equals(v1.getAreaid()) && v.getUnitno().equals(v1.getUnitno())) {
                                list1.add(v1.getDeviceid());
                            }
                        });
                        v.setResourceIndexCodes(list1);
                        personIds.add(v.getId());
                    }
            );
        }
        deletePeoples.put("personIds", personIds);
        String respDeletePeople = hkService.getResponse(CommonConstant.HK_BATCH_DELETE_PEOPLE, deletePeoples);
        log.info("批量删除人员返回值:" + respDeletePeople);
        List<String> resourceIndexCodeList = doTask();
        commitResult(resultRegister, resourceIndexCodeList);
    }


    /**
     * 创建任务
     * 开始任务
     *
     * @return 成功的设备号
     */

    List<String> doTask() {
        //创建下载任务
        //1卡片 4人脸  null全部
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
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //查看下载任务进度
        Map<String, String> task = new HashMap();
        task.put("taskId", taskId);
        String taskProgress = hkService.getResponse(CommonConstant.HK_TASK_PROGRESS, task);

        log.info("查看下载任务进度返回：" + taskProgress);

        TaskProgress progress = JSONObject.parseObject(taskProgress, TaskProgressReturn.class).getData();


        //成功下发设备的list
        List<String> resourceIndexCodeList = new ArrayList<>();

        //获取下发成功的设备号
        if (!Objects.isNull(progress)) {
            List<ResourceDownloadProgress> resourceDownloadProgress = progress.getResourceDownloadProgress();
            if (!CollectionUtils.isEmpty(resourceDownloadProgress)) {
                resourceDownloadProgress.forEach(v -> {
                    if (CommonConstant.SUCCESS_CODE.equals(v.getErrorCode()) || Objects.isNull(v.getErrorCode())) {
                                resourceIndexCodeList.add(v.getResourceInfo().getResourceIndexCode());
                            }
                        }
                );
            }
        }
        return resourceIndexCodeList;
    }


    /**
     * 提交给app端最终确认码
     *
     * @param resultRegister
     * @param resourceIndexCodeList
     */
    private void commitResult(ResultRegister resultRegister, List<String> resourceIndexCodeList) {

        if (!Objects.isNull(resultRegister) && !CollectionUtils.isEmpty(resourceIndexCodeList)) {

            log.info("提交给app端开始=================");
            resultRegister.getCommand().forEach(v -> {
                if (resourceIndexCodeList.containsAll(v.getResourceIndexCodes())) {
                    ResultCommit resultCommit = new ResultCommit();
                    resultCommit.setId(v.getId());
                    resultCommit.setSynctag("2");
//                        resultCommit.setSyncmsg("授权成功");

                    HttpHeaders headers = new HttpHeaders();
                    headers.add("token", "c26aaf2a5bd4a3c495cbf1a1290a0b57");
                    ResultBase resultBase = restTemplate.exchange("http://sq.wxsmart.xyz/qzf/front/anon/doorAccessSync.json", HttpMethod.POST, new HttpEntity<>(resultCommit, headers), ResultBase.class).getBody();
                    log.info("门禁校验结果提交" + resultBase.toString());
                }

            });
        }
    }


    /**
     * 权限下发
     *
     * @param resultRegister
     */
    private void authAddCommit(ResultRegister resultRegister) {
        //权限下发do
        AuthAdd authAdd = new AuthAdd();
        authAdd.setStartTime("2018-09-03T17:30:08.000+08:00");
        authAdd.setEndTime("2028-09-06T17:30:08.000+08:00");
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
            String response = hkService.getResponse(CommonConstant.HK_AUTH_ADD, authAdd);
            log.info("权限下发返回值" + response);
        });
    }


    /**
     * 权限删除提交
     *
     * @param resultRegister
     */
    private void authDelCommit(ResultRegister resultRegister) {
        AuthDelete authDelete = new AuthDelete();
        List<PersonDatas> personDatasList = new ArrayList<>();
        List<String> personDeleteIndexs = new ArrayList<>();
        PersonDatas personDeleteDatas = new PersonDatas();
        resultRegister.getCommand().forEach(v -> {
            personDeleteIndexs.add(v.getId());
        });
        personDeleteDatas.setIndexCodes(personDeleteIndexs);
        personDatasList.add(personDeleteDatas);
        authDelete.setPersonDatas(personDatasList);
        String response = hkService.getResponse(CommonConstant.HK_AUTH_DELETE, authDelete);
        log.info("权限删除返回值" + response);
    }

}
