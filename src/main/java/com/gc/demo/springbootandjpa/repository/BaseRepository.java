package com.gc.demo.springbootandjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018-10-10
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
    public HashMap<String, Object> sqlQuery(String queryString, String countSql, Map<String, ?> values, int offset, int limit, String countName, String rowsName);

    public List<T> sqlQuery(String queryString, Map<String, ?> values);

    public List<T> sqlQuery(String queryString, Object... values);

    public HashMap<String, Object> retrieve(String queryString, String countHql, Map<String, ?> values, int offset, int limit, String countName, String rowsName);

    int deleteWithStatus(Object id);

    /**
     * 伪删除
     */
    int fakeDelete(Object id, String columnName, int state);
}
