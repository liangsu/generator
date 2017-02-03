package com.ls.generator.support;

public class Varchar2Resolver extends DataTypeResolverSupport{

	public Varchar2Resolver() {
		super("VARCHAR2");
	}
	
	@Override
	public String resolveInternal(DataType dataType) {
		return "VARCHAR2("+dataType.getDataLength()+")";
	}

}
