package org.wii.clickhouse.orm.util;

import org.apache.commons.collections4.MapUtils;
import org.mvel2.templates.TemplateRuntime;
import org.springframework.util.NumberUtils;
import org.wii.clickhouse.orm.core.exception.ParseSqlExpressException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 22:06
 */
public class SqlExpressUtil {
    private final static String PREFIX = "@{";

    private static String fillValueByMark(Object value){
        if (Objects.isNull(value)){
            return "null";
        }
        return "'" + value + "'";
    }

    private static Object getColumnValue(Class<?> requiredType, Object value){
        if (value == null){
            return "null";
        }

        if (String.class == requiredType) {
            return fillValueByMark(value);
        }
        else if (boolean.class == requiredType || Boolean.class == requiredType) {
            return value;
        }
        else if (byte.class == requiredType || Byte.class == requiredType) {
            return value;
        }
        else if (short.class == requiredType || Short.class == requiredType) {
            return value;
        }
        else if (int.class == requiredType || Integer.class == requiredType) {
            return value;
        }
        else if (long.class == requiredType || Long.class == requiredType) {
            return value;
        }
        else if (float.class == requiredType || Float.class == requiredType) {
            return value;
        }
        else if (double.class == requiredType || Double.class == requiredType ||
                Number.class == requiredType) {
            return value;
        }
        else if (BigDecimal.class == requiredType) {
            return value;
        }
        else if (java.sql.Date.class == requiredType) {
            java.sql.Date date = (java.sql.Date)value;
            return fillValueByMark(date.toString());
        }
        else if (java.sql.Time.class == requiredType) {
            java.sql.Time time = (java.sql.Time)value;
            return fillValueByMark(time.toString());
        }
        else if (java.sql.Timestamp.class == requiredType) {
            java.sql.Timestamp timestamp = (java.sql.Timestamp)value;
            return fillValueByMark(timestamp.toString());
        } else if (java.util.Date.class == requiredType){
            java.util.Date date = (java.util.Date)value;
            return fillValueByMark(date.toString());
        }
        else if (byte[].class == requiredType) {
            return value;
        }
        else if (Blob.class == requiredType) {
            return value;
        }
        else if (Clob.class == requiredType) {
            return value;
        }
        else if (requiredType.isEnum()) {
            // Enums can either be represented through a String or an enum index value:
            // leave enum type conversion up to the caller (e.g. a ConversionService)
            // but make sure that we return nothing other than a String or an Integer.
            if (value instanceof String) {
                return fillValueByMark(value);
            }
            else if (value instanceof Number) {
                // Defensively convert any Number to an Integer (as needed by our
                // ConversionService's IntegerToEnumConverterFactory) for use as index
                return NumberUtils.convertNumberToTargetClass((Number) value, Integer.class);
            }
            else {
                // e.g. on Postgres: getObject returns a PGObject but we need a String
                return value;
            }
        }
        return fillValueByMark(null);
    }


    public static Map<String, Object> objectToMap(Object object){
        Class<?> clazz = object.getClass();
        List<Field> fields = FieldUtil.getAllFields(clazz, null, null);
        Map<String, Object> map = new HashMap<>(fields.size() + 10);
        try {
            for (Field field:fields){
                field.setAccessible(true);
                map.put(field.getName(), getColumnValue(field.getType(), field.get(object)));
            }
        } catch (Exception e){
            throw new ParseSqlExpressException("Class Set Field Value Error!");
        }
        return map;
    }

    /**
     * 解析SQL表达式
     * 1.插入语句：insert into table columnExpress values valueExpress
     * 1)columnExpress: (@{a},@{b},@{c}) -> Map("a", columnName)
     * 2)valueExpress: (@{a},@{b},@{c}) -> Map("a", value)
     * 2.查询语句: select * from table where a = paramExpress and b = paramExpress
     * 1)paramExpress: @{a} -> Map("a", value)
     * @param express
     * @param params
     * @return
     */
    public static String parse(String express, Map<String, Object> params){
        if (!express.contains(PREFIX)){
            throw new RuntimeException("");
        }
        params.forEach((k, v) -> {
            params.put(k, getColumnValue(v.getClass(), v));
        });
        return (String) TemplateRuntime.eval(express, params);
    }

    public static String parseColumn(String express, Map<String, String> params){
        if (!express.contains(PREFIX)){
            throw new ParseSqlExpressException("Express Error!");
        }
        return (String) TemplateRuntime.eval(express, params);
    }

    /**
     * 解析SQL表达式
     * @param express
     * @param object
     * @return
     */
    public static String parse(String express, Object object){
        Map<String, Object> map = objectToMap(object);
        if (MapUtils.isNotEmpty(map)){
            return (String) TemplateRuntime.eval(express, map);
        }
        throw new ParseSqlExpressException("Parse SQL Express Error!");
    }

}
