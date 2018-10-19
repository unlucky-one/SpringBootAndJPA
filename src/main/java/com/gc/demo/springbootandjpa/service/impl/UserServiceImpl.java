package com.gc.demo.springbootandjpa.service.impl;

import com.gc.demo.springbootandjpa.repository.UserRepository;
import com.gc.demo.springbootandjpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
