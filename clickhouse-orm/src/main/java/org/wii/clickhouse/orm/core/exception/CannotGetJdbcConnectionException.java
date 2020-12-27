package org.wii.clickhouse.orm.core.exception;

public class CannotGetJdbcConnectionException extends DataAccessException {
    public CannotGetJdbcConnectionException(String msg) {
        super(msg);
    }

    public CannotGetJdbcConnectionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
