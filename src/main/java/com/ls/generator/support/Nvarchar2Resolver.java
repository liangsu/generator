package com.ls.generator.support;

public class Nvarchar2Resolver extends DataTypeResolverSupport{

	public Nvarchar2Resolver() {
		super("NVARCHAR2");
	}
	
	@Override
	protected String resolveInternal(DataType dataType) {
		return "NVARCHAR2("+dataType.getDataLength()+")";
	}

}
