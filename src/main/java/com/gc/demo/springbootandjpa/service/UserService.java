package com.gc.demo.springbootandjpa.service;

import com.gc.demo.springbootandjpa.utils.MyAppResult;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/10/18
 */
public interface UserService extends BaseService {
    MyAppResult getCurrentUser();

    Long login(String username, String password);
}
