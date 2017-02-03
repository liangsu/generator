package com.ls.generator.support;

public class TimestampResolver extends DataTypeResolverSupport{

	public TimestampResolver() {
		super("TIMESTAMP(6)","TIMESTAMP(6) WITH TIME ZONE");
	}

	@Override
	protected String resolveInternal(DataType dataType) {
		return dataType.getDataType();
	}

}
