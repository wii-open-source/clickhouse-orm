package org.wii.clickhouse.orm.core.exception;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 21:51
 */
public class ExecuteSQLException extends DataAccessException {
    public ExecuteSQLException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
