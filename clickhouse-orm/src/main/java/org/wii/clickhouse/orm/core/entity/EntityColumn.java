package org.wii.clickhouse.orm.core.entity;

import org.wii.clickhouse.orm.core.type.JdbcType;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 21:59
 */
public class EntityColumn {
    private EntityTable table;
    // 属性名
    private String property;
    // 列名
    private String column;
    private Class<?> javaType;
    private JdbcType jdbcType;
    private String sequenceName;
    private boolean id = false;
    private boolean uuid = false;
    private boolean identity = false;
    private String generator;
    //排序
    private String orderBy;
    //可插入
    private boolean insertable = true;
    //可更新
    private boolean updatable = true;
    /**
     * 对应的字段信息
     *
     * @since 3.5.0
     */
    private EntityField entityField;

    public EntityColumn() {
    }

    public EntityColumn(EntityTable table) {
        this.table = table;
    }

    public EntityTable getTable() {
        return table;
    }

    public void setTable(EntityTable table) {
        this.table = table;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

    public boolean isUuid() {
        return uuid;
    }

    public void setUuid(boolean uuid) {
        this.uuid = uuid;
    }

    public boolean isIdentity() {
        return identity;
    }

    public void setIdentity(boolean identity) {
        this.identity = identity;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    public EntityField getEntityField() {
        return entityField;
    }

    public void setEntityField(EntityField entityField) {
        this.entityField = entityField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityColumn that = (EntityColumn) o;

        if (id != that.id) return false;
        if (uuid != that.uuid) return false;
        if (identity != that.identity) return false;
        if (table != null ? !table.equals(that.table) : that.table != null) return false;
        if (property != null ? !property.equals(that.property) : that.property != null) return false;
        if (column != null ? !column.equals(that.column) : that.column != null) return false;
        if (javaType != null ? !javaType.equals(that.javaType) : that.javaType != null) return false;
        if (jdbcType != that.jdbcType) return false;
        if (sequenceName != null ? !sequenceName.equals(that.sequenceName) : that.sequenceName != null) return false;
        if (generator != null ? !generator.equals(that.generator) : that.generator != null) return false;
        return !(orderBy != null ? !orderBy.equals(that.orderBy) : that.orderBy != null);

    }

    @Override
    public int hashCode() {
        int result = table != null ? table.hashCode() : 0;
        result = 31 * result + (property != null ? property.hashCode() : 0);
        result = 31 * result + (column != null ? column.hashCode() : 0);
        result = 31 * result + (javaType != null ? javaType.hashCode() : 0);
        result = 31 * result + (jdbcType != null ? jdbcType.hashCode() : 0);
        result = 31 * result + (sequenceName != null ? sequenceName.hashCode() : 0);
        result = 31 * result + (id ? 1 : 0);
        result = 31 * result + (uuid ? 1 : 0);
        result = 31 * result + (identity ? 1 : 0);
        result = 31 * result + (generator != null ? generator.hashCode() : 0);
        result = 31 * result + (orderBy != null ? orderBy.hashCode() : 0);
        return result;
    }
}

