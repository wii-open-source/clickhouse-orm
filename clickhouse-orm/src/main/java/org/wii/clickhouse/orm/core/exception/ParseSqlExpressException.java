package org.wii.clickhouse.orm.core.exception;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 21:52
 */
public class ParseSqlExpressException extends RuntimeException {
    public ParseSqlExpressException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ParseSqlExpressException(String msg) {
        super(msg);
    }
}