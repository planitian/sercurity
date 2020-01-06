package com.example.sercurity.component;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

//全局异常捕捉
@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    private ResponseEntity handlerErrorInfo(HttpServletRequest request, Exception e) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("内部错误");

    }

}
