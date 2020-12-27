package org.wii.clickhouse.orm.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.wii.clickhouse.orm.core.exception.CannotGetJdbcConnectionException;
import org.wii.clickhouse.orm.core.exception.DataAccessException;
import org.wii.clickhouse.orm.core.exception.UncategorizedSQLException;
import org.wii.clickhouse.orm.jdbc.callback.ClickHouseStatementCallback;
import org.wii.clickhouse.orm.jdbc.callback.RwCallbackFactory;
import org.wii.clickhouse.orm.util.CheckErrorUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Slf4j
public class ClickHouseJdbcTemplate extends ClickHouseJdbcAccessor {
    private final RwCallbackFactory rwCallbackFactory;

    public ClickHouseJdbcTemplate(DataSource dataSource) {
        super(dataSource);
        this.rwCallbackFactory = new RwCallbackFactory();
    }

    private Connection getConnection(DataSource dataSource) throws CannotGetJdbcConnectionException {
        try {
            return dataSource.getConnection();
        }
        catch (SQLException ex) {
            throw new CannotGetJdbcConnectionException("Failed to obtain JDBC Connection", ex);
        }
        catch (IllegalStateException ex) {
            throw new CannotGetJdbcConnectionException("Failed to obtain JDBC Connection: " + ex.getMessage());
        }
    }

    private void releaseConnection(Connection connection){
        if (null == connection){
            return;
        }
        try {
            connection.close();
        } catch (Exception e){
            log.error("Close Connection Exception", e);
        }
    }

    @Override
    public <T> T execute(ClickHouseStatementCallback<T> action) throws DataAccessException {
        Assert.notNull(action, "statement callback object must not be null");
        Connection con = getConnection(obtainDataSource());
        try {
            // Create close-suppressing Connection proxy, also preparing returned Statements.
            return action.doInStatement(con.createStatement());
        }
        catch (Exception ex) {
            releaseConnection(con);
            con = null;
            throw new UncategorizedSQLException("ConnectionCallback", ex);
        }
        finally {
            releaseConnection(con);
        }
    }

