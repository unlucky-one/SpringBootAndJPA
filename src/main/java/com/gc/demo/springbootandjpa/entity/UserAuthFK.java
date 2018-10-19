package com.gc.demo.springbootandjpa.entity;

import lombok.Data;

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
@Table(name = "user_auth")
public class UserAuthFK extends UserAuth {

    @OneToOne
    @JoinColumn(name = "id")
    User user;
}
