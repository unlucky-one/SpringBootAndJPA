package com.gc.demo.springbootandjpa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gc.demo.springbootandjpa.Listener.MyHttpSessionListener;
import com.gc.demo.springbootandjpa.filter.MyFilter;
import com.gc.demo.springbootandjpa.interceptor.MyAppInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Configuration
//@EnableWebMvc + WebMvcConfigurerAdapter = WebMvcConfigurationSupport (不使用springboot的@EnableAutoConfiguration)
/**
 * extends WebMvcConfigurationSupport  不使用@EnableAutoConfiguration自动配置，因此配置需手动自定义添加
 */
public class MyAppConfig extends WebMvcConfigurationSupport {


    /**
     * @return DataSource
     * @brief Druid连接池
     */
//    @Bean
//    public DataSource getDataSource() {
//        DruidDataSource druidDataSource = new DruidDataSource();
//        druidDataSource.setUrl(dataSourceConfig.getUrl());
//        druidDataSource.setUsername(dataSourceConfig.getUsername());
//        druidDataSource.setPassword(dataSourceConfig.getPassword());
//        return druidDataSource;
//    }


    /**
     * @param registry
     * @brief 拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //忽略拦截的路径
        registry.addInterceptor(new MyAppInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/test/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/images/**")
                .excludePathPatterns("/test")
                .excludePathPatterns("/login");
    }

    /**
     * @param registry
     * @bref 自定义资源处理器配置-->静态资源放行（默认@EnableAutoConfiguration自动配置默认值）
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/templates/")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
    }

    /**
     * @return
     * @brief 配置页面跳转
     * 设置默认首页
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    /**
     * @return
     * @brief response响应自定义配置（一般@EnableAutoConfiguration自动配置）,此处中文乱码
     */
//    public HttpMessageConverter<String> responseBodyConverter() {
//        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
//        return converter;
//    }

    /**
     * @return
     * @brief jackson转json自定义配置（一般@EnableAutoConfiguration自动配置）
     */
//    @Bean //加入bean 防止后面会用到该自动以MappingJackson转换器
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter() {
            // 重写 writeInternal 方法，在返回内容前首先进行加密
            @Override
            protected void writeInternal(Object object,
                                         HttpOutputMessage outputMessage) throws IOException,
                    HttpMessageNotWritableException {
                // 使用 Jackson 的 ObjectMapper 将 Java 对象转换成 Json String
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(object);
                logger.error(json);
                // 输出
                outputMessage.getBody().write(json.getBytes());
            }
        };
    }

    /**
     * @return
     * @brief 配置转换器，json,response响应等配置的自定义（一般@EnableAutoConfiguration自动配置）
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        converters.add(mappingJackson2HttpMessageConverter());
    }

    /**
     * @return
     * @brief 解决Enableautoconfiguration引起的超前配置异常的问题
     */
//    @Override
//    protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
//    }

    /**
     * 忽略路径大小写
     *
     * @param configurer
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCaseSensitive(false);
        configurer.setPathMatcher(matcher);
    }


    /**
     * 跨域限制 Cross方案
     *
     * @param registry
     */
    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        super.addCorsMappings(registry);
        CorsRegistration corsRegistration = registry.addMapping("/**");
        corsRegistration.allowedOrigins("*").allowedMethods("*").allowCredentials(true).maxAge(3600);
    }

    /**
     * 跨域
     */
//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(0);
//        return bean;
//    }

    /**
     * 增加过滤器，用于收集所有请求
     */
    @Bean
    public FilterRegistrationBean registrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new MyFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    /**
     * 增加SessionListener用于记录在线人数
     */
    @Bean
    public ServletListenerRegistrationBean listenerRegist() {
        ServletListenerRegistrationBean srb = new ServletListenerRegistrationBean();
        srb.setListener(new MyHttpSessionListener());
        return srb;
    }

}
