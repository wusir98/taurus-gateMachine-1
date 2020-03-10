package com.kaituo.comparison.back.core.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kaituo.comparison.back.core.dto.hksdk.Param;
import com.kaituo.comparison.back.core.entity.system.Log;
import com.kaituo.comparison.back.core.entity.system.Organization;
import com.kaituo.comparison.back.core.mapper.system.LogMapper;
import com.kaituo.comparison.back.core.mapper.system.OrganizationMapper;
import com.kaituo.comparison.back.core.service.system.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    LogMapper logMapper;
    @Autowired
    OrganizationMapper organizationMapper;

    @Override
    public String addLog(Param param, String token) {
        String id = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        Log log=new Log();
        log.setId(id);
        log.setParam(param.getParam().toString());
        log.setUri(param.getUri());
        Organization organization = organizationMapper.selectOne(
                new QueryWrapper<Organization>().eq("token", token));
        System.out.println(organization);
        log.setOrganizationId(organization.getOrganizationId());
        System.out.println(log);
        logMapper.insert(log);
        return id;
    }

    @Override
    public void update(String id,String result,String msg) {
        Log log=new Log();
        log.setId(id);
        log.setResult(result);
        log.setMsg(msg);
        logMapper.updateById(log);
    }
}
