package org.wii.clickhouse.orm.jdbc.callback;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 21:53
 */
public interface ResultSetCallback {
    <T> T doInCallback(ResultSet resultSet, Class<T> requireType) throws Exception;

    <T> T doInCallbackForAnnotation(ResultSet resultSet, Class<T> requireType) throws Exception;

    <T> List<T> doListInCallback(ResultSet resultSet, Class<T> requireType) throws Exception;

    <T> List<T> doListInCallbackForAnnotation(ResultSet resultSet, Class<T> requireType) throws Exception;

}
