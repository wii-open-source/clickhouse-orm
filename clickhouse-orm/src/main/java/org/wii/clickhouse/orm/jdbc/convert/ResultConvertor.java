package org.wii.clickhouse.orm.jdbc.convert;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 22:03
 */
public class ResultConvertor  implements IResultConvertor {
    protected IConvertFactory convertFactory;

    public ResultConvertor(IConvertFactory convertFactory){
        this.convertFactory = convertFactory;
    }

    @Override
    public <T> T resultToEntity(ResultSet resultSet, Class<T> requireType) throws Exception{
        return convertFactory.createSingleResultEntity(resultSet, requireType);
    }

    @Override
    public <T> List<T> resultToEntitys(ResultSet resultSet, Class<T> requireType)
            throws Exception{
        return convertFactory.createResultEntity(resultSet, requireType);
    }

    @Override
    public <T> T resultToAnnotationEntity(ResultSet resultSet, Class<T> requireType)
            throws Exception{
        return convertFactory.createSingleResultAnnotationEntity(resultSet, requireType);
    }

    @Override
    public <T> List<T> resultToAnnotationEntitys(ResultSet resultSet, Class<T> requireType)
            throws Exception{
        return convertFactory.createResultAnnotationEntity(resultSet, requireType);
    }
}

