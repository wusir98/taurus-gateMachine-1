package com.kaituo.comparison.back.core.entity.event;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("T_TASK")
public class Task implements Serializable {
    @TableId
    private int id;
    @TableField("last_time")
    private Date lastTime;
}
