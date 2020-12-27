package org.wii.clickhouse.orm.core.exception;

public abstract class DataAccessException extends RuntimeException {
    public DataAccessException(String msg) {
        super(msg);
    }
    public DataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}