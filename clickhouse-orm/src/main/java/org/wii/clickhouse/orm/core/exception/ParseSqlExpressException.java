package org.wii.clickhouse.orm.core.exception;

public class ParseSqlExpressException extends RuntimeException {
    public ParseSqlExpressException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ParseSqlExpressException(String msg) {
        super(msg);
    }
}