package com.gc.demo.springbootandjpa.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/10/18
 */
@Data
@Entity
@Table(name = "user")
@DynamicUpdate//更新时忽略空字段
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    int id;
    String name;
    Date birth;
    int gender;
    String remark;
}
