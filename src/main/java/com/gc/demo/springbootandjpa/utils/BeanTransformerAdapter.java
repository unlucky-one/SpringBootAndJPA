package com.gc.demo.springbootandjpa.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.transform.ResultTransformer;
import org.springframework.beans.*;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Author: Raiden
 * Date: 2018-10-10
 * Time: 13:57
 */
public class BeanTransformerAdapter<T>
        implements ResultTransformer {
    private static final long serialVersionUID = 8149706114474043039L;
    protected final Log logger = LogFactory.getLog(getClass());
    private Class<T> mappedClass;
    private boolean checkFullyPopulated = false;

    private boolean primitivesDefaultedForNullValue = false;
    private Map<String, PropertyDescriptor> mappedFields;
    private Set<String> mappedProperties;

    public BeanTransformerAdapter() {
    }

    public BeanTransformerAdapter(Class<T> mappedClass) {
        initialize(mappedClass);
    }

    public BeanTransformerAdapter(Class<T> mappedClass, boolean checkFullyPopulated) {
        initialize(mappedClass);
        this.checkFullyPopulated = checkFullyPopulated;
    }

    public void setMappedClass(Class<T> mappedClass) {
        if (this.mappedClass == null) {
            initialize(mappedClass);
        } else if (!this.mappedClass.equals(mappedClass))
            throw new InvalidDataAccessApiUsageException("The mapped class can not be reassigned to map to " +
                    mappedClass + " since it is already providing mapping for " + this.mappedClass);
    }

    protected void initialize(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        this.mappedFields = new HashMap();
        this.mappedProperties = new HashSet();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        for (PropertyDescriptor pd : pds)
            if (pd.getWriteMethod() != null) {
                this.mappedFields.put(pd.getName().toLowerCase(), pd);
                String underscoredName = underscoreName(pd.getName());
                if (!pd.getName().toLowerCase().equals(underscoredName)) {
                    this.mappedFields.put(underscoredName, pd);
                }
                this.mappedProperties.add(pd.getName());
            }
    }

    private String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(name.substring(0, 1).toLowerCase());
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = s.toLowerCase();
            if (!s.equals(slc))
                result.append("_").append(slc);
            else {
                result.append(s);
            }
        }
        return result.toString();
    }

    public final Class<T> getMappedClass() {
        return this.mappedClass;
    }

    public void setCheckFullyPopulated(boolean checkFullyPopulated) {
        this.checkFullyPopulated = checkFullyPopulated;
    }

    public boolean isCheckFullyPopulated() {
        return this.checkFullyPopulated;
    }

    public void setPrimitivesDefaultedForNullValue(boolean primitivesDefaultedForNullValue) {
        this.primitivesDefaultedForNullValue = primitivesDefaultedForNullValue;
    }

    public boolean isPrimitivesDefaultedForNullValue() {
        return this.primitivesDefaultedForNullValue;
    }

    protected void initBeanWrapper(BeanWrapper bw) {
    }

    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd)
            throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
    }

    public static <T> BeanPropertyRowMapper<T> newInstance(Class<T> mappedClass) {
        BeanPropertyRowMapper newInstance = new BeanPropertyRowMapper();
        newInstance.setMappedClass(mappedClass);
        return newInstance;
    }

    public Object transformTuple(Object[] tuple, String[] aliases) {
        Object mappedObject = BeanUtils.instantiate(this.mappedClass);
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
        initBeanWrapper(bw);

        Set populatedProperties = isCheckFullyPopulated() ? new HashSet() : null;
        for (int i = 0; i < aliases.length; i++) {
            String column = aliases[i];
            PropertyDescriptor pd = (PropertyDescriptor) this.mappedFields.get(column.replaceAll(" ", "").toLowerCase());
            if (pd == null) continue;
            try {
                Object value = tuple[i];
                try {
                    bw.setPropertyValue(pd.getName(), value);
                } catch (TypeMismatchException e) {
                    if ((value == null) && (this.primitivesDefaultedForNullValue))
                        this.logger.debug("Intercepted TypeMismatchException for column " + column + " and column '" +
                                column + "' with value " + value + " when setting property '" + pd.getName() + "' of type " + pd.getPropertyType() +
                                " on object: " + mappedObject);
                    else {
                        throw e;
                    }
                }
                if (populatedProperties != null)
                    populatedProperties.add(pd.getName());
            } catch (NotWritablePropertyException ex) {
                throw new DataRetrievalFailureException("Unable to map column " + column +
                        " to property " + pd.getName(), ex);
            }

        }

        if ((populatedProperties != null) && (!populatedProperties.equals(this.mappedProperties))) {
            throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields necessary to populate object of class [" +
                    this.mappedClass + "]: " + this.mappedProperties);
        }

        return mappedObject;
    }

    public List transformList(List list) {
        return list;
    }

}
