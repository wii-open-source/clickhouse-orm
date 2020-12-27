package org.wii.clickhouse.orm.jdbc;

import org.wii.clickhouse.orm.core.exception.DataAccessException;
import org.wii.clickhouse.orm.jdbc.callback.ClickHouseStatementCallback;

import java.util.List;
import java.util.Map;

public interface ClickHouseJdbcOperations {
    /**
     * 执行
     * @param action
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> T execute(ClickHouseStatementCallback<T> action) throws DataAccessException;

    /**
     * 执行
     * @param sql
     * @throws DataAccessException
     */
    void execute(String sql) throws DataAccessException;

    /**
     * 新增/删除/更新
     * @param sql
     * @return
     * @throws DataAccessException
     */
    int upsert(final String sql) throws DataAccessException;

    /**
     * 批量插入
     * @param sql
     * @return
     * @throws DataAccessException
     */
    int batchInsert(String sql) throws DataAccessException;

    /**
     * 批量插入
     * insert into cluster_test.test8_all(biz,uid,ts) values('1','n','2020-12-19 20:10:44');
     * @param sql: insert into cluster_test.test8_all(biz,uid,ts)
     * @param valueExpress (@{biz},@{uid},@{ts})
     * @param params 集合
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> int batchInsert(String sql, String valueExpress, List<T> params) throws DataAccessException;

    /**
     * 批量插入
     * insert into cluster_test.test8_all(biz,uid,ts) values('1','n','2020-12-19 20:10:44');
     * @param table: cluster_test.test8_all
     * @param columnTuple: (biz,uid,ts)
     * @param valueExpress: (@{biz},@{uid},@{ts})
     * @param params 集合
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> int batchInsert(String table, String columnTuple, String valueExpress, List<T> params) throws DataAccessException;

    /**
     * 批量插入,要求必须是注解类
     * @param requireType 注解类
     * @param columnExpress: (@{biz},@{uid},@{ts}) 填写属性名
     * @param params 注解类集合
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> int batchInsert(Class<T> requireType, String columnExpress, List<T> params) throws DataAccessException;

    /**
     * 查询单个对象
     * @param sql: select * from cluster_test.test8_all where biz=@{biz} and uid=@{uid}
     * @param requiredType 需要返回的类型
     * @param params Map("biz" -> "1", "uid", "A")
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> T queryForObject(String sql, Class<T> requiredType, Map<String, Object> params) throws DataAccessException;

    /**
     * 查询注解实体
     * @param sql: select * from cluster_test.test8_all where biz=@{biz} and uid=@{uid}
     * @param requiredType 注解类型
     * @param params Object(biz="1",uid="C")
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> T queryForObjectByAnnotation(String sql, Class<T> requiredType, Object params) throws DataAccessException;

    /**
     * 查询注解实体
     * @param sql: select * from cluster_test.test8_all where biz=@{biz} and uid=@{uid}
     * @param requiredType 注解类型
     * @param params
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> T queryForObjectByAnnotation(String sql, Class<T> requiredType, Map<String, Object> params) throws DataAccessException;

    /**
     * 查询单个对象
     * @param sql: select * from cluster_test.test8_all where biz='1' and uid='C'
     * @param requiredType
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException;

    /**
     * 查询单个对象
     * @param sql: select * from cluster_test.test8_all where biz=@{biz} and uid=@{uid}
     * @param requiredType 需要返回的类型
     * @param params Object(biz="1",uid="C")
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> T queryForObject(String sql, Class<T> requiredType, Object params) throws DataAccessException;

    /**
     * 查询注解实体
     * @param sql：select * from cluster_test.test8_all where biz='1' and uid='C'
     * @param requiredType 注解类
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> T queryForObjectByAnnotation(String sql, Class<T> requiredType) throws DataAccessException;

    /**
     * 查询Map
     * @param sql: select * from cluster_test.test8_all order by biz limit 1
     * @return
     * @throws DataAccessException
     */
    Map<String, Object> queryForMap(String sql) throws DataAccessException;

    /**
     * 查询Map
     * @param sql: select * from cluster_test.test8_all where biz=@{biz} and uid=@{uid}
     * @param params Map("biz", "1")
     * @return
     * @throws DataAccessException
     */
    Map<String, Object> queryForMap(String sql, Map<String, Object> params) throws DataAccessException;

    /**
     * 查询Map
     * @param sql: select * from cluster_test.test8_all where biz=@{biz} and uid=@{uid}
     * @param params Object(biz="1",uid="C")
     * @return
     * @throws DataAccessException
     */
    Map<String, Object> queryForMap(String sql, Object params) throws DataAccessException;

    /**
     * 查询列表
     * @param sql: select * from cluster_test.test8_all where biz='1'
     * @param elementType 任意类型
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException;

    /**
     * 查询列表
     * @param sql: select * from cluster_test.test8_all where biz=@{biz}
     * @param elementType 任意类型
     * @param params Map("biz", "1")
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> List<T> queryForList(String sql, Class<T> elementType, Map<String, Object> params) throws DataAccessException;

    /**
     * 查询列表
     * @param sql: select * from cluster_test.test8_all where biz=@{biz}
     * @param elementType 任意类型
     * @param params Object("biz", "1")
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> List<T> queryForList(String sql, Class<T> elementType, Object params) throws DataAccessException;

    /**
     * 查询注解类列表
     * @param sql: select * from cluster_test.test8_all where biz=@{biz} and uid=@{uid}
     * @param elementType 注解类型
     * @param params Map("biz", "1")
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> List<T> queryForListByAnnotation(String sql, Class<T> elementType, Map<String, Object> params) throws DataAccessException;

    /**
     * 查询注解类列表
     * @param sql:select * from cluster_test.test8_all where biz=@{biz} and uid=@{uid}
     * @param elementType
     * @param params Object(biz="1",uid="C")
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> List<T> queryForListByAnnotation(String sql, Class<T> elementType, Object params) throws DataAccessException;

    /**
     * 查询注解类列表
     * @param sql：select * from cluster_test.test8_all
     * @param elementType 注解类型
     * @param <T>
     * @return
     * @throws DataAccessException
     */
    <T> List<T> queryForListByAnnotation(String sql, Class<T> elementType) throws DataAccessException;

    /**
     * 查询列表
     * @param sql
     * @return
     * @throws DataAccessException
     */
    List<Map<String, Object>> queryForList(String sql) throws DataAccessException;

    /**
     * 查询列表
     * @param sql：select * from cluster_test.test8_all where biz=@{biz} and uid=@{uid}
     * @param params Map("biz", "1")
     * @return
     * @throws DataAccessException
     */
    List<Map<String, Object>> queryForList(String sql, Map<String, Object> params) throws DataAccessException;

    /**
     * 查询集合
     * @param sql：select * from cluster_test.test8_all where biz=@{biz} and uid=@{uid}
     * @param params Object(biz="1",uid="C")
     * @return
     * @throws DataAccessException
     */
    List<Map<String, Object>> queryForList(String sql, Object params) throws DataAccessException;
}
