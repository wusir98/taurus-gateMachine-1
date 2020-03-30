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
    private String lx;
    private String areaid;
    private String unitno;
    private String doorname;
    private String resourceType;

    private String cannelNo;
    private String cannel;
    private int flag;
}
