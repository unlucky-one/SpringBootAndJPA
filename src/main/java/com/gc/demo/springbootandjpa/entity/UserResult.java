package com.gc.demo.springbootandjpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/12/7
 */
@Data
@NoArgsConstructor//lombok注解，编译生成无参数构造
@AllArgsConstructor//lombok注解，编译生成全参数构造
public class UserResult {
    int id;
    String name;
    Date birth;
    int gender;
    String remark;
    String token;
}
