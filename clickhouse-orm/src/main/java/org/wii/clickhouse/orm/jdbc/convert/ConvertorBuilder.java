package org.wii.clickhouse.orm.jdbc.convert;

/**
 * @author tanghong
 * @Desc
 * @Date 27/12/2020 22:04
 */
public class ConvertorBuilder {
    private RwConvertor rwConvertor;

    public ConvertorBuilder(){
    }

    public ConvertorBuilder setDefaultConvertFactory(){
        this.rwConvertor = new RwConvertor(new ConvertFactory());
        return this;
    }

    public ConvertorBuilder setConvertFactory(IConvertFactory convertFactory){
        this.rwConvertor = new RwConvertor(convertFactory);
        return this;
    }

    public RwConvertor build(){
        return this.rwConvertor;
    }

}

