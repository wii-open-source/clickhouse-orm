package org.wii.clickhouse.orm.jdbc.convert;

import java.util.List;
import java.util.Map;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 22:03
 */
public class WriteConvertor extends ResultConvertor implements IWriteConvertor {

    public WriteConvertor(IConvertFactory convertFactory) {
        super(convertFactory);
    }

    @Override
    public <T> String insertValueToSql(String sql, String valueExpress, List<T> list){
        return convertFactory.buildInsertSql(sql, valueExpress, list);
    }

    @Override
    public <T> String insertToSql(String table, String columnTuple, String valueExpress, List<T> list){
        return convertFactory.buildInsertSql(table, columnTuple, valueExpress, list);
    }

    @Override
    public <T> String insertToSqlForAnnotation(Class<T> requireType, String columnExpress, List<T> list){
        return convertFactory.buildInsertSqlForAnnotation(requireType, columnExpress, list);
    }

    @Override
    public String queryToSql(String sql, Map<String, Object> params){
        return convertFactory.buildQuerySql(sql, params);
    }

    @Override
    public String queryToSql(String sql, Object object) {
        return convertFactory.buildQuerySql(sql, object);
    }

}
