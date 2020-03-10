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

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    LogMapper logMapper;
    @Autowired
    OrganizationMapper organizationMapper;

    @Override
    public Log addLog(Param param, String token) {
        Log log=new Log();
        log.setParam(param.getParam().toString());
        log.setUri(param.getUri());
        Organization organization = organizationMapper.selectOne(
                new QueryWrapper<Organization>().eq("token", token));
        System.out.println(organization);
        log.setOrganizationId(organization.getOrganizationId());
        System.out.println(log);
        logMapper.insert(log);
        return log;
    }

    @Override
    public void update(Log log) {
        logMapper.updateById(log);
    }
}
