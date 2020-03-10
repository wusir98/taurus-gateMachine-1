package com.kaituo.comparison.back.common.exception;


import com.kaituo.comparison.back.common.bean.ResponseCode;
import lombok.*;

import java.io.Serializable;

/**
 *
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestException extends RuntimeException implements Serializable {
    private String code;
    private String msg;
    private Exception e;

    public RequestException(ResponseCode statusEnum, Exception e) {
        this.code = statusEnum.code;
        this.msg = statusEnum.msg;
        this.e = e;
    }


    public RequestException(ResponseCode statusEnum) {
        this.code = statusEnum.code;
        this.msg = statusEnum.msg;
    }

    public synchronized static RequestException fail(String msg){
        return RequestException.builder()
                .code(ResponseCode.OTHER_ERROR.code)
                .msg(msg)
                .build();
    }

    public synchronized static RequestException fail(String msg,Exception e){
        return RequestException.builder()
                .code(ResponseCode.OTHER_ERROR.code)
                .msg(msg)
                .e(e)
                .build();
    }

    public synchronized static RequestException fail(String code,String msg,Exception e){
        return RequestException.builder()
                .code(code)
                .msg(msg)
                .e(e)
                .build();
    }




}