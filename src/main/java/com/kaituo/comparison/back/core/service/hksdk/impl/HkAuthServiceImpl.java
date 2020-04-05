package com.kaituo.comparison.back.core.service.hksdk.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaituo.comparison.back.core.constant.CommonConstant;
import com.kaituo.comparison.back.core.dto.app.*;
import com.kaituo.comparison.back.core.dto.hksdk.*;
import com.kaituo.comparison.back.core.entity.event.Door;
import com.kaituo.comparison.back.core.mapper.event.DoorMapper;
import com.kaituo.comparison.back.core.service.hksdk.HkAuthService;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
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
    DoorMapper doorMapper;

    @Autowired
    HkService hkService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${xh.commitResult}")
    String commitResult;

    @Value("${xh.token}")
    String token;

    @Override
    public void addAuth(ResultRegister resultRegister) {
        if (!Objects.isNull(resultRegister.getCommand())) {
            List<PeoPleData> peoPleDatas = resultRegister.getCommand();
            //处理每一条人——楼的权限关系
            peoPleDatas.forEach(v-> {
                String personId = v.getCardid();
                //1.发卡
                // 判断是否此人已有卡，如果没卡，则开卡
                addCard(personId);
                //2.删除该人所有权限


                AuthDelete authDelete = new AuthDelete();
                List<PersonDatas> personDatasList = new ArrayList<>();
                List<String> personDeleteIndexs = new ArrayList<>();
                personDeleteIndexs.add(personId);
                PersonDatas personDatas=new PersonDatas();
                personDatas.setIndexCodes(personDeleteIndexs);
                personDatasList.add(personDatas);
                authDelete.setPersonDatas(personDatasList);
                log.info("权限删除入参:" + authDelete);
                String response = hkService.getResponse(CommonConstant.HK_AUTH_DELETE, authDelete);
                log.info("权限删除返回值" + response);
                //3.权限下发信息
                auth(v);
                commitResult(v.getId(),"4");
            });
        }
    }

    @Override
    public void qr(List<PeoPleData> listPeoPleData) {
        //处理每一条人——楼的权限关系
        listPeoPleData.forEach(peoPleData-> {
            Set<ResourceInfo> resourceInfos=addResource2(peoPleData);
            boolean flag=true;
            boolean isNew=false;
            A:for(ResourceInfo resourceInfo:resourceInfos){
                AuthSingleQueryDTO authSingleQueryDTO=new AuthSingleQueryDTO();
                authSingleQueryDTO.setPersonId(peoPleData.getCardid());
                authSingleQueryDTO.setResourceInfo(resourceInfo);
                log.info("权限确认入参" + authSingleQueryDTO);
                String respDeletePeople = hkService.getResponse(CommonConstant.HK_AUTH_QUERY, authSingleQueryDTO);
                log.info("权限确认返回值" + respDeletePeople);
                JSONObject jsonObject = JSONObject.parseObject(respDeletePeople);
                JSONObject data = jsonObject.getJSONObject("data");
                Integer personStatus=0;
                try {
                    personStatus = data.getInteger("personStatus");
                }catch (Exception e){
                }
                if(personStatus==null){
                    isNew=true;
                    flag=false;
                    break A;
                }
                if(personStatus!=3){
                    flag=false;
                    break A;
                }
            }
            if(flag){
                commitResult(peoPleData.getId(),"2");
                log.info("下发指令成功:personId=" +peoPleData.getId());
            }else {
                commitResult(peoPleData.getId(),"3");
                if(isNew){
                    AuthDelete authDelete = new AuthDelete();
                    List<PersonDatas> personDatasList = new ArrayList<>();
                    List<String> personDeleteIndexs = new ArrayList<>();
                    personDeleteIndexs.add(peoPleData.getCardid());
                    PersonDatas personDatas=new PersonDatas();
                    personDatas.setIndexCodes(personDeleteIndexs);
                    personDatasList.add(personDatas);
                    authDelete.setPersonDatas(personDatasList);
                    log.info("重新下发权限" +peoPleData.getCardid());
                    log.info("权限删除入参:" + authDelete);
                    String response = hkService.getResponse(CommonConstant.HK_AUTH_DELETE, authDelete);
                    log.info("权限删除返回值" + response);
                    //3.权限下发信息
                    auth(peoPleData);
                }
                log.info("下发指令失败:personId=" +peoPleData.getId());
            }
        });
    }

    @Override
    public void tempAuth(String personId, String areaid, String unitno) {
        addCard(personId);
        List<Door> listAuthDoor = doorMapper.getAuthDoor(areaid, unitno);
        List<Door> listAuthDoor2 = doorMapper.getAuthDoor(areaid, "public1");
        listAuthDoor.addAll(listAuthDoor2);
        Set<ResourceInfo> resourceInfos=new HashSet<>(1);
        addResource(listAuthDoor,resourceInfos);
        //权限下发do
        AuthAdd authAdd = new AuthAdd();
        authAdd.setStartTime("2018-09-03T17:30:08.000+08:00");
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,3);
        Date endDate= cal.getTime();
        String s = convertTime(endDate);
        authAdd.setEndTime(s);
        List<PersonDatas> personAuthList = new ArrayList<>();
        PersonDatas personDatas = new PersonDatas();
        List<String> personIndexs = new ArrayList<>();
        personIndexs.add(personId);
        personDatas.setIndexCodes(personIndexs);
        personAuthList.add(personDatas);
        authAdd.setPersonDatas(personAuthList);
        authAdd.setResourceInfos(resourceInfos);
        log.info("权限下发入参：" + JSON.toJSONString(authAdd).toString());
        //添加权限配置
        String response = hkService.getResponse(CommonConstant.HK_AUTH_ADD, authAdd);
        log.info("权限下发返回值" + response);
        //startTask();

    }


    void addCard(String personId){
        if(!existCard(personId)){
            //下发卡信息
            CardInfo cardInfo = new CardInfo();
            cardInfo.setPersonId(personId);
            cardInfo.setCardNo(personId);
            List<CardInfo> cardInfos = new ArrayList<>();
            cardInfos.add(cardInfo);
            CardsBind cardsBind = new CardsBind();
            cardsBind.setStartDate("2018-10-30");
            cardsBind.setEndDate("2035-10-30");
            cardsBind.setCardList(cardInfos);
            log.info("批量开卡入参:" + cardsBind);
            String responseCardsBind = hkService.getResponse(CommonConstant.HK_CARD_BINDS, cardsBind);
            log.info("批量开卡" + responseCardsBind);
        }
    }

    /**
     * 创建任务
     * 开始任务
     *
     * @return 成功的设备号
     */
    @Override
    public String startTask(){
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
        log.info("开始下载任务返回 " +taskId+" : "+ response);
        if(!response.contains("success")){
            response = hkService.getResponse(CommonConstant.HK_TASK_DELETE, taskStartMap);
            log.info("删除任务 " +taskId+" : "+ response);
        }


        return taskId;
    }

    List<String> doTask() {
        String taskId=startTask();
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


    void commitResult(String personId,String tag){
        ResultCommit resultCommit = new ResultCommit();
        resultCommit.setId(personId);
        resultCommit.setSynctag(tag);
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", token);
        ResultBase resultBase = restTemplate.exchange(commitResult, HttpMethod.POST, new HttpEntity<>(resultCommit, headers), ResultBase.class).getBody();
        log.info("门禁校验结果提交" + resultBase.toString());
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
                    commitResult(v.getId(),"2");
                }

            });
        }
    }

    void addResource(List<Door> listAuthDoor,Set<ResourceInfo> resourceInfos){
        listAuthDoor.forEach(v->{
            String doorId = v.getId();
            String resourceType = v.getResourceType();
            String cannelNo = v.getCannelNo();
            String[] cannelNoArray = cannelNo.split(",");
            ResourceInfo resourceInfo=new ResourceInfo();
            resourceInfo.setResourceType(resourceType);
            resourceInfo.setResourceIndexCode(doorId);
            List<Integer> channelNos=new ArrayList<>(1);
            for(String cannel:cannelNoArray){
                channelNos.add(Integer.parseInt(cannel));
                resourceInfo.setChannelNos(channelNos);
            }
            resourceInfos.add(resourceInfo);
        });
    }
    void addResource2(List<Door> listAuthDoor,Set<ResourceInfo> resourceInfos){
        listAuthDoor.forEach(v->{
            String doorId = v.getId();
            String resourceType = v.getResourceType();
            String cannelNo = v.getCannelNo();
            String[] cannelNoArray = cannelNo.split(",");
            ResourceInfo resourceInfo=new ResourceInfo();
            resourceInfo.setResourceType(resourceType);
            resourceInfo.setResourceIndexCode(doorId);
            for(String cannel:cannelNoArray){
                List<Integer> channelNos=new ArrayList<>(1);
                channelNos.add(Integer.parseInt(cannel));
                resourceInfo.setChannelNos(channelNos);
                resourceInfos.add(resourceInfo);
            }
        });
    }
    Set<ResourceInfo> addResource(PeoPleData peoPleData){
        String permission = peoPleData.getPermission();
        String carUnit=existCar(peoPleData);
        Set<ResourceInfo> resourceInfos=new HashSet<>(1);
        try {
            JSONArray ja=JSONArray.parseArray(permission);
            if(ja==null){
                return resourceInfos;
            }
            for(int i=0;i<ja.size();i++){
                JSONObject jsonObject = ja.getJSONObject(i);
                String areaid = jsonObject.getString("areaid");
                String unitno = jsonObject.getString("unitno");
                List<Door> listAuthDoor = doorMapper.getAuthDoor(areaid, unitno);
                List<Door> listAuthDoor2 = doorMapper.getAuthDoor(areaid, carUnit);
                listAuthDoor.addAll(listAuthDoor2);
                addResource(listAuthDoor,resourceInfos);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return resourceInfos;
    }


    Set<ResourceInfo> addResource2(PeoPleData peoPleData){
        String permission = peoPleData.getPermission();
        String carUnit=existCar(peoPleData);
        Set<ResourceInfo> resourceInfos=new HashSet<>(1);
        try {
            JSONArray ja=JSONArray.parseArray(permission);
            if(ja==null){
                return resourceInfos;
            }
            for(int i=0;i<ja.size();i++){
                JSONObject jsonObject = ja.getJSONObject(i);
                String areaid = jsonObject.getString("areaid");
                String unitno = jsonObject.getString("unitno");
                List<Door> listAuthDoor = doorMapper.getAuthDoor(areaid, unitno);
                List<Door> listAuthDoor2 = doorMapper.getAuthDoor(areaid, carUnit);
                listAuthDoor.addAll(listAuthDoor2);
                addResource2(listAuthDoor,resourceInfos);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

        return resourceInfos;
    }
    /**
     * 权限下发
     * @param peoPleData 权限
     */
    private void auth(PeoPleData peoPleData) {
        String personId=peoPleData.getCardid();
        //权限下发do
        AuthAdd authAdd = new AuthAdd();
        authAdd.setStartTime("2018-09-03T17:30:08.000+08:00");
        authAdd.setEndTime("2035-09-06T17:30:08.000+08:00");
        List<PersonDatas> personAuthList = new ArrayList<>();
        PersonDatas personDatas = new PersonDatas();
        List<String> personIndexs = new ArrayList<>();
        personIndexs.add(personId);
        personDatas.setIndexCodes(personIndexs);
        personAuthList.add(personDatas);
        authAdd.setPersonDatas(personAuthList);
        Set<ResourceInfo> resourceInfos=addResource(peoPleData);
        if(resourceInfos.size()>0){
            authAdd.setResourceInfos(resourceInfos);
            log.info("权限下发入参：" + JSON.toJSONString(authAdd).toString());
            //添加权限配置
            String response = hkService.getResponse(CommonConstant.HK_AUTH_ADD, authAdd);
            log.info("权限下发返回值" + response);
        }else {
            log.info("权限为空，不下发");
        }
    }




    boolean existCard(String cardNo){
        Map<String,String> param=new HashMap<>(1);
        param.put("cardNo",cardNo);
        log.info("卡片信息入参:" + param);
        String response = hkService.getResponse(CommonConstant.HK_CARD_INFO, param);
        log.info("卡片信息:" + response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");
        if(data==null){
            return false;
        }else {
            return true;
        }
    }


    String existCar(PeoPleData peoPleData){
        String carinfo = peoPleData.getCarinfo();
        //无车
        if(StringUtils.isEmpty(carinfo)){
            return "public1";
            //todo 后续无车通道装了以后改成public2
        //有车
        }else {
            return "public1";
        }
    }

    public static String convertTime(Date date){
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf3=new SimpleDateFormat("HH:mm:ss");
        return sdf2.format(date)+"T"+sdf3.format(date)+"+08:00";
    }
}
