package com.kaituo.comparison.back;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaituo.comparison.back.common.bean.ResponseResult;
import com.kaituo.comparison.back.core.dto.event.EventQueryDTO;
import com.kaituo.comparison.back.core.dto.event.QueryDTO;
import com.kaituo.comparison.back.core.entity.event.Task;
import com.kaituo.comparison.back.core.mapper.event.EventMapper;
import com.kaituo.comparison.back.core.mapper.event.TaskMapper;
import com.kaituo.comparison.back.core.schedule.EventSchedule;
import com.kaituo.comparison.back.core.service.event.EventService;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
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
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EventTest {
    @Autowired
    EventSchedule eventSchedule;
    @org.junit.Test
    //@Scheduled(fixedDelay= 1*1000*60)
    public void schedulePeople() {
        eventSchedule.schedulePeople();
    }
}