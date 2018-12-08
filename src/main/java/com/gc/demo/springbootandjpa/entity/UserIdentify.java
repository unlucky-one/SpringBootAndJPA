package com.gc.demo.springbootandjpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/10/19
 */
@Data//lombok注解，编译生成get、set
@DynamicUpdate//更新时忽略空字段
@MappedSuperclass//不映射到表。用做基类，以便在子类中创建关联映射。
public class UserIdentify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "user_id")
    Long userId;
    @Column(name = "user_name")
    String userName;
    String password;

}
