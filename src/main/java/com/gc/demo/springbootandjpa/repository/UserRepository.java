package com.gc.demo.springbootandjpa.repository;

import com.gc.demo.springbootandjpa.entity.User;
import com.gc.demo.springbootandjpa.entity.UserResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/10/18
 */
@Repository
public interface UserRepository extends BaseRepository<User, Integer> {

    /**
     * 分页查询
     * 注意：在最后一个参数加上Pageable即可，返回类型为Page。
     * Pageable为分页参数，可以通过PageRequest.of(0,10)方法指定分页参数，page：页码（0开始）。size：每页数量。
     * 类似的参数还有Sort，但两个不能同时存在。Pageable中可以设置排序规则。
     *
     * @param name
     * @param pageable
     * @return
     */
    @Query("select u from User u where u.name like concat('%',?1,'%')")
    Page<User> searchUser(String name, Pageable pageable);


    /**
     * 如果想根据自定义类返回结果可以用这种方式。
     * 注意:自定义的实体类一定要一个带参数的构造方法，参数的顺序要和构造函数中变量的顺序数量
     * 都要一致，否则程序无法启动即报错。
     * 实体类一定要包含完整的包名，否则无法找到该类。
     * 还有一种方式返回自定义数据，方法返回值改成List<Object[]>，查询语句列出要查询的字段即可。
     * 这样可以避免返回值与实体不匹配的错误，但需要自己再对数据进行一次处理。
     *
     * @return
     */
    @Query("select new com.gc.demo.springbootandjpa.entity.UserResult" +
            "(u.id,u.name,u.birth,u.gender,u.remark,ua.token)" +
            "from User u " +
            "left join UserAuth ua on ua.userId=u.id")
    List<UserResult> getUserInfo();

    /**
     * 支持update语句。
     * 注意：要加 @Modifying注解和@Transactional注解。如果外层调用的方法写了@Transactional这里可以不写。
     *
     * @param id
     * @param name
     * @return
     */
    @Transactional
    @Modifying
    @Query("update User u set u.name=?2 where u.id=?1")
    int updateName(Long id, String name);
}
