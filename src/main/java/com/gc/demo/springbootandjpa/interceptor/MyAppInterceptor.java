package com.gc.demo.springbootandjpa.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.demo.springbootandjpa.service.UserService;
import com.gc.demo.springbootandjpa.utils.MyAppResult;
import com.gc.demo.springbootandjpa.utils.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class MyAppInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //用于支持options请求
        if(request.getMethod().toLowerCase().equals("options"))
            return true;
        BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        userService = factory.getBean(UserService.class);
        MyAppResult res = userService.getCurrentUser();
        //判断是否已登录
        if (res != null) {
            if (res.getStatus() == Tools.SUCCESSFUL) {
                return true;
            }
        }
        if (request.getRequestURI().equals("/") || request.getRequestURI().endsWith(".html")) {
            //重定向至登录页面
            response.sendRedirect("/login");
        } else {
            ObjectMapper objectMapper=new ObjectMapper();
            returnJson(response,  objectMapper.writeValueAsString(res));
        }
        return false;
    }


    private void returnJson(HttpServletResponse response, String json) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(400);
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
            log.error("response error", e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }
}
