package com.gc.demo.springbootandjpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/10/19
 */
@Data
@Entity
@Table(name = "user_identify")//映射的表名，基类不能有此注解，而且要设置为@MappedSuperclass
public class UserIdentifyFK extends UserIdentify {

    @OneToOne //一对一关联
    @JoinColumn(name = "id")
    User user;
}
