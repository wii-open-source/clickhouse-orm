package org.wii.clickhouse.orm.core.entity;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.wii.clickhouse.orm.core.exception.QueryResultConvertException;
import org.wii.clickhouse.orm.util.FieldUtil;
import org.wii.clickhouse.orm.util.ResultSetUtil;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 22:00
 */
@Slf4j
public class EntityTable<T> {
    public static final Pattern DELIMITER = Pattern.compile("^[`\\[\"]?(.*?)[`\\]\"]?$");
    //属性和列对应
    protected Map<String, EntityColumn> propertyMap;
    private String name;
    private String catalog;
    private String schema;
    private String orderByClause;
    private String baseSelect;
    //实体类 => 全部列属性
    private Set<EntityColumn> entityClassColumns;
    //useGenerator包含多列的时候需要用到
    private List<String> keyProperties;
    private List<String> keyColumns;
    //类
    private Class<T> entityClass;

    public EntityTable(Class<T> entityClass){
        this.entityClass = entityClass;
    }

    public static Pattern getDELIMITER() {
        return DELIMITER;
    }

    public Map<String, EntityColumn> getPropertyMap() {
        return propertyMap;
    }

    public EntityColumn getColumn(String property){
        return propertyMap.get(property);
    }

    public void setPropertyMap(Map<String, EntityColumn> propertyMap) {
        this.propertyMap = propertyMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getBaseSelect() {
        return baseSelect;
    }

    public void setBaseSelect(String baseSelect) {
        this.baseSelect = baseSelect;
    }

    public Set<EntityColumn> getEntityClassColumns() {
        return entityClassColumns;
    }

    public void setEntityClassColumns(Set<EntityColumn> entityClassColumns) {
        this.entityClassColumns = entityClassColumns;
    }

    public List<String> getKeyProperties() {
        return keyProperties;
    }

    public void setKeyProperties(List<String> keyProperties) {
        this.keyProperties = keyProperties;
    }

    public List<String> getKeyColumns() {
        return keyColumns;
    }

    public void setKeyColumns(List<String> keyColumns) {
        this.keyColumns = keyColumns;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public void setTable(Table table) {
        this.name = table.name();
        this.catalog = table.catalog();
        this.schema = table.schema();
    }

    public void initPropertyMap() {
        propertyMap = new HashMap<String, EntityColumn>(getEntityClassColumns().size());
        for (EntityColumn column : getEntityClassColumns()) {
            propertyMap.put(column.getProperty(), column);
        }
    }

    public String getPrefix() {
        if (StringUtils.isNotEmpty(catalog)) {
            return catalog;
        }
        if (StringUtils.isNotEmpty(schema)) {
            return schema;
        }
        return "";
    }

    public T getInstance(ResultSet resultSet) throws Exception{
        try {
            T obj = this.entityClass.newInstance();
            Map<String, Object> resultValues = ResultSetUtil.getResultValues(resultSet);
            List<Field> fields = FieldUtil.getAllFields(obj.getClass(), null, null);
            for (Field field: fields){
                String property = field.getName();
                EntityColumn column = this.getColumn(property);
                String columnName = column.getColumn();
                field.setAccessible(true);
                Object value = resultValues.get(columnName);
                if (Objects.isNull(value) && StringUtils.isNotBlank(property)){
                    value = resultValues.get(property);
                }
                if (Objects.nonNull(value)){
                    field.set(obj, value);
                }
            }
            return obj;
        } catch (Exception e) {
            log.error("resultSet to entity error", e);
            throw new QueryResultConvertException("Query ResultSet To Entity Error", e);
        }
    }

}
