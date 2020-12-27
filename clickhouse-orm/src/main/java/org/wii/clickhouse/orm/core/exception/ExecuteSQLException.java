package org.wii.clickhouse.orm.core.exception;

public class ExecuteSQLException extends DataAccessException {
    public ExecuteSQLException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
