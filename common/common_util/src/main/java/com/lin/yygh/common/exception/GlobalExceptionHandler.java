package com.lin.yygh.common.exception;

import com.lin.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result<?> error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }
}
