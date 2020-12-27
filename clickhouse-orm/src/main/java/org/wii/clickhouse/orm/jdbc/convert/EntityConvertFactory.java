package org.wii.clickhouse.orm.jdbc.convert;

import org.apache.commons.lang3.StringUtils;
import org.wii.clickhouse.orm.core.entity.EntityColumn;
import org.wii.clickhouse.orm.core.entity.EntityField;
import org.wii.clickhouse.orm.core.entity.EntityTable;
import org.wii.clickhouse.orm.util.FieldUtil;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityConvertFactory {
    private static final Map<Class<?>, EntityTable<?>> entityTableMap = new ConcurrentHashMap<>();

    public static synchronized <T> EntityTable<T> initEntity(Class<T> entityClass){
        if (entityTableMap.get(entityClass) != null){
            return (EntityTable<T>) entityTableMap.get(entityClass);
        }
        EntityTable<T> entityTable = new EntityTable<>(entityClass);
        entityTable.setName(entityClass.getName());
        entityTable.setEntityClassColumns(new LinkedHashSet<EntityColumn>());
        List<EntityField> fields = FieldUtil.getFields(entityClass);
        for (EntityField field: fields){
            if (Enum.class.isAssignableFrom(field.getJavaType())){
                continue;
            }
            processGenericFiled(entityTable, field);
        }
        entityTable.initPropertyMap();
        entityTableMap.put(entityClass, entityTable);
        return entityTable;
    }

    private static void processGenericFiled(EntityTable entityTable, EntityField field){
        EntityColumn entityColumn = new EntityColumn(entityTable);
        entityColumn.setEntityField(field);
        entityColumn.setColumn(field.getName());
        entityColumn.setProperty(field.getName());
        entityColumn.setJavaType(field.getJavaType());
        entityTable.getEntityClassColumns().add(entityColumn);
    }

    public static synchronized <T> EntityTable<T> initAnnotationEntity(Class<T> entityClass) {
        if (entityTableMap.get(entityClass) != null) {
            return (EntityTable<T>) entityTableMap.get(entityClass);
        }
        //创建并缓存EntityTable
        EntityTable<T> entityTable = null;
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            if (!table.name().equals("")) {
                entityTable = new EntityTable(entityClass);
                entityTable.setTable(table);
            }
        }
        if (entityTable == null) {
            entityTable = new EntityTable(entityClass);
            entityTable.setName(entityClass.getSimpleName());
        }
        entityTable.setEntityClassColumns(new LinkedHashSet<EntityColumn>());
        //处理所有列
        //config.isEnableMethodAnnotation())
        List<EntityField> fields = FieldUtil.getFields(entityClass);
        for (EntityField field : fields) {
            /*if (config.isUseSimpleType() &&
                    !(SimpleTypeUtil.isSimpleType(field.getJavaType())
                            ||
                            (config.isEnumAsSimpleType() && Enum.class.isAssignableFrom(field.getJavaType())))) {
                continue;
            }*/
            // 跳过枚举即可
            if (Enum.class.isAssignableFrom(field.getJavaType())){
                continue;
            }
            processField(entityTable, field);
        }
        entityTable.initPropertyMap();
        entityTableMap.put(entityClass, entityTable);
        return entityTable;
    }

    /**
     * @param entityTable
     * @param field
     */
    private static void processField(EntityTable entityTable, EntityField field) {
        //排除字段
        if (field.isAnnotationPresent(Transient.class)) {
            return;
        }
        //Id
        EntityColumn entityColumn = new EntityColumn(entityTable);
        //记录 field 信息，方便后续扩展使用
        entityColumn.setEntityField(field);
        if (field.isAnnotationPresent(Id.class)) {
            entityColumn.setId(true);
        }
        //Column
        String columnName = null;
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            columnName = column.name();
            entityColumn.setUpdatable(column.updatable());
            entityColumn.setInsertable(column.insertable());
        }
        //表名
        if (StringUtils.isEmpty(columnName)) {
            columnName = field.getName();
        }
        entityColumn.setProperty(field.getName());
        entityColumn.setColumn(columnName);
        entityColumn.setJavaType(field.getJavaType());
        //OrderBy
        if (field.isAnnotationPresent(OrderBy.class)) {
            OrderBy orderBy = field.getAnnotation(OrderBy.class);
            if (orderBy.value().equals("")) {
                entityColumn.setOrderBy("ASC");
            } else {
                entityColumn.setOrderBy(orderBy.value());
            }
        }
        entityTable.getEntityClassColumns().add(entityColumn);
    }


}

