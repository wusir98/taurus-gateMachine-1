package com.kaituo.comparison.back.common.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(value = "请求结果响应体")
public class ResponseResult2<T> implements Serializable {


    @ApiModelProperty(value = "响应状态回执码")
    private String code;

    @ApiModelProperty(value = "数据体")
    private T data;

    @ApiModelProperty(value = "响应回执消息")
    private String msg;
    public synchronized static <T> ResponseResult2<T> e(ResponseCode statusEnum) {

        return e(statusEnum,null);
    }

    public synchronized static <T> ResponseResult2<T> e(ResponseCode statusEnum, T data) {
        ResponseResult2<T> res = new ResponseResult2<>();
        res.setCode(statusEnum.code);
        res.setMsg(statusEnum.msg);
        res.setData(data);
        return res;
    }

    private static final long serialVersionUID = 8992436576262574064L;
}
