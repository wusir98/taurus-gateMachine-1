package com.kaituo.comparison.back.core.entity.event;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("T_DOOR")
public class Door implements Serializable {
    @TableId
    private String id;
    private String name;
    private String parentId;
    private String regionId;
    private String lx;
}
