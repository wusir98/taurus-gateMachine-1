package com.kaituo.comparison.back.core.entity.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_organization")
public class Organization {
    @TableId("organization_Id")
    private int organizationId;
    private String organization;
    private String token;
}
