package com.gc.demo.springbootandjpa.controller;

import com.gc.demo.springbootandjpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/10/18
 */
@RestController
@RequestMapping
public class TestController {

    @Autowired
    UserService userService;

    Object test() {
        userService.deleteWithStatus(1);
        return null;
    }
}
