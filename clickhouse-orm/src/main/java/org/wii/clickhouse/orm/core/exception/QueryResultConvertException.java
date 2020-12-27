package org.wii.clickhouse.orm.core.exception;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 21:52
 */
public class QueryResultConvertException extends DataAccessException {

    public QueryResultConvertException(String msg, Throwable cause) {
        super(msg, cause);
    }
}