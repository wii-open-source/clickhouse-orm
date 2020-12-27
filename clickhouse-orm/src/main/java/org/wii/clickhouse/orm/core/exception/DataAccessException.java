package org.wii.clickhouse.orm.core.exception;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 21:49
 */
public abstract class DataAccessException extends RuntimeException {
    public DataAccessException(String msg) {
        super(msg);
    }
    public DataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}