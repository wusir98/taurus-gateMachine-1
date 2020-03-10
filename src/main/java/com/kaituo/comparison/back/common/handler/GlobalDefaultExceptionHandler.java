package com.kaituo.comparison.back.common.handler;


import com.kaituo.comparison.back.common.bean.ResponseCode;
import com.kaituo.comparison.back.common.bean.ResponseResult;
import com.kaituo.comparison.back.common.exception.RequestException;
import lombok.extern.log4j.Log4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 *
 *
 */
@ControllerAdvice(basePackages = {"com.kaituo.comparison.back"})
@Log4j
public class GlobalDefaultExceptionHandler {
    @ExceptionHandler(value = RequestException.class)
    @ResponseBody
    public ResponseResult requestExceptionHandler(RequestException e) {
        if(e.getE()!=null) e.printStackTrace();
        return ResponseResult.builder().msg(e.getMsg()).code(e.getCode()).build();
    }


    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseResult requestExceptionHandler(DataIntegrityViolationException e) {
        e.printStackTrace();
        return ResponseResult.builder().msg("数据操作格式异常").code(ResponseCode.OTHER_ERROR.code).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        e.printStackTrace();
        BindingResult result = e.getBindingResult();
        String s = "参数验证失败";
        if(result.hasErrors()){
            List<ObjectError> errors = result.getAllErrors();
            s = errors.get(0).getDefaultMessage();
        }
        return ResponseResult.builder().code(ResponseCode.OTHER_ERROR.code).msg(s).build();
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult requestExceptionHandler(Exception e) {
        e.printStackTrace();
        return ResponseResult.builder().msg("服务器飘了，管理员去拿刀修理了~").code(ResponseCode.OTHER_ERROR.code).build();
    }






}
