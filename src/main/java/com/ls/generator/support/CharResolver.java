package com.ls.generator.support;

public class CharResolver extends DataTypeResolverSupport{

	public CharResolver() {
		super("CHAR");
	}
	
	@Override
	protected String resolveInternal(DataType dataType) {
		return "CHAR("+dataType.getDataLength()+")";
	}

}
