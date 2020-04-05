package com.kaituo.comparison.back;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaituo.comparison.back.core.constant.CommonConstant;
import com.kaituo.comparison.back.core.dto.hksdk.AuthDelete;
import com.kaituo.comparison.back.core.dto.hksdk.ResourceInfo;
import com.kaituo.comparison.back.core.entity.event.Door;
import com.kaituo.comparison.back.core.mapper.event.DoorMapper;
import com.kaituo.comparison.back.core.service.hksdk.HkAuthService;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class DelAuth {

    @Autowired
    DoorMapper doorMapper;
    @Autowired
    HkService hkService;
    @Autowired
    HkAuthService hkAuthService;
    @org.junit.Test
    public void test(){
        List<Door> doors = doorMapper.selectList(new QueryWrapper<Door>().eq("resource_type", "acsDevice"));
        AuthDelete authDelete = new AuthDelete();
        List<ResourceInfo> resourceInfos=new ArrayList<>(1);
        for(Door door:doors){
            ResourceInfo resourceInfo=new ResourceInfo();
            List<Integer> channelNos = new ArrayList<>();
            channelNos.add(1);
            resourceInfo.setChannelNos(channelNos);
            resourceInfo.setResourceIndexCode(door.getId());
            resourceInfo.setResourceType("acsDevice");
            resourceInfos.add(resourceInfo);
        }
        authDelete.setResourceInfos(resourceInfos);
        log.info("权限删除入参:" + authDelete);
        String response = hkService.getResponse(CommonConstant.HK_AUTH_DELETE, authDelete);
        log.info("权限删除返回值" + response);
/*        //4.人——权限遍历完，一起提交
        hkAuthService.startTask();*/
    }
}
