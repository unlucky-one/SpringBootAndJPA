package com.gc.demo.springbootandjpa.repository;

import com.gc.demo.springbootandjpa.entity.User;
import com.gc.demo.springbootandjpa.entity.UserAuth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/12/6
 */
@Repository
public interface UserAuthRepository extends BaseRepository<UserAuth, String> {

    UserAuth findTopByToken(String token);

    @Query("select user from User user where user.id =" +
            "(select ua.userId from UserAuth ua where ua.token=?1)")
    User getUser(String token);
}