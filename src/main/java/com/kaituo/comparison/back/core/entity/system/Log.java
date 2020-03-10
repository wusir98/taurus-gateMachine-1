package com.kaituo.comparison.back.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("T_LOG")
public class Log {
    @TableId(type = IdType.UUID)
    private String id;
    @TableField("interface")
    private String uri;
    private String param;
    private String result;
    private Date insertTime;
    @TableField("organization_Id")
    private Integer organizationId;
    private String msg;
}
