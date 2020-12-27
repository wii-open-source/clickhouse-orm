package org.wii.clickhouse.orm.jdbc.callback;

import java.sql.Statement;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 21:54
 */
public interface ClickHouseStatementCallback<T> {

    T doInStatement(Statement stmt) throws Exception;
}