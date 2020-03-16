package com.kaituo.comparison.back.core.entity.event;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("T_EVENT")
public class Event implements Serializable {
    @TableId
    private String eventId;

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

}
