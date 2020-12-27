package org.wii.clickhouse.orm.jdbc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.sql.DataSource;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 22:07
 */
public abstract class ClickHouseJdbcAccessor implements ClickHouseJdbcOperations, InitializingBean {

    private DataSource dataSource;

    public ClickHouseJdbcAccessor(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected DataSource obtainDataSource() {
        DataSource dataSource = getDataSource();
        Assert.state(dataSource != null, "No DataSource set");
        return dataSource;
    }

    @Override
    public void afterPropertiesSet() throws Exception{
        if (getDataSource() == null) {
            throw new IllegalArgumentException("Property 'clickhouse dataSource' is required");
        }
    }
}

