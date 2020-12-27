package org.wii.clickhouse.orm.core.exception;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 21:51
 */
public class CannotGetJdbcConnectionException extends DataAccessException {
    public CannotGetJdbcConnectionException(String msg) {
        super(msg);
    }

    public CannotGetJdbcConnectionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
