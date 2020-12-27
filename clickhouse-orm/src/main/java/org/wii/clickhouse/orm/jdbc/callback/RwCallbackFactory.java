package org.wii.clickhouse.orm.jdbc.callback;

import lombok.extern.slf4j.Slf4j;
import org.wii.clickhouse.orm.core.exception.DataAccessException;
import org.wii.clickhouse.orm.core.exception.ExecuteSQLException;
import org.wii.clickhouse.orm.jdbc.convert.ConvertorBuilder;
import org.wii.clickhouse.orm.jdbc.convert.RwConvertor;
import org.wii.clickhouse.orm.util.CheckErrorUtil;
import org.wii.clickhouse.orm.util.ResultSetUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 21:55
 */
@Slf4j
public class RwCallbackFactory {
    private RwConvertor rwConvertor;
    private ReadResultSetCallback readResultSetCallback;

    public RwCallbackFactory(){
        this.rwConvertor = new ConvertorBuilder().setDefaultConvertFactory().build();
        this.readResultSetCallback = new ReadResultSetCallback(this.rwConvertor);
    }

    private Map<String, Object> convertResultSetToMap(ResultSet rs) throws SQLException {
        return ResultSetUtil.getResultValues(rs);
    }

    public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Map<String, Object>> resList = new ArrayList<>();
        while (rs.next()){
            resList.add(convertResultSetToMap(rs));
        }
        return resList;
    }

    public <T> ClickHouseStatementCallback<Integer> createInsertStatementCallback(String sql){
        return stmt -> {
            // 校验sql合法性
            CheckErrorUtil.sqlSyntaxErr(sql);
            int rows = stmt.executeUpdate(sql);
            if (log.isTraceEnabled()) {
                log.trace("SQL update affected " + rows + " rows");
            }
            return rows;
        };
    }

    public <T> ClickHouseStatementCallback<Integer> createInsertStatementCallback(String sql, String valueExpress, List<T> params){
        String sqlResult = rwConvertor.insertValueToSql(sql, valueExpress, params);
        log.info("insert sql transfer result:[" + sqlResult + "]");
        return createInsertStatementCallback(sqlResult);
    }

    public <T> ClickHouseStatementCallback<Integer> createInsertStatementCallback(String table, String columnTuple, String valueExpress, List<T> params){
        String sqlResult = rwConvertor.insertToSql(table, columnTuple, valueExpress, params);
        // 校验sql合法性
        log.info("insert sql transfer result:[" + sqlResult + "]");
        return createInsertStatementCallback(sqlResult);
    }

    public <T> ClickHouseStatementCallback<Integer> createInsertStatementCallback(Class<T> requireType, String columnExpress, List<T> params){
        String sqlResult = rwConvertor.insertToSqlForAnnotation(requireType, columnExpress, params);
        // 校验sql合法性
        log.info("insert sql transfer result:[" + sqlResult + "]");
        return createInsertStatementCallback(sqlResult);
    }

    public ClickHouseStatementCallback<Map<String, Object>> createQueryStatementCallbackForMap(String sql){
        return stmt -> {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                return extractData(rs).get(0);
            } catch (Exception e) {
                log.error("Execute SQL error", e);
                throw new ExecuteSQLException("Execute SQL Error", e);
            }
        };
    }

    public ClickHouseStatementCallback<Map<String, Object>> createQueryStatementCallbackForMap(String sql, Map<String, Object> params){
        String sqlResult = rwConvertor.queryToSql(sql, params);
        log.info("Query Sql Transform Result:[" + sqlResult + "]");
        return createQueryStatementCallbackForMap(sqlResult);
    }

    public ClickHouseStatementCallback<Map<String, Object>> createQueryStatementCallbackForMap(String sql, Object params){
        String sqlResult = rwConvertor.queryToSql(sql, params);
        log.info("Query Sql Transform Result:[" + sqlResult + "]");
        return createQueryStatementCallbackForMap(sqlResult);
    }

    public ClickHouseStatementCallback<List<Map<String, Object>>> createQueryStatementCallback(String sql){
        return stmt -> {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                return extractData(rs);
            } catch (Exception e) {
                log.error("Execute SQL error", e);
                throw new ExecuteSQLException("Execute SQL Error", e);
            }
        };
    }

    public ClickHouseStatementCallback<List<Map<String, Object>>> createQueryStatementCallback(String sql, Map<String, Object> params){
        String sqlResult = rwConvertor.queryToSql(sql, params);
        log.info("Query Sql Transform Result:[" + sqlResult + "]");
        return stmt -> {
            try (ResultSet rs = stmt.executeQuery(sqlResult)) {
                return extractData(rs);
            } catch (Exception e) {
                log.error("Execute SQL error", e);
                throw new ExecuteSQLException("Execute SQL Error", e);
            }
        };
    }

    public ClickHouseStatementCallback<List<Map<String, Object>>> createQueryStatementCallback(String sql, Object params){
        String sqlResult = rwConvertor.queryToSql(sql, params);
        log.info("Query Sql Transform Result:[" + sqlResult + "]");
        return createQueryStatementCallback(sqlResult);
    }

    public <T> ClickHouseStatementCallback<T> createQueryStatementCallback(String sql, Class<T> requireType, boolean isAnnotation){
        return stmt -> {
            CheckErrorUtil.sqlSyntaxErr(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (isAnnotation) {
                    return readResultSetCallback.doInCallbackForAnnotation(rs, requireType);
                } else {
                    return readResultSetCallback.doInCallback(rs, requireType);
                }
            } catch (Exception e) {
                log.error("Execute SQL error", e);
                throw new ExecuteSQLException("Execute SQL Error", e);
            }
        };
    }

    public <T> ClickHouseStatementCallback<T> createQueryStatementCallback(String sql, Class<T> requireType, boolean isAnnotation, Map<String, Object> params){
        String sqlResult = rwConvertor.queryToSql(sql, params);
        log.info("Query Sql transform Result:[" + sqlResult + "]");
        return createQueryStatementCallback(sqlResult, requireType, isAnnotation);
    }

    public <T> ClickHouseStatementCallback<T> createQueryStatementCallback(String sql, Class<T> requireType, boolean isAnnotation, Object params){
        String sqlResult = rwConvertor.queryToSql(sql, params);
        log.info("query sql transfer result:[" + sqlResult + "]");
        return createQueryStatementCallback(sqlResult, requireType, isAnnotation);
    }

    public <T> ClickHouseStatementCallback<List<T>> createQueryListStatementCallback(String sql, Class<T> requireType, boolean isAnnotation){
        return stmt -> {
            CheckErrorUtil.sqlSyntaxErr(sql);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (isAnnotation) {
                    return readResultSetCallback.doListInCallbackForAnnotation(rs, requireType);
                } else {
                    return readResultSetCallback.doListInCallback(rs, requireType);
                }
            } catch (Exception e) {
                log.error("Execute SQL error", e);
                throw new ExecuteSQLException("Execute SQL Error", e);
            }
        };
    }

    public <T> ClickHouseStatementCallback<List<T>> createQueryListStatementCallback(String sql, Class<T> requireType, boolean isAnnotation, Map<String, Object> params){
        String sqlResult = rwConvertor.queryToSql(sql, params);
        log.info("query sql transfer result:[" + sqlResult + "]");
        return createQueryListStatementCallback(sqlResult, requireType, isAnnotation);
    }

    public <T> ClickHouseStatementCallback<List<T>> createQueryListStatementCallback(String sql, Class<T> requireType, boolean isAnnotation, Object params){
        String sqlResult = rwConvertor.queryToSql(sql, params);
        log.info("query sql transfer result:[" + sqlResult + "]");
        return createQueryListStatementCallback(sqlResult, requireType, isAnnotation);
    }

}
