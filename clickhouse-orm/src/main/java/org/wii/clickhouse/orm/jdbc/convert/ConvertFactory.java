package org.wii.clickhouse.orm.jdbc.convert;

import lombok.extern.slf4j.Slf4j;
import org.wii.clickhouse.orm.core.entity.EntityColumn;
import org.wii.clickhouse.orm.core.entity.EntityTable;
import org.wii.clickhouse.orm.core.exception.QueryResultConvertException;
import org.wii.clickhouse.orm.util.SqlExpressUtil;

import java.sql.ResultSet;
import java.util.*;

@Slf4j
public class ConvertFactory implements IConvertFactory{

    private static final String INSERT_SUFFIX = " values";

    @Override
    public <T> EntityTable<T> initResultEntity(Class<T> entityClass){
        return EntityConvertFactory.initEntity(entityClass);
    }

    @Override
    public <T> EntityTable<T> initResultAnnotationEntity(Class<T> entityClass){
        return EntityConvertFactory.initAnnotationEntity(entityClass);
    }

    @Override
    public <T> List<T> createResultAnnotationEntity(ResultSet resultSet, Class<T> requireType){
        EntityTable<T> entityTable = initResultAnnotationEntity(requireType);
        try {
            List<T> results = new ArrayList<>();
            while (resultSet.next()){
                results.add(entityTable.getInstance(resultSet));
            }
            return results;
        } catch (Exception e){
            log.error("convert to annotation entity error", e);
            throw new QueryResultConvertException("Convert To Annotation Entity Error", e);
        }
    }

    @Override
    public <T> List<T> createResultEntity(ResultSet resultSet, Class<T> requireType){
        EntityTable<T> entityTable = initResultEntity(requireType);
        try {
            List<T> results = new ArrayList<>();
            while (resultSet.next()){
                results.add(entityTable.getInstance(resultSet));
            }
            return results;
        } catch (Exception e){
            log.error("convert to entity error", e);
            throw new QueryResultConvertException("Convert To Annotation Entity Error", e);
        }
    }

    @Override
    public <T> T createSingleResultEntity(ResultSet resultSet, Class<T> requireType)
            throws Exception{
        EntityTable<T> entityTable = initResultEntity(requireType);
        resultSet.next();
        return entityTable.getInstance(resultSet);
    }

    @Override
    public <T> T createSingleResultAnnotationEntity(ResultSet resultSet, Class<T> requireType)
            throws Exception{
        EntityTable<T> entityTable = initResultAnnotationEntity(requireType);
        resultSet.next();
        return entityTable.getInstance(resultSet);
    }

    @Override
    public <T> String buildInsertSql(String sql, String valueExpress, List<T> list){
        StringBuilder builder = new StringBuilder(sql + INSERT_SUFFIX);
        // 时间转换
        list.forEach(res -> {
            builder.append(SqlExpressUtil.parse(valueExpress, res));
        });
        builder.append(";");
        String sqlResult = builder.toString();
        log.info("build insert sql: " + sqlResult);
        return sqlResult;
    }

    @Override
    public <T> String buildInsertSql(String table, String columnTuple, String valueExpress, List<T> list){
        String sql = "insert into " + table + columnTuple + INSERT_SUFFIX;
        StringBuilder builder = new StringBuilder(sql);
        list.forEach(obj -> {
            builder.append(SqlExpressUtil.parse(valueExpress, obj));
        });
        builder.append(";");
        String sqlResult = builder.toString();
        log.info("build insert sql:" + sqlResult);
        return sqlResult;
    }

    @Override
    public <T> String buildInsertSqlForAnnotation(Class<T> requireType, String propertyExpress, List<T> list){
        EntityTable<T> entityTable = initResultAnnotationEntity(requireType);
        String table = entityTable.getName();
        Map<String, String> columnMap = new HashMap<>();
        Set<EntityColumn> columnSet = entityTable.getEntityClassColumns();
        columnSet.forEach(column -> {
            columnMap.put(column.getProperty(), column.getColumn());
        });
        String sql = "insert into " + table + SqlExpressUtil.parseColumn(propertyExpress, columnMap) + INSERT_SUFFIX;
        StringBuilder builder = new StringBuilder(sql);
        list.forEach(obj -> {
            builder.append(SqlExpressUtil.parse(propertyExpress, obj));
        });
        builder.append(";");
        String sqlResult = builder.toString();
        log.info("build insert sql:" + sqlResult);
        return sqlResult;
    }

    @Override
    public String buildQuerySql(String sql, Map<String, Object> params) {
        return SqlExpressUtil.parse(sql, params);
    }

    @Override
    public String buildQuerySql(String sql, Object object) {
        return SqlExpressUtil.parse(sql, object);
    }

}

