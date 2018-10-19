package com.gc.demo.springbootandjpa.repository.impl;

import com.gc.demo.springbootandjpa.repository.BaseRepository;
import com.gc.demo.springbootandjpa.utils.BeanTransformerAdapter;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018-10-10
 * Time: 13:53
 */
public class BaseRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    //父类没有不带参数的构造方法，这里手动构造父类
    private final JpaEntityInformation entityInformation;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public BaseRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
        this.entityClass = entityInformation.getJavaType();
    }


    private Class<T> entityClass;

    /**
     * 查询分页的方法.
     * <pre>
     *  带着条件进行动态拼接sql的查询方法
     * </pre>
     */
    @Override
    public HashMap<String, Object> sqlQuery(String queryString, String countSql, Map<String, ?> values, int offset,
                                            int limit, String countName, String rowsName) {

        Assert.hasText(queryString, "queryString不能为空");

        HashMap<String, Object> map = new HashMap<String, Object>();

        Query query = entityManager.createNativeQuery(queryString);
        Query countQuery = entityManager.createNativeQuery(countSql);

        //给条件赋上值
        if (values != null && !values.isEmpty()) {
            for (Map.Entry<String, ?> entry : values.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
                countQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }

        query.setFirstResult(offset);
        query.setMaxResults(limit);
        query.unwrap(SQLQuery.class).setResultTransformer(new BeanTransformerAdapter(this.entityClass));

        Object results = query.getResultList();
        Object resultsCount = countQuery.getSingleResult();

        map.put(countName, resultsCount);
        map.put(rowsName, results);

        return map;
    }

    @Override
    public List sqlQuery(String queryString, Map<String, ?> values) {
        Session session = entityManager.unwrap(Session.class);
        SQLQuery query = session.createSQLQuery(queryString);

//      //给条件赋上值
//      if (values != null && !values.isEmpty()) {
//          for (Map.Entry<String, ?> entry : values.entrySet()) {
//              query.setParameter(entry.getKey(), entry.getValue());
//          }
//      }

        if (values != null) {
            query.setProperties(values);
        }

        query.setResultTransformer(new BeanTransformerAdapter(this.entityClass));

        return query.list();
    }

    @Override
    public List sqlQuery(String queryString, Object... values) {
        Query query = entityManager.createNativeQuery(queryString);
//      Session session = entityManager.unwrap(org.hibernate.Session.class);
//      SQLQuery query = session.createSQLQuery(queryString);


        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                query.setParameter(i + 1, values[i]);
            }
        }

        query.unwrap(SQLQuery.class).setResultTransformer(new BeanTransformerAdapter(this.entityClass));

        return query.getResultList();
    }

    @Override
    public HashMap<String, Object> retrieve(String queryString, String countHql, Map<String, ?> values, int offset,
                                            int limit, String countName, String rowsName) {

        HashMap<String, Object> map = new HashMap<String, Object>();

        Query query = entityManager.createQuery(queryString);
        Query countQuery = entityManager.createQuery(countHql);

        //给条件赋上值
        if (values != null && !values.isEmpty()) {
            for (Map.Entry<String, ?> entry : values.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
                countQuery.setParameter(entry.getKey(), entry.getValue());
            }
        }

        query.setFirstResult(offset);
        query.setMaxResults(limit);

        Object results = query.getResultList();
        Object resultsCount = countQuery.getSingleResult();

        map.put(countName, resultsCount);
        map.put(rowsName, results);

        return map;
    }


    @Override
    public int deleteWithStatus(Object id) {
        return fakeDelete(id, "state", 2);
    }

    /**
     * 伪删除
     */
    @Override
    @Transactional
    public int fakeDelete(Object id, String columnName, int state) {
        Field field = null;
        try {
            field = entityClass.getDeclaredField(columnName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (field == null) {
            return -1;
        }
        CriteriaUpdate<T> criteriaUpdate = entityManager.getCriteriaBuilder().createCriteriaUpdate(entityClass);
        Predicate predicate = entityManager.getCriteriaBuilder().equal(criteriaUpdate.from(entityClass)
                .get(entityInformation.getIdAttribute()), id);
        criteriaUpdate.set(field.getName(), state).where(predicate);
        return entityManager.createQuery(criteriaUpdate).executeUpdate();
    }

    /**
     * save方法，保存或新增
     */
    @Override
//    @Transactional
    public <S extends T> S save(S entity) {
        if (entityInformation.isNew(entity)) {
            entityManager.persist(entity);
            return entity;
        } else {
            DynamicUpdate dynamicUpdateValue = entity.getClass().getAnnotation(DynamicUpdate.class);
            //如果有动态更新的注解,并且值是true，那么动态更新
            if (dynamicUpdateValue != null && dynamicUpdateValue.value()) {
                //获取ID
                ID entityId = (ID) entityInformation.getId(entity);
                //查询数据库数据
                Optional<T> optional = findById(entityId);
                if (optional.isPresent()) {
                    T dataFromDb = optional.get();
                    //获取为空的属性-覆盖的时候忽略
                    String[] ignoreProperties = getNotNullProperties(entity);
                    //用数据库对象对应的信息覆盖实体中属性为null的信息
                    BeanUtils.copyProperties(dataFromDb, entity, ignoreProperties);
                }
            }
            //更新
            return entityManager.merge(entity);
        }
    }

    /**
     * 获取对象的不为null的属性
     */
    private static String[] getNotNullProperties(Object src) {
        //1.获取Bean
        BeanWrapper srcBean = new BeanWrapperImpl(src);
        //2.获取Bean的属性描述
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        //3.获取Bean的空属性
        Set<String> properties = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : pds) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = srcBean.getPropertyValue(propertyName);
            if (propertyValue != null) {
                properties.add(propertyName);
            }
        }
        return properties.toArray(new String[0]);
    }

}
