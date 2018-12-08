package com.gc.demo.springbootandjpa.repository;

import com.gc.demo.springbootandjpa.entity.UserIdentify;
import com.gc.demo.springbootandjpa.entity.UserIdentifyFK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/12/6
 */
@Repository
public interface UserIdentifyRepository extends BaseRepository<UserIdentifyFK, Long> {

    /**
     * 第一种，JPA 方法名自动转sql方式查询。
     * 优点：不用写Sql，支持简单条件 and、or、默认等于、大于、小于、like、between、OrderBy等等。
     * 缺点：比较受限制，条件多了方法名会很长，适合简单查询。
     * 备注：必须是find和get开头，findTop是查第一条，findAll是所有。
     * Optional关键字是java8中的，可以不用直接返回对象即可。
     */
    Optional<UserIdentifyFK> findTopByUserNameAndPassword(String username, String password);

    /**
     * 第二种，使用JPQL方式查询。
     * 优点：可随意更换不同数据库，编译时会验证，如果有错误无法编译通过(需要能看懂错误)。
     * 缺点：只能写标准通用的语句，不同数据库特定的关键字不支持比如limit。
     *
     * @param username
     * @param password
     * @return
     */
    @Query("select ui from UserIdentifyFK ui where ui.userName=?1 and ui.password=?2")
    List<UserIdentifyFK> loginValidate(String username, String password);

    /**
     * 第三种，使用原生sql方式查询。
     * 优点：原生sql最自由。
     * 缺点：如果语法错误只有在运行的时候才会报错，不支持换数据库（场景较少）。
     * 备注：传递参数可以使用？和:两种方式。
     * 第一种教简洁，填写参数位置即可（从1开始）。
     * 第二种需要@Param注解，比较繁琐而且必选给全部参数都加上，除了Pageable和Sort。优点是查询语句可以一目了然。
     */
    @Query(nativeQuery = true, value = "select ui from user_identify ui " +
            "where ui.user_name=:username and ui.password=:password limit 1")
    UserIdentifyFK loginValidateSql(@Param("username") String username, @Param("password") String password);


}