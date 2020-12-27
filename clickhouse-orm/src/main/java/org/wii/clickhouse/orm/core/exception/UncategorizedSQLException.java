package org.wii.clickhouse.orm.core.exception;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 21:52
 */
public class UncategorizedSQLException extends DataAccessException {
    public UncategorizedSQLException(String msg) {
        super(msg);
    }

    public UncategorizedSQLException(String msg, Throwable cause) {
        super(msg, cause);
    }

}