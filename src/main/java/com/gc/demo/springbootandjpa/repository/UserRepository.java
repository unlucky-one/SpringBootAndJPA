package com.gc.demo.springbootandjpa.repository;

import com.gc.demo.springbootandjpa.entity.User;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/10/18
 */
@Repository
public interface UserRepository extends BaseRepository<User, Integer> {
}
