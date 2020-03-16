package com.kaituo.comparison.back.core.entity.event;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("T_REGION")
public class Region implements Serializable {
    @TableId
    private String id;
    private String name;
}
