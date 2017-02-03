package com.ls.generator.support;

public class RawResolver extends DataTypeResolverSupport{

	public RawResolver() {
		super("RAW");
	}

	@Override
	protected String resolveInternal(DataType dataType) {
		return "RAW("+dataType.getDataLength()+")";
	}

}
