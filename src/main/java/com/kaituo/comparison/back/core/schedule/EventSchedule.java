package com.kaituo.comparison.back.core.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaituo.comparison.back.common.bean.ResponseResult;
import com.kaituo.comparison.back.core.dto.event.EventQueryDTO;
import com.kaituo.comparison.back.core.dto.event.QueryDTO;
import com.kaituo.comparison.back.core.entity.event.Task;
import com.kaituo.comparison.back.core.mapper.event.EventMapper;
import com.kaituo.comparison.back.core.mapper.event.TaskMapper;
import com.kaituo.comparison.back.core.service.event.EventService;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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
public class EventSchedule {
    @Autowired
    HkService hkService;
    @Autowired
    EventMapper eventMapper;
    @Autowired
    TaskMapper taskMapper;

    @Autowired
    EventService eventService;
    @Autowired
    RestTemplate restTemplate;

    String convertTime(Date date){
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf3=new SimpleDateFormat("HH:mm:ss");
        return sdf2.format(date)+"T"+sdf3.format(date)+"+08:00";
    }

    //@Scheduled(fixedDelay= 1*1000*60)
    public void schedulePeople() {
        log.info("开始任务");
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd");
      SimpleDateFormat sdf3=new SimpleDateFormat("HH:mm:ss-dd");
      Task task = taskMapper.selectById(1);
      Date newestTime =new Date();
      Date lastTime = task.getLastTime();
      if(lastTime==null){
          try {
              lastTime=sdf.parse("2020-03-13");
          } catch (ParseException e) {
          }
      }
      String startTime=convertTime(lastTime);
      String endTime=convertTime(newestTime);
      int pageNo=1;
      int pageSize=1000;
      int total=0;
      int totalPage=0;
      EventQueryDTO eventQueryDTO = new EventQueryDTO();
      eventQueryDTO.setStartTime(startTime);
      eventQueryDTO.setEndTime(endTime);
      eventQueryDTO.setPageNo(pageNo);
      eventQueryDTO.setPageSize(pageSize);
      while(true){
          eventQueryDTO.setPageNo(pageNo);
          //QueryDTO query=new QueryDTO();
          //query.setUri("/api/acs/v1/door/events");
         // query.setParam(JSONObject.parseObject(JSONObject.toJSONString(eventQueryDTO)));
          //HttpEntity<QueryDTO> httpEntity = new HttpEntity<QueryDTO>(query, header);
          //ResponseResult<Map> result  = restTemplate.postForObject("http://"+host+"/hk/api", httpEntity, ResponseResult.class);
          String response = hkService.getResponse("/api/acs/v1/door/events", eventQueryDTO);
          ResponseResult result = JSONObject.parseObject(response, ResponseResult.class);
          Object data1 = result.getData();
          JSONObject data=JSONObject.parseObject(JSON.toJSONString(data1));
          if(data!=null){
              total=data.getInteger("total");
              totalPage=data.getInteger("totalPage");
              JSONArray list = data.getJSONArray("list");
              try {
                  eventService.handle(list);
              }catch (Exception e){
                  log.error(e.getMessage(),e);
              }
              if(pageNo>=totalPage){
                  log.info("任务结束");
                  return;
              }
              pageNo++;
          }else {
              log.info("任务结束");
              return;
          }
      }
  }
}