    @Override
    public void execute(String sql) throws DataAccessException {
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL statement [" + sql + "]");
        }
        /**
         * Callback to execute the statement.
         */
        class ExecuteStatementCallback implements ClickHouseStatementCallback<Object> {
            @Override
            @Nullable
            public Object doInStatement(Statement stmt) throws SQLException {
                stmt.execute(sql);
                return null;
            }
        }
        execute(new ExecuteStatementCallback());
    }

    @Override
    public int batchInsert(String sql) throws DataAccessException{
        Assert.notNull(sql, "SQL must not be null");
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL update [" + sql + "]");
        }
        return execute(rwCallbackFactory.createInsertStatementCallback(sql));
    }

    @Override
    public <T> int batchInsert(String sql, String valueExpress, List<T> params) throws DataAccessException{
        Assert.notNull(sql, "SQL must not be null");
        CheckErrorUtil.paramsErr(params);
        Assert.notNull(valueExpress, "value express must not be null");
        CheckErrorUtil.illegalExpress(valueExpress);
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL update [" + sql + "]");
        }
        return execute(rwCallbackFactory.createInsertStatementCallback(sql, valueExpress, params));
    }

    @Override
    public <T> int batchInsert(String table, String columnTuple, String valueExpress, List<T> params) throws DataAccessException{
        Assert.notNull(table, "table must not be null");
        CheckErrorUtil.paramsErr(params);
        Assert.notNull(columnTuple, "columnTuple must not be null");
        Assert.notNull(valueExpress, "valueExpress must not be null");
        CheckErrorUtil.illegalExpress(valueExpress);
        return execute(rwCallbackFactory.createInsertStatementCallback(table, columnTuple, valueExpress, params));
    }

    @Override
    public <T> int batchInsert(Class<T> requireType, String columnExpress, List<T> params) throws DataAccessException{
        Assert.notNull(columnExpress, "columnExpress must not be null");
        CheckErrorUtil.paramsErr(params);
        CheckErrorUtil.illegalExpress(columnExpress);
        CheckErrorUtil.nonAnnotationClass(requireType);
        return execute(rwCallbackFactory.createInsertStatementCallback(requireType, columnExpress, params));
    }

    @Override
    public int upsert(final String sql) throws DataAccessException {
        Assert.notNull(sql, "SQL must not be null");
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL update [" + sql + "]");
        }

        /**
         * Callback to execute the update statement.
         */
        class UpdateStatementCallback implements ClickHouseStatementCallback<Integer> {
            @Override
            public Integer doInStatement(Statement stmt) throws SQLException {
                int rows = stmt.executeUpdate(sql);
                if (log.isTraceEnabled()) {
                    log.trace("SQL update affected " + rows + " rows");
                }
                return rows;
            }
        }
        return execute(new UpdateStatementCallback());
    }

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType, Map<String, Object> params) throws DataAccessException {
        return execute(rwCallbackFactory.createQueryStatementCallback(sql, requiredType, false, params));
    }

    @Override
    public <T> T queryForObjectByAnnotation(String sql, Class<T> requiredType, Object params) throws DataAccessException {
        Assert.notNull(sql, "SQL must not be null");
        Assert.notNull(params, "params not empty");
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryStatementCallback(sql, requiredType, true, params));
    }

    @Override
    public <T> T queryForObjectByAnnotation(String sql, Class<T> requiredType, Map<String, Object> params) throws DataAccessException {
        Assert.notNull(sql, "SQL must not be null");
        Assert.notNull(params, "params not empty");
        CheckErrorUtil.nonAnnotationClass(requiredType);
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryStatementCallback(sql, requiredType, true, params));
    }

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType) throws DataAccessException {
        Assert.notNull(sql, "SQL must not be null");
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryStatementCallback(sql, requiredType, false));
    }

    @Override
    public <T> T queryForObject(String sql, Class<T> requiredType, Object params) throws DataAccessException {
        Assert.notNull(sql, "SQL must not be null");
        Assert.notNull(params, "params not empty");
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryStatementCallback(sql, requiredType, false, params));
    }

    @Override
    public <T> T queryForObjectByAnnotation(String sql, Class<T> requiredType) throws DataAccessException {
        Assert.notNull(sql, "SQL must not be null");
        CheckErrorUtil.nonAnnotationClass(requiredType);
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryStatementCallback(sql, requiredType, true));
    }

    @Override
    public Map<String, Object> queryForMap(String sql) throws DataAccessException {
        Assert.notNull(sql, "sql not null");
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryStatementCallbackForMap(sql));
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Map<String, Object> params) throws DataAccessException {
        Assert.notNull(sql, "sql not null");
        Assert.notEmpty(params, "params not empty");
        CheckErrorUtil.illegalSql(sql);
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryStatementCallbackForMap(sql, params));
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Object params) throws DataAccessException {
        Assert.notNull(sql, "sql not null");
        Assert.notNull(params, "params not empty");
        CheckErrorUtil.illegalSql(sql);
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryStatementCallbackForMap(sql, params));
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> elementType) throws DataAccessException {
        Assert.notNull(sql, "sql not null");
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryListStatementCallback(sql, elementType, false));
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> elementType, Object params) throws DataAccessException {
        Assert.notNull(sql, "sql not null");
        Assert.notNull(params, "params not empty");
        CheckErrorUtil.illegalSql(sql);
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryListStatementCallback(sql, elementType, false, params));
    }

    @Override
    public <T> List<T> queryForList(String sql, Class<T> elementType, Map<String, Object> params) throws DataAccessException {
        Assert.notNull(sql, "sql not null");
        Assert.notNull(params, "params not empty");
        CheckErrorUtil.illegalSql(sql);
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryListStatementCallback(sql, elementType, false, params));
    }

    @Override
    public <T> List<T> queryForListByAnnotation(String sql, Class<T> elementType, Map<String, Object> params) throws DataAccessException {
        Assert.notNull(sql, "sql not null");
        Assert.notNull(params, "params not empty");
        CheckErrorUtil.illegalSql(sql);
        CheckErrorUtil.nonAnnotationClass(elementType);
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryListStatementCallback(sql, elementType, true, params));
    }

    @Override
    public <T> List<T> queryForListByAnnotation(String sql, Class<T> elementType, Object params) throws DataAccessException {
        Assert.notNull(sql, "sql not null");
        Assert.notNull(params, "params not empty");
        CheckErrorUtil.illegalSql(sql);
        CheckErrorUtil.nonAnnotationClass(elementType);
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryListStatementCallback(sql, elementType, true, params));
    }

    @Override
    public <T> List<T> queryForListByAnnotation(String sql, Class<T> elementType) throws DataAccessException {
        Assert.notNull(sql, "sql not null");
        CheckErrorUtil.nonAnnotationClass(elementType);
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryListStatementCallback(sql, elementType, true));
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {
        Assert.notNull(sql, "sql not null");
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryStatementCallback(sql));
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Map<String, Object> params) throws DataAccessException {
        Assert.notNull(sql, "sql not null");
        Assert.notEmpty(params, "params not empty");
        CheckErrorUtil.illegalSql(sql);
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryStatementCallback(sql, params));
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Object params) throws DataAccessException {
        Assert.notNull(sql, "sql not null");
        Assert.notNull(params, "params not empty");
        CheckErrorUtil.illegalSql(sql);
        if (log.isDebugEnabled()) {
            log.debug("Executing SQL query [" + sql + "]");
        }
        return execute(rwCallbackFactory.createQueryStatementCallback(sql, params));
    }


}
