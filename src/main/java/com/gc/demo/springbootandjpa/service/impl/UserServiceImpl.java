package com.gc.demo.springbootandjpa.service.impl;

import com.gc.demo.springbootandjpa.entity.User;
import com.gc.demo.springbootandjpa.entity.UserAuth;
import com.gc.demo.springbootandjpa.entity.UserIdentify;
import com.gc.demo.springbootandjpa.entity.UserIdentifyFK;
import com.gc.demo.springbootandjpa.repository.UserAuthRepository;
import com.gc.demo.springbootandjpa.repository.UserIdentifyRepository;
import com.gc.demo.springbootandjpa.repository.UserRepository;
import com.gc.demo.springbootandjpa.service.UserService;
import com.gc.demo.springbootandjpa.utils.MyAppResult;
import com.gc.demo.springbootandjpa.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/10/18
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<UserRepository> implements UserService {
    @Autowired
    void setRepository(UserRepository repository) {
        super.repository = repository;
    }

    @Autowired
    UserIdentifyRepository userIdentifyRepository;
    @Autowired
    UserAuthRepository userAuthRepository;

    @Override
    public Long login(String username, String password) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //查询用户是否存在
        Optional<UserIdentifyFK> optional = userIdentifyRepository.findTopByUserNameAndPassword(username, password);
        if (!optional.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        //生成token并存入数据库
        String uuid=UUID.randomUUID().toString();
        UserAuth ua=new UserAuth();
        ua.setToken(uuid);
        ua.setUserId(optional.get().getId());
        userAuthRepository.save(ua);
        //token写入客户端cookie，用于下次免登陆
        Cookie cookie=new Cookie("token",uuid);
        servletRequestAttributes.getResponse().addCookie(cookie);
        return optional.get().getId();
    }

    @Override
    public MyAppResult getCurrentUser() {
        //通过RequestContextHolder获取当前request，response对象
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String token = "";
        //获取cookie中token
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    continue;
                }
            }
        }
        if (StringUtils.isEmpty(token))
            return Tools.getFaildData("未登录");
        User user = userAuthRepository.getUser(token);
        return Tools.getSuccessData(user);
    }


}
