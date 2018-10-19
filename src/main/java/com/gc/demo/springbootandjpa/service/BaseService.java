package com.gc.demo.springbootandjpa.service;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/10/16
 */
public interface BaseService {
    <T> T save(T entity);

    <T> T update(T entity);

    int deleteWithStatus(Object id);

    int deleteById(Object id);

    <T> T findOne(T entity);

    <T> List<T> findAll();

    <T> List<T> findData(T entity);

    <T> Page<T> findDataWithPage(T entity, int page, int size);

}
