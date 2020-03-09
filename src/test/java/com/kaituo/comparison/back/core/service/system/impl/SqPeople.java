package com.kaituo.comparison.back.core.service.system.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Mybatis Generator 2020/02/22
 */
@Data
public class SqPeople {

    /**
     * 疫情城市
     */
    private String sickcity;

    private Integer page = 1;

    private Integer pageSize = 10;


}