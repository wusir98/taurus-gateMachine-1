package com.kaituo.comparison.back;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaituo.comparison.back.core.constant.CommonConstant;
import com.kaituo.comparison.back.core.dto.app.PeoPleData;
import com.kaituo.comparison.back.core.dto.app.ResultRegister;
import com.kaituo.comparison.back.core.entity.event.Door;
import com.kaituo.comparison.back.core.mapper.event.DoorMapper;
import com.kaituo.comparison.back.core.service.hksdk.HkAuthService;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class DoorInsert {
    @Autowired
    HkService hkService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    HkAuthService hkAuthService;
    @Autowired
    DoorMapper doorMapper;


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
    public void updateDoor() {
        Map<String,Object> param=new HashMap<>();
        param.put("pageNo",1);
        param.put("pageSize",1000);
        param.put("resourceType","acsDevice");
        String response = hkService.getResponse(CommonConstant.HK_RESOURCE_LIST, param);
        JSONObject result=JSONObject.parseObject(response);
        JSONObject data = result.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for(int i=0;i<list.size();i++){
            System.out.println("-----------------------");
            HkAcsDevice hkAcsDevice = list.getObject(i, HkAcsDevice.class);
            handleAcsDevice(hkAcsDevice);
        }


    }

    void handleAcsDevice(HkAcsDevice hkAcsDevice){
        Door door=new Door();
        door.setId(hkAcsDevice.getIndexCode());
        door.setResourceType(hkAcsDevice.getResourceType());
        String name = hkAcsDevice.getName();
        String[] split = name.split("-");
        String areaid = split[0];
        String name2=split[1];
        door.setName(name2);
        door.setDoorname(name2);
        String[] split2 = name2.split("_");
        String parentName=split2[0];
        Door parentDoor = doorMapper.selectOne(new QueryWrapper<Door>()
                .eq("areaId", areaid)
                .eq("doorName", parentName));
        String parentId = parentDoor.getId();
        door.setParentId(parentId);
        String cannelNo=handleDoor(hkAcsDevice.getIndexCode());
        door.setCannelNo(cannelNo);
        System.out.println("acsDevice:"+door.toString());
        try{
            doorMapper.insert(door);
        }catch (Exception e){
            System.out.println("插入失败");
        }
    }

    String handleDoor(String acsDeviceIndex){
        List<String> cannelList=new ArrayList<>();
        Map<String,Object> param=new HashMap<>();
        param.put("pageNo",1);
        param.put("pageSize",1000);
        param.put("acsDevIndexCode",acsDeviceIndex);
        String response = hkService.getResponse(CommonConstant.HK_DOOR_LIST, param);
        JSONObject result=JSONObject.parseObject(response);
        JSONObject data = result.getJSONObject("data");
        JSONArray list = data.getJSONArray("list");
        for(int i=0;i<list.size();i++){
            HkDoor hkdoor = list.getObject(i, HkDoor.class);
            String channelNo = hkdoor.getChannelNo();
            Door door=new Door();
            door.setId(hkdoor.getDoorIndexCode());
            door.setResourceType("door");
            String name = hkdoor.getDoorName();
            String[] split = name.split("-");
            String areaid = split[0];
            String name2=split[1];
            door.setName(name2);
            door.setDoorname(name2);
            door.setParentId(acsDeviceIndex);
            cannelList.add(channelNo);
            door.setCannel(channelNo);
            door.setLx("1");
            System.out.println("door:"+door.toString());
            try{
                doorMapper.insert(door);
            }catch (Exception e){
                System.out.println("插入失败");
            }
        }
        String join = StringUtils.join(cannelList, ",");
        return join;
    }
}



@Data
class HkAcsDevice{
    private String indexCode;
    private String name;
    private String resourceType;
    private String devCode;
    private String devTypeCode;
    private String devTypeDesc;
    private String ip;
    private String port;
    private String description;
    private String regionIndexCode;
    private String treatyType;
    private String capabilitySet;
    private int cardCapacity;
    private String fingerCapacity;
    private String veinCapacity;
    private String faceCapacity;
    private int doorCapacity;
    private String parentIndexCode;
    private Date createTime;
    private Date updateTime;
}

@Data
class HkDoor{
    private String doorIndexCode;
    private String doorName;
    private int doorNo;
    private String acsDevIndexCode;
    private String regionIndexCode;
    private String channelType;
    private String channelNo;
    private Date createTime;
    private Date updateTime;
}