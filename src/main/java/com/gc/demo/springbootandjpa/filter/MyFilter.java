package com.gc.demo.springbootandjpa.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.*;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/11/16
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestFacade requestFacade = (RequestFacade) request;
        String path = requestFacade.getRequestURI().toLowerCase();
        //过滤器，所有请求都会经过
        log.info(path);
        chain.doFilter(request, response);
    }


    @Override
    public void destroy() {

    }
}
