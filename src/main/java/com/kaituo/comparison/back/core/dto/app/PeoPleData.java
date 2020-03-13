package com.kaituo.comparison.back.core.dto.app;

import com.kaituo.comparison.back.core.dto.hksdk.ResourceInfo;
import lombok.Data;

import java.util.List;

/**
 * @Description:
 * @Author: yedong
 * @Date: 2020/3/12 9:44
 * @Modified by:
 */
@Data
public class PeoPleData {
    private String id;
    private String personid;
    private String truename;
    private String mobile;
    private String areaid;
    private String estate;
    private String unitno;
    private String houseno;
    private String roomno;
    private String cardid;
    private String gender;
    private String nation;
    private String cardaddress;
    private String carddatebegin;
    private String carddateend;
    private String cardauth;
    private String face;
    private String cardpic;
    private String status;
    private String remark;
    private String livetype;
    private String src;
    private String persondevicetag;
    private String synctag;
    private String syncmsg;
    private String createddate;
    private String lastmodifieddate;
    List<ResourceInfo> resourceInfos;
    /**
     * 人员绑定的设备resourceIndex集合
     */
    List<String> resourceIndexCodes;
}
