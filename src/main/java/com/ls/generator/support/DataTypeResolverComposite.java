package com.ls.generator.support;

import java.util.ArrayList;
import java.util.List;

public class DataTypeResolverComposite implements DataTypeResolver{

	public static DataTypeResolver INSTANCE = new DataTypeResolverComposite();
	
	List<DataTypeResolver> dataTypeResolvers = null;

	private DataTypeResolverComposite(){
		dataTypeResolvers = new ArrayList<DataTypeResolver>();
		registerDataTypeResolver(new CharResolver());
		registerDataTypeResolver(new ClobResolver());
		registerDataTypeResolver(new DateResolver());
		registerDataTypeResolver(new NumberResolver());
		registerDataTypeResolver(new Nvarchar2Resolver());
		registerDataTypeResolver(new TimestampResolver());
		registerDataTypeResolver(new Varchar2Resolver());
		registerDataTypeResolver(new RawResolver());
		registerDataTypeResolver(new LongResolver());
		registerDataTypeResolver(new BlobResolver());
	}
	
	public void registerDataTypeResolver(DataTypeResolver resolver){
		this.dataTypeResolvers.add(resolver);
	}
	
	@Override
	public String resolve(DataType dataType) {
		for (DataTypeResolver dataTypeResolver : dataTypeResolvers) {
			String  dataTypeStr = dataTypeResolver.resolve(dataType);
			if(dataTypeStr != null){
				return dataTypeStr;
			}
		}
		return null;
	}
	
}
