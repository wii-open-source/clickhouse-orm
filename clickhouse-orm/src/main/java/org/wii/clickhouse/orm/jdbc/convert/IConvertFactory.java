package org.wii.clickhouse.orm.jdbc.convert;

import org.wii.clickhouse.orm.core.entity.EntityTable;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 21:59
 */
public interface IConvertFactory {
    <T> EntityTable<T> initResultEntity(Class<T> entityClass);

    <T> EntityTable<T> initResultAnnotationEntity(Class<T> entityClass);

    <T> List<T> createResultAnnotationEntity(ResultSet resultSet, Class<T> requireType) throws Exception;

    <T> List<T> createResultEntity(ResultSet resultSet, Class<T> requireType) throws Exception;

    <T> T createSingleResultEntity(ResultSet resultSet, Class<T> requireType) throws Exception;

    <T> T createSingleResultAnnotationEntity(ResultSet resultSet, Class<T> requireType) throws Exception;

    <T> String buildInsertSql(String sql, String valueExpress, List<T> list);

    <T> String buildInsertSql(String table, String columnTuple, String valueExpress, List<T> list);

    <T> String buildInsertSqlForAnnotation(Class<T> requireType, String propertyExpress, List<T> list);

    String buildQuerySql(String sql, Map<String, Object> params);

    String buildQuerySql(String sql, Object object);

}

