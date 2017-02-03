package com.ls.generator.support;

public class ClobResolver extends DataTypeResolverSupport{

	public ClobResolver() {
		super("CLOB");
	}
	
	@Override
	protected String resolveInternal(DataType dataType) {
		return "CLOB("+dataType.getDataLength()+")";
	}

}
