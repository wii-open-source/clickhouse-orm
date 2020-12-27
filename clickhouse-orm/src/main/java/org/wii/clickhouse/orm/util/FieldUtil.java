package org.wii.clickhouse.orm.util;

import org.wii.clickhouse.orm.core.entity.EntityField;

import javax.persistence.Entity;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class FieldUtil {
    /**
     * 获取全部的Field
     * @param entityClass
     * @return
     */
    public static List<EntityField> getFields(Class<?> entityClass) {
        List<EntityField> fields = _getFields(entityClass, null, null);
        List<EntityField> properties = getProperties(entityClass);
        for (EntityField field : fields) {
            for (EntityField property : properties) {
                if (field.getName().equals(property.getName())) {
                    //泛型的情况下通过属性可以得到实际的类型
                    field.setJavaType(property.getJavaType());
                    break;
                }
            }
        }
        return fields;
    }

    public static List<Field> getAllFields(Class<?> entityClass, List<Field> fieldList, Integer level) {
        if (fieldList == null) {
            fieldList = new ArrayList<Field>();
        }
        if (level == null) {
            level = 0;
        }
        if (entityClass.equals(Object.class)) {
            return fieldList;
        }
        Field[] fields = entityClass.getDeclaredFields();
        int index = 0;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            //排除静态字段，解决bug#2
            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                if (level != 0) {
                    //将父类的字段放在前面
                    fieldList.add(index, field);
                    index++;
                } else {
                    fieldList.add(field);
                }
            }
        }

        Class<?> superClass = entityClass.getSuperclass();
        if (superClass != null){
            if (!superClass.equals(Object.class) || (!Map.class.isAssignableFrom(superClass) && !Collection.class.isAssignableFrom(superClass))) {
                return getAllFields(entityClass.getSuperclass(), fieldList, ++level);
            }
        }
        return fieldList;
    }


    /**
     * 获取全部的Field，仅仅通过Field获取
     *
     * @param entityClass
     * @param fieldList
     * @param level
     * @return
     */
    private static List<EntityField> _getFields(Class<?> entityClass, List<EntityField> fieldList, Integer level) {
        if (fieldList == null) {
            fieldList = new ArrayList<EntityField>();
        }
        if (level == null) {
            level = 0;
        }
        if (entityClass.equals(Object.class)) {
            return fieldList;
        }
        Field[] fields = entityClass.getDeclaredFields();
        int index = 0;
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            //排除静态字段，解决bug#2
            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                if (level != 0) {
                    //将父类的字段放在前面
                    fieldList.add(index, new EntityField(field, null));
                    index++;
                } else {
                    fieldList.add(new EntityField(field, null));
                }
            }
        }

        Class<?> superClass = entityClass.getSuperclass();
        if (superClass != null
                && !superClass.equals(Object.class)
                && (superClass.isAnnotationPresent(Entity.class)
                || (!Map.class.isAssignableFrom(superClass)
                && !Collection.class.isAssignableFrom(superClass)))) {
            return _getFields(entityClass.getSuperclass(), fieldList, ++level);
        }
        return fieldList;
    }

    public static List<EntityField> getProperties(Class<?> entityClass) {
        List<EntityField> entityFields = new ArrayList<EntityField>();
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(entityClass);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor desc : descriptors) {
            if (!desc.getName().equals("class")) {
                entityFields.add(new EntityField(null, desc));
            }
        }
        return entityFields;
    }
}
