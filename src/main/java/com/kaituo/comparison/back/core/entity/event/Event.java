package com.kaituo.comparison.back.core.entity.event;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("SMART_COMMUNITY.DISTRICT_T_EVENT")
public class Event implements Serializable {

    @TableField(exist = false)
    private String eventId;
    @TableId("source_CommunityId")
    private String sourceCommunityId;

    private String eventName;

    private Date eventTime;

    private String personId;

    @TableField("cardno")
    private String cardNo;

    private String personName;
    @TableField("orgIndex_Code")
    private String orgIndexCode;

    private String orgName;

    private String doorName;

    private String doorIndexCode;

    private String doorRegionIndexCode;
    @TableField("picuri")
    private String picUri;

    private String svrIndexCode;

    private String eventType;

    private String inAndOutType;
    @TableField("idCard")
    private String idCard;

    private String inOut;

    private String doorId;

    @TableField("SOURCE_SUBDISTRICTID")
    private String sourceSubdsitrictid;
}
