package org.wii.clickhouse.orm.core.exception;

public class UncategorizedSQLException extends DataAccessException {
    public UncategorizedSQLException(String msg) {
        super(msg);
    }

    public UncategorizedSQLException(String msg, Throwable cause) {
        super(msg, cause);
    }

}