package com.webbora.controller;

import com.webbora.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: Jupiter.Lin
 * @version: V1.0
 * @date: 2021年6月3日 下午5:12:09
 */
@ControllerAdvice //1.全局异常处理 2.全局数据绑定 3.全局数据预处理
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorCode", "400");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BizException ex) {
        ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(IOException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorCode", "500");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(cn.dev33.satoken.exception.NotLoginException.class)
    public String handleNotLoginException(cn.dev33.satoken.exception.NotLoginException ex) {
        return "login";
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorCode", "500");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @RequiredArgsConstructor
    private class ErrorResponse {
        private final String errorCode;
        private final String message;
    }
}
