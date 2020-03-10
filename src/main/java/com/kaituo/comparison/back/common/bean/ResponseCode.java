package com.kaituo.comparison.back.common.bean;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
@NoArgsConstructor
@AllArgsConstructor
public enum ResponseCode {

    OK("0","OK"),
    OTHER_ERROR("0x00000001","other error"),
    LOGOUT_OK("0x00000000","no permission");

    public String code;

    public String msg;

/*    public static List<ResponseMessage> getArrayMessage(){
        ArrayList<ResponseMessage> responseMessages = new ArrayList<>();
        for (ResponseCode statusEnum : ResponseCode.values()) {
            responseMessages.add(new ResponseMessageBuilder()
                    .code(statusEnum.code)
                    .message(statusEnum.msg)
                    .build());
        }
        return responseMessages;
    }*/

}
