package com.gc.demo.springbootandjpa.service.impl;

import com.gc.demo.springbootandjpa.repository.BaseRepository;
import com.gc.demo.springbootandjpa.service.BaseService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018/10/16
 */
public class BaseServiceImpl<R extends BaseRepository> implements BaseService {

    //基本的repository
    protected R repository;

//    void setRepository(R repository) {
//        this.repository = repository;
//    }


    @Override
    public <T> T save(T entity) {
        if (repository != null)
            return (T) repository.save(entity);
        else
            return null;
    }


    @Override
    public <T> T update(T entity) {
        if (repository != null)
            return (T) repository.save(entity);
        else
            return null;
    }

    @Override
    public int deleteWithStatus(Object id) {
        return repository.deleteWithState(id);
    }

    @Override
    public int deleteById(Object id) {
        if (repository != null) {
            repository.deleteById(id);
            return 0;
        } else
            return -1;
    }

    @Override
    public <T> List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public <T> T findOne(T entity) {
        Example example = Example.of(entity, ExampleMatcher.matching().withIgnoreNullValues());
        Optional<T> optional = repository.findOne(example);
        return optional.orElse(null);
    }

    @Override
    public <T> List<T> findData(T entity) {
        Example example = Example.of(entity);
        return repository.findAll(example);
    }

    @Override
    public <T> Page<T> findDataWithPage(T entity, int page, int size) {
        Example example = Example.of(entity, ExampleMatcher.matching().withIgnoreNullValues());
        return repository.findAll(example, PageRequest.of(page, size));
    }
}
