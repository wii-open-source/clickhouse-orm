package org.wii.clickhouse.orm.jdbc.convert;

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

