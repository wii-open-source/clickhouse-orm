package org.wii.clickhouse.orm.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 22:06
 */
public class ResultSetUtil {
    public static Map<String, Object> getResultValues(ResultSet rs) throws SQLException {
        Map<String, Object> resMap = new HashMap<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        for (int i=1;i<=columnCount;i++){
            Object value = rs.getObject(i);
            resMap.put(rsmd.getColumnName(i), value);
        }
        return resMap;
    }
}
