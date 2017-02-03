package com.ls.generator.support;

public class DateResolver extends DataTypeResolverSupport {

	public DateResolver() {
		super("DATE");
	}

	@Override
	protected String resolveInternal(DataType dataType) {
		return "DATE";
	}

}
