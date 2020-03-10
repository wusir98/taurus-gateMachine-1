package com.kaituo.comparison.back.core.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Organization_Interface")
public class OrganizationInterface {
    @TableId("organization_Id")
    private int organizationId;
    @TableField("interface")
    private String  uri         ;
    @TableField("interface_name")
    private String  interfaceName         ;
}
