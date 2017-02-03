package com.ls.generator.support;

public class LongResolver extends DataTypeResolverSupport{

	public LongResolver() {
		super("LONG");
	}

	@Override
	protected String resolveInternal(DataType dataType) {
		return "LONG("+dataType.getDataLength()+")";
	}

}
