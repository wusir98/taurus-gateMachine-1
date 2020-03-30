package com.kaituo.comparison.back.core.service.event.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaituo.comparison.back.common.bean.ResponseResult;
import com.kaituo.comparison.back.core.dto.event.PersonQueryDTO;
import com.kaituo.comparison.back.core.dto.event.QueryDTO;
import com.kaituo.comparison.back.core.entity.event.Door;
import com.kaituo.comparison.back.core.entity.event.Event;
import com.kaituo.comparison.back.core.entity.event.Task;
import com.kaituo.comparison.back.core.mapper.event.DoorMapper;
import com.kaituo.comparison.back.core.mapper.event.EventMapper;
import com.kaituo.comparison.back.core.mapper.event.TaskMapper;
import com.kaituo.comparison.back.core.service.event.EventService;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    @Autowired
    HkService hkService;
    @Autowired
    EventMapper eventMapper;
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    DoorMapper doorMapper;

    @Autowired
    RestTemplate restTemplate;


    String getCardId(String personId){
        PersonQueryDTO personQueryDTO=new PersonQueryDTO();
        personQueryDTO.setPersonId(personId);
       // QueryDTO query=new QueryDTO();
        //query.setUri("/api/resource/v1/person/personId/personInfo");
        //query.setParam(JSONObject.parseObject(JSONObject.toJSONString(personQueryDTO)));
        //HttpHeaders header = new HttpHeaders();
        // 需求需要传参为form-data格式
        //header.add("token",token);
        //HttpEntity<QueryDTO> httpEntity = new HttpEntity<QueryDTO>(query, header);
        try {
            //ResponseResult<Map> result  = restTemplate.postForObject("http://"+host+"/hk/api", httpEntity, ResponseResult.class);
            String response = hkService.getResponse("/api/resource/v1/person/personId/personInfo", personQueryDTO);
            ResponseResult result = JSONObject.parseObject(response, ResponseResult.class);
            Object data1 = result.getData();
            JSONObject data=JSONObject.parseObject(JSON.toJSONString(data1));
            if(data!=null){
                return data.getString("certificateNo");
            }else {
                return "";
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return "";
        }
    }
    @Override
    public void handle(JSONArray list) {
        for (int i = 0; i < list.size(); i++) {
            Event event = list.getObject(i, Event.class);
            Event event1 = eventMapper.selectById(event.getEventId());
            if(event1==null){
                String personId = event.getPersonId();
                String doorId = event.getDoorIndexCode();
                Door door = doorMapper.selectById(doorId);
                if(door!=null){
                    Door pDoor = doorMapper.getPidById(doorId);
                    String areaid = pDoor.getAreaid();
                    if("32021405203103".equals(areaid)){
                        event.setSourceSubdsitrictid("abc123");
                    }else {
                        event.setSourceSubdsitrictid(areaid);
                    }
                    event.setInOut(door.getLx());
                    event.setDoorId(pDoor.getId());
                }
                event.setIdCard(personId);
                event.setSourceCommunityId(event.getEventId());
                eventMapper.insert(event);
            }
        }
        Date date = eventMapper.countMaxEventTime();
        if(date!=null){
            Task task=new Task();
            task.setId(1);
            task.setLastTime(date);
            taskMapper.updateById(task);

        }
    }
}
