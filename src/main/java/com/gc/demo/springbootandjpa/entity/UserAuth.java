package com.gc.demo.springbootandjpa.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/12/6
 */
@Data
@Entity
@DynamicUpdate//更新时忽略空字段
@Table(name = "user_auth")
public class UserAuth {
    @Id
    String token;

    @Column(name = "user_id")
    Long userId;
}
