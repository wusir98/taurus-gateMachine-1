package com.kaituo.comparison.back.core.service.hksdk.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.kaituo.comparison.back.common.bean.ResponseResult2;
import com.kaituo.comparison.back.core.dto.hksdk.RestToken;
import com.kaituo.comparison.back.core.entity.system.Organization;
import com.kaituo.comparison.back.core.entity.system.OrganizationInterface;
import com.kaituo.comparison.back.core.mapper.system.LogMapper;
import com.kaituo.comparison.back.core.mapper.system.OrganizationInterfaceMapper;
import com.kaituo.comparison.back.core.mapper.system.OrganizationMapper;
import com.kaituo.comparison.back.core.service.hksdk.HkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HkServiceImpl implements HkService {
    @Value("${hk.host}")
    String host;
    @Value("${hk.appKey}")
    String appKey;
    @Value("${hk.appSecret}")
    String appSecret;
    @Autowired
    LogMapper logMapper;
    @Autowired
    OrganizationInterfaceMapper organizationInterfaceMapper;
    @Autowired
    OrganizationMapper organizationMapper;
    public  String getResponse(String uri, Object param){
        /**
         * STEP1：设置平台参数，根据实际情况,设置host appkey appsecret 三个参数.
         */
        ArtemisConfig.host = host; // artemis网关服务器ip端口
        ArtemisConfig.appKey = appKey;  // 秘钥appkey
        ArtemisConfig.appSecret = appSecret;// 秘钥appSecret

        /**
         * STEP2：设置OpenAPI接口的上下文
         */
        final String ARTEMIS_PATH = "/artemis";

        /**
         * STEP3：设置接口的URI地址
         */
        final String previewURLsApi = ARTEMIS_PATH + uri;
        Map<String, String> path = new HashMap<String, String>(2) {
            {
                put("https://", previewURLsApi);//根据现场环境部署确认是http还是https
            }
        };

        /**
         * STEP4：设置参数提交方式
         */
        String contentType = "application/json";

        /**
         * STEP5：组装请求参数
         */
        String body = JSONObject.toJSONString(param);


        /**
         * STEP6：调用接口
         */
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType , null);


        return result;
    }

    @Override
    public boolean isAllow(String uri,String token) {
        Organization organization = organizationMapper.selectOne(new QueryWrapper<Organization>().eq("token", token));
        int id = organization.getOrganizationId();
        int i = organizationInterfaceMapper.selectCount(new QueryWrapper<OrganizationInterface>()
                .eq("organization_Id", id)
                .eq("interface", uri));
        if(i>0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void updateToken(RestToken restToken) {
        Organization organization=new Organization();
        organization.setToken(restToken.getNewToken());

        organizationMapper.update(organization,new UpdateWrapper<Organization>().eq("token",restToken.getOldToken()));
    }
}
