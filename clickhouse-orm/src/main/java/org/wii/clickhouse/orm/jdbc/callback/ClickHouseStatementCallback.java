package org.wii.clickhouse.orm.jdbc.callback;

import java.sql.Statement;

public interface ClickHouseStatementCallback<T> {

    T doInStatement(Statement stmt) throws Exception;
}