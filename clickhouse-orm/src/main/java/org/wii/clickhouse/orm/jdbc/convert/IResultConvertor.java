package org.wii.clickhouse.orm.jdbc.convert;

import java.sql.ResultSet;
import java.util.List;

public interface IResultConvertor {
    /**
     * 结果转实体
     * @param resultSet
     * @param requireType
     * @param <T>
     * @return
     */
    <T> T resultToEntity(ResultSet resultSet, Class<T> requireType) throws Exception;

    /**
     * 结果转实体集合
     * @param resultSet
     * @param requireType
     * @param <T>
     * @return
     */
    <T> List<T> resultToEntitys(ResultSet resultSet, Class<T> requireType) throws Exception;

    /**
     * 根据查询表达式结果转实体
     * @param resultSet
     * @param requireType
     * @param <T>
     * @return
     */
    <T> T resultToAnnotationEntity(ResultSet resultSet, Class<T> requireType) throws Exception;

    /**
     * 根据查询表达式结果转实体集合
     * @param resultSet
     * @param requireType
     * @param <T>
     * @return
     */
    <T> List<T> resultToAnnotationEntitys(ResultSet resultSet, Class<T> requireType) throws Exception;
}

