package com.gc.demo.springbootandjpa.advice;

import com.gc.demo.springbootandjpa.utils.MyAppResult;
import com.gc.demo.springbootandjpa.utils.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/12/4
 */
@Slf4j
@RestControllerAdvice
public class MyResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return Tools.getSuccessResult(null);
        } else if (body instanceof Map) {
            return body;
        } else if (body instanceof MyAppResult) {
            return Tools.getResult((MyAppResult) body);
        } else {
            return Tools.getSuccessResult(body);
        }
    }

    /*
    * 处理全局错误错误返回
    * */
//    @ExceptionHandler
//    @ResponseBody
//    public Object exceptionHandler(Exception e) {
//        log.error(e.toString());
//        return Tools.getFaildData(e.getMessage());
//    }
}
