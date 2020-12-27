package org.wii.clickhouse.orm.jdbc.callback;

import org.wii.clickhouse.orm.jdbc.convert.RwConvertor;

import java.sql.ResultSet;
import java.util.List;

public class ReadResultSetCallback {
    private RwConvertor rwConvertor;

    public ReadResultSetCallback(RwConvertor rwConvertor){
        this.rwConvertor = rwConvertor;
    }

    public <T> T doInCallback(ResultSet resultSet, Class<T> requireType) throws Exception {
        return rwConvertor.resultToEntity(resultSet, requireType);
    }

    public <T> T doInCallbackForAnnotation(ResultSet resultSet, Class<T> requireType) throws Exception {
        return rwConvertor.resultToAnnotationEntity(resultSet, requireType);
    }

    public <T> List<T> doListInCallback(ResultSet resultSet, Class<T> requireType) throws Exception {
        return rwConvertor.resultToEntitys(resultSet, requireType);
    }

    public <T> List<T> doListInCallbackForAnnotation(ResultSet resultSet, Class<T> requireType) throws Exception {
        return rwConvertor.resultToAnnotationEntitys(resultSet, requireType);
    }
}
