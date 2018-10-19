package com.gc.demo.springbootandjpa.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/10/19
 */
@Data
@DynamicUpdate//更新时忽略空字段
@MappedSuperclass//不映射到表。用做基类，以便在子类中创建关联映射。
public class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "user_id")
    int userId;
    @Column(name = "user_name")
    String userName;
    String password;

}
